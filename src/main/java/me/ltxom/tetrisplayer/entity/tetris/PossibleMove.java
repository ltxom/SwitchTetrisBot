package me.ltxom.tetrisplayer.entity.tetris;

import java.util.List;

public class PossibleMove {
    private Move move;
    private double score;
    private Block block;
    private int rotation;
    private int[][] boardMatrix;

    public PossibleMove(Move move, double score, Block block, int rotation,
                        int[][] boardMatrix) {
        this.move = move;
        this.score = score;
        this.block = block;
        this.rotation = rotation;
        this.boardMatrix = boardMatrix;
    }

    public static PossibleMove findHighestScoreMove(List<PossibleMove> possibleMoves) {
        double score = -Double.MAX_VALUE;
        PossibleMove result = null;
        for (PossibleMove possibleMove : possibleMoves) {
            if (score < possibleMove.score) {
                result = possibleMove;
                score = result.score;
            }
        }
        return result;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void addScore(double score) {
        this.score += score;
    }


}
