package ru.myitschool.vsu2023.eds.memo_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBmemo {

    private static final String DATABASE_NAME = "memo.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CATEGORY_TABLE_NAME = "tableCategory";
    private static final String QUESTION_TABLE_NAME = "tableQuestions";

    private static final String COLUMN_ID_CAT = "id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_ID_QU = "id";
    private static final String COLUMN_WORD = "word";
    private static final String COLUMN_RIGHT_LETTER = "rightLetter";
    private static final String COLUMN_WRONG_LETTER = "wrongLetter";
    private static final String COLUMN_IMAGE_NAME = "imageName";
    private static final String COLUMN_CATEGORY_QU = "category_id";

    private static final int NUM_COLUMN_ID_CAT = 0;
    private static final int NUM_COLUMN_CATEGORY = 1;
    private static final int NUM_COLUMN_ID_QU = 0;
    private static final int NUM_COLUMN_WORD = 1;
    private static final int NUM_COLUMN_RIGHT_LETTER = 2;
    private static final int NUM_COLUMN_WRONG_LETTER = 3;
    private static final int NUM_COLUMN_IMAGE_NAME = 4;
    private static final int NUM_COLUMN_CATEGORY_QU = 5;

    private SQLiteDatabase mDataBase;

    public DBmemo(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(Question qu) {
        return insert(qu.getText(), qu.getRightLet(), qu.getWrongLet(), qu.getImageName(), qu.getCategoryId());

    }

    public long insert(String word, String rightLetter, String wrongLetter, String imageName, Integer categoryQu) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_WORD, word);
        cv.put(COLUMN_RIGHT_LETTER, rightLetter);
        cv.put(COLUMN_WRONG_LETTER, wrongLetter);
        cv.put(COLUMN_IMAGE_NAME, imageName);
        cv.put(COLUMN_CATEGORY_QU, categoryQu);
        return mDataBase.insert(QUESTION_TABLE_NAME, null, cv);
    }

    public long insert(String category) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CATEGORY, category);

        return mDataBase.insert(CATEGORY_TABLE_NAME, null, cv);
    }

    /*public int update(Category md) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CATEGORY, md.getName());
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(md.getId())});
    }

    public int update(Ques md) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_WORD, md.getWord());
        cv.put(COLUMN_RIGHT_LETTER, md.getRightLetter());
        cv.put(COLUMN_WRONG_LETTER, md.getWrongLetter());
        cv.put(COLUMN_IMAGE_NAME, md.getImageName());
        cv.put(COLUMN_CATEGORY_QU, md.getCategory());

        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(md.getId())});
    }*/


    public void deleteQuest(int id) {
        mDataBase.delete(QUESTION_TABLE_NAME, COLUMN_ID_QU + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteCategory(int id) {
        mDataBase.delete(QUESTION_TABLE_NAME, COLUMN_CATEGORY_QU + " = ?", new String[]{String.valueOf(id)});
        mDataBase.delete(CATEGORY_TABLE_NAME, COLUMN_ID_CAT + " = ?", new String[]{String.valueOf(id)});
    }

    public Question selectQues(int id) {
        Cursor mCursor = mDataBase.query(QUESTION_TABLE_NAME, null, COLUMN_ID_QU + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        String Word = mCursor.getString(NUM_COLUMN_WORD);
        String RightLet = mCursor.getString(NUM_COLUMN_RIGHT_LETTER);
        String WrongLet = mCursor.getString(NUM_COLUMN_WRONG_LETTER);
        String ImageName = mCursor.getString(NUM_COLUMN_IMAGE_NAME);
        Integer Category = mCursor.getInt(NUM_COLUMN_CATEGORY_QU);

        return new Question(id, Word, RightLet, WrongLet, ImageName, Category);
    }

    public Category selectCat(int id) {
        Cursor mCursor = mDataBase.query(CATEGORY_TABLE_NAME, null, COLUMN_ID_CAT + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        String Category = mCursor.getString(NUM_COLUMN_CATEGORY);


        return new Category(id, Category);
    }

    public List<Category> selectAllCat() {
        Cursor mCursor = mDataBase.query(CATEGORY_TABLE_NAME, null, null, null, null, null, null);

        List<Category> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                Integer id = mCursor.getInt(NUM_COLUMN_ID_CAT);
                String categoryName = mCursor.getString(NUM_COLUMN_CATEGORY);

                arr.add(new Category(id, categoryName));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    public List<Question> selectQuestOfCat(int categoryId) {
        Cursor mCursor = mDataBase.query(QUESTION_TABLE_NAME, null, COLUMN_CATEGORY_QU + " = ?", new String[]{String.valueOf(categoryId)}, null, null, null);

        List<Question> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                Integer id = mCursor.getInt(NUM_COLUMN_ID_QU);
                String questWord = mCursor.getString(NUM_COLUMN_WORD);
                String qustRLet = mCursor.getString(NUM_COLUMN_RIGHT_LETTER);
                String questWLet = mCursor.getString(NUM_COLUMN_WRONG_LETTER);
                String qustImNme = mCursor.getString(NUM_COLUMN_IMAGE_NAME);
                Integer qustCategId = mCursor.getInt(NUM_COLUMN_CATEGORY_QU);


                arr.add(new Question(id, questWord, qustRLet, questWLet, qustImNme, qustCategId));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query1 = "CREATE TABLE " + CATEGORY_TABLE_NAME + " (" +
                    COLUMN_ID_CAT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY + " TEXT);";
            db.execSQL(query1);

            String query = "CREATE TABLE " + QUESTION_TABLE_NAME + " (" +
                    COLUMN_ID_QU + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WORD + " TEXT, " +
                    COLUMN_RIGHT_LETTER + " TEXT, " +
                    COLUMN_WRONG_LETTER + " TEXT," +
                    COLUMN_IMAGE_NAME + " TEXT," +
                    COLUMN_CATEGORY_QU + " INTEGER);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + QUESTION_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
            onCreate(db);

        }
    }


}
