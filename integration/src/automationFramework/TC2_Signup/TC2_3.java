package automationFramework.TC2_Signup;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;
import org.sikuli.script.Screen;
import org.sikuli.script.FindFailed;
import org.sikuli.script.ImagePath;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.util.*;
import java.io.File;  
import java.io.IOException; 
	
public class TC2_3 {

	public static Object main(String[] args) throws InterruptedException, FindFailed {
		ImagePath.setBundlePath("C:\\Users\\non-admin\\groupthinq\\integration\\src\\imgDictionary");
		
		System.setProperty("webdriver.gecko.driver","C:\\Users\\non-admin\\groupthinq\\integration\\lib\\drivers\\geckodriver.exe");
		FirefoxOptions capabilities = new FirefoxOptions(); //DesiredCapabilities.firefox()
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
		capabilities.setCapability("marionette", true);
		JavascriptExecutor js;
		
		// Create a new instance of the Firefox driver
		WebDriver driver = new FirefoxDriver();
		js = (JavascriptExecutor) driver;
		
	    //Launch the groupthinq Website
		driver.get("https://staging.groupthinq.us/#/");
		
	    driver.manage().window().setSize(new Dimension(1200, 1000));
	    driver.findElement(By.cssSelector(".q-btn:nth-child(1) .block")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label/div/div/div/input")).sendKeys("");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[2]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[2]/div/div/div/input")).sendKeys("tester");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[3]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[3]/div/div/div/input")).sendKeys("autotester@gmail.com");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[4]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[4]/div/div/div/input")).sendKeys("1990/01/01");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[5]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[5]/div/div/div/input")).sendKeys("autotester1");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[6]/div/div/div/input")).sendKeys("autotester1");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[3]/div/button[2]/span[2]/span/span")).click();
	    Thread.sleep(2000);

        String result = "unchanged";
        Screen screen = new Screen();
        Pattern img = new Pattern("missingFirstName.png");
        
        try {
            Region region = screen.find(img);
            region.highlight(2);
            result = "TC2_3 = PASS";
        } catch (Exception e) {
        	result = "TC2_3 = FAIL";
        }
        
		//Wait for 5 Sec
		Thread.sleep(1000);
		 
        // Close the driver
        driver.quit();
        
        return result;
    }

}