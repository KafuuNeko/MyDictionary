package com.smallcake.mydictionary.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

public class MainViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;


    public MainViewPageAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        Log.d("MainViewPageAdapter", "destroyItem");
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        Log.d("MainViewPageAdapter", "instantiateItem");
        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_UNCHANGED;
    }

}
