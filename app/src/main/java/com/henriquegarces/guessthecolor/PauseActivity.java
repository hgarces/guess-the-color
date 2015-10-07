package com.henriquegarces.guessthecolor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.henriquegarces.guessthecolor.util.ColorUtils;

/**
 * Created by henrique on 25/09/15.
 */
public class PauseActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);
        setupLayout();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.unpause_button) {
            onBackPressed();
        }
        else  if(id == R.id.restart_button) {
            int[] colors = new ColorUtils().getRandomColor(3);
            Intent intent = new Intent(PauseActivity.this, GameActivity.class);
            intent.putExtra(GameActivity.EXTRA_COLOR, colors);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.exit_to_menu_button) {
            Intent intent = new Intent(PauseActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
         else {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        }

    }

    private void setupLayout() {
        Button unpauseButton = (Button) findViewById(R.id.unpause_button);
        unpauseButton.setOnClickListener(this);
        Button restartButton = (Button) findViewById(R.id.restart_button);
        restartButton.setOnClickListener(this);
        Button exitButton = (Button) findViewById(R.id.exit_to_menu_button);
        exitButton.setOnClickListener(this);
        Button settingsButton = (Button) findViewById(R.id.action_settings);
        settingsButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
