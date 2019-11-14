package com.smallcake.mydictionary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smallcake.mydictionary.R;
import com.smallcake.mydictionary.struct.Words;

import java.util.List;

public class ListViewWordsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Words> mWords;

    public ListViewWordsAdapter(Context context,List<Words> words){
        mContext = context;
        mWords = words;

    }

    @Override
    public Words getItem(int position) {
        return mWords.get(position);
    }

    @Override
    public int getCount() {
        return mWords.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view){
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_item_words, null);
        }

        Words words = getItem(position);

        ((TextView) view.findViewById(R.id.listview_item_words_word)).setText(words.getWords());
        ((TextView) view.findViewById(R.id.listview_item_words_describe)).setText(words.getDescribe());
        ((TextView) view.findViewById(R.id.listview_item_words_status)).setText((words.familiar?R.string.tr_familiar_word:R.string.tr_new_word));

        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
