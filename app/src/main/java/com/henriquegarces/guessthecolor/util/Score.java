package com.henriquegarces.guessthecolor.util;

/**
 * Created by henrique on 21/05/15.
 */
public class Score implements Comparable<Score>{

    private String player;
    private int scoreNum;

    public Score(String nick, int num) {
        player = nick;
        scoreNum = num;
    }

    public int getScore() {
        return scoreNum;
    }

    @Override
    public int compareTo(Score another) {
        //return 0 if equal
        //1 if passed greater than this
        //-1 if this greater than passed
        return another.scoreNum > scoreNum? 1 : another.scoreNum < scoreNum? -1 : 0;
    }

    public String getScoreText() {
        return player + " - " + scoreNum;
    }
}