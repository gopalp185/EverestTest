package com.framework.utility;

import java.io.File;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.automation.ui.AmazonUI;

public class Browser {
	static File file = new File("");
	static String currentdirectory = file.getAbsolutePath();
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static Actions actions = null;
	public static JavascriptExecutor JS;
	AmazonUI aui = new AmazonUI();

	public void triggerSafari(String bname, String qty) {
		try {
			driver = new SafariDriver();
			wait = new WebDriverWait(driver, 60);
			actions = new Actions(driver);
			JS = (JavascriptExecutor) driver;
			aui.getamznbookdetails(bname, qty);
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void triggerChrome(String bname, String qty, String dpath) {
		try {
			System.setProperty("webdriver.chrome.driver", dpath);
			driver = new ChromeDriver();
			wait = new WebDriverWait(driver, 30);
			wait = new WebDriverWait(driver, 60);
			actions = new Actions(driver);
			JS = (JavascriptExecutor) driver;
			aui.getamznbookdetails(bname, qty);
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void triggerFirefox(String bname, String qty, String dpath) {
		try {
			System.setProperty("webdriver.gecko.driver", dpath);
			driver = new ChromeDriver();
			wait = new WebDriverWait(driver, 30);
			wait = new WebDriverWait(driver, 60);
			actions = new Actions(driver);
			JS = (JavascriptExecutor) driver;
			aui.getamznbookdetails(bname, qty);
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
