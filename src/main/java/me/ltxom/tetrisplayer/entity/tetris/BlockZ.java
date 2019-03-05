package me.ltxom.tetrisplayer.entity.tetris;

public class BlockZ extends Block{
	static {
		standardSpaceMatrix = new int[][]{{1, 1, 0}, {0, 1, 1}};
		rotationalSpaceMatrix = new int[][][]{{{0, 1}, {1, 1}, {1, 0}}};
	}
}
