package generics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class Utility 
{
	public static String getFormatedDateTime()
	{
		SimpleDateFormat sd = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		return sd.format(new Date());
	}
	
	public static String getPageScreenshot(WebDriver driver, String imageFolderPath)
	{
		String imagePath = imageFolderPath+"/"+getFormatedDateTime()+".png";
		EventFiringWebDriver eDriver = new EventFiringWebDriver(driver);
		
		try
		{
			FileUtils.copyFile(eDriver.getScreenshotAs(OutputType.FILE), new File(imagePath));
		}
		catch(Exception e){}
		
		return imagePath;
	}
}
