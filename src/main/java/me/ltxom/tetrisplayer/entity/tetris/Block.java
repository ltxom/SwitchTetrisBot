package me.ltxom.tetrisplayer.entity.tetris;

public interface Block {

    int[][] getStandardSpaceMatrix();

    int[][][] getRotationalSpaceMatrix();

	@Override
	public boolean equals(Object block2);
}
