package com.henriquegarces.guessthecolor;

import android.animation.Animator;
import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.henriquegarces.guessthecolor.util.CircleButton;
import com.henriquegarces.guessthecolor.util.ColorUtils;
import com.henriquegarces.guessthecolor.util.GUIUtils;
import com.henriquegarces.guessthecolor.util.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by henrique on 25/09/15.
 */
public class GameActivity extends Activity implements View.OnClickListener, View.OnTouchListener{

    private static final String TAG = "Guess the Color";
    public static final String EXTRA_SCORE = "com.android.henrique.guessthecolor.SCORE";
    public static final String EXTRA_COLOR = "com.android.henrique.guessthecolor.COLOR";
    public static final String GAME_PREFS = "GuessTheColorFile";
    public static final int NEXT_COLOR_REQUEST = 1;
    private static final int DELAY = 100;
    private LinearLayout bgViewGroup;
    private Interpolator interpolator;
    private SharedPreferences gamePrefs;
    private Bundle bundle;
    private FrameLayout revealView;
    private TextView scoreTextView;
    private TextView rgbColorTextView;
    private CircleButton circleButton1;
    private CircleButton circleButton2;
    private CircleButton circleButton3;

    private int[] colors;
    private int target;
    private int thisGuy;
    private int score = 0;
    private int position;
    private String rgb;
    private boolean isRight = false;
    private int mX, mY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_game);

        setEnterSharedElementCallback(new SharedElementCallback() {
            View mSnapshot;

            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                for (int i = 0; i < sharedElementNames.size(); i++) {
                    if ("play".equals(sharedElementNames.get(i))) {
                        FrameLayout element = (FrameLayout) sharedElements.get(i);
                        mSnapshot = sharedElementSnapshots.get(i);
                        int width = mSnapshot.getWidth();
                        int height = mSnapshot.getHeight();
                        int widthSpec = View.MeasureSpec.makeMeasureSpec(width,
                                View.MeasureSpec.EXACTLY);
                        int heightSpec = View.MeasureSpec.makeMeasureSpec(height,
                                View.MeasureSpec.EXACTLY);
                        mSnapshot.measure(widthSpec, heightSpec);
                        mSnapshot.layout(0, 0, width, height);
                        mSnapshot.setTransitionName("snapshot");
                        element.addView(mSnapshot);
                        break;
                    }
                }
            }
        });

        gamePrefs = getSharedPreferences(GAME_PREFS, 0);
        setupLayout(savedInstanceState);
        setupWindowAnimations();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart");
        //revealView.setVisibility(View.INVISIBLE);
        generateRandomColors();
        animateButtonsIn();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.circle_button1:
            {
                thisGuy = colors[0];
                position = 0;
                if (target == thisGuy) {
                    isRight = true;
                    rightAnswer(v);
                }
                else
                    wrongAnswer(v);
                break;
            }
            case R.id.circle_button2:
            {
                thisGuy = colors[1];
                position = 1;
                if (target == thisGuy) {
                    isRight = true;
                    rightAnswer(v);
                }
                else
                    wrongAnswer(v);
                break;
            }
            case R.id.circle_button3:
            {
                thisGuy = colors[2];
                position = 2;
                if (target == thisGuy) {
                    isRight = true;
                    rightAnswer(v);
                }
                else
                    wrongAnswer(v);
                break;
            }
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("score", score);
        savedInstanceState.putInt("target", target);
        savedInstanceState.putIntArray("colors", colors);
        savedInstanceState.putString("rgb", rgb);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEXT_COLOR_REQUEST) {
            if(resultCode == RESULT_OK) {
                revealView.setVisibility(View.INVISIBLE);
                colors = data.getIntArrayExtra(EXTRA_COLOR);
                generateRandomColors();
            }
        }
    }

    private void setupLayout(Bundle savedInstanceState) {
        Log.d(TAG, "setupLayout");
        revealView = (FrameLayout)findViewById(R.id.root_layout);
        //revealView.setVisibility(View.INVISIBLE);
        bgViewGroup = (LinearLayout) findViewById(R.id.button_container);
        scoreTextView = (TextView) findViewById(R.id.final_score_textview);
        rgbColorTextView = (TextView) findViewById(R.id.rgb_color_textview);
        circleButton1 = (CircleButton) findViewById(R.id.circle_button1);
        circleButton1.setOnClickListener(this);
        circleButton1.setOnTouchListener(this);
        circleButton2 = (CircleButton) findViewById(R.id.circle_button2);
        circleButton2.setOnClickListener(this);
        circleButton2.setOnTouchListener(this);
        circleButton3 = (CircleButton) findViewById(R.id.circle_button3);
        circleButton3.setOnClickListener(this);
        circleButton3.setOnTouchListener(this);
        if(savedInstanceState != null) {
            Log.v(TAG, "savedInstanceState not null");
            score = savedInstanceState.getInt("score");
            target = savedInstanceState.getInt("target");
            colors = savedInstanceState.getIntArray("colors");
            rgb = savedInstanceState.getString("rgb");

            circleButton1.setColor(colors[0]);
            circleButton2.setColor(colors[1]);
            circleButton3.setColor(colors[2]);

            scoreTextView.setText(getScore());
            rgbColorTextView.setText(rgb);
        }
        else {
            colors = getIntent().getIntArrayExtra(EXTRA_COLOR);
            if(colors == null)
                colors = new ColorUtils().getRandomColor(3);
            generateRandomColors();
        }
    }

    private void setupWindowAnimations() {
        interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);
        //setupExitAnimations();
        animateButtonsIn();
    }

    protected void generateRandomColors() {
        isRight = false;
        circleButton1.setColor(colors[0]);
        circleButton2.setColor(colors[1]);
        circleButton3.setColor(colors[2]);

        scoreTextView.setText(getScore());
        rgbColorTextView.setText(getRandomColor());
    }

    protected String getRandomColor() {

        final int random = new Random().nextInt(3);
        target = colors[random];
        Color c = new Color();
        int red = c.red(target);
        int green = c.green(target);
        int blue = c.blue(target);
        rgb = "rgb("+red+", "+green+", "+blue+")";
        return rgb;
    }

    protected String getScore() {
        return "SCORE: " + score;
    }

    protected int getColor() {
        return target;
    }

    protected void rightAnswer(View view) {
        score++;
        revealView.setBackgroundColor(thisGuy);
        //revealView.setVisibility(View.VISIBLE);
        int [] location = new int[2];
        view.getLocationOnScreen(location);
        location[0] += (view.getWidth() / 2);
        location[1] += (view.getHeight()  / 2);
        bundle = new Bundle();
        bundle.putInt(EXTRA_COLOR, target);
        bundle.putIntArray("location", location);
        bundle.putInt("position", position);
        GUIUtils.showRevealEffect(revealView, location[0], location[1], revealAnimationListener);
    }

    protected void wrongAnswer(View view) {
        setHighScore();
        revealView.setBackgroundColor(thisGuy);
        int [] location = new int[2];
        view.getLocationOnScreen(location);
        location[0] += (view.getWidth() / 2);
        location[1] += (view.getHeight() / 2);
        bundle = new Bundle();
        bundle.putInt(EXTRA_COLOR, thisGuy);
        bundle.putString(EXTRA_SCORE, score + "");
        bundle.putIntArray("location", location);
        bundle.putInt("position", position);
        GUIUtils.showRevealEffect(revealView, location[0], location[1], revealAnimationListener);
    }

    private void setHighScore() {
        if(score > 0) {
            SharedPreferences.Editor scoreEdit = gamePrefs.edit();
            String nickname = getPreferredNickname(this);
            String scores = gamePrefs.getString("highScores", "");

            if(scores.length() > 0) {
                List<Score> scoreStrings = new ArrayList<Score>();
                String[] exScores = scores.split("\\|");
                for(String eSc : exScores){
                    String[] parts = eSc.split(" - ");
                    scoreStrings.add(new Score(parts[0], Integer.parseInt(parts[1])));
                }
                Score newScore = new Score(nickname, score);
                scoreStrings.add(newScore);
                Collections.sort(scoreStrings);

                StringBuilder scoreBuild = new StringBuilder("");
                for(int i = 0; i < scoreStrings.size(); i++){
                    if(i >= 10) break;//only want ten
                    if(i > 0) scoreBuild.append("|");//pipe separate the score strings
                    scoreBuild.append(scoreStrings.get(i).getScoreText());
                }
                //write to prefs
                scoreEdit.putString("highScores", scoreBuild.toString());
                scoreEdit.commit();
            }
            else {
                scoreEdit.putString("highScores", nickname + " - " + score);
                scoreEdit.commit();
            }

        }
    }

    public static String getPreferredNickname(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_nickname_key),
                context.getString(R.string.pref_nickname_default));
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(GameActivity.this, PauseActivity.class));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(v.getId() == R.id.circle_button1 || v.getId() == R.id.circle_button2 || v.getId() == R.id.circle_button3) {
                mX = (int) event.getX();
                mY = (int) event.getY();
            }
        }
        return false;
    }

    private void animateButtonsIn() {
        Log.d(TAG, "animateButtonsIn");
        for (int i = 0; i < bgViewGroup.getChildCount(); i++) {
            Log.d(TAG, i+" child");
            View child = bgViewGroup.getChildAt(i);
            child.animate()
                    .setStartDelay(100 + i * DELAY)
                    .setInterpolator(interpolator)
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1);
        }
    }

    Animator.AnimatorListener revealAnimationListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {}

        @Override
        public void onAnimationEnd(Animator animation) {
            Intent intent;
            if(isRight) {
                intent = new Intent(GameActivity.this, RightActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtras(bundle);
                startActivityForResult(intent, NEXT_COLOR_REQUEST);
                overridePendingTransition(0, 0);
            }
            else {
                intent = new Intent(GameActivity.this, WrongActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {}

        @Override
        public void onAnimationRepeat(Animator animation) {}
    };
}
