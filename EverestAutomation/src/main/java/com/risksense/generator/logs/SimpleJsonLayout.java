package com.risksense.generator.logs;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleJsonLayout  {

	private static String ISBN="";
	private static String BNAME="";
	private static String PUBLISHER="";
	private static String AUTHOR="";
	private static String QTY="";
	private static String AMZNQTY="";
	private static String FLPQTY="";
	private static String AMZNPRICE="";
	private static boolean AMZNAVL;
	private static String FLPPRICE="";
	private static boolean FLPAVL;
	private static int tcount=0;
	public static String logvalues(int count,String isbn, String bname, String pblsr,String author,String qty,String amznprice,boolean amznavl,String amznqty,String flpprice,boolean flpavl,String flpqty)
	{
		String logs=" ";
		try
		{
		ISBN=isbn;
		BNAME=bname;
		PUBLISHER=pblsr;
		AUTHOR=author;
		QTY=qty;
		AMZNPRICE=amznprice;
		AMZNAVL=amznavl;
		AMZNQTY=amznqty;
		FLPPRICE=flpprice;
		FLPAVL=flpavl;
		FLPQTY=flpqty;
		tcount=tcount+count;
		logs=logs+format();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return logs;
	}
	public static String format() {


		Map<String,Object> r = new LinkedHashMap<>();
		String logs="";
		try
		{
			r.put("book ISBN", ISBN);
    		r.put("Book Name", BNAME);
    		r.put("Publisher", PUBLISHER);
         	r.put("Author",AUTHOR);
     		r.put("QTY",QTY);
     		r.put("AmzPrice", AMZNPRICE);
    		r.put("AmzIsavailable",AMZNAVL);
     		r.put("AmzQTY",AMZNQTY);
    		r.put("FlpPrice", FLPPRICE);
    		r.put("FlpIsavailable",FLPAVL);
 		    r.put("FlpQTY",FLPQTY);
 		  logs=logs+ formatlogs(r,tcount);
 		    
		}
		catch(NullPointerException npe)
		{
			npe.printStackTrace();
		}
		return logs;
	}

	private static String formatlogs(Map<String, Object> r,int count) {
		String logsform="\r\n{\r\n\""+"book"+(tcount)+": "+r.get("book ISBN")+"\"\r\n{\r\n"
				+ "\"Book Name\":\""+r.get("Book Name")+"\",\r\n"
				+ "\"Publisher\":\""+r.get("Publisher")+"\",\r\n"
				+ "\"Author\":\""+r.get("Author")+"\",\r\n"
				+ "\"Qty\":"+r.get("QTY")+",\r\n"
				+ "\"Amazon\":{\r\n"
				+ "\"Price\":\""+r.get("AmzPrice")+"\",\r\n"
				+ "\"Is available\":\""+r.get("AmzIsavailable")+"\",\r\n"
				+ "\"Quantity\":\""+r.get("AmzQTY")+"\"\r\n"
				+ "},\r\n"
				+ "\"Flipkart\":{\r\n"
				+ "\"Price\":\""+r.get("FlpPrice")+"\",\r\n"
				+ "\"Is available\":\""+r.get("FlpIsavailable")+"\",\r\n"
				+ "\"Quantity\":\""+r.get("FlpQTY")+"\"\r\n"
				+ "}\r\n"
				+ "},";
		return logsform;
		
	}
	
}