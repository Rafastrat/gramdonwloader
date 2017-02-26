package com.tamayovski.gramdownloader.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {

	private static final String BASE_URL = "https://www.instagram.com";

	public List<String> getPrimaryImageUrl(String input) throws IOException {
		System.out.println("Extracting URL publications...");
		List<String> imageUrl = new LinkedList<>();
		File in = new File(input);
		Document doc = Jsoup.parse(in, null);
		Elements pngs = doc.select("a");
		System.out.println("Extracted " + pngs.size() + " urls.");
		//System.out.println("Mostrando urls: ");
		for (Element element : pngs) {

			String url = element.attr("href");
			imageUrl.add(BASE_URL + url);
			//System.out.println(BASE_URL + url);
		}
		return imageUrl;
	}

	public Map<String, String> getHighQualityImageVideoUrl(List<String> publicaciones) throws IOException {
		System.out.println("Extracting URL media files...");
		//System.out.println("Mostrando urls: ");
		Map<String, String> urlFileName = new HashMap<String, String>();
		String dirName = "";

		Integer count = 0;
		for (String url : publicaciones) {
			/*
			 * Document doc =
			 * Jsoup.connect(url).followRedirects(true).ignoreContentType(true).
			 * timeout(12000) // optional .header("Accept-Language",
			 * "pt-BR,pt;q=0.8") // missing .header("Accept-Encoding",
			 * "gzip,deflate,sdch") // missing .userAgent(
			 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"
			 * ) // missing .referrer("http://www.google.com") // optional
			 * .execute().parse();
			 */
			/*
			 * String docu = "";
			 * Observable.from(ClientBuilder.newClient().property(
			 * "CONNECT_TIMEOUT", 10000).property("READ_TIMEOUT",
			 * 10000).target(url).request().async().get(String.class),
			 * Schedulers .newThread()) .subscribe( next ->
			 * System.out.println(next), error -> System.err.println(error), ()
			 * -> System.out.println("Stream ended.") );
			 */
			/*
			 * Element article = doc.select("article").first(); Elements pngs =
			 * article.select("[src]");
			 * 
			 * String urlHighQuality = pngs.get(1).attr("abs:src");
			 * imageUrl.add(urlHighQuality); System.out.println(urlHighQuality);
			 */
			URL urls = new URL(url);
			URLConnection uc = urls.openConnection();
			uc.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			InputStream inputStream = uc.getInputStream();

			String docString = IOUtils.toString(inputStream, "utf-8");

			Document doc = Jsoup.parse(docString, "");
			Elements pngs = doc.select("meta");

			if (pngs.toString().contains("og:video:secure_url") || pngs.toString().contains("og:image")) {

				Integer idxT1, idxT2, idxD1, idxD2, idxD3, idxD4;
				String name = "";
				String imageVideoUrl = "";
				for (Element element : pngs) {

					String property = element.attr("property");

					if (property.contains("og:title")) {
						//System.out.println(element.attr("content"));
						String title = element.attr("content");
						idxT1 = title.indexOf("by");
						idxT2 = title.indexOf(" • ");
						name = "_" + title.substring(idxT1 + 3, idxT2) + "_" + title.substring(idxT2 + 3);
					}

					if (property.contains("og:description")) {
						//System.out.println(element.attr("content"));
						String description = element.attr("content");
						idxD1 = description.indexOf(",");
						idxD2 = description.indexOf("(@");
						idxD3 = description.indexOf("Instagram") - 5;
						idxD4 = description.indexOf("“");
						name = description.substring(idxD2 + 2, idxD3) + "_"
								+ description.substring(idxD4 + 1, description.length() - 1) + "_"
								+ description.substring(0, idxD1) + name;
						dirName = description.substring(idxD2 + 2, idxD3);
					}

					if (property.contains("og:title") || property.contains("og:description")) {
						// name = name.replaceAll(" ", "_").replaceAll(",",
						// "").replaceAll("\\(", "").replaceAll("\\)",
						// "").replaceAll("\\.", "").replaceAll(":", "-");
						name = name.replaceAll("[^\\w ]", "").replaceAll("\\s+", "_");
					}

					if (property.contains("og:video:secure_url")) {
						//System.out.println(element.attr("content"));
						imageVideoUrl = element.attr("content");

					}
					if (property.contains("og:image")) {
						//System.out.println(element.attr("content"));
						imageVideoUrl = element.attr("content");

					}

				}
				System.out.println("Extracting " + count + " of " + publicaciones.size() + " File: " + name);
				count++;
				urlFileName.put(name, imageVideoUrl);
				/*
				 * FileOutputStream outputStream = new FileOutputStream
				 * ("currentImagePage.html");
				 * 
				 * int bytesRead = -1; int BUFFER_SIZE = 4096;
				 * 
				 * byte[] buffer = new byte[BUFFER_SIZE]; while ((bytesRead =
				 * inputStream.read(buffer)) != -1) { outputStream.write(buffer,
				 * 0, bytesRead); }
				 * 
				 * outputStream.close(); inputStream.close();
				 */

			}
		}

		// Creamos directorios
		File video = new File(dirName + "_video");
		if (!video.exists()) {
			video.mkdir();
		}
		File image = new File(dirName + "_image");
		if (!image.exists()) {
			image.mkdir();
		}

		return urlFileName;
	}

	public void downloadMedia(Map<String, String> urlFileName) {
		FileOutputStream fos;
		Integer count = 0;
		System.out.println("Downloading media data...");
		for (Map.Entry<String, String> entry : urlFileName.entrySet()) {
			System.out.println("Downloading " + count + " of " + urlFileName.size() + " from URL: " + entry.getValue());
			count++;
			try {
				URL website = new URL(entry.getValue());
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				if(entry.getValue().contains(".jpg")){
					fos = new FileOutputStream(entry.getKey().substring(0, entry.getKey().indexOf("_")) + "_image/" + entry.getKey() + ".jpg");
				}else{
					fos = new FileOutputStream(entry.getKey().substring(0, entry.getKey().indexOf("_")) + "_video/" + entry.getKey() + ".mp4");
				}
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
