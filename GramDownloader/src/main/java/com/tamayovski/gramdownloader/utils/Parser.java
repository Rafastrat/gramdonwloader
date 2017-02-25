package com.tamayovski.gramdownloader.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {

	private static final String BASE_URL = "https://www.instagram.com";

	public List<String> getPrimaryImageUrl(String input) throws IOException {
		System.out.println("Extrayendo urls de publicaciones...");
		List<String> imageUrl = new LinkedList<>();
		File in = new File(input);
		Document doc = Jsoup.parse(in, null);
		Elements pngs = doc.select("a");
		System.out.println("Extraidas " + pngs.size() + " urls.");
		System.out.println("Mostrando urls: ");
		for (Iterator iterator = pngs.iterator(); iterator.hasNext();) {
			Element element = (Element) iterator.next();
			String url = element.attr("href");
			imageUrl.add(BASE_URL + url);
			System.out.println(BASE_URL + url);
		}
		return imageUrl;
	}
	public List<String> getHighQualityImageUrl(List<String> publicaciones) throws IOException {
		System.out.println("Extrayendo urls para imagenes de alta calidad...");
		System.out.println("Mostrando urls: ");
		List<String> imageUrl = new LinkedList<>();
		
		for (String url : publicaciones) {
			/*Document doc = Jsoup.connect(url).followRedirects(true).ignoreContentType(true).timeout(12000) // optional
					.header("Accept-Language", "pt-BR,pt;q=0.8") // missing
					.header("Accept-Encoding", "gzip,deflate,sdch") // missing
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36") // missing
					.referrer("http://www.google.com") // optional
					.execute().parse();*/
			/*String docu = "";
			Observable.from(ClientBuilder.newClient().property("CONNECT_TIMEOUT", 10000).property("READ_TIMEOUT", 10000).target(url).request().async().get(String.class), Schedulers
		            .newThread())
		            .subscribe(
		                    next -> System.out.println(next),
		                    error -> System.err.println(error),
		                    () -> System.out.println("Stream ended.")
		            );*/
			/*Element article = doc.select("article").first();
			Elements pngs = article.select("[src]");

			String urlHighQuality = pngs.get(1).attr("abs:src");
			imageUrl.add(urlHighQuality);
			System.out.println(urlHighQuality);*/
			URL urls = new URL(url);
			URLConnection uc = urls.openConnection();
	        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
	        InputStream inputStream = uc.getInputStream();
	        
	        String docString = IOUtils.toString(inputStream, "utf-8");
	        
	        Document doc = Jsoup.parse(docString, "");
			Elements pngs = doc.select("meta");
			System.out.println(pngs);
	        /*FileOutputStream  outputStream = new FileOutputStream ("currentImagePage.html");

	        int bytesRead = -1;
	        int BUFFER_SIZE = 4096;

	        byte[] buffer = new byte[BUFFER_SIZE];
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }

	        outputStream.close();
	        inputStream.close();*/
			
		}

		return imageUrl;
	}
}
