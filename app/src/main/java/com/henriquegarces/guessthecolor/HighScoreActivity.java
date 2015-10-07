package com.henriquegarces.guessthecolor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by henrique on 25/09/15.
 */
public class HighScoreActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        TextView scoreView = (TextView)findViewById(R.id.high_scores_list);
        SharedPreferences scorePrefs = getSharedPreferences(GameActivity.GAME_PREFS, 0);
        String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
        StringBuilder scoreBuild = new StringBuilder("");
        for(String score : savedScores){
            scoreBuild.append(score+"\n");
        }
        scoreView.setText(scoreBuild.toString());
    }
}
