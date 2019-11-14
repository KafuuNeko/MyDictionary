package com.smallcake.mydictionary.mvp.presenter;

import android.content.Context;

import com.smallcake.mydictionary.mvp.model.IWordsModel;
import com.smallcake.mydictionary.mvp.model.ReadWordsType;
import com.smallcake.mydictionary.mvp.model.WordsModelImpV1;
import com.smallcake.mydictionary.struct.Words;
import com.smallcake.mydictionary.mvp.view.IWordsView;

import java.util.List;

public class WordsPresenterV1 implements IWordsPresenter {
    private IWordsView mWordsView;
    private IWordsModel mWordsModel;

    public WordsPresenterV1(Context context, IWordsView iWordsView, ReadWordsType readType){
        mWordsView = iWordsView;
        mWordsModel = new WordsModelImpV1(context, readType);
    }

    @Override
    public void fetch(){
        if(mWordsView == null || mWordsModel == null) return;
        mWordsView.onWordsLoading();
        mWordsModel.loadData(new IWordsModel.onListenerComplete() {
            @Override
            public void loadComplete(List<Words> words) {
                mWordsView.onWordsLoadComplete(words);
            }
        });
    }

}
