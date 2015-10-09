package com.henriquegarces.guessthecolor;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.henriquegarces.guessthecolor.util.ColorUtils;
import com.henriquegarces.guessthecolor.util.GUIUtils;

/**
 * Created by henrique on 25/09/15.
 */
public class WrongActivity extends Activity implements OnClickListener {

    private int[] colors;
    private LinearLayout linearLayout;
    private FrameLayout revealView;
    private boolean newGame;
    private int cx;
    private int cy;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong);

        linearLayout = (LinearLayout) findViewById(R.id.wrong_answer_linearlayout);
        revealView = (FrameLayout)findViewById(R.id.root_layout);
        TextView scoreTextView = (TextView) findViewById(R.id.final_score_textview);
        Button playAgainButton = (Button) findViewById(R.id.play_again_button);
        Button exitToMenuButton = (Button) findViewById(R.id.exit_to_menu_button);

        Bundle bundle = getIntent().getExtras();

        int color = bundle.getInt(GameActivity.EXTRA_COLOR);
        String score = "SCORE: " + bundle.getString(GameActivity.EXTRA_SCORE);
        int actionBarSize = bundle.getInt("actionBarSize");
        int[] location = bundle.getIntArray("location");
        cx = location[0];
        cy = location[1] + actionBarSize;
        pos = bundle.getInt("position");
        linearLayout.setBackgroundColor(color);
        scoreTextView.setText(score);

        colors = new ColorUtils().getRandomColor(3);
        revealView.setBackgroundColor(colors[pos]);

        playAgainButton.setOnClickListener(this);
        playAgainButton.setBackgroundResource(R.drawable.button_shape);
        exitToMenuButton.setOnClickListener(this);
        exitToMenuButton.setBackgroundResource(R.drawable.button_shape);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.exit_to_menu_button) {
            //GUIUtils.hideRevealEffect(rootLayout, cx, cy, 1920, hideAnimationListener);
            Intent intent = new Intent(WrongActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.play_again_button) {
            newGame = true;
            GUIUtils.hideRevealEffect(revealView, cx, cy, 1920, hideAnimationListener);
        }
    }

    Animator.AnimatorListener hideAnimationListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {
            linearLayout.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            revealView.setVisibility(View.INVISIBLE);

            Intent intent;
            if(newGame) {
                intent = new Intent(WrongActivity.this, GameActivity.class);
                intent.putExtra(GameActivity.EXTRA_COLOR, colors);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            else {
                intent = new Intent(WrongActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {}

        @Override
        public void onAnimationRepeat(Animator animation) {}
    };
}