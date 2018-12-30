package com.selenium.framework.datadriven.StockSuite;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.selenium.framework.datadriven.TestBase;
import com.selenium.framework.util.Constants;
import com.selenium.framework.util.TestDataProvider;

public class AddStock extends TestBase{
	
	@BeforeTest
	public void initLogs(){
		initLogs(this.getClass());
	}
	
	@Test(dataProviderClass=TestDataProvider.class,dataProvider="StockSuiteTestDataProvider")
	public void AddStock(Hashtable<String,String> table) throws InterruptedException{
	
	
		validateRunmodes("AddStock", Constants.STOCK_SUITE, table.get("Runmode"));
				doDefaultLogin(table.get(Constants.BROWSER_COL));
				click("AddStock_xpath");
				
				Actions builder = new Actions(driver);
			
				
				Actions seriesOfActions = builder.sendKeys(driver.findElement(By.xpath(prop.getProperty("StockName_xpath"))), table.get("Stock Name"),Keys.SPACE,Keys.CONTROL+"T").keyUp(Keys.CONTROL).click();
				seriesOfActions.perform();				
				//driver.findElement(By.xpath(prop.getProperty("StockName_xpath"))).clear();
				//driver.findElement(By.xpath(prop.getProperty("StockName_xpath"))).sendKeys(table.get("Stock Name"),Keys.BACK_SPACE,Keys.ARROW_DOWN,Keys.DOWN,Keys.ENTER);
				//Thread.sleep(50000);
						
				//input("StockName_xpath", table.get("Stock Name"));
				click("calendar_xpath");
				String date=table.get("PurchaseDate");
				System.out.println(date);
				
				Date currentDate = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date dateToBeSelected =null;
				try {
					 dateToBeSelected = formatter.parse(date);
					 System.out.println(dateToBeSelected);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String month=new SimpleDateFormat("MMMM").format(dateToBeSelected);		
				Calendar cal = Calendar.getInstance();
			    cal.setTime(dateToBeSelected);
			    int year = cal.get(Calendar.YEAR);
			    int day = cal.get(Calendar.DAY_OF_MONTH);
			    String month_yearExpected = month+" "+year;
			    
				while(true){
					
					String month_yearDisplayed = getText("monthAndYearText_xpath");
					if(month_yearDisplayed.equals(month_yearExpected))
						break; // correct month
					
					if(currentDate.after(dateToBeSelected))
						click("calBack_xpath");
					else
						click("calFront_xpath");
				}
				
				driver.findElement(By.xpath("//td[text()='"+day+"']")).click();
				//input("DateofPurchase_xpath", table.get("PurchaseDate"));
				input("Quantity_xpath", table.get("Quantity"));
				input("PurchasePrice_xpath", table.get("Price"));
				click("AddStcok2_xpath");
				click("AddStcok2_xpath");
	}
	
	@AfterMethod
	
	public void close(){
		quit();
	}

}
