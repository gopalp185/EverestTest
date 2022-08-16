package com.everest.main;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import com.everest.api.AmazonAPI;
import com.framework.data.BooksData;
import com.framework.utility.Browser;
import com.framework.utility.PriceComparator;
import com.opencsv.CSVWriter;

public class EverestMain {
	static File file = new File("");
	static String currentdirectory = file.getAbsolutePath();
	public static String logFile;
	public static FileWriter fWriter;
	public static FileWriter outputfile;
	public static CSVWriter writef;

	public static void main(String[] args) {
		try {
			AmazonAPI api = new AmazonAPI();
			BooksData bd = new BooksData();
			fWriter = new FileWriter(currentdirectory + File.separator + "log.json");
			outputfile = new FileWriter(currentdirectory + File.separator + "pricecomparison.csv");
			writef = new CSVWriter(outputfile);
			String[] allval = { "Book Name Name", "Amazon Price", "Amazon Availabilty", "Flipkart Price",
					"Flipkart Availabity", "Quantity Requested", "Total Amazon Price", "Total Flipkart Price",
					"Cheaper" };
			writef.writeNext(allval);
			HashMap<String, String> allbooks = bd
					.readDataLineByLine(currentdirectory + File.separator + "bookList.csv");
			for (int i = 1; i < allbooks.size() / 2; i++) {
				System.out.println(allbooks.get(bd.header.get(0) + "_" + i));
				Thread.sleep(2000);
				if (args[0].equalsIgnoreCase("API")) {
					api.getBookDetails(allbooks.get(bd.header.get(0) + "_" + i),
							allbooks.get(bd.header.get(1) + "_" + i));
				} else if (args[0].equalsIgnoreCase("UI")) {
					Browser bl = new Browser();
					if (args[1].equalsIgnoreCase("Safari")) {
						bl.triggerSafari(allbooks.get(bd.header.get(0) + "_" + i),
								allbooks.get(bd.header.get(1) + "_" + i));
					} else if (args[1].equalsIgnoreCase("Chrome")) {
						bl.triggerChrome(allbooks.get(bd.header.get(0) + "_" + i),
								allbooks.get(bd.header.get(1) + "_" + i),
								currentdirectory + File.separator + "chromedriver.exe");
					} else if (args[1].equalsIgnoreCase("Firefox")) {
						bl.triggerFirefox(allbooks.get(bd.header.get(0) + "_" + i),
								allbooks.get(bd.header.get(1) + "_" + i),
								currentdirectory + File.separator + "geckodriver.exe");
					} else {
						api.getBookDetails(allbooks.get(bd.header.get(0) + "_" + i),
								allbooks.get(bd.header.get(1) + "_" + i));
					}
				}

			}
			fWriter.close();
			PriceComparator pc = new PriceComparator();
			pc.masterPriceComparison(writef);
			writef.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
