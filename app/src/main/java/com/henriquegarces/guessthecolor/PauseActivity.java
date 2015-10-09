package com.henriquegarces.guessthecolor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.henriquegarces.guessthecolor.util.ColorUtils;

/**
 * Created by henrique on 25/09/15.
 */
public class PauseActivity extends Activity implements View.OnClickListener {
    ColorUtils mColorUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);
        mColorUtils = new ColorUtils();
        setupLayout();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.unpause_button) {
            onBackPressed();
        }
        else  if(id == R.id.restart_button) {
            int[] colors = mColorUtils.getRandomColor(3);
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
        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        rootLayout.setBackgroundColor(mColorUtils.getRandomColor());
        Button unpauseButton = (Button) findViewById(R.id.unpause_button);
        unpauseButton.setBackgroundResource(R.drawable.button_shape);
        unpauseButton.setOnClickListener(this);
        Button restartButton = (Button) findViewById(R.id.restart_button);
        restartButton.setBackgroundResource(R.drawable.button_shape);
        restartButton.setOnClickListener(this);
        Button exitButton = (Button) findViewById(R.id.exit_to_menu_button);
        exitButton.setBackgroundResource(R.drawable.button_shape);
        exitButton.setOnClickListener(this);
        Button settingsButton = (Button) findViewById(R.id.action_settings);
        settingsButton.setBackgroundResource(R.drawable.button_shape);
        settingsButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
