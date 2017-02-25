package com.tamayovski.gramdownloader.mainapp;

import java.io.IOException;

import com.tamayovski.gramdownloader.utils.Parser;

public class Launcher {

	public static void main(String[] args) {
		Parser parser = new Parser();
		try {
			parser.getHighQualityImageUrl(parser.getPrimaryImageUrl("C:/Users/Rafa/Desktop/new 3.html"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
