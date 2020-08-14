package automationFramework;

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
import org.sikuli.script.Key;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.util.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;  
	import java.io.IOException; 
	import java.awt.event.InputEvent;

	
public class TC7_1 {
	
	public static Object main(String[] args) throws InterruptedException, FindFailed {
		ImagePath.setBundlePath("C:\\Users\\non-admin\\groupthinq\\integration\\src\\imgDictionary");
		
		String tcNumber = "TC7_1 ";
		
		//Object createUser;
		
		System.setProperty("webdriver.gecko.driver","C:\\Users\\non-admin\\groupthinq\\integration\\lib\\drivers\\geckodriver.exe");
		FirefoxOptions capabilities = new FirefoxOptions(); //DesiredCapabilities.firefox()
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
		capabilities.setCapability("marionette", true);
		JavascriptExecutor js;
		
		// Create a new instance of the Firefox driver
		WebDriver driver = new FirefoxDriver();
		js = (JavascriptExecutor) driver;
		
		//String userName = "autotester1";
		//TC_CreaterUser.main(userName); aria
		//userName = "autotester2";
		//TC_CreaterUser.main(userName);
		
	    //Launch the groupthinq Website
		driver.get("https://staging.groupthinq.us/#/");
		
	    driver.manage().window().setSize(new Dimension(1200, 1000));
	    driver.findElement(By.xpath("//a[2]/span[2]/span/span")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label/div/div/div/input")).sendKeys("autotester1");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[2]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[2]/div/div/div/input")).sendKeys("autotester1");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[3]/div/button[2]/span[2]/span")).click();
	    Thread.sleep(4000);

	    

	    driver.findElement(By.name("nav-account")).click();  // user account button
	    Thread.sleep(1000);
	    driver.findElement(By.name("account-edit")).click();  // edit account button
	    Thread.sleep(1000);

	    driver.findElement(By.xpath("//div[@id='q-app']/div/div[2]/main/div/div/div[2]/label/div/div/div/input")).click();
	    try {
	        Robot robot = new Robot();
	        // Simulate a key press
	        robot.keyPress(KeyEvent.VK_CONTROL);
	        robot.keyPress(KeyEvent.VK_A);
	        robot.keyRelease(KeyEvent.VK_CONTROL);
	        robot.keyPress(KeyEvent.VK_DELETE);

		} catch (AWTException e) {
		        e.printStackTrace();
		}
	    driver.findElement(By.xpath("//div[@id='q-app']/div/div[2]/main/div/div/div[2]/label/div/div/div/input")).sendKeys("my new name");
	    
	    Thread.sleep(1000);
	    driver.findElement(By.name("account-confirm")).click();  // edit account button
	    Thread.sleep(1000);
		String result = "unchanged";
        Screen screen = new Screen();
        Pattern img = new Pattern("myNewName.png");
        
        try {
            Region region = screen.find(img);
            region.highlight(2);
            result = (tcNumber + "= PASS");
        } catch (Exception e) {
        	result = (tcNumber + "= FAIL");
        }

		Thread.sleep(1000);
		 
        // Close the driver
        driver.quit();
        
        return result;
    }

}