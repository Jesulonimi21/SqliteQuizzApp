package com.jesulonimi.user.sqlitequizzapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.jesulonimi.user.sqlitequizzapp.StartingActivity.EXTRA_CATEGORY_ID;
import static com.jesulonimi.user.sqlitequizzapp.StartingActivity.EXTRA_CATEGORY_NAME;
import static com.jesulonimi.user.sqlitequizzapp.StartingActivity.EXTRA_DIFFICULTY;

public class QuizzActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "etrascore";
    public static final long COUNTDOWN_IN_MILLIS = 30000;

    public static final String KEY_SCORE = "KEY_SCORE";
    public static final String KEY_QUESTION_COUNT = "KEY_QUESTION_COUNT";
    public static final String KEY_MILIIS_LEFT = "KEY_MILLIS_LEFT";
    public static final String KEY_ANSWERRED = "KEY_ANSWERRED";
    public static final String KEY_QUESTION_LIS = "KEY_QUESTION_LIST";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private TextView textViewDifficulty;
    private TextView textViewCategory;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioGroup radioGroup;
    private Button buttonConfirm;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInmillis;

    private int questionCounter = 0;
    private int questionCountTotal;
    private int score = 0;
    private boolean answered;
    private Question currentQuestion;
    long backPressedTime;
    private ArrayList<Question> allQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewQuestionCount = findViewById(R.id.text_question_count);
        buttonConfirm = findViewById(R.id.button_confirm_next);
        textViewCategory=findViewById(R.id.text_view_category);
        rb1 = findViewById(R.id.radioButton1);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        radioGroup = findViewById(R.id.radio_group);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizzActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    showNextQuestion();
                }
            }
        });
        int categoryId=getIntent().getIntExtra(EXTRA_CATEGORY_ID,0);
        String categoryName=getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        textViewCategory.setText("Category : "+categoryName);
        textViewDifficulty=findViewById(R.id.text_view_difficulty);
        String difficulty=getIntent().getStringExtra(EXTRA_DIFFICULTY);
        textViewDifficulty.setText("Difficulty : "+ difficulty);
        if (savedInstanceState == null) {
            QuizzDbHelper quizzDbHelper = QuizzDbHelper.getInstance(this);
            allQuestion = quizzDbHelper.getQuestions(categoryId,difficulty);
            Collections.shuffle(allQuestion);
            questionCountTotal = allQuestion.size();
            showNextQuestion();
        }else{
            allQuestion=savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIS);
            questionCountTotal=allQuestion.size();
            score=savedInstanceState.getInt(KEY_SCORE);
            questionCounter=savedInstanceState.getInt(KEY_QUESTION_COUNT);
            timeLeftInmillis=savedInstanceState.getLong(KEY_MILIIS_LEFT);
            currentQuestion=allQuestion.get(questionCounter-1);
            answered=savedInstanceState.getBoolean(KEY_ANSWERRED);

            if(!answered){
                startCountDownTimer();
            }else{
                updateCountDownText();
                showSolution();
            }
        }
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        radioGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = allQuestion.get(questionCounter);
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            textViewQuestion.setText(currentQuestion.getQuestion());

            questionCounter++;
            textViewQuestionCount.setText("Question : " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirm.setText("Confirm");

            timeLeftInmillis = COUNTDOWN_IN_MILLIS;
            startCountDownTimer();
        } else {
            finishQuizz();
        }
    }


    private void finishQuizz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();

    }

    public void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
        RadioButton checkedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        int answeredNo = radioGroup.indexOfChild(checkedRadioButton) + 1;
        if (answeredNo == currentQuestion.getAnswerNo()) {
            score++;
            textViewScore.setText("score : " + score);
        }
        showSolution();


    }

    public void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNo()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 1 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Option 1 is correct");
                break;
        }
        if (questionCounter < questionCountTotal) {
            buttonConfirm.setText("Next");
        } else {
            buttonConfirm.setText("finish");
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuizz();
        } else {
            Toast.makeText(this, "press back button again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }


    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeLeftInmillis, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInmillis = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInmillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();

    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInmillis / 1000) / 60;
        int seconds = (int) (timeLeftInmillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);

        if (timeLeftInmillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILIIS_LEFT, timeLeftInmillis);
        outState.putBoolean(KEY_ANSWERRED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIS, allQuestion);
    }
}
