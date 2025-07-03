package BaseTest;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;


public class Baseclass {
	     public static Properties prop;
	     public static WebDriver driver;
	     

		 
		 public Baseclass() {
				try {

					FileInputStream file = new FileInputStream("C:\\Users\\suriyapathy.b\\eclipse-workspace\\d\\config\\configure.properties");
					//C:\Users\suriyapathy.b\eclipse-workspace\d\config\configure.properties
					
					prop = new Properties();
				    
					prop.load(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		 
	public void initilizeConfig()  {
		
		if(prop.getProperty("browser").equalsIgnoreCase("chrome")) {
			driver=new ChromeDriver();
		}
		else if(prop.getProperty("browser").equalsIgnoreCase("edge")) {
			driver=new EdgeDriver();
		}
		else if(prop.getProperty("browser").equalsIgnoreCase("IE")) {
			driver=new SafariDriver();
		}
		else {
			throw new IllegalAccessError("Kindly provide correct configuration");
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.get(prop.getProperty("baseURL"));
	}
 
	 public void tearDown() {
	        if (driver != null) {
	            driver.quit();
	        }
}
	}