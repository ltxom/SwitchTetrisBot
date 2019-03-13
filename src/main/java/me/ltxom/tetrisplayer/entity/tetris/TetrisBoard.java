package me.ltxom.tetrisplayer.entity.tetris;

import java.util.Date;

public class TetrisBoard {
	public static double completeLineScore = 19.9;
	// 正上方有方块不算
	public static double leavingHoldScore = -1.6;

	public static double havingNoBlockAbove = -1.0;
	public static double fillSpaceScore = 5;
	public static double heightDeductScore = -9;

	public static double depthWeight = 5;

	private Date captureTime;
	private int[][] boardMatrix;
	private Block fallingBlock;
	private Block[] nextBlocks;
	private Block holdingBlock;
	private int attackers;

	public static double computeScore(int[][] boardMatrix) {
		double score = 0.0;
		boolean lineComplete = true;

		for (int y = 0; y < 20; y++) {
			boolean flag = false;
			boolean hasBlockAbove = false;
			for (int x = 0; x < 10; x++) {
				if (boardMatrix[y][x] == 1)
					flag = true;
			}
			if (!flag) {
				break;
			}
			lineComplete = true;
			for (int x = 0; x < 10; x++) {
				hasBlockAbove = false;
				for (int y2 = y + 1; y2 < 20; y2++) {
					if (boardMatrix[y2][x] == 1)
						hasBlockAbove = true;
				}
				score += (boardMatrix[y][x] == 1 ? fillSpaceScore : (hasBlockAbove ? leavingHoldScore :
						havingNoBlockAbove)) * (20 - y) * depthWeight;

				if (boardMatrix[y][x] == 0)
					lineComplete = false;
			}
			if (lineComplete) {
				score += completeLineScore * (20 - y) * depthWeight;
			}
			score += heightDeductScore * (20 - y) * depthWeight;
		}
		return score;
	}

	public Date getCaptureTime() {
		return captureTime;
	}

	public void setCaptureTime(Date captureTime) {
		this.captureTime = captureTime;
	}

	public int[][] getBoardMatrix() {
		return boardMatrix;
	}

	public void setBoardMatrix(int[][] boardMatrix) {
		this.boardMatrix = boardMatrix;
	}

	public Block getFallingBlock() {
		return fallingBlock;
	}

	public void setFallingBlock(Block fallingBlock) {
		this.fallingBlock = fallingBlock;
	}

	public Block[] getNextBlocks() {
		return nextBlocks;
	}

	public void setNextBlocks(Block[] nextBlocks) {
		this.nextBlocks = nextBlocks;
	}

	public Block getHoldingBlock() {
		return holdingBlock;
	}

	public void setHoldingBlock(Block holdingBlock) {
		this.holdingBlock = holdingBlock;
	}

	public int getAttackers() {
		return attackers;
	}

	public void setAttackers(int attackers) {
		this.attackers = attackers;
	}

}
