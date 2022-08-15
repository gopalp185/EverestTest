package com.everest.main;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import com.everest.api.AmazonAPI;
import com.framework.data.BooksData;
import com.framework.utility.BOL;
import com.framework.utility.PriceComparator;
import com.opencsv.CSVWriter;


public class EverestMain {
	static File file=new File("");
	static String currentdirectory=file.getAbsolutePath();
	public static String logFile;


	public static void main(String[] args) {
		try
		{
	        FileWriter fWriter = new FileWriter(currentdirectory+File.separator+"log.json");
	        FileWriter outputfile = new FileWriter(currentdirectory+File.separator+"pricecomparison.csv");
			CSVWriter writef=new CSVWriter(outputfile);
			String[] allval = {"Book Name Name","Amazon Price","Amazon Availabilty","Flipkart Price","Flipkart Availabity","Quantity Requested","Total Amazon Price","Total Flipkart Price","Cheaper"};
	        writef.writeNext(allval);
	        HashMap<String,String> allbooks=BooksData.readDataLineByLine(currentdirectory+File.separator+"bookList.csv");
		for(int i=1;i<allbooks.size()/2;i++)
		{
			if(args[0].equalsIgnoreCase("API"))
			{
				AmazonAPI.getBookDetails(allbooks.get(BooksData.header.get(0)+"_"+i),allbooks.get(BooksData.header.get(1)+"_"+i),fWriter,writef);	
			}
			else if(args[0].equalsIgnoreCase("UI"))
			{
				if(args[1].equalsIgnoreCase("Safari"))
				{
				BOL.triggerSafari(allbooks.get(BooksData.header.get(0)+"_"+i),allbooks.get(BooksData.header.get(1)+"_"+i),fWriter,writef);	
				}
				else if(args[1].equalsIgnoreCase("Chrome"))
				{
				BOL.triggerChrome(allbooks.get(BooksData.header.get(0)+"_"+i),allbooks.get(BooksData.header.get(1)+"_"+i),currentdirectory+File.separator+"chromedriver",fWriter,writef);		
				}
				else if(args[1].equalsIgnoreCase("Firefox"))
				{
				BOL.triggerFirefox(allbooks.get(BooksData.header.get(0)+"_"+i),allbooks.get(BooksData.header.get(1)+"_"+i),currentdirectory+File.separator+"geckodriver",fWriter,writef);		
				}
				else
				{
				AmazonAPI.getBookDetails(allbooks.get(BooksData.header.get(0)+"_"+i),allbooks.get(BooksData.header.get(1)+"_"+i),fWriter,writef);		
				}
				}

		}
		fWriter.close();
		PriceComparator.masterPriceComparison(writef);
		writef.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	

}
