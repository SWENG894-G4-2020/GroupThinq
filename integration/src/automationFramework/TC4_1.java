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
	
public class TC4_1 {
	
	public static Object main(String[] args) throws InterruptedException, FindFailed {
		ImagePath.setBundlePath("C:\\Users\\non-admin\\groupthinq\\integration\\src\\imgDictionary");
		
		String tcNumber = "TC4_1 ";
		
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
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label/div/div/div/input")).sendKeys("auto");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[2]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[2]/div/div/div/input")).sendKeys("tester");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[3]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[3]/div/div/div/input")).sendKeys("autotester@gmail.com");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[4]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[4]/div/div/div/input")).sendKeys("1990/01/01");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[5]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[5]/div/div/div/input")).sendKeys("TC4_1_TesterUser0");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[6]/div/div/div/input")).sendKeys("TC4_1_TesterUser0");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[3]/div/button[2]/span[2]/span/span")).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".q-btn__content > .material-icons:nth-child(1)")));
		Thread.sleep(1000);

	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/aside/div/div/a[3]/div[3]/div")).click();
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div[2]/main/div/div/button/span[2]/span/span")).click();
	    driver.findElement(By.xpath("//label/div/div/div/input")).click();
	    driver.findElement(By.xpath("//label/div/div/div/input")).sendKeys("TC4_1 create decision test"); // Title of Decision
	    
		//try {Robot robot = new Robot();  // Tab to next...
        //	robot.keyPress(KeyEvent.VK_TAB);
        //	robot.keyRelease(KeyEvent.VK_TAB);
		//} catch (AWTException e) {
		//	e.printStackTrace();
		//}
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//label[2]/div/div/div")).click();
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//textarea")).sendKeys("create decision"); // Text for Decision Description
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//label[3]/div/div/div/input")).click();
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//label[3]/div/div/div/input")).sendKeys("2021/01/01 00:00");  // Expiration Date & Time value
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//div[3]/div/button/span[2]/span/i")).click(); 				// Expand 'Add Decision Options'
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//div[2]/div/div/label/div/div/div/input")).click();				// Click Option Title
	    driver.findElement(By.xpath("//*[@aria-label='Option Title']")).click();				// Click Option Title
	    
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//*[@aria-label='Option Title']")).sendKeys("option 1");  // Text for Option Title
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//label[2]/div/div/div/input")).click();
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//*[@aria-label='Option Description']")).sendKeys("option description");    // Text for Option Description
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//div[3]/div[2]/div/button/span[2]/span/i")).click();				// Click add decision option 
	    Thread.sleep(500);
	    driver.findElement(By.xpath("//div[4]/button[2]/span[2]/span/span")).click();				// Click add decision option 
	    Thread.sleep(500);
	    
		String result = "unchanged";
        Screen screen = new Screen();
        Pattern img = new Pattern("successfulDecisionCreated.png");
        
        try {
            Region region = screen.find(img);
            region.setW(450);
            region.setH(100);
            region.highlight(2);
            result = (tcNumber + "= PASS");
        } catch (Exception e) {
        	result = (tcNumber + "= FAIL");
        }
        Thread.sleep(100);
		/*
		 * driver.findElement(By.xpath("//span[text()='TC4_1_TesterUser2']")).click();
		 * // User Account Drop Down Thread.sleep(500);
		 * driver.findElement(By.xpath("//div[2]/div/div")).click(); // Account Settings
		 * Thread.sleep(500);
		 * driver.findElement(By.xpath("//div[2]/div/div/button/span[2]/span/i")).click(
		 * ); // Edit Profile Thread.sleep(500);
		 * driver.findElement(By.xpath("//div[2]/div[2]")).click(); // Delete
		 * Thread.sleep(500);
		 * driver.findElement(By.xpath("//button[2]/span[2]/span")).click(); // Confirm
		 * Delete
		 */		
        
		//Wait for 5 Sec
		Thread.sleep(1000);
		 
        // Close the driver
        driver.quit();
        
        return result;
    }

}