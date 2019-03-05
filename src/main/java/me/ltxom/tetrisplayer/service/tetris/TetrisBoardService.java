package me.ltxom.tetrisplayer.service.tetris;

import me.ltxom.tetrisplayer.entity.cv.TraceResult;
import me.ltxom.tetrisplayer.entity.tetris.TetrisBoard;
import me.ltxom.tetrisplayer.entity.tetris.TetrisBoardQueue;
import me.ltxom.tetrisplayer.service.cv.ScreenStreamService;
import me.ltxom.tetrisplayer.util.CVUtil;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class TetrisBoardService {
	private static final double RATIO_LEFT_PART = 233;
	private static final double RATIO_LEFT_BAR = 60;
	private static final double RATIO_BOARD = 200;
	private static final double RATIO_RIGHT_BAR = 60;
	private static final double RATIO_RIGHT_PART = 233;
	private static final double RATIO_DENOMINATOR =
			RATIO_LEFT_PART + RATIO_LEFT_BAR + RATIO_BOARD + RATIO_RIGHT_BAR + RATIO_RIGHT_PART;
	private static final float PREDICT_ROWS = 22.3f;
	private static final float PREDICT_COLS = 36.9f;

	private static final float RESERVE_RATIO_PERCENTAGE = .8f;
	private static final float LINE_VARIANCE_THRESHOLD_PERCENTAGE = .3f;
	@Autowired
	private TetrisBoardQueue tetrisBoardQueue;
	@Autowired
	private ScreenStreamService screenStreamService;

	public static TetrisBoard analyzeBoardByImage(BufferedImage bufferedImage) {
		Mat mat = CVUtil.bufferedImageToMat(bufferedImage);
		mat = new ScreenStreamService().cunnyScreenshot(mat);
		Size screenSize = mat.size();
		int predictBlockLength = (int) (screenSize.height / PREDICT_ROWS);
		int predictBlockLength2 = (int) (screenSize.width / PREDICT_COLS);
		if (Math.abs(predictBlockLength - predictBlockLength2) / ((predictBlockLength + predictBlockLength2) / 2) > (1 - RESERVE_RATIO_PERCENTAGE)) {
			System.err.println("Please check if the board has correctly set up");
		}
		predictBlockLength = (predictBlockLength + predictBlockLength2) / 2;

		int holdX =
				(int) (screenSize.width * ((RATIO_LEFT_BAR + RATIO_LEFT_PART) / RATIO_DENOMINATOR) * RESERVE_RATIO_PERCENTAGE);
		int holdY = (int) (screenSize.height * (1 / PREDICT_ROWS) * RESERVE_RATIO_PERCENTAGE);
		holdX = traceCoordinateInYDirection(bufferedImage, holdX, holdY,
				(int) (screenSize.width * (RATIO_LEFT_PART + RATIO_LEFT_BAR) / RATIO_DENOMINATOR),
				(int) (screenSize.height * (5 / PREDICT_ROWS)), (int) (screenSize.height * (2.5 / PREDICT_ROWS)), false,
				LINE_VARIANCE_THRESHOLD_PERCENTAGE).get(0).getValue();
		ArrayList<TraceResult> list = traceCoordinateInXDirection(bufferedImage, holdX, holdY + predictBlockLength / 2,
				(int) (holdX + predictBlockLength * 2.7),
				(holdY + predictBlockLength * 2), (int) (predictBlockLength * 2.7), true,
				LINE_VARIANCE_THRESHOLD_PERCENTAGE);
		holdY = list.get(1).getValue();
		System.out.println(list.toString());
		System.out.println(holdX + ":" + holdY);
		System.out.println(predictBlockLength + ":" + predictBlockLength2);
		return null;
	}

	private static ArrayList<TraceResult> traceCoordinateInYDirection(BufferedImage bufferedImage, int startX,
																	  int startY, int endX,
																	  int endY, int yCount, boolean doubleAnalysis,
																	  float variancePercentage) {
		ArrayList<TraceResult> results = new ArrayList<TraceResult>();
		int previous = 0;
		int sumOfWhite = 0;
		for (int x = startX; x < endX; x++) {
			if (doubleAnalysis) previous = sumOfWhite;
			sumOfWhite = 0;
			for (int y = startY; y < endY; y++) {
				if ((bufferedImage.getRGB(x, y) & 0x000000FF) != 0) {
					sumOfWhite++;
				}
			}
			float confidence = Math.abs(((sumOfWhite + previous) / (float) yCount) - 1);
			if (confidence < variancePercentage) {
				results.add(new TraceResult(1 - confidence, x));
			}
		}
		return results;
	}

	private static ArrayList<TraceResult> traceCoordinateInXDirection(BufferedImage bufferedImage, int startX,
																	  int startY, int endX,
																	  int endY, int xCount, boolean doubleAnalysis,
																	  float variancePercentage) {
		ArrayList<TraceResult> results = new ArrayList<TraceResult>();
		int previous = 0;
		int sumOfWhite = 0;
		for (int y = startY; y < endY; y++) {
			if (doubleAnalysis) previous = sumOfWhite;
			sumOfWhite = 0;
			for (int x = startX; x < endX; x++) {
				if (((bufferedImage.getRGB(x, y) >> 8) & 0x000000FF) != 0) {
					sumOfWhite++;
				}
			}
			float confidence = Math.abs(((sumOfWhite + previous) / (float) xCount) - 1);
			if (confidence < variancePercentage) {
				results.add(new TraceResult(1 - confidence, y));
			}

		}
		return results;
	}

	private static int[] traceCoordinateInXDirection(Mat mat, int startX, int startY, int endX, int endY, int xCount,
													 float confidence) {
		return null;
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		File file = new File("out/img/1551761006389.png");
		try {
			BufferedImage bi = ImageIO.read(file);
			analyzeBoardByImage(bi);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
