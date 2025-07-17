package TestUtility;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{
	  private int retryCount = 0;
	    private static final int maxRetryCount = 5; // 2 retries + 1 original = 3 attempts

	    @Override
	    public boolean retry(ITestResult result) {
	        if (retryCount < maxRetryCount) {
	            System.out.println("Retrying test: " + result.getName() + " | Attempt: " + (retryCount + 1));
	            retryCount++;
	            return true;
	        }
	        return false;
	    }


}
