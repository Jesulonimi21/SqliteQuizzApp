package com.jesulonimi.user.sqlitequizzapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import  com.jesulonimi.user.sqlitequizzapp.QuizzContract;

import java.util.ArrayList;
import java.util.List;

import static com.jesulonimi.user.sqlitequizzapp.QuizzContract.QuestionsTable.COLUMN_ANSWER_NO;
import static com.jesulonimi.user.sqlitequizzapp.QuizzContract.QuestionsTable.COLUMN_CATEGORY_ID;
import static com.jesulonimi.user.sqlitequizzapp.QuizzContract.QuestionsTable.COLUMN_DIFFICULTY;
import static com.jesulonimi.user.sqlitequizzapp.QuizzContract.QuestionsTable.COLUMN_OPTION1;
import static com.jesulonimi.user.sqlitequizzapp.QuizzContract.QuestionsTable.COLUMN_OPTION2;
import static com.jesulonimi.user.sqlitequizzapp.QuizzContract.QuestionsTable.COLUMN_OPTION3;
import static com.jesulonimi.user.sqlitequizzapp.QuizzContract.QuestionsTable.COLUMN_QUESTION;
import static com.jesulonimi.user.sqlitequizzapp.QuizzContract.QuestionsTable.TABLE_NAME;

public class QuizzDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="MyLegendaryQuizz.db";
    public static final int DATABASE_VERSION=1;

    private SQLiteDatabase db;
    private static QuizzDbHelper instance;

    public static synchronized  QuizzDbHelper getInstance(Context context){
        if(instance==null){
            instance=new QuizzDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private QuizzDbHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;

   final String SQL_CREATE_CATEGORIES_TABLE=" CREATE TABLE "+
           QuizzContract.CategoriesTable.TABLE_NAME + " ( "+
           QuizzContract.CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
           QuizzContract.CategoriesTable.COLUMN_NAME +" TEXT "+")";


   final String SQL_CREATE_TABLE="CREATE TABLE "+
            TABLE_NAME +" ( "+
            QuizzContract.QuestionsTable._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            QuizzContract.QuestionsTable.COLUMN_QUESTION + " TEXT, "+
            QuizzContract.QuestionsTable.COLUMN_OPTION1 + " TEXT, "+
            QuizzContract.QuestionsTable.COLUMN_OPTION2 + " TEXT, "+
            QuizzContract.QuestionsTable.COLUMN_OPTION3 + " TEXT, "+
            QuizzContract.QuestionsTable.COLUMN_ANSWER_NO + " INTEGER, "+
            QuizzContract.QuestionsTable.COLUMN_DIFFICULTY+" TEXT,  "+
           QuizzContract.QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, "+
           "FOREIGN KEY("+QuizzContract.QuestionsTable.COLUMN_CATEGORY_ID+") REFERENCES "+
           QuizzContract.CategoriesTable.TABLE_NAME +"("+QuizzContract.CategoriesTable._ID+")"+ " ON DELETE CASCADE "+
            ")";
            db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
            db.execSQL(SQL_CREATE_TABLE);
            fillCategoriesTable();
            fillQuestions();
         

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+QuizzContract.CategoriesTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    public void fillCategoriesTable(){
        Category category=new Category("Programming");
        addCategory(category);
        Category category1=new Category("Geography");
        addCategory(category1);
        Category category2=new Category("Math");
        addCategory(category2);

    }
        public void addCategory(Category category){
        ContentValues contentValues=new ContentValues();
        contentValues.put(QuizzContract.CategoriesTable.COLUMN_NAME,category.getName());
        db.insert(QuizzContract.CategoriesTable.TABLE_NAME,null,contentValues);

        }
    private void fillQuestions(){
        Log.d("calledDatabase","fillQuestions");
        Question question1=new Question("Programming easy :A is correct","A","B","C",1,
                Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        addQuestion(question1);

        Question question2=new Question("Geography easy :A is correct","A","B","C",1,
                Question.DIFFICULTY_EASY,Category.GEOGRAPHY);
        addQuestion(question2);

        Question question3=new Question("Math medium :A is correct","A","B","C",1,
                Question.DIFFICULTY_MEDIUM,Category.MATH);
        addQuestion(question3);

        Question question4=new Question("Programming medium :A is correct","A","B","C",1,
                Question.DIFFICULTY_MEDIUM,Category.PROGRAMMING);
        addQuestion(question4);

        Question question5=new Question("Geography hard :A is correct","A","B","C",1,
                Question.DIFFICULTY_HARD,Category.GEOGRAPHY);
        addQuestion(question5);

        Question question6=new Question("Math hard :A is correct","A","B","C",1,
                Question.DIFFICULTY_HARD,Category.MATH);
        addQuestion(question6);
        Question question7=new Question("Math hard :A is correct","A","B","C",1,
                Question.DIFFICULTY_HARD,Category.MATH);
        addQuestion(question7);


    }

    private void addQuestion(Question question){
        ContentValues contentValues=new ContentValues();
        contentValues.put(QuizzContract.QuestionsTable.COLUMN_QUESTION,question.getQuestion());
        contentValues.put(QuizzContract.QuestionsTable.COLUMN_OPTION1,question.getOption1());
        contentValues.put(QuizzContract.QuestionsTable.COLUMN_OPTION2,question.getOption2());
        contentValues.put(QuizzContract.QuestionsTable.COLUMN_OPTION3,question.getOption3());
        contentValues.put(QuizzContract.QuestionsTable.COLUMN_ANSWER_NO,question.getAnswerNo());
        contentValues.put(QuizzContract.QuestionsTable.COLUMN_DIFFICULTY,question.getDifficulty());
        contentValues.put(QuizzContract.QuestionsTable.COLUMN_CATEGORY_ID,question.getCategoryId());
        db.insert(TABLE_NAME,null,contentValues);
    }

    public List<Category> getAllCategories(){
        db=getReadableDatabase();
        ArrayList<Category> categoryArrayList=new ArrayList<>();
        Cursor c=db.rawQuery("SELECT * FROM "+QuizzContract.CategoriesTable.TABLE_NAME,null);
        if(c.moveToFirst()){
            do{
                Category category=new Category();
                category.setId(c.getInt(c.getColumnIndex(QuizzContract.CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(QuizzContract.CategoriesTable.COLUMN_NAME)));
                categoryArrayList.add(category);

            }while (c.moveToNext());
        }
        c.close();
        return  categoryArrayList;
    }
    public ArrayList<Question> getAllQuestions(){
        ArrayList<Question> allQuestions=new ArrayList<>();
        db=getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);

        if(cursor.moveToFirst()){
            do{
             Question question=new Question();
             question.setId(cursor.getInt(cursor.getColumnIndex(QuizzContract.QuestionsTable._ID)));
             question.setQuestion(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION)));
             question.setOption1(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION1)));
             question.setOption2(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION2)));
             question.setOption3(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION3)));
             question.setAnswerNo(cursor.getShort(cursor.getColumnIndex(COLUMN_ANSWER_NO)));
             question.setDifficulty(cursor.getString(cursor.getColumnIndex(COLUMN_DIFFICULTY)));
             question.setCategoryId(cursor.getShort(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));

             allQuestions.add(question);
            }while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("calledDatabase","getAllQuestions"+ allQuestions.size());
        return allQuestions;
    }
    public ArrayList<Question> getQuestions(int categoryId,String difficulty){
        ArrayList<Question> allQuestions=new ArrayList<>();


        db=getReadableDatabase();
      String selection=QuizzContract.QuestionsTable.COLUMN_CATEGORY_ID+" = ? "+
              " AND "+QuizzContract.QuestionsTable.COLUMN_DIFFICULTY +" = ? ";
        String[] selectionArg=new String[]{String.valueOf(categoryId),difficulty};

        Cursor cursor=db.query(QuizzContract.QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArg,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            do{
                Question question=new Question();
                question.setId(cursor.getInt(cursor.getColumnIndex(QuizzContract.QuestionsTable._ID)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION)));
                question.setOption1(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndex(COLUMN_OPTION3)));
                question.setAnswerNo(cursor.getShort(cursor.getColumnIndex(COLUMN_ANSWER_NO)));
                question.setDifficulty(cursor.getString(cursor.getColumnIndex(COLUMN_DIFFICULTY)));
                question.setCategoryId(cursor.getShort(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
                allQuestions.add(question);
            }while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("calledDatabase","getAllQuestions"+ allQuestions.size());
        return allQuestions;
    }
}
