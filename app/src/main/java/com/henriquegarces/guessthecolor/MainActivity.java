package com.henriquegarces.guessthecolor;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

/**
 * Created by henrique on 25/09/15.
 */
public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG = "Guess the Color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        View newButton = findViewById(R.id.play_button);
        newButton.setOnClickListener(this);
        View scoreButton = findViewById(R.id.scores_button);
        scoreButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View settingsButton = findViewById(R.id.action_settings);
        settingsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_button:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.play_button:
                startGame(v);
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.scores_button:
                startActivity(new Intent(this, HighScoreActivity.class));
                break;
        }
    }

    private void startGame(View view) {
        Log.d(TAG, "new game started");
        getWindow().setExitTransition(new Fade());
        Intent intent = new Intent(this, GameActivity.class);
        //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view, "play");
        startActivity(intent, ActivityOptions
                .makeSceneTransitionAnimation(this).toBundle());
    }
}

