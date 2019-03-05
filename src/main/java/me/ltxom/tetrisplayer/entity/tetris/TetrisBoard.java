package me.ltxom.tetrisplayer.entity.tetris;

import java.util.Date;

public class TetrisBoard {
	private Date captureTime;
	private int[][] boardMatrix;
	private Block fallingBlock;
	private Block[] nextBlocks;
	private Block holdingBlock;
	private int attackers;

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
