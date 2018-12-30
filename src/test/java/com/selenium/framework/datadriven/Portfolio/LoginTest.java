package com.selenium.framework.datadriven.Portfolio;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

import java.util.Hashtable;

import org.testng.annotations.Test;

import com.selenium.framework.datadriven.TestBase;
import com.selenium.framework.util.Constants;
import com.selenium.framework.util.ErrorUtil;
import com.selenium.framework.util.TestDataProvider;

public class LoginTest extends TestBase {
	
	@BeforeTest
	public void initLogs(){
		initLogs(this.getClass());
	}

	@Test(dataProviderClass = TestDataProvider.class, dataProvider = "PortFolioTestDataProvider")
	public void loginTest(Hashtable<String, String> table) {

		APPLICATION_LOG.debug("Executing the LoginTest");

		validateRunmodes("LoginTest", Constants.PORTFOLIO_SUITE,
				table.get(Constants.RUNMODE_COL));
		doLogin(table.get(Constants.BROWSER_COL),
				table.get(Constants.USERNAME_COL),
				table.get(Constants.PASSWORD_COL));
		boolean signoutlink = isElementPresent("signout_xpath");

		if (!(((table.get(Constants.EXPECTEDRESULT_COL)
				.equalsIgnoreCase("SUCCESS")) && signoutlink)))
			Assert.fail("Not able to login with correct credentials");
		else if (table.get(Constants.EXPECTEDRESULT_COL).equalsIgnoreCase(
				"FAILURE")) {
			if (signoutlink) {
				Assert.fail("Logged in with wrong credentials");
			}
		}
		
		

		try{
		Assert.assertEquals("A1", "B1");
		}catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);
		}
		
	}
	
	@AfterMethod
	public void close(){
		quit();
	}

}
