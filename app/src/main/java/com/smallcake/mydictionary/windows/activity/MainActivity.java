/*

 * 项目采用MVP架构

 */

package com.smallcake.mydictionary.windows.activity;

import com.smallcake.mydictionary.R;
import com.smallcake.mydictionary.sqlite.WordsDataBase;
import com.smallcake.mydictionary.struct.Words;
import com.smallcake.mydictionary.windows.fragment.*;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> mPage = new ArrayList<>();

    private List<TextView> mTvMenu = new ArrayList<>();

    private TextView mTitle;

    private int mNowPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = findViewById(R.id.mTitle);

        //加载底部导航标签
        mTvMenu.add((TextView) findViewById(R.id.mTvMenuNewWords));
        mTvMenu.add((TextView) findViewById(R.id.mTvMenuFamiliarWords));
        mTvMenu.add((TextView) findViewById(R.id.mTvMenuAllWords));

        //加载所有Fragment Fragment的数量应与底部导航标签相等
        mPage.add(new FragmentNewWords());
        mPage.add(new FragmentFamiliarWords());
        mPage.add(new FragmentAllWords());

        //设置默认Fragment
        setNowPage(0);
    }

    /*
    * 设置当前显示的Fragment
    * */
    private void setNowPage(int index){
        mNowPage = index;
        //获得Fragment管理器与开启事务
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        /*
        * 此迭代进行的操作
        * 判断所有的Fragment是否已添加，未添加则进行添加操作
        * 隐藏所有Fragment
        * 将所有底部导航标签文本颜色设置为黑色
        * */
        for (int i = 0; i < mPage.size(); i++){
            Fragment fragment = mPage.get(i);
            if(!fragment.isAdded()){
                fragmentTransaction.add(R.id.mFrame, fragment);
            }
            fragmentTransaction.hide(fragment);

            mTvMenu.get(i).setTextColor(Color.argb(255,0,0,0));

        }

        //设置选中的底部导航文本颜色为红色
        mTvMenu.get(index).setTextColor(Color.argb(255,255,0,0));
        //显示指定的Fragment
        fragmentTransaction.show(mPage.get(index));

        fragmentTransaction.commit();

    }

    public void onAddNewWords(View v){
        View DialogView = View.inflate(this, R.layout.dialog_edit_world, null);

        final EditText edWords = DialogView.findViewById(R.id.dialog_edit_world_edWords);
        final EditText edDescribe = DialogView.findViewById(R.id.dialog_edit_world_edDescribe);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(DialogView)
                .setTitle("添加")
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        (new WordsDataBase(MainActivity.this))
                                .addWord(new Words(edWords.getText().toString(), edDescribe.getText().toString(), mNowPage!=0))
                                .close();

                        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(new Intent("load.all_words"));
                        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(new Intent("load.familiar_words"));
                        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(new Intent("load.new_words"));

                    }
                })

                .setNegativeButton("取消",null)
                .create();

        dialog.show();
    }

    /*
     * 关于软件
     * onAboutSoftware
     * */
    public void onAboutSoftware(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Version:1.0\nAuthor:短毛猫\nE-mail:66492422@qq.com");
        builder.create().show();
    }

    /*
    * 底部导航点击事件
    * onMenuButtonNewWords
    * */
    public void onMenuButtonNewWords(View v){
        mTitle.setText(R.string.main_menu_new_words);
        setNowPage(0);

    }

    /*
     * 底部导航点击事件
     * onMenuButtonMediumTerm
     * */
    public void onMenuButtonMediumTerm(View v){
        mTitle.setText(R.string.main_menu_familiar_words);
        setNowPage(1);
    }

    /*
     * 底部导航点击事件
     * onMenuButtonAll
     * */
    public void onMenuButtonAll(View v){
        mTitle.setText(R.string.main_menu_all);
        setNowPage(2);
    }
}
