package com.smallcake.mydictionary.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smallcake.mydictionary.mvp.presenter.IWordsPresenter;

public class BWordsLoad extends BroadcastReceiver {

    private IWordsPresenter mWordsPresenter;
    private String mAction;

    public BWordsLoad(IWordsPresenter wordsPresenter, String pAction){
        mWordsPresenter = wordsPresenter;
        mAction = pAction;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(mAction)){
            mWordsPresenter.fetch();
        }

    }
}
