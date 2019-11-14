package com.smallcake.mydictionary.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.smallcake.mydictionary.struct.Words;

public class WordsDataBase extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 1;

    public WordsDataBase(Context context){
        super(context,context.getFilesDir()+"/words.db", null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS basic_words(id integer primary key,words string,describe string,familiar boolean) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public WordsDataBase addWord(Words words){
        Log.d("addWord","add words = " + words.getWords() + ",describe = "+words.getDescribe() + "  Describe = " +words.getDescribe());

        String[] path = new String[2];

        path[0] = words.getWords();
        path[1] = words.getDescribe();

        String familiar = (words.familiar?"1":"0");

        getWritableDatabase().execSQL("INSERT INTO basic_words(words,describe,familiar) VALUES(?,?,"+ familiar +")", path);

        return this;
    }


    public WordsDataBase updateWord(Words words){
        Log.d("updateWord","update words: id = "+words.getWId() + "  words = " + words.getWords() + "  Describe = " +words.getDescribe());


        if (words.wid == -1) return this;

        SQLiteDatabase db = getWritableDatabase();
        String[] path = new String[2];

        path[0] = words.getWords();
        path[1] = words.getDescribe();

        String familiar = (words.familiar?"1":"0");

        db.execSQL("UPDATE basic_words SET words=?,describe=?,familiar= "+ familiar +" WHERE id = " + words.wid, path);

        return this;
    }

    public WordsDataBase deleteWord(Words words){
        Log.d("deleteWord","update words: id = "+words.getWId() + "  words = " + words.getWords() + "  Describe = " +words.getDescribe());

        if (words.wid == -1) return this;
        getWritableDatabase().execSQL("DELETE FROM basic_words WHERE id = " + words.wid);

        return this;
    }

    public WordsDataBase setFamiliar(Words words,boolean familiar){
        words.setFamiliar(familiar);
        updateWord(words);

        return this;
    }

}
