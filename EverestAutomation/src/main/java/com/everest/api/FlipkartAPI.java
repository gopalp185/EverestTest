package com.everest.api;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.framework.utility.PriceComparator;
import com.opencsv.CSVWriter;
import com.risksense.generator.logs.SimpleJsonLayout;

import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;

public class FlipkartAPI {
	static File file=new File("");
	static String currentdirectory=file.getAbsolutePath();

	public static void getBookDetails(List<String> bookdetails, String bname, String qty, FileWriter fWriter, CSVWriter writef, int count) {
		try
		{
			List<String> fbookdetails=new ArrayList<>();
			boolean amzavl;
			boolean fblavl;
			Response response = given()
					.config(RestAssuredConfig.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
					.header("accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
					.header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
					.header("cookie",
							"T=TI166027313445200351826564609219911580556350964553898322418653353832; AMCVS_17EB401053DAF4840A490D4C%40AdobeOrg=1; pxcts=bc66dfaf-19ea-11ed-8f92-505055585a45; _pxvid=bc66d490-19ea-11ed-8f92-505055585a45; AMCV_17EB401053DAF4840A490D4C%40AdobeOrg=-227196251%7CMCIDTS%7C19217%7CMCMID%7C32977095239867732368740709998129814741%7CMCAID%7CNONE%7CMCOPTOUT-1660280349s%7CNONE%7CMCAAMLH-1660877949%7C12%7CMCAAMB-1660877949%7Cj8Odv6LonN4r3an7LhD3WZrU1bUpAkFkkiY1ncBR96t2PTI; __pxvid=235eeae1-1aca-11ed-aae1-0242ac120002; Network-Type=4g; s_sq=flipkart-prd%3D%2526pid%253Dwww.flipkart.com%25253A%2526pidt%253D1%2526oid%253DfunctionEr%252528%252529%25257B%25257D%2526oidt%253D2%2526ot%253DDIV; SN=VI9B16DAA1DC584D35A19A6FFDA4E50337.TOK70BA0973B27145ED884744AC3751B4BC.1660385613.LO; _px3=e417349810926c4f1379d32fc8294c027d2b426815fd6595e5330aa1ad3a17e0:bFr7/VkBZnKiQCVPVrvtnV7a/RX/Zaq4A93gnnQ6EsihE6FcTbwYkDgxnmFqDg5Rt1gy5OSaK+FQtzs93O51xw==:1000:E9QHMJ3QhufJ8HU00Bq4xfyHWEwdjA+1xmmzTxxxjf5I7UpF7PYfhtp7WWFz26HH9mq2Kex4++KWdOn+yX4X5vF/sCikowzU0SXkkgcoZrMH/TBkCF7wJ2pO6juWf5XY08M2sN32weOO0fwLashfFK78zAUx0UIcr1zAao1tdBw4mCoQXf7mvI9lsGCdHtSe9nTWagYo9fIJJXIwgJZMwQ==; S=d1t18cj9rND87UVM/Pz8/NyMRP1UzJz7aY/z16lszpW6UTDfUJP+gnXnWvD90tjSHxVX1J9Hh7/+G3bKkCmDJMVkYuQ==; qH=6af85ed33aa6bf39")
					.header("cp-extension-installed", "Yes")
					.when().get("https://www.flipkart.com/"+bname.replace(" ", "-")+"/p/itmfc9jxsc7dckfm?pid="+(bookdetails.get(4).replace("-", "")).trim()).then().extract().response();
			String htmlBody = (response.getBody().asString());
			Document document = Jsoup.parse(htmlBody);
			if(bname.length()>0)
			{
			fbookdetails.add(bname);
			}
			else
			{
		    fbookdetails.add("Null");	
			}
			String valpor=(document.select(".col-8-12").html());
			Document documentx = Jsoup.parse(valpor);
			if(documentx.select("._16Jk6d").size()>0)
			{
			fbookdetails.add(documentx.select("._16Jk6d").text());
			}
			else
			{
				fbookdetails.add("Null");	
			}
			if(documentx.select("._14Me7y").size()>0)
			{
			fbookdetails.add(documentx.select("._14Me7y").text());
			}
			else
			{
				fbookdetails.add("Null");	
			}
			int pbsize=documentx.select("._21Ahn-").size();
			if(pbsize>1)
			{
			for(int i=0;i<pbsize;i++)
			{
				String pball=documentx.select("._21Ahn-").get(i).text();
				if(pball.contains("Publisher"))
				{
					String pbsr=(pball.substring(pball.lastIndexOf(":")+1,pball.length())).trim();
					fbookdetails.add(pbsr);
				}
			}
			}
			else
			{
				fbookdetails.add("Null");	
			}
			fbookdetails.add(bookdetails.get(4));
			int bnsize=documentx.select("._3AWRsL").size();
			if(bnsize>0)
			{
				fbookdetails.add("Available");	
			}
			else
			{
				fbookdetails.add("Currently unvailable");	
			}
			if(qty.length()>0)
			{
			fbookdetails.add(qty);
			}
			else
			{
				fbookdetails.add("Null");	
			}
			System.out.println("Amazon API:"+bookdetails);
			System.out.println("Flipkart API:"+fbookdetails);
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
	
	}
		