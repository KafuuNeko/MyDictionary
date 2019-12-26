package com.smallcake.mydictionary.windows.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.smallcake.mydictionary.mvp.presenter.WordsPresenterV1;
import com.smallcake.mydictionary.mvp.view.IWordsView;
import com.smallcake.mydictionary.struct.Words;

import java.util.List;

import static com.smallcake.mydictionary.mvp.model.ReadWordsType.New_Words;

public class FragmentNewWords extends Fragment implements IWordsView {
    private View mRootView = null;
    private ListView mLvNewWords;
    private TextView tvNewWordsTips;
    private BWordsLoad mBroadcast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null != mRootView){
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            parent.removeView(mRootView);
        }else{
            mRootView = inflater.inflate(R.layout.fragment_new_words, null);
        }



        IWordsPresenter wordsPresenter = new WordsPresenterV1(getContext(),this, New_Words);

        mLvNewWords = mRootView.findViewById(R.id.fragment_new_words_lv_new_words);
        tvNewWordsTips = mRootView.findViewById(R.id.fragment_new_words_tv_new_words_tips);

        //注册列表更新广播，以便接收更新列表请求
        String action = "load.new_words";
        mBroadcast = new BWordsLoad(wordsPresenter, action);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcast, new IntentFilter(action));

        //发送广播，通知更新列表
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(action));
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcast);
        Log.d("FragmentNewWords","onDestroyView");
    }

    @Override
    public void onWordsLoading() {
        Log.d("onWordsLoading","正在加载新词");
    }

    @Override
    public void onWordsLoadComplete(List<Words> words) {
        int wordsNumber = words.size();
        int position = mLvNewWords.getFirstVisiblePosition();//记录第一条显示记录位置

        mLvNewWords.setAdapter(new ListViewWordsAdapter(getContext(), words));
        mLvNewWords.setOnItemClickListener(new WordsListItemClickListener(getContext(), words));

        if (wordsNumber == 0) {
            tvNewWordsTips.setText(R.string.tr_new_word_is_null);
            tvNewWordsTips.setVisibility(View.VISIBLE);
        }else{
            tvNewWordsTips.setVisibility(View.GONE);
        }

        mLvNewWords.setSelection(position);//还原位置第一条显示的记录的位置
        System.gc();
    }


}
