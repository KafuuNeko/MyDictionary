package com.smallcake.mydictionary.mvp.model;

import com.smallcake.mydictionary.struct.Words;

import java.util.List;

public interface IWordsModel {

    void loadData(onListenerComplete listener);

    interface onListenerComplete{
        void loadComplete(List<Words> words);
    }


}
