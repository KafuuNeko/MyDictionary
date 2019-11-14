package com.smallcake.mydictionary.mvp.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.smallcake.mydictionary.sqlite.WordsDataBase;
import com.smallcake.mydictionary.struct.Words;

import java.util.ArrayList;
import java.util.List;

public class WordsModelImpV1 implements IWordsModel {
    private ReadWordsType mReadWordsType;
    private Context mContext;

    public WordsModelImpV1(Context context, ReadWordsType readType){
        mContext = context;
        mReadWordsType = readType;
    }

    @Override
    public void loadData(onListenerComplete listener) {
        List<Words> Data = new ArrayList<Words>();

        WordsDataBase wordsDataBaseOpenHelper = new WordsDataBase(mContext);

        SQLiteDatabase dbWords = wordsDataBaseOpenHelper.getReadableDatabase();

        String sql = "SELECT id,words,describe,familiar FROM basic_words";

        switch (mReadWordsType){

            case Familiar_Words:
                sql += " WHERE familiar != 0";
                break;

            case New_Words:
                sql += " WHERE familiar == 0";
                break;

        }

        sql += " ORDER BY LOWER(words) ASC";

        Cursor cursor = dbWords.rawQuery(sql,null);

        while (cursor.moveToNext()){

            Log.d("ReadWords","id:"+cursor.getLong(0));

            Data.add(new Words(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    (cursor.getInt(3) != 0)));

        }

        cursor.close();

        wordsDataBaseOpenHelper.close();

        listener.loadComplete(Data);

    }

}
