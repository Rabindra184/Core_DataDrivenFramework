package com.selenium.framework.util;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

import com.selenium.framework.datadriven.TestBase;

public class TestDataProvider {

	@DataProvider(name="PortFolioTestDataProvider")
		public static Object[][] getDataSuiteA(Method M){
      TestBase.init();
	Xls_Reader xls1= new Xls_Reader(TestBase.prop.getProperty("xlsFileLocation")+Constants.PORTFOLIO_SUITE+".xlsx");
	
	return Utility.getData(M.getName(), xls1);	
		
	}
	
	@DataProvider(name="StockSuiteTestDataProvider")
	public static Object[][] getDataSuiteB(Method M){
		TestBase.init();
Xls_Reader xls1= new Xls_Reader(TestBase.prop.getProperty("xlsFileLocation")+Constants.STOCK_SUITE+".xlsx");
return Utility.getData(M.getName(), xls1);	
	
}
	
	
	
}
