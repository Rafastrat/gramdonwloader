package com.tamayovski.gramdownloader.mainapp;

import java.io.IOException;

import com.tamayovski.gramdownloader.utils.Parser;

public class Launcher {

	public static void main(String[] args) {
		Parser parser = new Parser();
		try {
			//parser.downloadMedia(parser.getHighQualityImageVideoUrl(parser.getPrimaryImageUrl("C:/Users/Rafa/Desktop/taylor/s.html")));
			parser.downloadMedia(parser.getHighQualityImageVideoUrl(parser.getPrimaryImageUrl(args[0])));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
