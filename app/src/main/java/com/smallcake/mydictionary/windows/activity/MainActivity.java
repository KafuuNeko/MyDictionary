/*

 * 项目采用MVP架构

 */

package com.smallcake.mydictionary.windows.activity;

import com.smallcake.mydictionary.R;
import com.smallcake.mydictionary.adapter.MainViewPageAdapter;
import com.smallcake.mydictionary.io.ServiceConfig;
import com.smallcake.mydictionary.service.SPushWord;
import com.smallcake.mydictionary.sqlite.WordsDataBase;
import com.smallcake.mydictionary.struct.Words;
import com.smallcake.mydictionary.windows.fragment.*;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> mPage = new ArrayList<>();

    private DrawerLayout mDrawer;
    private NavigationView mDrawerNav;

    private Toolbar mToolbar;

    private ViewPager mViewPage;
    private PagerAdapter mPagerAdapter;

    private BottomNavigationView mBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstOpenService(MainActivity.this);
        //设置默认Fragment
        initView();

    }

    private static void firstOpenService(Context context) {
        if(!(new ServiceConfig(context)).isClose("MainActivityOpenService"))
        {
            //开启单词推送服务
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(new Intent(context, SPushWord.class));
            }
            else
            {
                context.startService(new Intent(context, SPushWord.class));
            }
            (new ServiceConfig(context)).setOpenStatus("MainActivityOpenService", false);
            Log.d("Service", "第一次运行软件，启动服务");
        }
    }


    private void initView() {
        mDrawer = findViewById(R.id.mDrawer);
        mDrawerNav = findViewById(R.id.mDrawerNav);

        mDrawerNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.openPush:
                        (new ServiceConfig(MainActivity.this)).setOpenStatus("push_new_word", true);
                        Toast.makeText(MainActivity.this, getResources().getText(R.string.tr_open_service), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.closePush:
                        (new ServiceConfig(MainActivity.this)).setOpenStatus("push_new_word", false);
                        Toast.makeText(MainActivity.this, getResources().getText(R.string.tr_closed_service), Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.addWord:
                        onAddNewWords();
                        break;
                }

                mDrawer.closeDrawers();
                return false;
            }
        });

        //标题栏
        mToolbar = findViewById(R.id.mToolsBar);
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawer, mToolbar, R.string.tr_open_drawer, R.string.tr_close_drawer);
        actionBarDrawerToggle.syncState();
        mDrawer.addDrawerListener(actionBarDrawerToggle);

        //底部导航栏
        mBottomNavigation = findViewById(R.id.mBottomMenu);

        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.navNewWords:
                        UpdateActionBarTitle(0);
                        mViewPage.setCurrentItem(0);
                        break;
                    case R.id.navFamiliarWords:
                        UpdateActionBarTitle(1);
                        mViewPage.setCurrentItem(1);
                        break;
                    case R.id.navAllWords:
                        UpdateActionBarTitle(2);
                        mViewPage.setCurrentItem(2);
                        break;
                }

                return false;
            }
        });

        //加载所有Fragment Fragment的数量应与底部导航标签相等
        mPage.add(new FragmentNewWords());
        mPage.add(new FragmentFamiliarWords());
        mPage.add(new FragmentAllWords());

        mViewPage = findViewById(R.id.mViewPage);
        mPagerAdapter = new MainViewPageAdapter(getSupportFragmentManager(), mPage);
        mViewPage.setAdapter(mPagerAdapter);
        mViewPage.setCurrentItem(0);


        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mBottomNavigation.getMenu().getItem(i).setChecked(true);
                UpdateActionBarTitle(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void onAddNewWords(){
        View DialogView = View.inflate(this, R.layout.dialog_edit_world, null);

        final EditText edWords = DialogView.findViewById(R.id.dialog_edit_world_edWords);
        final EditText edDescribe = DialogView.findViewById(R.id.dialog_edit_world_edDescribe);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(DialogView)
                .setTitle(R.string.tr_add)
                .setPositiveButton(R.string.tr_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        (new WordsDataBase(MainActivity.this))
                                .addWord(new Words(edWords.getText().toString(), edDescribe.getText().toString(), mBottomNavigation.getSelectedItemId() == R.id.navFamiliarWords))
                                .close();

                        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(new Intent("load.all_words"));
                        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(new Intent("load.familiar_words"));
                        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(new Intent("load.new_words"));

                    }
                })

                .setNegativeButton(R.string.tr_cancel,null)
                .create();

        dialog.show();
    }

    private void UpdateActionBarTitle(int index) {
        switch (index)
        {
            case 0:
                getSupportActionBar().setTitle(R.string.main_menu_new_words);
                break;
            case 1:
                getSupportActionBar().setTitle(R.string.main_menu_familiar_words);
                break;
            case 2:
                getSupportActionBar().setTitle(R.string.main_menu_all);
                break;
        }
    }

}
