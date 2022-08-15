package com.everest.locators;

public class AmazonLocators {
	public static final String JSCLICK = "arguments[0].click();";
	public static final String BOOKTITLE = "//span[contains(@class,\"a-size-medium a-color-base a-text-normal\")]";
	public static final String BOOKLINKS = "//span[contains(@class,\"a-size-medium a-color-base a-text-normal\")]/ancestor::node()[1]";
	public static final String LEASTPRICEPAP ="//a[contains(@href,\"pap_new\")][contains(@class,\"a-size-mini a-link-normal\")]";
	public static final String LEASTPRICEHRD ="//a[contains(@href,\"hrd_new\")][contains(@class,\"a-size-mini a-link-normal\")]";
	public static final String PUBLISHER ="//span[text()=\"Publisher\"]/ancestor::node()[contains(@class,\"a-section rpi-attribute-content\")]/div[contains(@class,\"a-section a-spacing-none a-text-center rpi-attribute-value\")]/span";
	public static final String ISBN13 ="//span[contains(text(),\"ISBN-13\")][contains(@class,\"a-text-bold\")]/ancestor::node()[contains(@class,\"a-list-item\")]/span[2]";
	public static final String UNAVAILABLE ="//span[contains(text(),\"Currently unavailable\")]";
}
