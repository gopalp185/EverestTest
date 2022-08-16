package com.framework.utility;

import java.util.ArrayList;
import java.util.List;

import com.everest.main.EverestMain;
import com.opencsv.CSVWriter;

public class PriceComparator {
	public static List<String> mastercomparisonlist = new ArrayList<>();

	public void comparePrices(List<String> amzbookdetails, List<String> fktbookdetails, int qty) {
		try {
			List<String> comparisonlist = new ArrayList<>();
			if (amzbookdetails.size() == fktbookdetails.size()) {
				int amznprice = Integer.parseInt(amzbookdetails.get(1).substring(1, amzbookdetails.get(1).length()));
				int fktprice = Integer.parseInt(fktbookdetails.get(1).substring(1, fktbookdetails.get(1).length()));
				comparisonlist.add(amzbookdetails.get(0));
				comparisonlist.add(amzbookdetails.get(1));
				comparisonlist.add(amzbookdetails.get(5));
				comparisonlist.add(fktbookdetails.get(1));
				comparisonlist.add(fktbookdetails.get(5));
				comparisonlist.add(String.valueOf(qty));
				comparisonlist.add("₹" + (amznprice * qty));
				comparisonlist.add("₹" + (fktprice * qty));


				if (!(amzbookdetails.get(5).equalsIgnoreCase("Available"))) {
					//If the book is not available on Amazon, we pass "!;" to the arraylist
					mastercomparisonlist
							.add(amznprice + "-" + (amznprice * qty) + "!;" + fktprice + "-" + (fktprice * qty));
				} else if (!(fktbookdetails.get(5).equalsIgnoreCase("Available"))) {
					//If the book is not available on Flipkart, we pass ";!" to the arraylist
					mastercomparisonlist
							.add(amznprice + "-" + (amznprice * qty) + ";!" + fktprice + "-" + (fktprice * qty));
				} else {
					//If the book is available on both the platforms we pass ";" to the arraylist
					mastercomparisonlist
							.add(amznprice + "-" + (amznprice * qty) + ";" + fktprice + "-" + (fktprice * qty));
				}
				if ((amznprice * qty) > (fktprice * qty)) {
					comparisonlist.add("Flipkart");
				} else if ((amznprice * qty) < (fktprice * qty)) {
					comparisonlist.add("Amazon");
				} else {
					comparisonlist.add("Price same in both");
				}
				String datasetl[] = comparisonlist.toArray(new String[comparisonlist.size()]);
				EverestMain.writef.writeNext(datasetl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void masterPriceComparison(CSVWriter writef) {
		try {
			List<String> masterdetails = new ArrayList<>();
			List<String> notavllist = new ArrayList<>();
			int singlebookamznprice = 0;
			int singlebookfktprice = 0;
			int multibookamznprice = 0;
			int multibookfktprice = 0;
			int qtyreq = 0;

			for (int i = 0; i < mastercomparisonlist.size(); i++) {
				if (mastercomparisonlist.get(i).contains("!;")) {
					String avlonlyfkt = mastercomparisonlist.get(i).substring(
							mastercomparisonlist.get(i).lastIndexOf(";") + 1, mastercomparisonlist.get(i).length());
					singlebookfktprice = singlebookfktprice
							+ Integer.parseInt((avlonlyfkt.substring(0, avlonlyfkt.indexOf("-"))).trim());
					multibookfktprice = multibookfktprice + Integer
							.parseInt((avlonlyfkt.substring(avlonlyfkt.indexOf("-") + 1, avlonlyfkt.length())).trim());
					qtyreq = qtyreq + ((Integer
							.parseInt((avlonlyfkt.substring(avlonlyfkt.indexOf("-") + 1, avlonlyfkt.length())).trim())
							/ Integer.parseInt((avlonlyfkt.substring(0, avlonlyfkt.indexOf("-"))).trim())));
					notavllist.add("Flipkart");
				} else if (mastercomparisonlist.get(i).contains(";!")) {
					String avlonlyamzn = mastercomparisonlist.get(i).substring(0,
							mastercomparisonlist.get(i).lastIndexOf(";"));
					singlebookamznprice = singlebookamznprice
							+ Integer.parseInt((avlonlyamzn.substring(0, avlonlyamzn.indexOf("-"))).trim());
					multibookamznprice = multibookamznprice + Integer.parseInt(
							(avlonlyamzn.substring(avlonlyamzn.indexOf("-") + 1, avlonlyamzn.length())).trim());
					qtyreq = qtyreq + ((Integer.parseInt(
							(avlonlyamzn.substring(avlonlyamzn.indexOf("-") + 1, avlonlyamzn.length())).trim())
							/ (Integer.parseInt((avlonlyamzn.substring(0, avlonlyamzn.indexOf("-"))).trim()))));
					notavllist.add("Amazon");
				} else {
					String avlonlyfkt = mastercomparisonlist.get(i).substring(
							mastercomparisonlist.get(i).lastIndexOf(";") + 1, mastercomparisonlist.get(i).length());
					singlebookfktprice = singlebookfktprice
							+ Integer.parseInt((avlonlyfkt.substring(0, avlonlyfkt.indexOf("-"))).trim());
					multibookfktprice = multibookfktprice + Integer
							.parseInt((avlonlyfkt.substring(avlonlyfkt.indexOf("-") + 1, avlonlyfkt.length())).trim());
					String avlonlyamzn = mastercomparisonlist.get(i).substring(0,
							mastercomparisonlist.get(i).lastIndexOf(";"));
					singlebookamznprice = singlebookamznprice
							+ Integer.parseInt((avlonlyamzn.substring(0, avlonlyamzn.indexOf("-"))).trim());
					multibookamznprice = multibookamznprice + Integer.parseInt(
							(avlonlyamzn.substring(avlonlyamzn.indexOf("-") + 1, avlonlyamzn.length())).trim());
					qtyreq = qtyreq + ((Integer
							.parseInt((avlonlyfkt.substring(avlonlyfkt.indexOf("-") + 1, avlonlyfkt.length())).trim())
							/ Integer.parseInt((avlonlyfkt.substring(0, avlonlyfkt.indexOf("-"))).trim())));
				}
			}
			masterdetails.add("AllAbove Listed Books");
			masterdetails.add("₹" + String.valueOf(singlebookamznprice));
			masterdetails.add("Amazon Book Prices for only available items are calculated");
			masterdetails.add("₹" + String.valueOf(singlebookfktprice));
			masterdetails.add("Flipkart Book Prices for only available items are calculated");
			masterdetails.add("Total quantity requested:" + qtyreq);
			masterdetails.add("₹" + String.valueOf(multibookamznprice));
			masterdetails.add("₹" + String.valueOf(multibookfktprice));
			// testFindCheap();
			if (multibookamznprice > multibookfktprice && !(notavllist.contains("Flipkart"))
					&& notavllist.contains("Amazon")) {
				masterdetails.add("Overall Flipkart is cheaper");
			} else if (multibookamznprice < multibookfktprice && !(notavllist.contains("Amazon"))
					&& notavllist.contains("Flipkart")) {
				masterdetails.add("Overall Amazon is cheaper");
			} else if (multibookamznprice > multibookfktprice && notavllist.size() == 0) {
				masterdetails.add("Overall Flipkart is cheaper");
			} else if (multibookamznprice < multibookfktprice && notavllist.size() == 0) {
				masterdetails.add("Overall Amazon is cheaper");
			} else if (multibookamznprice > multibookfktprice && notavllist.contains("Flipkart")
					&& notavllist.contains("Amazon")) {
				masterdetails.add("Overall Flipkart is cheaper");
			} else if (multibookamznprice < multibookfktprice && notavllist.contains("Flipkart")
					&& notavllist.contains("Amazon")) {
				masterdetails.add("Overall Amazon is cheaper");
			}
			String datasetl1[] = masterdetails.toArray(new String[masterdetails.size()]);
			writef.writeNext(datasetl1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
