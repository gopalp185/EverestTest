package com.automation.ui;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.everest.locators.AmazonAllLocators;
import com.framework.utility.Browser;

public class AmazonUI {

	static int count = 0;

	public void getamznbookdetails(String bname, String qty) {
		List<String> amznbookdetails = new ArrayList<>();
		try {
			List<String> booklinks = getBookUrls(bname, qty);
			for (int i = 0; i < booklinks.size(); i++) {
				Browser.driver.navigate().to(
						booklinks.get(i).substring(booklinks.get(i).lastIndexOf(";") + 1, booklinks.get(i).length()));
				Thread.sleep(2000);
				Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));
				if (Browser.driver.findElements(By.xpath(AmazonAllLocators.PUBLISHER)).size() > 0) {
					try {
						amznbookdetails.add(booklinks.get(i).substring(0, booklinks.get(i).lastIndexOf(";")));
					} catch (Exception e) {
						amznbookdetails.add("Null");
					}
					try {
						int paperback_price = 0;
						int hardbook_price = 0;
						if (Browser.driver.findElements(By.xpath(AmazonAllLocators.LEASTPRICEPAP)).size() > 0
								|| Browser.driver.findElements(By.xpath(AmazonAllLocators.LEASTPRICEHRD)).size() > 0) {
							if (Browser.driver.findElements(By.xpath(AmazonAllLocators.LEASTPRICEPAP)).size() > 0) {
								String paperback_new = Browser.driver
										.findElement(By.xpath(AmazonAllLocators.LEASTPRICEPAP)).getText();
								paperback_price = paperback_price + Integer.parseInt(((paperback_new
										.substring(paperback_new.indexOf("₹") + 1, paperback_new.indexOf("."))
										.replace(",", "")).trim()));
							}
							if (Browser.driver.findElements(By.xpath(AmazonAllLocators.LEASTPRICEHRD)).size() > 0) {
								String hardbook_new = Browser.driver.findElement(By.xpath(AmazonAllLocators.LEASTPRICEHRD))
										.getText();
								hardbook_price = hardbook_price + Integer.parseInt((((hardbook_new
										.substring(hardbook_new.indexOf("₹") + 1, hardbook_new.indexOf("."))
										.replace(",", ""))).trim()));
							}
							if (hardbook_price == 0 && paperback_price > 0) {
								amznbookdetails.add(String.valueOf("₹" + paperback_price));
							} else if (hardbook_price > 0 && paperback_price == 0) {
								amznbookdetails.add(String.valueOf("₹" + hardbook_price));
							} else if (hardbook_price > 0 && paperback_price > 0) {
								if (hardbook_price > paperback_price) {
									amznbookdetails.add(String.valueOf("₹" + hardbook_price));
								} else if (hardbook_price < paperback_price) {
									amznbookdetails.add(String.valueOf("₹" + paperback_price));
								} else if (hardbook_price == paperback_price) {
									amznbookdetails.add(String.valueOf("₹" + paperback_price));
								}
							}
						} else {
							amznbookdetails.add("Null");
						}
					} catch (Exception e) {
						amznbookdetails.add("Null");
					}
					try {
						if (Browser.driver.findElements(By.className("contributorNameID")).size() > 0) {
							amznbookdetails
									.add(Browser.driver.findElement(By.className("contributorNameID")).getText());
						} else {
							amznbookdetails.add("Null");
						}
					} catch (Exception e) {
						amznbookdetails.add("Null");
					}
					try {
						amznbookdetails.add(Browser.driver.findElement(By.xpath(AmazonAllLocators.PUBLISHER)).getText());
					} catch (Exception e) {
						amznbookdetails.add("Null");
					}
					try {
						Browser.wait.until(
								ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(AmazonAllLocators.ISBN13)));
						if (Browser.driver.findElements(By.xpath(AmazonAllLocators.ISBN13)).size() > 0) {
							amznbookdetails.add(Browser.driver.findElement(By.xpath(AmazonAllLocators.ISBN13)).getText());
						} else {
							amznbookdetails.add("Null");
						}
					} catch (Exception e) {
						amznbookdetails.add("Null");
					}
					try {
						if (Browser.driver.findElements(By.xpath(AmazonAllLocators.UNAVAILABLE)).size() > 0) {
							amznbookdetails.add("Currently unavailable");
						} else {
							amznbookdetails.add("Available");
						}
					} catch (Exception e) {
						amznbookdetails.add("Null");
					}
					try {
						if (amznbookdetails.contains("Currently unavailable")) {
							amznbookdetails.add("0");
						} else {
							amznbookdetails.add(qty);
						}
					} catch (Exception e) {
						amznbookdetails.add("Null");
					}
					if (amznbookdetails.contains("Null")) {
					} else {
						FlipkartUI fui = new FlipkartUI();
						fui.getBookDetails(amznbookdetails, bname, qty, count + 1);
					}
					amznbookdetails.clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getBookUrls(String bname, String qty) {
		List<String> bookurls = new ArrayList<String>();
		try {
			Browser.driver.navigate().to("https://amazon.in");
			Browser.driver.manage().window().maximize();
			Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("twotabsearchtextbox")));
			Browser.driver.findElement(By.id("twotabsearchtextbox")).sendKeys(bname + " book" + Keys.RETURN);
			Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(AmazonAllLocators.BOOKTITLE)));
			List<WebElement> titles = Browser.driver.findElements(By.xpath(AmazonAllLocators.BOOKTITLE));
			List<WebElement> booklinks = Browser.driver.findElements(By.xpath(AmazonAllLocators.BOOKLINKS));
			if (titles.size() == booklinks.size()) {
				for (int i = 0; i < titles.size(); i++) {
					if (((titles.get(i).getText()).toLowerCase()).startsWith(bname.toLowerCase())
							&& !((titles.get(i).getText().contains("+")))) {
						bookurls.add(titles.get(i).getText() + ";" + booklinks.get(i).getAttribute("href").toString());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookurls;
	}
}
