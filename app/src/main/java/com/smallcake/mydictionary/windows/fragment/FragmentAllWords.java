package com.smallcake.mydictionary.windows.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.smallcake.mydictionary.R;
import com.smallcake.mydictionary.adapter.ListViewWordsAdapter;
import com.smallcake.mydictionary.broadcast.BWordsLoad;
import com.smallcake.mydictionary.listener.WordsListItemClickListener;
import com.smallcake.mydictionary.mvp.presenter.IWordsPresenter;
import com.smallcake.mydictionary.struct.Words;
import com.smallcake.mydictionary.mvp.view.IWordsView;
import com.smallcake.mydictionary.mvp.presenter.WordsPresenterV1;

import java.util.List;

import static android.support.v4.content.LocalBroadcastManager.*;
import static com.smallcake.mydictionary.mvp.model.ReadWordsType.ALL_WORDS;

public class FragmentAllWords extends Fragment implements IWordsView {
    private View mRootView = null;
    private ListView mLvAllWords;
    private TextView tvAllWordsTips;
    private BWordsLoad mBroadcast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //加载Fragment布局
        if(null != mRootView){
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            parent.removeView(mRootView);
        }else{
            mRootView = inflater.inflate(R.layout.fragment_all_words, null);
        }

        IWordsPresenter wordsPresenter = new WordsPresenterV1(getContext(),this, ALL_WORDS);

        //查找组件
        mLvAllWords = mRootView.findViewById(R.id.fragment_all_words_lv_all_words);
        tvAllWordsTips = mRootView.findViewById(R.id.fragment_all_words_tv_all_words_tips);

        //注册列表更新广播，以便接收更新列表请求
        String action = "load.all_words";
        mBroadcast = new BWordsLoad(wordsPresenter, action);
        getInstance(getContext()).registerReceiver(mBroadcast, new IntentFilter(action));

        //发送广播，通知更新列表
        getInstance(getContext()).sendBroadcast(new Intent(action));

        return mRootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getInstance(getContext()).unregisterReceiver(mBroadcast);
        mRootView = null;
    }

    @Override
    public void onWordsLoading() {
        Log.d("onWordsLoading","正在加载所有单词");
    }

    @Override
    public void onWordsLoadComplete(List<Words> words) {
        int wordsNumber = words.size();
        int position = mLvAllWords.getFirstVisiblePosition();//记录第一条显示记录位置

        mLvAllWords.setAdapter(new ListViewWordsAdapter(getContext(), words));
        mLvAllWords.setOnItemClickListener(new WordsListItemClickListener(getContext(), words));

        if (wordsNumber == 0) {
            tvAllWordsTips.setText(R.string.tr_all_word_is_null);
            tvAllWordsTips.setVisibility(View.VISIBLE);
        }else{
            tvAllWordsTips.setVisibility(View.GONE);
        }

        mLvAllWords.setSelection(position);//还原位置第一条显示的记录的位置

        System.gc();
    }


}
