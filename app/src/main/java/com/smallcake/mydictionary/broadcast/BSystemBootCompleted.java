package com.smallcake.mydictionary.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.smallcake.mydictionary.service.SPushWord;

public class BSystemBootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        * 系统开机广播
        * */

        //开启单词推送服务
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(new Intent(context, SPushWord.class));
        }
        else
        {
            context.startService(new Intent(context, SPushWord.class));
        }
    }
}
