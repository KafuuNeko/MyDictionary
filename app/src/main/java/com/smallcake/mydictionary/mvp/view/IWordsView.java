package com.smallcake.mydictionary.mvp.view;

import com.smallcake.mydictionary.struct.Words;

import java.util.List;

public interface IWordsView {
    void onWordsLoading();
    void onWordsLoadComplete(List<Words> words);
}
