package com.framework.utility;
import java.io.File;
import java.io.FileWriter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.ui.AmazonUI;
import com.opencsv.CSVWriter;

public class BOL {
	static File file=new File("");
	static String currentdirectory=file.getAbsolutePath();
	public static WebDriver driver;
	public static WebDriverWait wait; 
	public static Actions actions = null;
	public static JavascriptExecutor JS;
	
	public static void triggerSafari(String bname, String qty, FileWriter fWriter, CSVWriter writef)
	{
	try
	{
	driver = new SafariDriver();  
	wait=new WebDriverWait(driver,60);
	actions = new Actions(driver);
	JS = (JavascriptExecutor) driver; 
	AmazonUI.getBookDetails(bname, qty,driver,wait,actions,JS,fWriter,writef);
    driver.close();  
     }
	catch(Exception e)
	{
		e.printStackTrace();
	}
	}
	public static void triggerChrome(String bname, String qty,String dpath, FileWriter fWriter, CSVWriter writef)
	{
		try
		{
		System.setProperty("webdriver.chrome.driver",dpath);
		driver= new ChromeDriver();
		wait=new WebDriverWait(driver,30);
		wait=new WebDriverWait(driver,60);
		actions = new Actions(driver);
		JS = (JavascriptExecutor) driver; 
		AmazonUI.getBookDetails(bname, qty,driver,wait,actions,JS,fWriter,writef);
	    driver.close();  
			}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
	public static void triggerFirefox(String bname, String qty, String dpath, FileWriter fWriter, CSVWriter writef)
	{
		try
		{
		System.setProperty("webdriver.gecko.driver", dpath);
		driver= new ChromeDriver();
		wait=new WebDriverWait(driver,30);
		wait=new WebDriverWait(driver,60);
		actions = new Actions(driver);
		JS = (JavascriptExecutor) driver; 
		AmazonUI.getBookDetails(bname, qty,driver,wait,actions,JS,fWriter,writef);
	    driver.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
}
