package delta.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import generics.Excel;
import generics.Property;
import generics.Utility;

public class DeltaDriver extends BaseDriver
{	
	String browser  ;
	public String hubURL ;
	@BeforeTest
	public void initFrameWork()
	{
		//eReport = new ExtentReports("./report/results.html");
		eReport = new ExtentReports(reportFilePath);
	}
	
	@BeforeMethod
	public void launchApp(XmlTest xmltest) throws MalformedURLException{
		//String browser = xmltest.getParameter("browser") ;
		browser = xmltest.getParameter("browser") ;
		hubURL = xmltest.getParameter("hubURL") ;
		DesiredCapabilities dc = new DesiredCapabilities() ;
		dc.setBrowserName(browser);
		dc.setPlatform(Platform.ANY);
		driver = new RemoteWebDriver(new URL(hubURL), dc) ;
		/*if(browser.equals("chrome")){
			System.setProperty("webdriver.chrome.driver","./driver/chromedriver.exe" );
			driver= new ChromeDriver() ;
		}
		else{

			driver = new FirefoxDriver();
		}*/
	
		//String configPptPath = "./config/config.properties";
		String appURL = Property.getPropertyValue(configPptPath, "URL");
		String TimeOut = Property.getPropertyValue(configPptPath, "TimeOut");
		
		driver.get(appURL);
		driver.manage().timeouts().implicitlyWait(Long.parseLong(TimeOut), TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
	
	@AfterMethod
	public void quitApp(ITestResult test)
	{
		if(test.getStatus()==ITestResult.FAILURE)
		{
			String pImage = Utility.getPageScreenshot(driver, imageFolderPath);
			String p = testReport.addScreenCapture("."+pImage);
			testReport.log(LogStatus.FAIL, "Page screen shot:" +p);
		}
		eReport.endTest(testReport);
		driver.close();
	}
	
	
	@Test(dataProvider="getScenarios")
	//@Test(dataProvider="getScenarios", dataProviderClass=BaseDriver.class)
	public void testScenarios(String scenarioSheet, String ExecuteStatus)
	{
		//String scenarioPath="./scripts/Scenarios.xlsx";
		//String scenarioSheet = "Scenario1";
		testReport = eReport.startTest(browser +"_" + scenarioSheet);
		if(ExecuteStatus.equalsIgnoreCase("Yes"))
				{
		int stepCount=Excel.getRowCount(scenariosPath, scenarioSheet);
		for(int i=1;i<=stepCount; i++)
			{
				String description = Excel.getCellValue(scenariosPath, scenarioSheet, i, 0);
				String action =  Excel.getCellValue(scenariosPath, scenarioSheet, i, 1);
				String input1 = Excel.getCellValue(scenariosPath, scenarioSheet, i, 2);
				String input2 = Excel.getCellValue(scenariosPath, scenarioSheet, i, 3);
				//System.out.println(description + action + input1 + input2);
				String msg = "description:"+description+"action:"+action+"input1:"+input1+"input2:"+input2;
				testReport.log(LogStatus.INFO, msg);
				Keyword.executeKeyWord(driver, action, input1, input2);
				//Assert.fail();//just to check incase script fails
			}
		}
		else
		{
			testReport.log(LogStatus.SKIP, "Execution Status is 'NO'");
			throw new SkipException("Skipping this scenario");
		}
	}
	
	
	
	@AfterTest
	public void endFrameWork()
	{
		eReport.flush();
	}
}
