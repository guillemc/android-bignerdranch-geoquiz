package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String TAG = "CheatActivity";

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String KEY_ANSWER_SHOWN = "answer_shown";

    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private boolean mAnswerShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN, false);
            setAnswerShownResult();
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswerTextView.setText(mAnswerIsTrue ? R.string.true_button : R.string.false_button);
                mAnswerShown = true;
                setAnswerShownResult();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswer.getWidth() / 2;
                    int cy = mShowAnswer.getHeight() / 2;
                    float radius = mShowAnswer.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mAnswerTextView.setVisibility(View.VISIBLE);
                            mShowAnswer.setVisibility(View.INVISIBLE);

                        }
                    });
                    anim.start();
                } else {
                    mAnswerTextView.setVisibility(View.VISIBLE);
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });

        TextView tv = (TextView) findViewById(R.id.view_version);
        tv.setText(String.format("API level %d", Build.VERSION.SDK_INT));

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "CheatActivity.onSaveInstanceState(Bundle) called");
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, mAnswerShown);
    }

    private void setAnswerShownResult()
    {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mAnswerShown);

        // setting a result with result code and intent with data
        setResult(RESULT_OK, data);
    }

    public static Intent newIntent(Context c, boolean answerIsTrue) {
        Intent i = new Intent(c, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent data) {
        return data.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

}
