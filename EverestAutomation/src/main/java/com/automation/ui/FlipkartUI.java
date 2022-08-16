package com.automation.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.everest.locators.FlipkartAllLocators;
import com.everest.main.EverestMain;
import com.framework.utility.Browser;
import com.framework.utility.PriceComparator;
import com.risksense.generator.logs.LogsGenerator;

public class FlipkartUI {

	public void getBookDetails(List<String> amznbookdetails, String bname, String qty, int count) {
		List<String> fktbookdetails = new ArrayList<>();
		boolean amzavl;
		boolean fblavl;
		try {
			Browser.driver.navigate().to("https://www.flipkart.com/" + (bname.replace(" ", "-")).replace("'s", "s")
					+ "/p/itmfc7hkcke9bnyh?pid=" + (amznbookdetails.get(4).replace("-", "")).trim());
			if (Browser.driver.findElements(By.className("_2RZvAZ")).size() == 0) {
				Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(FlipkartAllLocators.MRP)));
				fktbookdetails.add(amznbookdetails.get(0));
				try {
					if (Browser.driver.findElements(By.xpath(FlipkartAllLocators.MRP)).size() > 0) {
						fktbookdetails.add(Browser.driver.findElement(By.xpath(FlipkartAllLocators.MRP)).getText());
					} else {
						fktbookdetails.add("Null");
					}
				} catch (Exception e) {
					fktbookdetails.add("Null");
				}
				try {
					if (Browser.driver.findElements(By.className("_14Me7y")).size() > 0) {
						fktbookdetails.add(Browser.driver.findElement(By.className("_14Me7y")).getText());
					} else {
						fktbookdetails.add("Null");
					}
				} catch (Exception e) {
					fktbookdetails.add("Null");
				}
				try {
					if (Browser.driver.findElements(By.className("_21Ahn-")).size() > 0) {
						List<WebElement> highlights = Browser.driver.findElements(By.className("_21Ahn-"));
						for (int i = 0; i < highlights.size(); i++) {
							if ((highlights.get(i).getText()).contains("Publisher")) {
								fktbookdetails.add((highlights.get(i).getText().substring(
										highlights.get(i).getText().indexOf(":") + 1,
										highlights.get(i).getText().length())).trim());
							}
						}
					} else {
						fktbookdetails.add("Null");
					}
				} catch (Exception e) {
					fktbookdetails.add("Null");
				}
				fktbookdetails.add(amznbookdetails.get(4));
				try {
					if (Browser.driver.findElements(By.xpath(FlipkartAllLocators.SOLDOUT)).size() > 0) {
						fktbookdetails.add("Currently unavailable");
						fktbookdetails.add("0");
					} else {
						fktbookdetails.add("Available");
						if (Browser.driver.findElements(By.className("_2JC05C")).size() > 0) {
							int remqty = regexInteger(Browser.driver.findElement(By.className("_2JC05C")).getText());
							if ((Integer.parseInt(qty.trim()) > remqty)) {
								fktbookdetails.add(String.valueOf((Integer.parseInt(qty.trim()) - remqty)));
							} else if ((Integer.parseInt(qty.trim()) < remqty)) {
								fktbookdetails.add(String.valueOf(remqty));
							} else {
								fktbookdetails.add(String.valueOf(qty));
							}
						} else {
							fktbookdetails.add(String.valueOf(qty));
						}
					}
				} catch (Exception e) {
					fktbookdetails.add("Null");
					fktbookdetails.add("0");
				}
			} else {
				fktbookdetails.add(amznbookdetails.get(0));
				for (int i = 0; i < 5; i++) {
					fktbookdetails.add("Page not Available");
				}
				fktbookdetails.add("0");
			}
			System.out.println("Amazon UI:" + amznbookdetails);
			System.out.println("Flipkart UI:" + fktbookdetails);
			PriceComparator pc = new PriceComparator();
			pc.comparePrices(amznbookdetails, fktbookdetails, Integer.parseInt(qty.trim()));
			String qtyx = "";
			if (Integer.parseInt(amznbookdetails.get(amznbookdetails.size() - 1)) > Integer
					.parseInt(fktbookdetails.get(fktbookdetails.size() - 1))) {
				qtyx = qtyx + amznbookdetails.get(amznbookdetails.size() - 1);
			} else if (Integer.parseInt(amznbookdetails.get(amznbookdetails.size() - 1)) < Integer
					.parseInt(fktbookdetails.get(fktbookdetails.size() - 1))) {
				qtyx = qtyx + fktbookdetails.get(fktbookdetails.size() - 1);
			} else {
				qtyx = qtyx + fktbookdetails.get(fktbookdetails.size() - 1);
			}
			if (amznbookdetails.get(5).equalsIgnoreCase("Available")) {
				amzavl = true;
			} else {
				amzavl = false;
			}
			if (fktbookdetails.get(5).equalsIgnoreCase("Available")) {
				fblavl = true;
			} else {
				fblavl = false;
			}
			LogsGenerator smpl = new LogsGenerator();
			String logs = smpl.logvalues(count, amznbookdetails.get(4), bname, amznbookdetails.get(3),
					amznbookdetails.get(2), qtyx, amznbookdetails.get(1), amzavl,
					amznbookdetails.get(amznbookdetails.size() - 1), fktbookdetails.get(1), fblavl,
					fktbookdetails.get(fktbookdetails.size() - 1));
			EverestMain.fWriter.write(logs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer regexInteger(String rq) {
		int ql = 0;
		try {
			Pattern p = Pattern.compile("\\d+");
			Matcher m = p.matcher(rq);
			while (m.find()) {
				ql = ql + Integer.parseInt(m.group(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ql;

	}

}
