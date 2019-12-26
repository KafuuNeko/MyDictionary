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
import com.smallcake.mydictionary.mvp.presenter.WordsPresenterV1;
import com.smallcake.mydictionary.mvp.presenter.IWordsPresenter;
import com.smallcake.mydictionary.mvp.view.IWordsView;
import com.smallcake.mydictionary.struct.Words;

import java.util.List;

import static android.support.v4.content.LocalBroadcastManager.getInstance;
import static com.smallcake.mydictionary.mvp.model.ReadWordsType.Familiar_Words;

public class FragmentFamiliarWords extends Fragment implements IWordsView {
    private View mRootView = null;
    private BWordsLoad mBroadcast;
    private ListView mLvFamiliarWords;
    private TextView tvFamiliarWordsTips;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //加载Fragment布局
        if (null != mRootView){
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            parent.removeView(mRootView);
        }else{
            mRootView = inflater.inflate(R.layout.fragment_familiar_words, null);
        }

        IWordsPresenter wordsPresenter = new WordsPresenterV1(getContext(),this, Familiar_Words);

        //查找组件
        mLvFamiliarWords = mRootView.findViewById(R.id.fragment_familiar_words_lv_familiar_words);
        tvFamiliarWordsTips = mRootView.findViewById(R.id.fragment_familiar_words_tv_familiar_words_tips);

        //注册列表更新广播，以便接收更新列表请求
        String action = "load.familiar_words";
        mBroadcast = new BWordsLoad(wordsPresenter,action);
        getInstance(getContext()).registerReceiver(mBroadcast, new IntentFilter(action));

        //发送广播，通知更新列表
        getInstance(getContext()).sendBroadcast(new Intent(action));
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
        getInstance(getContext()).unregisterReceiver(mBroadcast);
        Log.d("FragmentFamiliarWords","onDestroyView");

    }

    @Override
    public void onWordsLoading() {
        Log.d("onWordsLoading","正在加载熟词");
    }

    @Override
    public void onWordsLoadComplete(List<Words> words) {
        int wordsNumber = words.size();
        int position = mLvFamiliarWords.getFirstVisiblePosition();//记录第一条显示记录位置

        mLvFamiliarWords.setAdapter(new ListViewWordsAdapter(getContext(), words));
        mLvFamiliarWords.setOnItemClickListener(new WordsListItemClickListener(getContext(), words));

        if (wordsNumber == 0) {
            tvFamiliarWordsTips.setText(R.string.tr_familiar_word_is_null);
            tvFamiliarWordsTips.setVisibility(View.VISIBLE);
        }else{
            tvFamiliarWordsTips.setVisibility(View.GONE);
        }

        mLvFamiliarWords.setSelection(position);//还原位置第一条显示的记录的位置

        System.gc();
    }


}
