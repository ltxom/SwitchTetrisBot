package me.ltxom.tetrisplayer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

//@SpringBootApplication
public class TetrisplayerApplication {

	public static void main(String[] args) {
		File dir = new File("img/");

		for (File f : dir.listFiles()) {
			System.out.println(f.getPath());

		}
		System.setProperty("java.awt.headless", "false");
		//SpringApplication.run(TetrisplayerApplication.class, args);

//		File image = new File("demo.png");
//		ITesseract iTesseract = new Tesseract1();
//
//		try {
//			String result = iTesseract.doOCR(image);
//			System.out.println(result);
//		} catch (TesseractException e) {
//			e.printStackTrace();
//		}
	}

}
