package com.framework.utility;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.opencsv.CSVWriter;

public class PriceComparator {
public static List<String> mastercomplist=new ArrayList<>();

 
@Test  
public static void testFindCheap(){  
	for(int i=0;i<mastercomplist.size();i++)
	{
	String ofkt=mastercomplist.get(i).substring(mastercomplist.get(i).lastIndexOf(";")+1,mastercomplist.get(i).length());
	int sfp=Integer.parseInt((ofkt.substring(0,ofkt.indexOf("-"))).trim());
    int mfp=Integer.parseInt((ofkt.substring(ofkt.indexOf("-")+1,ofkt.length())).trim());
    String oamzn=mastercomplist.get(i).substring(0,mastercomplist.get(i).lastIndexOf(";"));
	int sap=Integer.parseInt((oamzn.substring(0,oamzn.indexOf("-"))).trim());
    int map=Integer.parseInt((oamzn.substring(oamzn.indexOf("-")+1,oamzn.length())).trim());
 
    assertEquals(sfp,sap);  
    assertEquals(mfp,map);  
	}
}  

public static void comparePrices(List<String> amzbookdetails, List<String> fktbookdetails,int qty, CSVWriter writef) {
try
{
List<String> complist=new ArrayList<>();
if(amzbookdetails.size()==fktbookdetails.size())
{
	int ap=Integer.parseInt(amzbookdetails.get(1).substring(1, amzbookdetails.get(1).length()));
	int fp=Integer.parseInt(fktbookdetails.get(1).substring(1, fktbookdetails.get(1).length()));
	complist.add(amzbookdetails.get(0));
	complist.add(amzbookdetails.get(1));
	complist.add(amzbookdetails.get(5));
	complist.add(fktbookdetails.get(1));
	complist.add(fktbookdetails.get(5));
	complist.add(String.valueOf(qty));
	complist.add("₹"+(ap*qty));
	complist.add("₹"+(fp*qty));
	if(!(amzbookdetails.get(5).equalsIgnoreCase("Available")))
	{
	mastercomplist.add(ap+"-"+(ap*qty)+"!;"+fp+"-"+(fp*qty));
	}
	else if(!(fktbookdetails.get(5).equalsIgnoreCase("Available")))
	{
	mastercomplist.add(ap+"-"+(ap*qty)+";!"+fp+"-"+(fp*qty));	
	}
	else
	{
	mastercomplist.add(ap+"-"+(ap*qty)+";"+fp+"-"+(fp*qty));		
	}
	if((ap*qty)>(fp*qty))
	{
		complist.add("Flipkart");	
	}
	else if((ap*qty)<(fp*qty))
	{
		complist.add("Amazon");
	}
	else
	{
		complist.add("Price same in both");
	}
	String datasetl[]=complist.toArray(new String[complist.size()]);
	writef.writeNext(datasetl);
}
}
catch(Exception e)
{
	e.printStackTrace();
}
	}

	public static void masterPriceComparison(CSVWriter writef) {
		try
		{
			List<String> masterdetails=new ArrayList<>();
			int sap=0;
			int sfp=0;
			int map=0;
			int mfp=0;
			int qtyreq=0;
		for(int i=0;i<mastercomplist.size();i++)
		{
			if(mastercomplist.get(i).contains("!;"))
			{
			String ofkt=mastercomplist.get(i).substring(mastercomplist.get(i).lastIndexOf(";")+1,mastercomplist.get(i).length());
			sfp=sfp+Integer.parseInt((ofkt.substring(0,ofkt.indexOf("-"))).trim());
            mfp=mfp+Integer.parseInt((ofkt.substring(ofkt.indexOf("-")+1,ofkt.length())).trim());
            qtyreq=qtyreq+((Integer.parseInt((ofkt.substring(ofkt.indexOf("-")+1,ofkt.length())).trim())/Integer.parseInt((ofkt.substring(0,ofkt.indexOf("-"))).trim())));
			}
			else if(mastercomplist.get(i).contains(";!"))
			{
			String oamzn=mastercomplist.get(i).substring(0,mastercomplist.get(i).lastIndexOf(";"));
			sap=sap+Integer.parseInt((oamzn.substring(0,oamzn.indexOf("-"))).trim());
            map=map+Integer.parseInt((oamzn.substring(oamzn.indexOf("-")+1,oamzn.length())).trim());
            qtyreq=qtyreq+((Integer.parseInt((oamzn.substring(oamzn.indexOf("-")+1,oamzn.length())).trim())/(Integer.parseInt((oamzn.substring(0,oamzn.indexOf("-"))).trim()))));
			}
			else
			{
				String ofkt=mastercomplist.get(i).substring(mastercomplist.get(i).lastIndexOf(";")+1,mastercomplist.get(i).length());
				sfp=sfp+Integer.parseInt((ofkt.substring(0,ofkt.indexOf("-"))).trim());
	            mfp=mfp+Integer.parseInt((ofkt.substring(ofkt.indexOf("-")+1,ofkt.length())).trim());
	            String oamzn=mastercomplist.get(i).substring(0,mastercomplist.get(i).lastIndexOf(";"));
				sap=sap+Integer.parseInt((oamzn.substring(0,oamzn.indexOf("-"))).trim());
	            map=map+Integer.parseInt((oamzn.substring(oamzn.indexOf("-")+1,oamzn.length())).trim());
	            qtyreq=qtyreq+((Integer.parseInt((ofkt.substring(ofkt.indexOf("-")+1,ofkt.length())).trim())/Integer.parseInt((ofkt.substring(0,ofkt.indexOf("-"))).trim())));
			}
		}
		masterdetails.add("AllAbove Listed Books");
		masterdetails.add("₹"+String.valueOf(sap));
		masterdetails.add("Amazon Book Prices for only available items are calculated");
		masterdetails.add("₹"+String.valueOf(sfp));
		masterdetails.add("Flipkart Book Prices for only available items are calculated");
		masterdetails.add("Total quantity requested:"+qtyreq);
		masterdetails.add("₹"+String.valueOf(map));
		masterdetails.add("₹"+String.valueOf(mfp));
		//testFindCheap();
		if(map>mfp)
		{
			masterdetails.add("Overall Flipkart is cheaper");	
		}
		else if(map<mfp)
		{
			masterdetails.add("Overall Amazon is cheaper");	
		}
		else
		{
			masterdetails.add("Price on both are same");	
		}
		String datasetl1[]=masterdetails.toArray(new String[masterdetails.size()]);
		writef.writeNext(datasetl1);
		}
		catch(Exception e)
		{
		e.printStackTrace();	
		}
		
	}

}
