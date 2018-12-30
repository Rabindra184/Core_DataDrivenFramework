package com.selenium.framework.datadriven.Portfolio;
import java.util.Hashtable;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.selenium.framework.datadriven.TestBase;
import com.selenium.framework.util.Constants;
import com.selenium.framework.util.TestDataProvider;

public class LeastPerAssetTest extends TestBase {
	
	@BeforeTest
	public void initLogs(){
		initLogs(this.getClass());
	}
	
	@Test(dataProviderClass=TestDataProvider.class,dataProvider="PortFolioTestDataProvider")
	public void leastPerAssetTest(Hashtable<String,String> table){
		
		validateRunmodes("LeastPerAssetTest",Constants.PORTFOLIO_SUITE,table.get(Constants.RUNMODE_COL));
		APPLICATION_LOG.debug("The Execution started for LeasrPerAsset Test");
		doDefaultLogin(table.get(Constants.BROWSER_COL));
		String leasetpertext=getText("leastPerAsset_xpath");
		String temp[] = leasetpertext.split("\\(");
		String compName = temp[0].trim();
		String percentageChange = temp[1].split("\\)")[0].split("%")[0];
		
		
		APPLICATION_LOG.debug(percentageChange);
		Assert.assertTrue(isElementPresent("//a[text()='"+compName+"']"), "Lease per asset company not found in table "+ compName);
		Assert.assertTrue(isElementPresent("//td/span[text()='"+percentageChange+"']"), "xxx");
		
		//String x = driver.findElement(By.xpath("//td/span[text()='-73.64']")).getText();
		//System.out.println(x);
		
		// searching inside table
		
	}
	@AfterMethod
	public void close(){
		quit();
	}
		
}
