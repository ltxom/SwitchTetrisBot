package me.ltxom.tetrisplayer.entity.tetris;

public class Move {
    // deltaX > 0 toward right x unit
    private int deltaX;

    public Move(int deltaX) {
        this.deltaX = deltaX;
    }

    public int getDeltaX() {
        return deltaX;
    }
}
