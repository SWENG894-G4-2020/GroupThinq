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
	import java.io.File;  
	import java.io.IOException; 
	
public class TC1_9 {
	
	public static Object main(String[] args) throws InterruptedException, FindFailed {
		ImagePath.setBundlePath("C:\\Users\\non-admin\\groupthinq\\integration\\src\\imgDictionary");
		//Map<String, Object> vars;
		String tcNumber = "TC2_9 ";
		//vars = new HashMap<String, Object>();
		
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
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div[3]/a/span[2]/span/span")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label/div/div/div/input")).sendKeys("1");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[2]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[2]/div/div/div/input")).sendKeys("1");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[3]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[3]/div/div/div/input")).sendKeys("1@1.com");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[4]/div/div/div[2]/i")).click();
	    driver.findElement(By.xpath("//div[3]/div/div[4]/button/span[2]/span")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[5]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[5]/div/div/div/input")).sendKeys("lockMyAccount");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[6]/div/div/div/input")).click();
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[6]/div/div/div/input")).sendKeys("lockMyAccount");
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[3]/div/button[2]/span[2]/span/span")).click();
	    
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".q-btn__content > .material-icons:nth-child(1)"))); // wait for logged-in home screen
		Thread.sleep(1000);
		
	    driver.findElement(By.xpath("//div[@id=\'q-app\']/div/header/div/div[2]/button/span[2]/span/span")).click(); // click account name drop-down
	    driver.findElement(By.xpath("//div[2]/div/div[2]")).click(); // click logout
	    int i = 1;
	    //vars.put("check", js.executeScript("return 1"));
	    do {
	      driver.get("https://staging.groupthinq.us/#/");
	      Thread.sleep(1000);
	      driver.get("https://staging.groupthinq.us/#/login");
	      Thread.sleep(2000);
	      //WebElement usernameField = driver.findElement(By.type("text"));
	      //WebElement usernameField = driver.findElement(By.cssSelector("input"));
	      //String usernameBox = usernameField.getAttribute("type"); 
	      //driver.findElement(By.cssSelector("input[type='text']")).click();    // click on username text box
	      driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label/div/div/div/input")).click();
	      driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label/div/div/div/input")).sendKeys("lockMyAccount");
	      driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[2]/div/div/div/input")).click();
	      if (i < 4) {			//((Boolean) js.executeScript("return (arguments[0] < 4)", vars.get("check"))) {
	        driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[2]/div/div/div/input")).sendKeys("1111");
	      } else {
	        driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[2]/label[2]/div/div/div/input")).sendKeys("lockMyAccount");
	      }
	      driver.findElement(By.xpath("//div[@id=\'q-app\']/div/div/main/div/div[3]/div/button[2]/span[2]/span/span")).click();
	      Thread.sleep(1000);
	      //vars.put("check", js.executeScript("return arguments[0] + 1", vars.get("check")));
	      i ++;
	    } while (i < 5);		//((Boolean) js.executeScript("return (arguments[0] < 5)", vars.get("check")));

		Thread.sleep(1000);
		
        String result = "unchanged";
        Screen screen = new Screen();
        Pattern img = new Pattern("login.png");
        
        try {
            Region region = screen.find(img);
            region.highlight(2);
            result = (tcNumber + "= PASS");
        } catch (Exception e) {
        	result = (tcNumber + "= FAIL");
        }
        
		//Wait for 5 Sec
		Thread.sleep(1000);
		 
        // Close the driver
        driver.quit();
        
        return result;
    }

}