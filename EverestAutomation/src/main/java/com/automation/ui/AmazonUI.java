package com.automation.ui;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.everest.locators.AmazonLocators;
import com.opencsv.CSVWriter;

public class AmazonUI {

	static int count=0;
	public static void getBookDetails(String bname, String qty, WebDriver driver, WebDriverWait wait, Actions actions,
			JavascriptExecutor JS, FileWriter fWriter, CSVWriter writef) {
		List<String> bookdetails = new ArrayList<>();
		try
		{
		List<String> booklinks=getBookUrls(bname, qty, driver, wait, actions, JS);
		for(int i=0;i<booklinks.size();i++)
		{
			driver.navigate().to(booklinks.get(i).substring(booklinks.get(i).lastIndexOf(";")+1,booklinks.get(i).length()));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));
			if(driver.findElements(By.xpath(AmazonLocators.PUBLISHER)).size()>0)
			{
			try
			{
			bookdetails.add(booklinks.get(i).substring(0, booklinks.get(i).lastIndexOf(";")));
			}
			catch(Exception e)
			{
				bookdetails.add("Null");	
			}
			try
			{
			int pap_prc=0;
			int hrd_prc=0;
			if(driver.findElements(By.xpath(AmazonLocators.LEASTPRICEPAP)).size()>0 || driver.findElements(By.xpath(AmazonLocators.LEASTPRICEHRD)).size()>0)
			{
			if(driver.findElements(By.xpath(AmazonLocators.LEASTPRICEPAP)).size()>0)
			{
			String pap_new=driver.findElement(By.xpath(AmazonLocators.LEASTPRICEPAP)).getText();
		    pap_prc=pap_prc+Integer.parseInt(((pap_new.substring(pap_new.indexOf("₹")+1, pap_new.indexOf(".")).replace(",", "")).trim()));
			}
			if(driver.findElements(By.xpath(AmazonLocators.LEASTPRICEHRD)).size()>0)
			{
			String hrd_new=driver.findElement(By.xpath(AmazonLocators.LEASTPRICEHRD)).getText();
		    hrd_prc=hrd_prc+Integer.parseInt((((hrd_new.substring(hrd_new.indexOf("₹")+1, hrd_new.indexOf(".")).replace(",", ""))).trim()));
			}
		    if(hrd_prc==0 && pap_prc>0)
			{
				bookdetails.add(String.valueOf("₹"+pap_prc));	
			}
			else if(hrd_prc>0 && pap_prc==0)
			{
				bookdetails.add(String.valueOf("₹"+hrd_prc));	
			}
			else if(hrd_prc>0 && pap_prc>0)
			{
				if(hrd_prc>pap_prc)
				{
				bookdetails.add(String.valueOf("₹"+hrd_prc));		
				}
				else if(hrd_prc<pap_prc)
				{
				bookdetails.add(String.valueOf("₹"+pap_prc));		
				}
				else if(hrd_prc==pap_prc)
				{
				bookdetails.add(String.valueOf("₹"+pap_prc));	
				}	
			}
			}
			else
			{
				bookdetails.add("Null");	
			}
			}
			catch(Exception e)
			{
				bookdetails.add("Null");	
			}
			try
			{
			if(driver.findElements(By.className("contributorNameID")).size()>0)	
			{
            bookdetails.add(driver.findElement(By.className("contributorNameID")).getText());
			}
			else
			{
				bookdetails.add("Null");	
			}
			}
			catch(Exception e)
			{
				bookdetails.add("Null");	
			}
			try
			{
	            bookdetails.add(driver.findElement(By.xpath(AmazonLocators.PUBLISHER)).getText());
			}
			catch(Exception e)
			{
				bookdetails.add("Null");	
			}
			try
			{
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(AmazonLocators.ISBN13)));
			if(driver.findElements(By.xpath(AmazonLocators.ISBN13)).size()>0)
			{
            bookdetails.add(driver.findElement(By.xpath(AmazonLocators.ISBN13)).getText());
			}
			else
			{
				bookdetails.add("Null");		
			}
			}
			catch(Exception e)
			{
				bookdetails.add("Null");	
			}
			try
			{
            if(driver.findElements(By.xpath(AmazonLocators.UNAVAILABLE)).size()>0)
            {
                bookdetails.add("Currently unavailable");
	        }
            else
            {
                bookdetails.add("Available");
            }
			}
			catch(Exception e)
			{
				bookdetails.add("Null");	
			}
			try
			{
            if(bookdetails.contains("Currently unavailable"))
            {
                bookdetails.add("0");	
            }
            else
            {
                bookdetails.add(qty);	
            }
			}
			catch(Exception e)
			{
				bookdetails.add("Null");	
			}
			if(bookdetails.contains("Null")) {
			}
			else
			{
				FlipkartUI.getBookDetails(bookdetails,bname, qty,driver,wait,actions,JS,fWriter,writef,count+1);
			}
			bookdetails.clear();
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		}


	public static List<String> getBookUrls(String bname, String qty, WebDriver driver, WebDriverWait wait,
			Actions actions, JavascriptExecutor jS) {
		List<String> bookurls = new ArrayList<String>();
		try {
			driver.navigate().to("https://amazon.in");
			driver.manage().window().maximize();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("twotabsearchtextbox")));
			driver.findElement(By.id("twotabsearchtextbox")).sendKeys(bname + " book" + Keys.RETURN);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(AmazonLocators.BOOKTITLE)));
			List<WebElement> titles = driver.findElements(By.xpath(AmazonLocators.BOOKTITLE));
			List<WebElement> blinks = driver.findElements(By.xpath(AmazonLocators.BOOKLINKS));
			if (titles.size() == blinks.size()) {
				for (int i = 0; i < titles.size(); i++) {
					if (((titles.get(i).getText()).toLowerCase()).startsWith(bname.toLowerCase()) && !((titles.get(i).getText().contains("+")))) {
						bookurls.add(titles.get(i).getText()+";"+blinks.get(i).getAttribute("href").toString());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookurls;
	}
}
