package me.ltxom.tetrisplayer.service.cv;

import me.ltxom.tetrisplayer.entity.cv.ScreenRobot;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class ScreenStreamService {
	@Autowired
	protected ScreenRobot screenRobot;

	public BufferedImage captureScreen(Rectangle rectangle) {
		BufferedImage screenshot = null;
		try {
			screenshot = screenRobot.getRobot().createScreenCapture(rectangle);
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
		return screenshot;
	}

	public Robot getRobot(){
		return screenRobot.getRobot();
	}

	public Mat cunnyScreenshot(Mat image){
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(image, image, new Size(2, 2));
		double lowThresh = 113;//双阀值抑制中的低阀值
		double heightThresh = 290;//双阀值抑制中的高阀值
		Imgproc.Canny(image, image, lowThresh, heightThresh);

		return image;
	}
}

