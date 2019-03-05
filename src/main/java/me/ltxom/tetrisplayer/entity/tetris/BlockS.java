package me.ltxom.tetrisplayer.entity.tetris;

public class BlockS extends Block {
	static {
		standardSpaceMatrix = new int[][]{{0, 1, 1}, {1, 1, 0}};
		rotationalSpaceMatrix = new int[][][]{{{1, 0}, {1, 1}, {0, 1}}};
	}
}
