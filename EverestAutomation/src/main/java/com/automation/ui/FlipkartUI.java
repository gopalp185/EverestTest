package com.automation.ui;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.everest.locators.FlipkartLocators;
import com.framework.utility.PriceComparator;
import com.opencsv.CSVWriter;
import com.risksense.generator.logs.SimpleJsonLayout;

public class FlipkartUI {
	
	public static void getBookDetails(List<String> bookdetails, String bname, String qty, WebDriver driver,
			WebDriverWait wait, Actions actions, JavascriptExecutor jS, FileWriter fWriter, CSVWriter writef,int count) {
		List<String> fbookdetails = new ArrayList<>();
		boolean amzavl;
		boolean fblavl;
		try
		{
			driver.navigate().to("https://www.flipkart.com/"+(bname.replace(" ", "-")).replace("'s", "s")+"/p/itmfc7hkcke9bnyh?pid="+(bookdetails.get(4).replace("-", "")).trim());
			if(driver.findElements(By.className("_2RZvAZ")).size()== 0)
			{
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(FlipkartLocators.MRP)));
		fbookdetails.add(bookdetails.get(0));
		try
		{
		if(driver.findElements(By.xpath(FlipkartLocators.MRP)).size()>0)
		{
		fbookdetails.add(driver.findElement(By.xpath(FlipkartLocators.MRP)).getText());
		}
		else
		{
			fbookdetails.add("Null");	
		}
		}
		catch(Exception e)
		{
			fbookdetails.add("Null");	
		}
		try
		{
		if(driver.findElements(By.className("_14Me7y")).size()>0)
		{
		fbookdetails.add(driver.findElement(By.className("_14Me7y")).getText());
		}
		else
		{
			fbookdetails.add("Null");	
		}
		}
		catch(Exception e)
		{
			fbookdetails.add("Null");
		}
		try
		{
		if(driver.findElements(By.className("_21Ahn-")).size()>0)
		{
       List<WebElement> highlights=driver.findElements(By.className("_21Ahn-"));
        for(int i=0;i<highlights.size();i++)
        {
        	if((highlights.get(i).getText()).contains("Publisher"))
        	{
        		fbookdetails.add((highlights.get(i).getText().substring(highlights.get(i).getText().indexOf(":")+1, highlights.get(i).getText().length())).trim());
         	}
        }
		}
		else
		{
			fbookdetails.add("Null");
		}
		}
		catch(Exception e)
		{
			fbookdetails.add("Null");	
		}
		fbookdetails.add(bookdetails.get(4));
		try
		{
		if(driver.findElements(By.xpath(FlipkartLocators.SOLDOUT)).size()>0)
		{
			fbookdetails.add("Currently unavailable");
			fbookdetails.add("0");	
		}
		else
		{
			fbookdetails.add("Available");
			if(driver.findElements(By.className("_2JC05C")).size()>0)
			{
				int remqty=regexInteger(driver.findElement(By.className("_2JC05C")).getText());
				if((Integer.parseInt(qty.trim())>remqty))
						{
					fbookdetails.add(String.valueOf((Integer.parseInt(qty.trim())-remqty)));
						}
				else if((Integer.parseInt(qty.trim())<remqty))
				{
					fbookdetails.add(String.valueOf(remqty));
				}
				else
				{
					fbookdetails.add(String.valueOf(qty));	
				}
		}
			else
			{
				fbookdetails.add(String.valueOf(qty));		
			}
		}
		}
		catch(Exception e)
		{
			fbookdetails.add("Null");
			fbookdetails.add("0");	
		}
			}
			else
			{
				fbookdetails.add(bookdetails.get(0));
				for(int i=0;i<5;i++)
				{
				fbookdetails.add("Page not Available");
				}
				fbookdetails.add("0");
			}
			System.out.println("Amazon UI:"+bookdetails);
			System.out.println("Flipkart UI:"+fbookdetails);
			PriceComparator.comparePrices(bookdetails,fbookdetails,Integer.parseInt(qty.trim()),writef);
			String qtyx="";
			if(Integer.parseInt(bookdetails.get(bookdetails.size()-1))>Integer.parseInt(fbookdetails.get(fbookdetails.size()-1)))
			{
			qtyx=qtyx+bookdetails.get(bookdetails.size()-1);
			}
			else if(Integer.parseInt(bookdetails.get(bookdetails.size()-1))<Integer.parseInt(fbookdetails.get(fbookdetails.size()-1)))
			{
			qtyx=qtyx+fbookdetails.get(fbookdetails.size()-1);	
			}
			else
			{
			qtyx=qtyx+fbookdetails.get(fbookdetails.size()-1);		
			}
            if(bookdetails.get(5).equalsIgnoreCase("Available"))
            {
            amzavl=true;	
            }
            else
            {
            amzavl=false;	
            }
            if(fbookdetails.get(5).equalsIgnoreCase("Available"))
            {
            fblavl=true;	
            }
            else
            {
            fblavl=false;	
            }
			String logs=SimpleJsonLayout.logvalues(count,bookdetails.get(4),bname,bookdetails.get(3),bookdetails.get(2),qtyx,bookdetails.get(1),amzavl,bookdetails.get(bookdetails.size()-1),fbookdetails.get(1),fblavl,fbookdetails.get(fbookdetails.size()-1));
			fWriter.write(logs);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Integer regexInteger(String rq)
	{
		int ql=0;
		try
		{
			 Pattern p = Pattern.compile("\\d+");
		        Matcher m = p.matcher(rq);
		        while(m.find()) {
		            ql=ql+Integer.parseInt(m.group(0));
		        }	
		}
		catch(Exception e)
		{
		e.printStackTrace();	
		}
		return ql;
		
	}

}
