package com.everest.api;

import static io.restassured.RestAssured.given;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.everest.main.EverestMain;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;

public class AmazonAPI {

	public static Document docaudio;
	static int count = 0;

	public void getBookDetails(String bname, String qty) {
		List<String> bookdetails = new ArrayList<>();
		try {
			List<String> booklinks = getBookUrls(bname);
			String htmlBody = "";
			for (int i = 0; i < booklinks.size(); i++) {
				Response response = given()
						.config(RestAssuredConfig.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
						.header("authority", "www.amazon.in")
						.header("accept",
								"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
						.header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
						.header("cookie",
								"session-id=261-6163985-4538505; ubid-acbin=258-8836448-6306921; x-acbin=\"dFTPpXKL?A5cuMwthAXwUoDIvJAFPf9mRX?ZASdKmXei4upSLQfgj@mpUHMYrhQa\"; at-acbin=Atza|IwEBIL1sZcEfaWQV-RaoDmXmc_0ce41Rl0wCi0EaZZ3qHuWm8W3Vf7FJgbc9kp9YLZsSu8X6FdrKFuYZcLjFS8ymyFKY-iJUXJLNbmbyBwsDMPIVChggxuuhufTZ2NSRgLBc4ydKV6FyAXEHQstbbaAsMneBeLN-eN58SdFdxd4be2ohPpMd2qSGwX2mLxy9eMxWUf9mqSl4EiLQOALnIetE6les_Oxd2G1QHwdgJNcU9qQ9PA; sess-at-acbin=\"XZkkPjqsvgzMKz4y2XdfSDaWQcrGhsRN+4TdNJ5p9F8=\"; lc-acbin=en_IN; i18n-prefs=INR; csm-hit=tb:VSN0P96YMEBABWRCA01D+s-VSN0P96YMEBABWRCA01D|1660244885304&t:1660244885304&adb:adblk_no; session-token=\"fQFCORopdxhkLvHVDpA4GcPp86uOqckDMUZ6/CL+KCHMjO7NIdl54TqzjvCzkG0l9PUFQVhGYq+V3crJT++BN4BW0jQinS8bqXegBXi9UzgHghINzs2GVf8mA3Cm7Gg+F5Z3D9hPjPix0Hp99ODfghYM+9IhTFrbY1TGE/1Hf/R5WRJj+Yz2Osf6lcwnA2BdSeBo+BBfHy+22xQt7Doyx8GJL3FA3DfaAvTDGzoC+GiapExrwg9NZA==\"; session-id-time=2082758401l")
						.header("cp-extension-installed", "Yes").when().get(booklinks.get(i)
								.substring(booklinks.get(i).lastIndexOf(";") + 1, booklinks.get(i).length()))
						.then().extract().response();
				htmlBody = (response.getBody().asString());
				try {
					Document document = Jsoup.parse(htmlBody);
					try {
						docaudio = Jsoup.parse(String.valueOf(document.select(".rpi-attribute-value").get(0)));
					} catch (Exception e) {
						docaudio = Jsoup.parse("<span>Null</span>");
					}
					if ((docaudio.select("span").text().contains("minute"))
							|| (booklinks.get(i).substring(0, booklinks.get(i).lastIndexOf(";"))).contains(":")) {
						System.out.println("No comparison Audio Book!!");
					} else {
						bookdetails.add(booklinks.get(i).substring(0, booklinks.get(i).lastIndexOf(";")));
						try {
							int flag = 0;
							int pap_prc = 0;
							int hrd_prc = 0;
							int prsize = document.select(".a-size-mini").size();
							for (int c = 0; c < prsize; c++) {
								if (document.select(".a-size-mini").get(c).attr("href").contains("pap_new")) {
									String pap_new = ((document.select(".a-size-mini").get(c).text()
											.substring(document.select(".a-size-mini").get(c).text().lastIndexOf("from")
													+ 4, document.select(".a-size-mini").get(c).text().length())
											.trim()));
									pap_prc = pap_prc + Integer.parseInt(
											((pap_new.substring(1, pap_new.lastIndexOf(".")).replace(",", "")).trim()));
									flag++;
								} else if (document.select(".a-size-mini").get(c).attr("href").contains("hrd_new")) {
									String hrd_new = ((document.select(".a-size-mini").get(c).text()
											.substring(document.select(".a-size-mini").get(c).text().lastIndexOf("from")
													+ 4, document.select(".a-size-mini").get(c).text().length())
											.trim()));
									hrd_prc = hrd_prc + Integer.parseInt(
											((hrd_new.substring(1, hrd_new.lastIndexOf(".")).replace(",", "")).trim()));
									flag++;
								}
							}
							if (flag == 0) {
								if (document.select("#price").text().length() > 1) {
									bookdetails.add(document.select("#price").text());
								} else {
									bookdetails.add("Null");
								}
							} else if (flag > 0) {
								if (hrd_prc == 0 && pap_prc > 0) {
									bookdetails.add(String.valueOf("₹" + pap_prc));
								} else if (hrd_prc > 0 && pap_prc == 0) {
									bookdetails.add(String.valueOf("₹" + hrd_prc));
								} else if (hrd_prc > 0 && pap_prc > 0) {
									if (hrd_prc > pap_prc) {
										bookdetails.add(String.valueOf("₹" + hrd_prc));
									} else if (hrd_prc < pap_prc) {
										bookdetails.add(String.valueOf("₹" + pap_prc));
									} else if (hrd_prc == pap_prc) {
										bookdetails.add(String.valueOf("₹" + pap_prc));
									}
								}

							}

						} catch (Exception e) {
							bookdetails.add("Null");
						}
						try {
							String author = (document.select(".contributorNameID").text());
							if (author.length() == 0) {
								bookdetails.add("Null");
							} else {
								bookdetails.add(author);
							}
						} catch (Exception e) {
							bookdetails.add("Null");
						}
						try {
							String corbody = "";
							int corsz = document.select(".a-carousel-viewport").size();
							for (int q = 0; q < corsz; q++) {
								if (document.select(".a-carousel-viewport").get(q).html().contains("Publisher")) {
									corbody = corbody + document.select(".a-carousel-viewport").get(q).html();
								}
							}
							Document documentx = Jsoup.parse(corbody);
							int ls = documentx.select(".rpi-attribute-label").size();
							int ps = documentx.select(".rpi-attribute-value").size();
							if (ls == ps) {
								for (int x = 0; x < ls; x++) {
									if (documentx.select(".rpi-attribute-label").get(x).text().contains("Publisher")) {
										String publisher = (documentx.select(".rpi-attribute-value").get(x).html());
										Document documenty = Jsoup.parse(publisher);
										if (publisher.length() == 0) {
											bookdetails.add("Null");
										} else {
											bookdetails.add(documenty.select("span").text());
										}
									}
								}
							}

						} catch (Exception e) {
							bookdetails.add("Null");
						}

						try {
							int flag = 0;
							int isbnsz = (document.select(".a-text-bold")).size();
							for (int a = 0; a < isbnsz; a++) {
								if (document.select(".a-text-bold").get(a).text().contains("ISBN-13")) {
									bookdetails.add(document.select(".a-text-bold").get(a).nextElementSibling().text());
									flag++;
								}

							}
							if (flag == 0) {
								bookdetails.add("Null");
							}
						} catch (Exception e) {
							bookdetails.add("Null");
						}
						try {
							int flag = 0;
							int isbnsz = (document.select(".a-text-bold")).size();
							for (int x = 0; x < isbnsz; x++) {
								if (document.select(".a-text-bold").get(x).text().contains("Currently unavailable")) {
									bookdetails.add("Unavailable");
									flag++;
								}
							}
							if (flag == 0) {
								bookdetails.add("Available");
							}
						} catch (Exception e) {
							bookdetails.add("Null");
						}
						if (bookdetails.contains("Null")) {
							System.out.println("No comparison Kindle E-Book!");
						} else {
							if (bookdetails.contains("Unavailable")) {
								bookdetails.add("0");
							} else if (bookdetails.contains("Available")) {
								bookdetails.add(qty);
							}
						}
						if (!(bookdetails.contains("Null"))) {
							FlipkartAPI fapi = new FlipkartAPI();
							fapi.getBookDetails(bookdetails, bname, qty, EverestMain.fWriter, EverestMain.writef,
									count + 1);
						}
						bookdetails.clear();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getBookUrls(String bname) {

		List<String> bookurls = new ArrayList<>();
		try {
			String htmlBody = "";
			Response response = given()
					.config(RestAssuredConfig.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
					.header("accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
					.header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
					.header("cookie",
							"PHPSESSID=h2ltucv8lbhplmreeqbgmkpjjb; _ga=GA1.2.575772891.1660296590; _gid=GA1.2.1329958957.1660296590")
					.header("cp-extension-installed", "Yes").when().get("https://www.amazon.in/s?k="
							+ ((bname.replace(" ", "+")).replace("'s", "s")) + "&i=stripbooks&ref=nb_sb_noss")
					.then().extract().response();
			htmlBody = (response.getBody().asString());
			Document document = Jsoup.parse(htmlBody);
			String cpath = (document.select(".s-title-instructions-style")).html();
			Document documentx = Jsoup.parse(cpath);
			int psize = (documentx.select(".a-size-medium")).size();
			for (int b = 0; b < psize; b++) {
				if (((documentx.select(".a-size-medium").get(b).text()).toLowerCase()).startsWith(bname.toLowerCase())
						&& !(documentx.select(".a-size-medium").get(b).text().contains("+"))) {
					String bnameNURl = documentx.select(".a-size-medium").get(b).text() + ";" + ("https://amazon.in"
							+ (documentx.select(".a-size-medium").get(b).parentNode().attr("href")));
					bookurls.add(bnameNURl);
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return bookurls;
	}

}