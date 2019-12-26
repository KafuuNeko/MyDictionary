package com.smallcake.mydictionary.io;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseConfig extends Properties {

    private boolean mOpenSuccess = false;
    private Context mContext = null;
    private String mName = null;

    public BaseConfig() {
        super();
    }

    public BaseConfig(Context context, String name) {
        this();

        mContext = context;
        mName = name;
        try {
            FileInputStream fileInputStream = context.openFileInput(name);
            load(fileInputStream);
            fileInputStream.close();
            mOpenSuccess = true;
        }
        catch (IOException e)
        {
            Log.w("BaseConfig", e.toString());
        }
    }

    public boolean isOpenSuccess(){
        return mOpenSuccess;
    }

    public boolean save() {
        if (mContext == null || mName == null) return false;
        return save(mContext, mName);
    }

    public boolean save(Context context, String name) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            store(fileOutputStream, null);
            fileOutputStream.close();
        }
        catch (IOException e)
        {
            Log.w("BaseConfig.save", e.toString());
            return false;
        }

        return true;
    }

}
