package me.ltxom.tetrisplayer.entity.tetris;

public class BlockJ implements Block {

    private int[][] standardSpaceMatrix = new int[][]{{1, 0, 0}, {1, 1, 1}};
    private int[][][] rotationalSpaceMatrix = new int[][][]{{
            {1, 1}, {0, 1}, {0, 1}},
            {{1, 1, 1}, {0, 0, 1}},
            {{0, 1}, {0, 1}, {1, 1}}};

    @Override
    public int[][] getStandardSpaceMatrix() {
        return standardSpaceMatrix;
    }

    @Override
    public int[][][] getRotationalSpaceMatrix() {
        return rotationalSpaceMatrix;
    }
}
