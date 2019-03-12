package me.ltxom.tetrisplayer.entity.tetris;

public class BlockZ implements Block {

    private int[][] standardSpaceMatrix = new int[][]{{1, 1, 0}, {0, 1, 1}};
    private int[][][] rotationalSpaceMatrix = new int[][][]{{{0, 1}, {1, 1}, {1, 0}}};

    @Override
    public int[][] getStandardSpaceMatrix() {
        return standardSpaceMatrix;
    }

    @Override
    public int[][][] getRotationalSpaceMatrix() {
        return rotationalSpaceMatrix;
    }
}
