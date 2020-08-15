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
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;

import java.util.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;  
	import java.io.IOException; 

	//Test if User is able to create decision
	//Create TC4_1_TesterUser
	//Navigate to http://staging.groupthinq.us/#/main	
	//Click "Decisions" 	
	//Click "NewDecision"	
	//Click Calendar Icon and select a date	
	//Click Time and select a time	
	//Click "Create Decision"	
	
public class TC4_2 {
	
	public static Object main(String[] args) throws InterruptedException, FindFailed {
		ImagePath.setBundlePath("C:\\Users\\non-admin\\groupthinq\\integration\\src\\imgDictionary");
		
		String tcNumber = "TC4_2 ";
		
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
		//TC_CreaterUser.main(userName);
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
	    Thread.sleep(3000);
	    driver.findElement(By.xpath("//div[2]/div/a/span[2]")).click();
	    driver.findElement(By.name("details-name")).click();
	    driver.findElement(By.name("details-name")).sendKeys("create decision");
	    driver.findElement(By.name("details-description")).click();
	    driver.findElement(By.name("details-description")).sendKeys("all fields populated");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div[2]/main/div/div/div/div/div[2]/label/div/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div[2]/main/div/div/div/div/div[2]/label/div/div/div/div/input")).sendKeys("autotester2");
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".q-item__section:nth-child(2) > .q-item__label")).click();    /// click pop-up
	    //driver.findElement(By.xpath("//div[4]/div[2]/div")).click();   /// click pop-up
	    driver.findElement(By.name("expiration-datetime")).click();
	    driver.findElement(By.name("expiration-datetime")).sendKeys("2020/09/02 01:05");

	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div[2]/main/div/div[2]/div/button/span[2]/span/span")).click();

        //WebDriverWait wait = new WebDriverWait(driver, 10);
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".q-btn__content > .material-icons:nth-child(1)")));
		Thread.sleep(2000);

	    
		String result = "unchanged";
        Screen screen = new Screen();
        Pattern img = new Pattern("failCreateDecision.png");
        
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