package me.ltxom.tetrisplayer.entity.tetris;

import java.util.Date;

public class TetrisBoard {
    private static final double completeLineScore = 5;
    private static final double leavingHoldScore = -3;
    private static final double fillSpaceScore = 1.0;
    private static final double depthWeight = 2;

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
            for (int x = 0; x < 10; x++) {
                if (boardMatrix[y][x] == 1)
                    flag = true;
            }
            if (!flag) {
                break;
            }
            lineComplete = true;
            for (int x = 0; x < 10; x++) {
                score += (boardMatrix[y][x] == 1 ? fillSpaceScore : leavingHoldScore) * (20 - y) * depthWeight;

                if (boardMatrix[y][x] == 0)
                    lineComplete = false;
            }
            if (lineComplete) {
                System.out.println(y);
                score += completeLineScore * (20 - y) * depthWeight;
            }
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
