package delta.main;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.xml.XmlTest;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import generics.Excel;
import generics.Property;
import generics.Utility;


public class BaseDriver implements AutomationConstants
{
	public WebDriver driver;
	public static ExtentReports eReport;  // it is being made static else reports will be overrided as if we make it instance different instances will be created
	public ExtentTest testReport;
	

	
	@DataProvider
	public String[][] getScenarios()
	{
		int scenarioCount = Excel.getRowCount(controllerPath, suiteSheet);
		System.out.println(scenarioCount);
//		String[][] data = new String[2][1];
//		data[0][0] = "Scenario1";
//		data[1][0] = "Scenario2";
//		return data;
//		String[][] data = new String[2][1];
//		data[0][0] = Excel.getCellValue(path, sheet, 1, 0);
//		data[1][0] = Excel.getCellValue(path, sheet, 1, 1);
		String[][] data = new String[scenarioCount][2];
		for(int i=1;i<=scenarioCount;i++)
		{
			String scenarioName = Excel.getCellValue(controllerPath, suiteSheet, i, 0);
			String executionStatus = Excel.getCellValue(controllerPath, suiteSheet, i, 1);
			data[i-1][0] = scenarioName;
			data[i-1][1] = executionStatus;
		}
		return data;
	}
	
	
}
