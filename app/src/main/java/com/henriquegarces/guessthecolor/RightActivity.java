package com.henriquegarces.guessthecolor;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.henriquegarces.guessthecolor.util.ColorUtils;
import com.henriquegarces.guessthecolor.util.GUIUtils;

/**
 * Created by henrique on 25/09/15.
 */
public class RightActivity extends Activity {

    private int[] colors;
    private LinearLayout linearLayout;
    private FrameLayout revealView;
    private int cx;
    private int cy;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right);

        linearLayout = (LinearLayout) findViewById(R.id.right_answer_linearlayout);
        Button nextColorButton = (Button) findViewById(R.id.next_color_button);
        revealView = (FrameLayout)findViewById(R.id.root_layout);

        Bundle bundle = getIntent().getExtras();
        int color = bundle.getInt(GameActivity.EXTRA_COLOR);
        int[] location = bundle.getIntArray("location");
        cx = location[0];
        cy = location[1] + bundle.getInt("actionBarSize");
        pos = bundle.getInt("position");
        linearLayout.setBackgroundColor(color);

        colors = new ColorUtils().getRandomColor(3);

        nextColorButton.setBackgroundResource(R.drawable.button_shape);
        nextColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealView.setBackgroundColor(colors[pos]);
                GUIUtils.hideRevealEffect(revealView, cx, cy, 1920, hideAnimationListener);
            }
        });
    }

    Animator.AnimatorListener hideAnimationListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {
            linearLayout.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            revealView.setVisibility(View.INVISIBLE);
            Intent intent = new Intent();
            intent.putExtra(GameActivity.EXTRA_COLOR, colors);
            setResult(Activity.RESULT_OK, intent);
            overridePendingTransition(0, 0);
            finish();
        }

        @Override
        public void onAnimationCancel(Animator animation) {}

        @Override
        public void onAnimationRepeat(Animator animation) {}
    };

}
