package BaseTest;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;




/**
 * ✅ Baseclass
 * - Loads config from `configure.properties`
 * - Initializes WebDriver based on browser type
 * - Sets window and timeout
 * - Launches target URL
 * - Closes browser
 */
public class Baseclass {
	
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public Properties prop;

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    // Constructor loads config
    public Baseclass() {
        try {
            String configPath = System.getProperty("user.dir") + "/config/configure.properties";
            FileInputStream file = new FileInputStream(configPath);
            prop = new Properties();
            prop.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Initialize browser based on config
    public void initilizeConfig() {
        String browser = prop.getProperty("browser");

        WebDriver localDriver = null;
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions opts = new ChromeOptions();
            opts.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            opts.addArguments("–disable-background-timer-throttling");
            opts.addArguments("--disable-notifications");
             localDriver = new ChromeDriver(opts);
            
            
        } else if (browser.equalsIgnoreCase("edge")) {
            localDriver = new EdgeDriver();
        } else if (browser.equalsIgnoreCase("safari")) {
            localDriver = new SafariDriver(); // Safari, not IE
        } else {
            throw new IllegalArgumentException("❌ Invalid browser type in config file: " + browser);
        }

        localDriver.manage().window().maximize();
        localDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        localDriver.get(prop.getProperty("baseURL"));

        setDriver(localDriver); // Store in ThreadLocal
    }

    // Close the browser
    public void tearDown() {
        WebDriver localDriver = Baseclass.getDriver();
        if (localDriver != null) {
            localDriver.quit();
            driver.remove(); // Important to clean up ThreadLocal
        }
    }
}