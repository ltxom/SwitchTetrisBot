package me.ltxom.tetrisplayer.service.tetris;

import me.ltxom.tetrisplayer.entity.tetris.*;
import me.ltxom.tetrisplayer.service.cv.ScreenStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class TetrisBoardService {
	private static final double startX = 299.0;
	private static final double startY = 399.0;
	private static final int brightDx = (int) (startX - 295.0);
	private static final int brightDy = (int) (startY - 394.0);
	private static final double blockLength = 19.333;
	private static final int nextBlockIX = 498;
	private static final int nextBlockIY = 53;
	private static final int nextBlockTX = 498;
	private static final int nextBlockTY = 63;
	private static final int nextBlockOX = 509;
	private static final int nextBlockOY = 52;
	private static final int nextBlockJX = 519;
	private static final int nextBlockJY = 64;
	private static final int nextBlockSX = 509;
	private static final int nextBlockSY = 63;
	private static final int nextBlockZX = 509;
	private static final int nextBlockZY = 63;
	private static final int nextBlockLX = 498;
	private static final int nextBlockLY = 63;
	private static final int nextFollowingBlockStartX = 507;
	private static final int nextFollowingBlockStartY = 89;
	private static final int nextBlockLength = 11;
	private static final int nextFollowingBlockLength = 9;
	private static final int nextFollowingBlockInterval = 15;
	private static final int nextColorThreshold = 36;
	private static final int attacksX = 274;
	private static final int attacksY = 399;
	@Autowired
	private ScreenStreamService screenStreamService;

	public static void main(String[] args) {
//        Block block = new BlockZ();
//        int[][] a = block.getStandardSpaceMatrix();
		BufferedImage image = null;

		try {
			image = ImageIO.read(new File("img/1551760979071.png"));
			image = ScreenStreamService.imgToGray(image);
			ImageIO.write(image, "png", new File("demo.png"));
			TetrisBoardService tetrisBoardService = new TetrisBoardService();
			TetrisBoard tetrisBoard = tetrisBoardService.analyzeBoardByImg(new Date(), image);
			tetrisBoard.setFallingBlock(tetrisBoard.getNextBlocks()[0]);
			TetrisBoardService.getNextLocation(tetrisBoard);
//			int[][] result = sumBoardAndBlockMatrix(tetrisBoard.getBoardMatrix(),tetrisBoard.getFallingBlock()
//			.getRotationalSpaceMatrix()[2],3);


			PossibleMove move = train(tetrisBoard, 1, 1);
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static PossibleMove train(TetrisBoard tetrisBoard, int expectedDeltaX, int expectedRotation) {

		List<double[]> possibleValues = new ArrayList<>();
		HashMap<PossibleMove, double[]> bestMoves = new HashMap<>();

		for (double leavingHoldScore = -1.5; leavingHoldScore > -2.5; leavingHoldScore -= 0.1) {
			for (double havingNoBlockAbove = -1; havingNoBlockAbove > -2; havingNoBlockAbove -= 0.1) {
				//System.out.println(leavingHoldScore+" "+havingNoBlockAbove);
				for (double completeLineScore = 18; completeLineScore < 20; completeLineScore += 0.1) {
					for (double heightDeductScore = -9; heightDeductScore > -11; heightDeductScore -= 0.1) {
						for (double depthWeight = 3; depthWeight < 5; depthWeight += 0.1) {
							for (double fillSpaceScore = 4; fillSpaceScore < 5; fillSpaceScore += 0.1) {
								TetrisBoard.leavingHoldScore = leavingHoldScore;
								TetrisBoard.havingNoBlockAbove = havingNoBlockAbove;
								TetrisBoard.fillSpaceScore = fillSpaceScore;
								TetrisBoard.heightDeductScore = heightDeductScore;
								TetrisBoard.depthWeight = depthWeight;
								TetrisBoard.completeLineScore = completeLineScore;
								PossibleMove move = TetrisBoardService.getNextLocation(tetrisBoard);
								if (move.getRotation() == expectedRotation && move.getMove().getDeltaX() == expectedDeltaX) {
									double[] data = new double[]{leavingHoldScore, havingNoBlockAbove,
											completeLineScore,
											heightDeductScore,
											depthWeight, fillSpaceScore};
									bestMoves.put(move, data);
									possibleValues.add(data);
									System.out.println(leavingHoldScore + "\t" + havingNoBlockAbove + "\t" + completeLineScore + "\t" + heightDeductScore + "\t" +
											depthWeight + "\t" + fillSpaceScore);
								}
							}
						}
					}
				}
			}
		}

		ArrayList<PossibleMove> moves = new ArrayList<>();
		for (PossibleMove move : bestMoves.keySet()) {
			moves.add(move);
		}
		PossibleMove result = PossibleMove.findHighestScoreMove(moves);
		double[] bestData = bestMoves.get(result);

		return result;
	}

	private static int[][] removeFallingBlock(int[][] boardMatrix) {
		int[][] copy = new int[20][10];
		int endY = 0;
		int lines = 2;
		int counter = 1;
		boolean flag = false;
		for (int y = 0; y < 20; y++) {
			flag = false;
			for (int x = 0; x < 10; x++) {
				if (boardMatrix[y][x] == 1) {
					flag = true;
				}
			}
			if (!flag && counter++ < lines) {
				endY = y;
			}
		}
		for (int y = 0; y < endY; y++) {
			for (int x = 0; x < 10; x++) {
				copy[y][x] = boardMatrix[y][x];
			}
		}
		return copy;
	}

	private static int[][] sumBoardAndBlockMatrix(int[][] boardMatrix, int[][] blockMatrix,
												  int xOfBoard) {
		if (blockMatrix[0].length + xOfBoard > 10)
			return null;
		int[][] result = null;
		int initY = 0;
		for (int y = 0; y < 20; y++) {
			if (boardMatrix[y][xOfBoard] == 1) {
				initY = y;
			}
		}
		a:
		for (int y = initY + 1; y < 20; y++) {

			int[][] trial = new int[20][10];
			for (int i = 0; i < boardMatrix.length; i++)
				for (int j = 0; j < boardMatrix[0].length; j++)
					trial[i][j] = boardMatrix[i][j];

			// 从block matrix左下角遍历
			for (int blockY = blockMatrix.length - 1; blockY >= 0; blockY--) {
				for (int blockX = 0; blockX < blockMatrix[0].length; blockX++) {
					int boardMatrixAtThisPoint = 0;
					try {
						if (y + (blockMatrix.length - blockY - 1) < 20)
							boardMatrixAtThisPoint =
									boardMatrix[y + (blockMatrix.length - blockY - 1) - 1][xOfBoard + blockX];
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (boardMatrixAtThisPoint == 1 && blockMatrix[blockY][blockX] == 1) {
						continue a;
					}
					// 查找上方有没有阻挡
//                    for (int y2 = y + 1; y2 < 20; y2++) {
//                        if (boardMatrix[y2][xOfBoard + blockX] == 1)
//                            break a;
//                    }
					if (!(boardMatrixAtThisPoint == 1 && blockMatrix[blockY][blockX] == 0))
						trial[y + (blockMatrix.length - blockY - 1) - 1][xOfBoard + blockX] =
								blockMatrix[blockY][blockX];
				}
			}
			result = trial.clone();
			break;
		}

		return result;
	}

	private static int analyzeAttacks(BufferedImage image) {
		for (int i = 0; i <= 16; i++) {
			if ((getBlue(image, attacksX, (int) (attacksY - i * blockLength)) < 50)) {
				return i;
			}
		}
		return 16;
	}

	private static Block[] nextBlocks(BufferedImage image) {
		Block[] result = new Block[6];

		// next
		for (int z = 0; z == 0; z++) {
			// Block I
			boolean iFlag = true;
			for (int i = 0; i < 4; i++) {
				if (!(getBlue(image, nextBlockIX + i * nextBlockLength, nextBlockIY) > nextColorThreshold)) {
					iFlag = false;
				}
			}
			if (iFlag) {
				result[0] = new BlockI();
				break;
			}
			// Block T
			boolean tFlag = true;
			for (int i = 0; i < 3; i++) {
				if (!(getBlue(image, nextBlockTX + i * nextBlockLength, nextBlockTY) > nextColorThreshold)) {
					tFlag = false;
				}
			}
			if (!(getBlue(image, nextBlockTX + 1 * nextBlockLength,
					nextBlockTY - 1 * nextBlockLength) > nextColorThreshold)) {
				tFlag = false;
			}
			if (tFlag) {
				result[0] = new BlockT();
				break;
			}
			// Block O
			boolean oFlag = true;
			for (int j = 0; j < 2; j++)
				for (int i = 0; i < 2; i++) {
					if (!(getBlue(image, nextBlockOX + i * nextBlockLength,
							nextBlockOY + j * nextBlockLength) > nextColorThreshold)) {
						oFlag = false;
					}
				}
			if (oFlag) {
				result[0] = new BlockO();
				break;
			}
			// Block J
			boolean jFlag = true;
			for (int i = 0; i < 3; i++) {
				if (!(getBlue(image, nextBlockJX - i * nextBlockLength, nextBlockJY) > nextColorThreshold)) {
					jFlag = false;
				}
			}
			if (!(getBlue(image, nextBlockJX,
					nextBlockJY - 1 * nextBlockLength) > nextColorThreshold)) {
				jFlag = false;
			}
			if (jFlag) {
				result[0] = new BlockJ();
				break;
			}
			// Block S
			boolean sFlag = true;
			if (!(getBlue(image, nextBlockSX - 1 * nextBlockLength, nextBlockSY) > nextColorThreshold)) {
				sFlag = false;
			}
			if (!(getBlue(image, nextBlockSX, nextBlockSY - 1 * nextBlockLength) > nextColorThreshold)) {
				sFlag = false;
			}
			if (!(getBlue(image, nextBlockSX + 1 * nextBlockLength,
					nextBlockSY - 1 * nextBlockLength) > nextColorThreshold)) {
				sFlag = false;
			}
			if (sFlag) {
				result[0] = new BlockS();
				break;
			}
			//Block Z
			boolean zFlag = true;
			if (!(getBlue(image, nextBlockZX + 1 * nextBlockLength, nextBlockZY) > nextColorThreshold)) {
				zFlag = false;
			}
			if (!(getBlue(image, nextBlockZX, nextBlockZY - 1 * nextBlockLength) > nextColorThreshold)) {
				zFlag = false;
			}
			if (!(getBlue(image, nextBlockZX - 1 * nextBlockLength,
					nextBlockZY - 1 * nextBlockLength) > nextColorThreshold)) {
				zFlag = false;
			}
			if (zFlag) {
				result[0] = new BlockZ();
				break;
			}
			//Block L
			boolean lFlag = true;
			for (int i = 0; i < 3; i++)
				if (!(getBlue(image, nextBlockLX + i * nextBlockLength, nextBlockLY) > nextColorThreshold)) {
					lFlag = false;
				}
			if (!(getBlue(image, nextBlockLX,
					nextBlockLY - 1 * nextBlockLength) > nextColorThreshold)) {
				lFlag = false;
			}
			if (lFlag) {
				result[0] = new BlockL();
				break;
			}
		}


		int xIndex = nextFollowingBlockStartX;
		int yIndex = nextFollowingBlockStartY;
		// next following blocks
		for (int z = 1; z <= 5; z++) {
			Block resultBlock = null;
			// block T
			boolean tFlag = true;
			if (!(getBlue(image, xIndex, yIndex) > nextColorThreshold)) {
				tFlag = false;
			}
			for (int i = 0; i < 3; i++) {
				if (!(getBlue(image,
						(xIndex - nextFollowingBlockLength) + i * nextFollowingBlockLength,
						yIndex + nextFollowingBlockLength) > nextColorThreshold)) {
					tFlag = false;
				}
			}
			if (tFlag) {
				resultBlock = new BlockT();
			}
			// block O
			boolean oFlag = true;
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					if (!(getBlue(image,
							xIndex + i * nextFollowingBlockLength,
							yIndex + j * nextFollowingBlockLength) > nextColorThreshold)) {
						oFlag = false;
					}
				}
			}
			if (oFlag) {
				resultBlock = new BlockO();
			}

			// block J
			boolean jFlag = true;
			if (!(getBlue(image, xIndex + nextFollowingBlockLength, yIndex) > nextColorThreshold)) {
				jFlag = false;
			}
			for (int i = 0; i < 3; i++) {
				if (!(getBlue(image,
						(xIndex - nextFollowingBlockLength) + i * nextFollowingBlockLength,
						yIndex + nextFollowingBlockLength) > nextColorThreshold)) {
					jFlag = false;
				}
			}
			if (jFlag) {
				resultBlock = new BlockJ();
			}
			// block S
			boolean sFlag = true;
			if (!(getBlue(image, xIndex, yIndex) > nextColorThreshold)) {
				sFlag = false;
			}
			if (!(getBlue(image, xIndex + nextFollowingBlockLength, yIndex) > nextColorThreshold)) {
				sFlag = false;
			}
			for (int i = 0; i < 2; i++) {
				if (!(getBlue(image,
						(xIndex - nextFollowingBlockLength) + i * nextFollowingBlockLength,
						yIndex + nextFollowingBlockLength) > nextColorThreshold)) {
					sFlag = false;
				}
			}
			if (sFlag) {
				resultBlock = new BlockS();
			}
			// block I
			boolean iFlag = true;
			for (int i = 0; i < 4; i++) {
				if (!(getBlue(image,
						(xIndex - nextFollowingBlockLength) + i * nextFollowingBlockLength,
						yIndex) > nextColorThreshold)) {
					iFlag = false;
				}
			}
			if (iFlag) {
				resultBlock = new BlockI();
			}
			// block L
			boolean lFlag = true;
			if (!(getBlue(image, xIndex - nextFollowingBlockLength, yIndex) > nextColorThreshold)) {
				lFlag = false;
			}
			for (int i = 0; i < 3; i++) {
				if (!(getBlue(image,
						(xIndex - nextFollowingBlockLength) + i * nextFollowingBlockLength,
						yIndex + nextFollowingBlockLength) > nextColorThreshold)) {
					lFlag = false;
				}
			}
			if (lFlag) {
				resultBlock = new BlockL();
			}
			// block Z
			boolean zFlag = true;
			if (!(getBlue(image, xIndex, yIndex) > nextColorThreshold)) {
				zFlag = false;
			}
			if (!(getBlue(image, xIndex - nextFollowingBlockLength, yIndex) > nextColorThreshold)) {
				zFlag = false;
			}
			for (int i = 0; i < 2; i++) {
				if (!(getBlue(image,
						(xIndex) + i * nextFollowingBlockLength,
						yIndex + nextFollowingBlockLength) > nextColorThreshold)) {
					zFlag = false;
				}
			}
			if (zFlag) {
				resultBlock = new BlockZ();
			}


			result[z] = resultBlock;
			yIndex += nextFollowingBlockInterval + 2 * nextFollowingBlockLength;
		}
		return result;
	}

	private static boolean isBlock(BufferedImage image, int x, int y) {

		if ((image.getRGB(x, y) & 0xFF) < 32)
			return false;
		return (image.getRGB(x - brightDx, y - brightDy) & 0xFF) > 53;
	}

	private static int getBlue(BufferedImage image, int x, int y) {
		return image.getRGB(x, y) & 0xFF;
	}

	public static PossibleMove getNextLocation(TetrisBoard tetrisBoard) {
		Block block = tetrisBoard.getFallingBlock();
		int[][] blockMatrix = block.getStandardSpaceMatrix();
		int[][][] rotationalSpaceMatrix = block.getRotationalSpaceMatrix();
		int[][] boardMatrix = tetrisBoard.getBoardMatrix();

		List<PossibleMove> possibleMoveList = new ArrayList<>();
		// from left to right
		for (int x = 0; x < 10; x++) {
			int[][] result = sumBoardAndBlockMatrix(boardMatrix, blockMatrix, x);
			if (result != null) {
				PossibleMove move = new PossibleMove(new Move(x),
						TetrisBoard.computeScore(result), block, 0, result);
				possibleMoveList.add(move);
			}
			// 几种不同的变化
			for (int i = 0; i < rotationalSpaceMatrix.length; i++) {

				int[][] result2 = sumBoardAndBlockMatrix(boardMatrix,
						rotationalSpaceMatrix[i], x);
				if (result2 != null) {
					PossibleMove move = new PossibleMove(new Move(x),
							TetrisBoard.computeScore(result2), block, i + 1, result2);
					possibleMoveList.add(move);
				}
			}
		}
		PossibleMove possibleMove = PossibleMove.findHighestScoreMove(possibleMoveList);
		return possibleMove;
	}

	public TetrisBoard analyzeBoardByImg(Date captureTime, BufferedImage image) {
		TetrisBoard tetrisBoard = new TetrisBoard();
		int[][] boardMatrix = new int[20][10]; // [y][x]
		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 10; x++) {
				boardMatrix[y][x] =
						isBlock(image,
								(int) (startX + x * blockLength),
								(int) (startY - y * blockLength)) ? 1 : 0;
			}
		}
		tetrisBoard.setBoardMatrix(removeFallingBlock(boardMatrix));
		tetrisBoard.setNextBlocks(nextBlocks(image));
		tetrisBoard.setAttackers(analyzeAttacks(image));
		tetrisBoard.setCaptureTime(captureTime);

		return tetrisBoard;
	}

//  下面都是shit，想到了一个超"高端"的算法（大雾 ↑
//  屎山：
//    private static final double RATIO_LEFT_PART = 233;
//    private static final double RATIO_LEFT_BAR = 60;
//    private static final double RATIO_BOARD = 200;
//    private static final double RATIO_RIGHT_BAR = 60;
//    private static final double RATIO_RIGHT_PART = 233;
//    private static final double RATIO_DENOMINATOR =
//            RATIO_LEFT_PART + RATIO_LEFT_BAR + RATIO_BOARD + RATIO_RIGHT_BAR +
// RATIO_RIGHT_PART;
//    private static final float PREDICT_ROWS = 22.3f;
//    private static final float PREDICT_COLS = 36.9f;
//
//    private static final float RESERVE_RATIO_PERCENTAGE = .8f;
//    private static final float LINE_VARIANCE_THRESHOLD_PERCENTAGE = .3f;
//    @Autowired
//    private TetrisBoardQueue tetrisBoardQueue;
//    @Autowired
//    private ScreenStreamService screenStreamService;
//
//    public static int[] analyzeBoardByImage(BufferedImage bufferedImage) {
//        Mat mat = CVUtil.bufferedImageToMat(bufferedImage);
//        mat = new ScreenStreamService().cunnyScreenshot(mat);
//        Size screenSize = mat.size();
//        int predictBlockLength = (int) (screenSize.height / PREDICT_ROWS);
//        int predictBlockLength2 = (int) (screenSize.width / PREDICT_COLS);
//        if (Math.abs(predictBlockLength - predictBlockLength2) / ((predictBlockLength +
// predictBlockLength2) / 2) > (1 - RESERVE_RATIO_PERCENTAGE)) {
//            System.err.println("Please check if the board has correctly set up");
//        }
//        predictBlockLength = (predictBlockLength + predictBlockLength2) / 2;
//
//        int holdX =
//                (int) (screenSize.width * ((RATIO_LEFT_BAR + RATIO_LEFT_PART) /
// RATIO_DENOMINATOR) * RESERVE_RATIO_PERCENTAGE);
//        int holdY = (int) (screenSize.height * (1 / PREDICT_ROWS) *
// RESERVE_RATIO_PERCENTAGE);
//        holdX = findTraceResultWithMostConfidence(traceCoordinateInYDirection(null,
//                holdX, holdY,
//                (int) (screenSize.width * (RATIO_LEFT_PART + RATIO_LEFT_BAR) /
// RATIO_DENOMINATOR),
//                (int) (screenSize.height * (5 / PREDICT_ROWS)),
//                (int) (screenSize.height * (2.5 / PREDICT_ROWS)), false,
//                LINE_VARIANCE_THRESHOLD_PERCENTAGE)).getValue();
//
//        holdY = findTraceResultWithMostConfidence(traceCoordinateInXDirection(bufferedImage,
//                holdX,
//                holdY + predictBlockLength / 2,
//                (int) (holdX + predictBlockLength * 2.7),
//                (holdY + predictBlockLength * 2), (int) (predictBlockLength * 2.7), true,
//                LINE_VARIANCE_THRESHOLD_PERCENTAGE)).getValue();
//        System.out.println(holdX + ":" + holdY);
//        System.out.println(predictBlockLength + ":" + predictBlockLength2);
//        return new int[]{holdX, holdY};
//    }
//
//    private static TraceResult findTraceResultWithMostConfidence(ArrayList<TraceResult>
// list) {
//        TraceResult result = null;
//        for (TraceResult traceResult : list) {
//            if (result == null) {
//                result = traceResult;
//            } else {
//                if (result.getConfidence() < traceResult.getConfidence())
//                    result = traceResult;
//            }
//        }
//
//        return result;
//    }
//
//    private static ArrayList<TraceResult> traceCoordinateInYDirection(BufferedImage
// bufferedImage, int startX,
//                                                                      int startY, int endX,
//                                                                      int endY, int yCount,
//                                                                      boolean doubleAnalysis,
//                                                                      float
// variancePercentage) {
//        ArrayList<TraceResult> results = new ArrayList<TraceResult>();
//        int previous = 0;
//        int sumOfWhite = 0;
//        for (int x = startX; x < endX; x++) {
//            if (doubleAnalysis) previous = sumOfWhite;
//            sumOfWhite = 0;
//            for (int y = startY; y < endY; y++) {
//                if ((bufferedImage.getRGB(x, y) & 0x000000FF) != 0) {
//                    sumOfWhite++;
//                }
//            }
//            float confidence = Math.abs(((sumOfWhite + previous) / (float) yCount) - 1);
//            if (confidence < variancePercentage) {
//                results.add(new TraceResult(1 - confidence, x));
//            }
//        }
//        return results;
//    }
//
//    private static ArrayList<TraceResult> traceCoordinateInXDirection(BufferedImage
// bufferedImage, int startX,
//                                                                      int startY, int endX,
//                                                                      int endY, int xCount,
//                                                                      boolean doubleAnalysis,
//                                                                      float
// variancePercentage) {
//        ArrayList<TraceResult> results = new ArrayList<TraceResult>();
//        int previous = 0;
//        int sumOfWhite = 0;
//        for (int y = startY; y < endY; y++) {
//            if (doubleAnalysis) previous = sumOfWhite;
//            sumOfWhite = 0;
//            for (int x = startX; x < endX; x++) {
////                if (((((bufferedImage.getRGB(x, y) >> 8) && 0xFF) != 0))) {
////                    sumOfWhite++;
////                }
//            }
//            float confidence = Math.abs(((sumOfWhite + previous) / (float) xCount) - 1);
//            if (confidence < variancePercentage) {
//                results.add(new TraceResult(1 - confidence, y));
//            }
//
//        }
//        return results;
//    }

	/*
	 * 这下面的代码或将成为新的屎山
	 * */
//    public static void main(String[] args) {
//        boolean printData = true;
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        File f = new File("img/1551761004268.png");
//        PrintStream fileStream;
//        File dataFile = new File("data/");
//        for (File file :
//                dataFile.listFiles()) {
//            if (file.getName().endsWith(".dat"))
//                file.delete();
//        }
//        //for (File f : file.listFiles())
//        try {
//            BufferedImage bi = ImageIO.read(f);
//            System.out.println(f.getName());
//            ScreenStreamService screenStreamService = new ScreenStreamService();
//
//            Mat mat = screenStreamService.cunnyScreenshot(CVUtil.bufferedImageToMat(bi));
//
//            // 得到左上hold坐标：
//            // int[] tempInt = analyzeBoardByImage(mat);
//            int holdX = 232;
//            int holdY = 435 - 402;
//            int endX = 544;
//
//            int botX = 262;
//            int botY1 = 435 - 339;
//            int botY2 = 435 - 26;
//
//            int rightBotX = 489;
//            int rightBotY = 435 - 185;
//
//            int boardX1 = 290;
//            int boardY1 = 435 - 413;
//
//            int boardX2 = 485;
//            int boardY2 = 435 - 26;
//
//            int titleY = 435 - 321;
//
//            List<MatOfPoint> list = screenStreamService.findContours(mat);
//            // Mat hierarchy = ;
//            // Size size = hierarchy.size();
//            // double arr[] = hierarchy.get(0,1061);
//            PrintStream defaultOut = System.out;
//
//            List<List<Point>> result = new ArrayList<>();
//            // <x,<y,hierarchy>>
//            HashMap<Integer, HashMap<Integer, Integer>> xToYBoardPixels = new HashMap<>();
//            HashMap<Integer, HashMap<Integer, Integer>> yToXBoardPixels = new HashMap<>();
//            HashMap<Integer, HashMap<Integer, HashSet<Integer>>> xToHieMap = new HashMap<>();
//            HashMap<Integer, HashMap<Integer, HashSet<Integer>>> yToHieMap = new HashMap<>();
//
//            int totalPoints = 0;
//            for (int i = 0; i < list.size(); i++) {
//                List<Point> pointsList = list.get(i).toList();
//                result.add(pointsList);
//
//                File file = new File("data/" + i + ".dat");
//                if (!file.exists())
//                    file.createNewFile();
//                fileStream = new PrintStream("data/" + i + ".dat");
//                System.setOut(fileStream);
//
//                boolean hasData = false;
//                for (Point point : pointsList) {
//                    if (point.x > holdX && point.y > holdY && point.x < endX) {
//                        if (point.y <= botY1 || (point.x >= botX && point.y <= botY2))
//                            if (point.x <= rightBotX || point.y <= rightBotY) {
//                                if (!(point.x >= boardX1 && point.x <= boardX2 && point.y
// <= titleY)) {
//                                    if (point.x >= boardX1 && point.x <= boardX2) {
//                                        int x = (int) point.x;
//                                        int y = (int) point.y;
//                                        // 提取框内的点
//                                        // x to y
//                                        if (!xToYBoardPixels.containsKey(x)) {
//                                            HashMap<Integer, Integer> tempMap =
//                                                    new HashMap<>();
//                                            xToYBoardPixels.put(x, tempMap);
//                                            tempMap.put(y, i);
//                                        } else {
//                                            xToYBoardPixels.get(x).put(y
//                                                    , i);
//                                        }
//                                        // y to x
//                                        if (!yToXBoardPixels.containsKey(y)) {
//                                            HashMap<Integer, Integer> tempMap =
//                                                    new HashMap<>();
//                                            yToXBoardPixels.put(y, tempMap);
//                                            tempMap.put(x, i);
//                                        } else {
//                                            yToXBoardPixels.get(y).put(x
//                                                    , i);
//                                        }
//                                        // x to hierarchy
//                                        if ((!xToHieMap.containsKey(x))) {
//                                            HashMap<Integer, HashSet<Integer>> tempMap2 =
//                                                    new HashMap<>();
//                                            HashSet<Integer> tempSet = new HashSet<>();
//                                            tempMap2.put(i, tempSet);
//                                            tempSet.add(y);
//                                            xToHieMap.put(x, tempMap2);
//                                        } else if (!xToHieMap.get(x).containsKey(i)) {
//                                            HashSet<Integer> tempSet = new HashSet<>();
//                                            tempSet.add(y);
//                                            xToHieMap.get(x).put(i, tempSet);
//                                        } else {
//                                            xToHieMap.get(x).get(i).add(y);
//                                        }
//                                        // y to hierarchy
//                                        if ((!yToHieMap.containsKey(y))) {
//                                            HashMap<Integer, HashSet<Integer>> tempMap2 =
//                                                    new HashMap<>();
//                                            HashSet<Integer> tempSet = new HashSet<>();
//                                            tempMap2.put(i, tempSet);
//                                            tempSet.add(x);
//                                            yToHieMap.put(y, tempMap2);
//                                        } else if (!yToHieMap.get(y).containsKey(i)) {
//                                            HashSet<Integer> tempSet = new HashSet<>();
//                                            tempSet.add(x);
//                                            yToHieMap.get(y).put(i, tempSet);
//                                        } else {
//                                            yToHieMap.get(y).get(i).add(x);
//                                        }
//                                        System.out.println(point.x + "\t" + point.y);
//                                        totalPoints++;
//                                        hasData = true;
//                                    }
//                                }
//                            }
//                    }
//                }
//                if (!hasData)
//                    file.delete();
//            }
//
//            File file = new File("data/" + "blocks" + ".dat");
//            if (!file.exists())
//                file.createNewFile();
//            fileStream = new PrintStream("data/" + "blocks" + ".dat");
//            System.setOut(fileStream);
//
//            List<Point> blocksList = analyzeBlocksLocations(boardX1, boardY2,
//                    xToYBoardPixels, yToXBoardPixels, xToHieMap, yToHieMap
//            );
//            for (Point point : blocksList) {
//                System.out.println(point.x + "\t" + point.y);
//            }
//            System.setOut(defaultOut);
//            System.out.println("Points:" + totalPoints);
//            //analyzeBoardByImage(bi);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static List<Point> analyzeBlocksLocations(int startX, int startY, HashMap<Integer,
//            HashMap<Integer, Integer>> xToYBoardPixels, HashMap<Integer, HashMap<Integer,
//            Integer>> yToXBoardPixels, HashMap<Integer, HashMap<Integer,
// HashSet<Integer>>> xToHieMap, HashMap<Integer, HashMap<Integer, HashSet<Integer>>>
// yToHieMap) {
//        List<Point> result = new ArrayList<>();
//        double blockLength = 19.333;
//        int vertexTolerance = 5;
//        int minimaPoints = 15;
//        boolean leftTop = false;
//        boolean leftBot = false;
//        boolean rightTop = false;
//        boolean rightBot = false;
//
//        // 直线上添加关键点(y-axis)
//        HashMap<Integer, HashMap<Integer, Integer>> additionMap = new HashMap<>();
//        int previousY;
//        for (Integer x : xToYBoardPixels.keySet()) {
//            if (xToYBoardPixels.get(x).size() >= 2) {
//                for (Integer hierarchy : xToHieMap.get(x).keySet()) {
//                    previousY = 0;
//                    for (Integer y : xToHieMap.get(x).get(hierarchy)) {
//                        if (previousY != 0) {
//
//                            int deltaY = y - previousY;
//                            //System.out.println(hierarchy+" : "+deltaY);
//                            if (Math.abs(deltaY) > 1.5 * blockLength) {
//                                int negative = 1;
//                                if (deltaY < 0)
//                                    negative = -1;
//                                for (int i = 1; i < (Math.abs(deltaY) / blockLength); i++) {
//                                    if (!additionMap.containsKey(x)) {
//                                        additionMap.put(x, new HashMap<>());
//                                    }
//                                    additionMap.get(x).put((int) (previousY + blockLength),
//                                            xToYBoardPixels.get(x).get(y));
////                                    Point tempPoint = new Point();
////                                    tempPoint.x = x;
////                                    tempPoint.y = previousY + i * blockLength * negative;
////                                    result.add(tempPoint);
//                                }
//                            }
//                        }
//                        previousY = y;
//                    }
//                }
//            }
//        }
//        // 直线上添加关键点(x-axis)
//        HashMap<Integer, HashMap<Integer, Integer>> additionMap2 = new HashMap<>();
//        int previousX;
//        for (Integer y : yToXBoardPixels.keySet()) {
//            if (yToXBoardPixels.get(y).size() >= 2) {
//                for (Integer hierarchy : yToHieMap.get(y).keySet()) {
//                    previousX = 0;
//                    for (Integer x : yToHieMap.get(y).get(hierarchy)) {
//                        if (previousX != 0) {
//                            int deltaX = x - previousX;
//                            if (Math.abs(deltaX) > 1.5 * blockLength) {
//                                int negative = 1;
//                                if (deltaX < 0)
//                                    negative = -1;
//                                for (int i = 1; i < (Math.abs(deltaX) / blockLength); i++) {
//                                    if (!additionMap2.containsKey(y)) {
//                                        additionMap2.put(y, new HashMap<>());
//                                    }
//                                    additionMap2.get(y).put((int) (previousX + blockLength),
//                                            yToXBoardPixels.get(y).get(x));
////                                    Point tempPoint = new Point();
////                                    tempPoint.x = previousX + i * blockLength * negative;
////                                    tempPoint.y = y;
////                                    result.add(tempPoint);
//                                }
//                            }
//                        }
//                        previousX = x;
//                    }
//                }
//            }
//        }
//        //辅助点添加至待分析map
//        for (Integer x : additionMap.keySet()) {
//            for (Integer y : additionMap.get(x).keySet()) {
//                xToYBoardPixels.get(x).put(y, additionMap.get(x).get(y));
//            }
//        }
//        for (Integer y : additionMap2.keySet()) {
//            for (Integer x : additionMap2.get(y).keySet()) {
//                if (!xToYBoardPixels.containsKey(x))
//                    xToYBoardPixels.put(x, new HashMap<>());
//                xToYBoardPixels.get(x).put(y, additionMap2.get(y).get(x));
//            }
//        }
//
//        for (int xBlock = 0; xBlock < 10; xBlock++) {
//            a:
//            for (int yBlock = 0; yBlock < 15; yBlock++) {
//                leftTop = false;
//                leftBot = false;
//                rightTop = false;
//                rightBot = false;
//
//                int currentBlockX1 = (int) Math.round(startX + xBlock * blockLength);
//                int currentBlockY1 = (int) Math.round(startY - (yBlock + 1) * blockLength);
//                int currentBlockX2 = (int) Math.round(startX + (xBlock + 1) * blockLength);
//                int currentBlockY2 = (int) Math.round(startY - yBlock * blockLength);
//
//                Point point = new Point();
//                point.x = (currentBlockX1 + currentBlockX2) / 2;
//                point.y = (currentBlockY1 + currentBlockY2) / 2;
////                Point point1 = new Point();
////                Point point2 = new Point();
////                point1.x = currentBlockX1;
////                point1.y = currentBlockY1;
////                point2.x = currentBlockX2;
////                point2.y = currentBlockY2;
////                result.add(point1);
////                result.add(point2);
//
//
//                // 分析1：如果Block四个点都存在（容差:+-vertexTolerance）
//
//                for (int x = -vertexTolerance; x <= vertexTolerance; x++) {
//                    for (int y = -vertexTolerance; y <= vertexTolerance; y++) {
//                        if (!leftTop)
//                            if (xToYBoardPixels.containsKey(currentBlockX1 + x) &&
//                                    xToYBoardPixels.get(currentBlockX1 + x).containsKey
// (currentBlockY1 + y))
//                                leftTop = true;
//
//                        if (!leftBot)
//                            if (xToYBoardPixels.containsKey(currentBlockX1 + x) &&
//                                    xToYBoardPixels.get(currentBlockX1 + x).containsKey
// (currentBlockY2 + y))
//                                leftBot = true;
//                        if (!rightTop)
//                            if (xToYBoardPixels.containsKey(currentBlockX2 + x) &&
//                                    xToYBoardPixels.get(currentBlockX2 + x).containsKey
// (currentBlockY1 + y))
//                                rightTop = true;
//                        if (!rightBot)
//                            if (xToYBoardPixels.containsKey(currentBlockX2 + x) &&
//                                    xToYBoardPixels.get(currentBlockX2 + x).containsKey
// (currentBlockY2 + y))
//                                rightBot = true;
//
//                    }
//                }
//
//                if (leftBot && leftTop && rightTop && rightBot) {
//                    result.add(point);
//                    continue a;
//                }
//
//
//                // 分析2 如果区域内的点的数量大于minimaPoints
//                int counter = 0;
//                for (int x1 = currentBlockX1; x1 < currentBlockX2; x1++) {
//                    for (int y1 = currentBlockY1; y1 < currentBlockY2; y1++) {
//                        if (xToYBoardPixels.containsKey(x1) && xToYBoardPixels.get(x1)
// .containsKey(y1)) {
//                            counter++;
//                        }
//                    }
//                }
//                if (counter >= minimaPoints)
//                    result.add(point);
//            }
//        }
//
//        return result;
//    }
//

}
