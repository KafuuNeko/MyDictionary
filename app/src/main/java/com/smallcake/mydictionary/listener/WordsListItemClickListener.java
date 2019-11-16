package com.smallcake.mydictionary.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.smallcake.mydictionary.R;
import com.smallcake.mydictionary.sqlite.WordsDataBase;
import com.smallcake.mydictionary.struct.Words;

import java.util.List;

public class WordsListItemClickListener implements AdapterView.OnItemClickListener {
    private Context mContext;
    private List<Words> mWords;

    public WordsListItemClickListener(Context context, List<Words> words){
        mContext = context;
        mWords = words;
    }

    private void updateAllWordsList(){
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("load.all_words"));
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("load.familiar_words"));
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("load.new_words"));
    }

    private void dialogEditWords(final Words words){
        View DialogView = View.inflate(mContext, R.layout.dialog_edit_world, null);

        final EditText edWords = DialogView.findViewById(R.id.dialog_edit_world_edWords);
        final EditText edDescribe = DialogView.findViewById(R.id.dialog_edit_world_edDescribe);

        edWords.setText(words.getWords());
        edDescribe.setText(words.getDescribe());

        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mContext)
                .setView(DialogView)
                .setTitle(R.string.tr_edit)
                .setPositiveButton(R.string.tr_modify, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        words.setWords(edWords.getText().toString());
                        words.setDescribe(edDescribe.getText().toString());

                        (new WordsDataBase(mContext)).updateWord(words).close();

                        updateAllWordsList();
                    }
                })

                .setNegativeButton(R.string.tr_cancel,null)
                .create();

        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Words words = mWords.get(position);

        String[] items = new String[]{
                (words.familiar?mContext.getString(R.string.tr_new_word):mContext.getString(R.string.tr_familiar_word)),
                mContext.getString(R.string.tr_edit),
                mContext.getString(R.string.tr_delete),
                mContext.getString(R.string.tr_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){
                    case 0:
                        (new WordsDataBase(mContext)).setFamiliar(words,!words.familiar).close();
                        updateAllWordsList();
                        break;

                    case 1:
                        dialogEditWords(words);
                        break;

                    case 2:
                        String msg = mContext.getString(R.string.tr_msg_tip_delete_word);
                        msg = msg.replace("%name", words.words);

                        (new AlertDialog.Builder(mContext))
                            .setMessage(msg)
                            .setPositiveButton(R.string.tr_delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    (new WordsDataBase(mContext)).deleteWord(words).close();
                                    updateAllWordsList();
                                }
                            })
                            .setNegativeButton(R.string.tr_cancel,null)
                            .create().show();

                        break;
                }
                
            }
        });

        builder.setTitle(words.words);
        builder.create().show();

    }

}
