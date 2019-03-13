package me.ltxom.tetrisplayer.entity.tetris;

public class BlockImp implements Block {

	private int[][] standardSpaceMatrix;
	private int[][][] rotationalSpaceMatrix;

	@Override
	public int[][] getStandardSpaceMatrix() {
		return standardSpaceMatrix;
	}

	@Override
	public int[][][] getRotationalSpaceMatrix() {
		return rotationalSpaceMatrix;
	}

	@Override
	public boolean equals(Object anotherBlock) {
		BlockImp block = (BlockImp) anotherBlock;
		if (block == null && this == null)
			return true;
		if (block.getStandardSpaceMatrix().length != this.getStandardSpaceMatrix().length || block.getStandardSpaceMatrix()[0].length != this.getStandardSpaceMatrix()[0].length)
			return false;
		for (int y = 0; y < this.getStandardSpaceMatrix().length; y++) {
			for (int x = 0; x < this.getStandardSpaceMatrix()[0].length; x++) {
				if (block.getStandardSpaceMatrix()[y][x] != this.getStandardSpaceMatrix()[y][x])
					return false;
			}
		}
		return true;
	}
}
