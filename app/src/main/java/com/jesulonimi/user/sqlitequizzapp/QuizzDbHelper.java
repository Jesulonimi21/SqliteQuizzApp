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
        Category category=new Category("Lonimi's Life");
        addCategory(category);
        Category category1=new Category("Lonimi's Career");
        addCategory(category1);
        Category category2=new Category("Lonimi's books");
        addCategory(category2);

    }
        public void addCategory(Category category){
        ContentValues contentValues=new ContentValues();
        contentValues.put(QuizzContract.CategoriesTable.COLUMN_NAME,category.getName());
        db.insert(QuizzContract.CategoriesTable.TABLE_NAME,null,contentValues);

        }
    private void fillQuestions(){
        Log.d("calledDatabase","fillQuestions");
        Question question1=new Question("Which month was Lonimi born","June","may","february",3,
                Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        addQuestion(question1);
        Question question12=new Question("Which year was Lonimi born","1999","1998","2000",1,
                Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        addQuestion(question12);
        Question question13=new Question("What is the age range Lonimi plans to get married","25 to 26 years old","27 to 28 years old","29 to 30 years old",1,
                Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        addQuestion(question13);
        Question question14=new Question("What is Lonimi's best food","Rice","Yam","any food i eat when am hungry",3,
                Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        addQuestion(question14);
        Question question15=new Question("Who is Lonimi's best scientist","Isaac Newton","Albert Einstein","Blaise Pascal",2,
                Question.DIFFICULTY_EASY,Category.PROGRAMMING);
        addQuestion(question15);

        Question question2=new Question("Which department is Lonimi","Computer Science","BioChemistry","Chemistry",1,
                Question.DIFFICULTY_EASY,Category.GEOGRAPHY);
        addQuestion(question2);
        Question question22=new Question("Which subject did Lonimi pass once and fail every other time through out his secondary school days ","Chemistry","Technical Drawing","Math",2,
                Question.DIFFICULTY_EASY,Category.GEOGRAPHY);
        addQuestion(question22);

        Question question23=new Question("Which was Lonimi's best subject in secondary school","Chemistry","Technical Drawing","Lonimi had no best subject and hated all subjects",3,
                Question.DIFFICULTY_EASY,Category.GEOGRAPHY);
        addQuestion(question23);
        Question question24=new Question("What is Lonimis belief about learning","Anything can be learnt by anyone, it just requires continous effort","Education is not for everyone, we all have different talents","Learning is the solely dependent on the students ability to learn",1,
                Question.DIFFICULTY_EASY,Category.GEOGRAPHY);
        addQuestion(question24);

        Question question25=new Question("Given the chance to start university over again, which course will Lonimi Study","Computer Science","History","Physics",2,
                Question.DIFFICULTY_EASY,Category.GEOGRAPHY);
        addQuestion(question25);

        Question question3=new Question("What is Lonimi's best book of all time","Harry Potter","Percy Jackson","Nobody's child",3,
                Question.DIFFICULTY_EASY,Category.MATH);
        addQuestion(question3);
        Question question31=new Question("What was the genre of Lonimis best book","Adventure","Magic","Romance",1,
                Question.DIFFICULTY_EASY,Category.MATH);
        addQuestion(question31);

        Question question32=new Question("At what range of age did Lonimi read this particular book","8-9 years","10-11","12-13",2,
                Question.DIFFICULTY_EASY,Category.MATH);
        addQuestion(question32);
        Question question33=new Question("What are the kind of novels Lonimi reads now adays","Spiritual","Academic","Magic",1,
                Question.DIFFICULTY_EASY,Category.MATH);
        addQuestion(question33);
        Question question34=new Question("If given the chance, what genre of books will Lonimi read","Spiritual","Academic","Magic",1,
                Question.DIFFICULTY_EASY,Category.MATH);
        addQuestion(question34);

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
