package com.selenium.framework.datadriven;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.selenium.framework.util.Constants;
import com.selenium.framework.util.Utility;
import com.selenium.framework.util.Xls_Reader;

public class TestBase 
{
  
	public static Properties prop;
	public WebDriver driver;
	public static Logger APPLICATION_LOG ;
	//= Logger.getLogger("devpinoyLogger");
	
	
	public void initLogs(Class<?> class1){

		FileAppender appender = new FileAppender();
		// configure the appender here, with file location, etc
		appender.setFile(System.getProperty("user.dir")+"//target//reports//"+CustomListener.resultFolderName+"//"+class1.getName()+".log");
		appender.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		appender.setAppend(false);
		appender.activateOptions();

		APPLICATION_LOG = Logger.getLogger(class1);
		APPLICATION_LOG.setLevel(Level.DEBUG);
		APPLICATION_LOG.addAppender(appender);
	}
	
	public static void init(){
		if(prop == null){
			String path=System.getProperty("user.dir")+"\\src\\test\\resources\\project.properties";
			
			 prop = new Properties();
			try {
				FileInputStream fs = new FileInputStream(path);
				prop.load(fs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
    public void validateRunmodes(String testName,String suiteName,String dataRunmodes)
    
    {
    	
    	
        boolean issuiteRunmode=   Utility.isSuiteRunnable(suiteName,new Xls_Reader(prop.getProperty("xlsFileLocation")+"Suite.xlsx"));
     
        boolean istestRunmode=   Utility.isTestCaseRunnable(testName,new Xls_Reader(prop.getProperty("xlsFileLocation")+suiteName+".xlsx"));

        boolean dataSetRunmode=false;
		if(dataRunmodes.equals(Constants.RUNMODE_YES))
			dataSetRunmode=true;
	
		if(!(issuiteRunmode && istestRunmode && dataSetRunmode)){
			
				APPLICATION_LOG.debug("Skipping the test "+testName+" inside the suite "+ suiteName);
			throw new SkipException("Skipping the test "+testName+" inside the suite "+ suiteName);
		}
        	
        }
    
	/****************Generic functions*********************/
	public void openBrowser(String browserType){
		
		if(browserType.equals("Mozilla")){
			System.setProperty("webdriver.gecko.driver", prop.getProperty("geckodriver.exe"));
			driver= new FirefoxDriver();
		}
		else if(browserType.equals("Chrome")){
			System.setProperty("webdriver.chrome.driver", prop.getProperty("chromedriver.exe"));
			driver= new ChromeDriver();
		}
		
		else if(browserType.equals("IE")){
			System.setProperty("webdriver.ie.driver", prop.getProperty("IEDriverServer.exe"));
			driver= new InternetExplorerDriver();
		}
		
		else if(browserType.equals("Edge")){
			System.setProperty("webdriver.edge.driver", prop.getProperty("MicrosoftWebdriver.exe"));
			driver= new EdgeDriver();
		}
	
		
		/*try{
		DesiredCapabilities cap = new DesiredCapabilities();
		if(browserType.equals("Mozilla")){
			cap.setBrowserName("firefox");
		}else if(browserType.equals("Chrome")){
			cap.setBrowserName("chrome"); // iexplore
		}
		cap.setPlatform(Platform.ANY);
		try {
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}catch(Exception e){
			Assert.fail("Not able to open browser - "+e.getMessage());
		}
		*/
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
	}
	
	public void navigate(String URLKey){
		driver.get(prop.getProperty(URLKey));
	}
	
	public void click(String identifier){
		try{
		if(identifier.endsWith("_xpath"))
			driver.findElement(By.xpath(prop.getProperty(identifier))).click();
		else if(identifier.endsWith("_id"))
			driver.findElement(By.id(prop.getProperty(identifier))).click();
		else if(identifier.endsWith("_name"))
			driver.findElement(By.name(prop.getProperty(identifier))).click();
		}catch(NoSuchElementException e){
			Assert.fail("Element not found - "+identifier);
		}

	}
	
	public void input(String identifier,String data){
		try{
		if(identifier.endsWith("_xpath"))
			driver.findElement(By.xpath(prop.getProperty(identifier))).sendKeys(data);
		else if(identifier.endsWith("_id"))
			driver.findElement(By.id(prop.getProperty(identifier))).sendKeys(data);
		else if(identifier.endsWith("_name"))
			driver.findElement(By.name(prop.getProperty(identifier))).sendKeys(data);
		}catch(NoSuchElementException e){
			Assert.fail("Element not found - "+identifier);
		}
	}
	
	public boolean verifyTitle(String expectedTitleKey){
		String expectedTitle=prop.getProperty(expectedTitleKey);
		String actualTitle=driver.getTitle();
		if(expectedTitle.equals(actualTitle))
			return true;
		else
			return false;
	}
	
	public boolean isElementPresent(String identifier){
		int size=0;
		if(identifier.endsWith("_xpath"))
			size = driver.findElements(By.xpath(prop.getProperty(identifier))).size();
		else if(identifier.endsWith("_id"))
			size = driver.findElements(By.id(prop.getProperty(identifier))).size();
		else if(identifier.endsWith("_name"))
			size = driver.findElements(By.name(prop.getProperty(identifier))).size();
		else // not in prop file
			size=driver.findElements(By.xpath(identifier)).size();
		
		if(size>0)
			return true;
		else
			return false;
	}
	
	public String getText(String identifier){
		String  text="";
		if(identifier.endsWith("_xpath"))
			text = driver.findElement(By.xpath(prop.getProperty(identifier))).getText();
		else if(identifier.endsWith("_id"))
			text = driver.findElement(By.id(prop.getProperty(identifier))).getText();
		else if(identifier.endsWith("_name"))
			text = driver.findElement(By.name(prop.getProperty(identifier))).getText();
		else
			text=driver.findElement(By.xpath(identifier)).getText();
		
		return text;
		
	}
	
	public void quit(){
		if(driver!=null){
			driver.quit();
			driver=null;
		}
	}
    
    
	/****************application functions*********************/
	
	public void doLogin(String browser,String username,String password){
		openBrowser(browser);
		navigate("URLKey");
		Assert.assertTrue(isElementPresent("money_xpath"),
				"Money Link is not found----money_xpath");
		click("money_xpath");
		click("myportfolio_xpath");
		Assert.assertTrue(verifyTitle("MyPortfoliotitle"),
				"Title doesnot match");
		verifyTitle("MyPortfoliotitle");
		input("Username_xpath", username);
		click("Continue_xpath");
		input("Password_xpath", password);
		click("Continue2_xpath");
	}
	
	
	
	public void doDefaultLogin(String browser){
		doLogin(browser, prop.getProperty("defaultUsername"), prop.getProperty("defaultPassword"));
	}
	
	
	public void checkLeastPerAsset(){
		
	}
	
	
	
	
	
	
    
    
    
    }

