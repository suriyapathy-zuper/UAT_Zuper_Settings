package TestUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Sends ONE final Slack report after the complete TestNG execution (called by TestUtility.AllureReportListener
 * once the latest Allure report has been generated - never per test).
 *
 * Slack file upload (current API): files.getUploadURLExternal -> upload bytes -> files.completeUploadExternal
 * (shares the file to the channel/thread with the summary as the message). Without a report file the summary is
 * posted with chat.postMessage.
 *
 * Configuration (config/configure.properties, environment variables override):
 *   slack_enabled     (SLACK_ENABLED / -Dslack.enabled)  true | false
 *   slack_bot_token   (SLACK_BOT_TOKEN)                  bot token - never logged
 *   slack_channel_id  (SLACK_CHANNEL_ID)
 *   slack_thread_ts   (SLACK_THREAD_TS)                  optional - reply inside an existing thread
 */
public class ReportToSlack {

	private static final Logger log = LogManager.getLogger(ReportToSlack.class);
	private static final String SLACK_API = "https://slack.com/api/";
	private static final int MAX_FAILED_TESTS_LISTED = 25;
	private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

	/** Slack Web API answered "ok": false - error = Slack's error code (e.g. not_in_channel, missing_scope). */
	static class SlackApiException extends IOException {
		private static final long serialVersionUID = 1L;
		final String error;
		final String neededScope;

		SlackApiException(String method, String error, String neededScope) {
			super(method + " failed: " + error + (neededScope.isEmpty() ? "" : " (needed scope: " + neededScope + ")"));
			this.error = error;
			this.neededScope = neededScope;
		}
	}

	/** Actual results of the finished TestNG execution. */
	public static class ExecutionSummary {
		public String environment;
		public String execution;
		public String account;
		public LocalDateTime start;
		public LocalDateTime end;
		public int passed;
		public int failed;
		public int skipped;
		public final List<String> failedTests = new ArrayList<>();

		public int total() {
			return passed + failed + skipped;
		}

		public long passRate() {
			return total() == 0 ? 0 : Math.round(passed * 100.0 / total());
		}
	}

	/**
	 * Sends the final report. reportFile = the Allure report of THIS execution (null when it was not generated).
	 * Returns true when Slack accepted the message.
	 */
	public static boolean sendFinalReport(Properties config, ExecutionSummary summary, File reportFile) {
		String message = buildMessage(summary, reportFile != null);
		if (!Boolean.parseBoolean(setting(config, "slack_enabled", "SLACK_ENABLED", "true"))) {
			log.info("[SLACK] Slack reporting disabled (slack_enabled=false) - final report not sent. Message:\n" + message);
			return false;
		}
		String token = setting(config, "slack_bot_token", "SLACK_BOT_TOKEN", "");
		String channelId = setting(config, "slack_channel_id", "SLACK_CHANNEL_ID", "");
		String threadTs = setting(config, "slack_thread_ts", "SLACK_THREAD_TS", "");
		if (token.isEmpty() || channelId.isEmpty()) {
			log.error("[SLACK FAIL] Unable to send final report: slack_bot_token / slack_channel_id not configured");
			return false;
		}
		log.info("[SLACK] Preparing final " + summary.execution + " report for channel " + channelId
				+ (threadTs.isEmpty() ? "" : " (thread " + threadTs + ")"));
		try {
			try {
				deliver(token, channelId, threadTs, summary, reportFile, message);
			} catch (SlackApiException e) {
				// the channel ID exists, but the bot user is not a member of it
				if (!"not_in_channel".equals(e.error) || !joinChannel(token, channelId)) {
					throw e;
				}
				log.info("[SLACK] Bot joined channel " + channelId + " - sending the final report again");
				deliver(token, channelId, threadTs, summary, reportFile, message);
			}
			log.info("[SLACK] Final report sent successfully");
			return true;
		} catch (SlackApiException e) {
			log.error("[SLACK FAIL] Unable to send final report: " + e.getMessage());
			if ("not_in_channel".equals(e.error)) {
				log.error("[SLACK FAIL] The Slack bot '" + botName(token) + "' is not a member of channel " + channelId
						+ ". In Slack open that channel and run: /invite @" + botName(token)
						+ "  (a private channel always needs this invite; a public one can also be joined automatically"
						+ " when the Slack app has the channels:join scope)");
			}
			return false;
		} catch (Exception e) {
			log.error("[SLACK FAIL] Unable to send final report: " + e.getClass().getSimpleName() + " - " + e.getMessage());
			return false;
		}
	}

	// the summary as the message, with the current Allure report attached when there is one
	private static void deliver(String token, String channelId, String threadTs, ExecutionSummary summary, File reportFile,
			String message) throws IOException {
		if (reportFile != null && reportFile.isFile()) {
			String fileName = "Allure-Report_" + summary.execution.replaceAll("[^A-Za-z0-9]+", "-") + "_"
					+ summary.end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")) + ".html";
			log.info("[SLACK] Uploading report: " + reportFile + " as '" + fileName + "' (" + reportFile.length() / 1024 + " KB)");
			uploadFile(token, channelId, threadTs, reportFile, fileName, summary.execution + " - Allure Report", message);
		} else {
			log.warn("[SLACK] No current Allure report available - sending the summary without attachment");
			postMessage(token, channelId, threadTs, message);
		}
	}

	// conversations.join - public channels only, needs the channels:join scope; a private channel needs an /invite
	private static boolean joinChannel(String token, String channelId) {
		log.warn("[SLACK] Bot is not a member of channel " + channelId + " - trying to join it");
		try {
			slackCall("conversations.join", token, "application/x-www-form-urlencoded",
					("channel=" + URLEncoder.encode(channelId, "UTF-8")).getBytes(StandardCharsets.UTF_8));
			return true;
		} catch (SlackApiException e) {
			String reason = "method_not_supported_for_channel_type".equals(e.error) || "channel_not_found".equals(e.error)
					? "the channel is private (or not visible to the bot) - the bot has to be invited"
					: "missing_scope".equals(e.error) ? "the Slack app has no channels:join scope" : e.error;
			log.warn("[SLACK] Could not join channel " + channelId + ": " + reason);
			return false;
		} catch (IOException e) {
			log.warn("[SLACK] Could not join channel " + channelId + ": " + e.getMessage());
			return false;
		}
	}

	// bot user name for the invite hint (auth.test needs no extra scope)
	private static String botName(String token) {
		try {
			return slackCall("auth.test", token, "application/x-www-form-urlencoded", new byte[0]).optString("user", "<bot>");
		} catch (IOException e) {
			return "<bot>";
		}
	}

	static String buildMessage(ExecutionSummary s, boolean reportAttached) {
		long seconds = Duration.between(s.start, s.end).getSeconds();
		StringBuilder text = new StringBuilder();
		text.append(s.failed == 0 ? ":white_check_mark: " : ":x: ").append("*QA Automation Report - UAT-ZuperSettings*\n\n");
		text.append("*Environment:* ").append(s.environment).append('\n');
		text.append("*Execution:* ").append(s.execution).append('\n');
		text.append("*Account Used:* ").append(s.account).append('\n');
		text.append("*Date:* ").append(s.start.format(DATE)).append("  |  *Start:* ").append(s.start.format(TIME))
				.append("  |  *End:* ").append(s.end.format(TIME)).append("  |  *Duration:* ")
				.append(seconds / 60).append("m ").append(seconds % 60).append("s\n\n");
		text.append("*Total Tests:* ").append(s.total()).append('\n');
		text.append("*Passed:* ").append(s.passed).append('\n');
		text.append("*Failed:* ").append(s.failed).append('\n');
		text.append("*Skipped:* ").append(s.skipped).append('\n');
		text.append("*Pass Rate:* ").append(s.passRate()).append("%\n\n");
		if (!s.failedTests.isEmpty()) {
			text.append("*Failed Tests:*\n");
			for (int i = 0; i < s.failedTests.size() && i < MAX_FAILED_TESTS_LISTED; i++) {
				text.append("- ").append(s.failedTests.get(i)).append('\n');
			}
			if (s.failedTests.size() > MAX_FAILED_TESTS_LISTED) {
				text.append("- ... and ").append(s.failedTests.size() - MAX_FAILED_TESTS_LISTED).append(" more (see the Allure report)\n");
			}
			text.append('\n');
		}
		text.append("*Detailed Report:* ").append(reportAttached
				? "Allure report attached - download the HTML file and open it in a browser (failure screenshots are under each failed test)."
				: "Allure report was not generated for this execution.");
		return text.toString();
	}

	// STEP 1 get upload URL -> STEP 2 upload bytes -> STEP 3 complete upload + share to channel/thread with the summary
	private static void uploadFile(String token, String channelId, String threadTs, File file, String fileName, String title,
			String comment) throws IOException {
		JSONObject step1 = slackCall("files.getUploadURLExternal", token, "application/x-www-form-urlencoded",
				("filename=" + URLEncoder.encode(fileName, "UTF-8") + "&length=" + file.length()).getBytes(StandardCharsets.UTF_8));
		String uploadUrl = step1.getString("upload_url");
		String fileId = step1.getString("file_id");

		HttpURLConnection upload = open(uploadUrl);
		upload.setRequestProperty("Content-Type", "application/octet-stream");
		try (OutputStream out = upload.getOutputStream()) {
			Files.copy(file.toPath(), out);
		}
		int code = upload.getResponseCode();
		if (code != 200) {
			throw new IOException("file upload returned HTTP " + code + ": " + read(upload));
		}
		read(upload);

		JSONObject complete = new JSONObject()
				.put("files", new JSONArray().put(new JSONObject().put("id", fileId).put("title", title)))
				.put("channel_id", channelId)
				.put("initial_comment", comment);
		if (!threadTs.isEmpty()) {
			complete.put("thread_ts", threadTs);
		}
		slackCall("files.completeUploadExternal", token, "application/json; charset=utf-8",
				complete.toString().getBytes(StandardCharsets.UTF_8));
	}

	private static void postMessage(String token, String channelId, String threadTs, String text) throws IOException {
		JSONObject message = new JSONObject().put("channel", channelId).put("text", text);
		if (!threadTs.isEmpty()) {
			message.put("thread_ts", threadTs);
		}
		slackCall("chat.postMessage", token, "application/json; charset=utf-8", message.toString().getBytes(StandardCharsets.UTF_8));
	}

	// Slack Web API call; fails on HTTP errors and on "ok": false (Slack's error code is reported, the token never)
	private static JSONObject slackCall(String method, String token, String contentType, byte[] body) throws IOException {
		HttpURLConnection conn = open(SLACK_API + method);
		conn.setRequestProperty("Authorization", "Bearer " + token);
		conn.setRequestProperty("Content-Type", contentType);
		try (OutputStream out = conn.getOutputStream()) {
			out.write(body);
		}
		int code = conn.getResponseCode();
		String response = read(conn);
		if (code != 200) {
			throw new IOException(method + " returned HTTP " + code);
		}
		JSONObject json = new JSONObject(response);
		if (!json.optBoolean("ok")) {
			throw new SlackApiException(method, json.optString("error", "unknown error"), json.optString("needed", ""));
		}
		return json;
	}

	private static HttpURLConnection open(String url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) URI.create(url).toURL().openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setConnectTimeout(15000);
		conn.setReadTimeout(120000);
		return conn;
	}

	private static String read(HttpURLConnection conn) throws IOException {
		InputStream in = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();
		if (in == null) {
			return "";
		}
		try (InputStream stream = in; ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[4096];
			int read;
			while ((read = stream.read(buffer)) != -1) {
				bytes.write(buffer, 0, read);
			}
			return new String(bytes.toByteArray(), StandardCharsets.UTF_8);
		}
	}

	// system property (-Dslack.enabled) > environment variable (SLACK_...) > configure.properties > default
	private static String setting(Properties config, String key, String environmentVariable, String defaultValue) {
		String value = System.getProperty(key.replace('_', '.'));
		if (value == null || value.trim().isEmpty()) {
			value = System.getenv(environmentVariable);
		}
		if ((value == null || value.trim().isEmpty()) && config != null) {
			value = config.getProperty(key);
		}
		return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
	}
}
