package me.ltxom.tetrisplayer.entity.tetris;

public class BlockO implements Block {

    private int[][] standardSpaceMatrix = new int[][]{{1, 1}, {1, 1}};
    private int[][][] rotationalSpaceMatrix = new int[][][]{};

    @Override
    public int[][] getStandardSpaceMatrix() {
        return standardSpaceMatrix;
    }

    @Override
    public int[][][] getRotationalSpaceMatrix() {
        return rotationalSpaceMatrix;
    }
}
