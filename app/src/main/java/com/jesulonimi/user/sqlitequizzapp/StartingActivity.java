package com.jesulonimi.user.sqlitequizzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.jesulonimi.user.sqlitequizzapp.QuizzActivity.EXTRA_SCORE;

public class StartingActivity extends AppCompatActivity {
public static final int REQ_CODE_QUIZ=1;
public static final String SHARED_PREFS="sharedPrefs";
public static final String KEY_HIGHSCORE="highscore_key";
public static final String EXTRA_DIFFICULTY="extra_difficulty";
public static final String EXTRA_CATEGORY_ID="extra_category_id";
public static final String EXTRA_CATEGORY_NAME="extra_category_name";



private Spinner spinnerDifficulty;
private Spinner spinnerCategory;
private TextView textViewHighScore;
private int highscore;
long backPressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button_start_quizz);
        textViewHighScore=findViewById(R.id.textview_highscore);
        loadHighScore();
        spinnerDifficulty=findViewById(R.id.spinner_dificultyLevel);
        spinnerCategory=findViewById(R.id.spinner_category);

        loadCategories();
        loadDifficultyLevels();

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    startQuizz();
                    }
                }
        );
    }

    private void startQuizz(){
        Category category=(Category)spinnerCategory.getSelectedItem();
        int categoryId=category.getId();
        String categoryName=category.getName();

        String difficulty=spinnerDifficulty.getSelectedItem().toString();
        Intent i=new Intent(this,QuizzActivity.class);
        i.putExtra(EXTRA_CATEGORY_NAME,categoryName);
        i.putExtra(EXTRA_CATEGORY_ID,categoryId);
        i.putExtra(EXTRA_DIFFICULTY,difficulty);
        startActivityForResult(i,REQ_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQ_CODE_QUIZ){
            if(resultCode==RESULT_OK){
                int score=data.getIntExtra(EXTRA_SCORE,0  );
                if(score>highscore){
                updateHighScore(score);
                }
            }
        }
    }
    private void loadCategories(){
        QuizzDbHelper quizzDbHelper=QuizzDbHelper.getInstance(this);
        List<Category> catList=quizzDbHelper.getAllCategories();
        ArrayAdapter<Category> categoryArrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,catList);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryArrayAdapter);
    }

    private void loadDifficultyLevels(){
        String[] difficultyLevels=Question.getAllDifficulties();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,difficultyLevels);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(arrayAdapter);

    }

    public void loadHighScore(){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        highscore=sharedPreferences.getInt(KEY_HIGHSCORE,0);
        textViewHighScore.setText("Highscore : "+highscore);

    }
    public void updateHighScore(int score){
        highscore=score;
        textViewHighScore.setText("Highscore : "+highscore);
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(KEY_HIGHSCORE,highscore);
        editor.apply();
        loadHighScore();
    }


}
