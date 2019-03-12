package me.ltxom.tetrisplayer.service.cv;

import me.ltxom.tetrisplayer.entity.cv.ScreenRobot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;

@Service
public class ScreenStreamService {
    @Autowired
    protected ScreenRobot screenRobot;

    public static BufferedImage imgToGray(Image image) {
        ImageFilter filter = new GrayFilter(true, 10);
        ImageProducer producer = new FilteredImageSource(image.getSource(), filter);
        Image result = Toolkit.getDefaultToolkit().createImage(producer);
        return toBufferedImage(result);
    }

    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }

    public BufferedImage captureScreen(Rectangle rectangle) {
        BufferedImage screenshot = null;
        try {
            screenshot = screenRobot.getRobot().createScreenCapture(rectangle);
        } catch (HeadlessException e) {
            e.printStackTrace();
        }
        return screenshot;
    }

    public Robot getRobot() {
        return screenRobot.getRobot();
    }
/*    public Mat cunnyScreenshot(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(image, image, new Size(2, 2));
        double lowThresh = 130;//双阀值抑制中的低阀值
        double heightThresh = 290;//双阀值抑制中的高阀值
        Imgproc.Canny(image, image, lowThresh, heightThresh);

        return image;
    }*/

/*    public List<MatOfPoint> findContours(Mat cannyOutput) {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_SIMPLE);

        return contours;
    }*/
}

