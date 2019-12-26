package com.smallcake.mydictionary.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.smallcake.mydictionary.R;
import com.smallcake.mydictionary.io.BaseConfig;
import com.smallcake.mydictionary.io.ServiceConfig;
import com.smallcake.mydictionary.sqlite.WordsDataBase;
import com.smallcake.mydictionary.struct.Words;
import com.smallcake.mydictionary.windows.activity.MainActivity;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SPushWord extends Service {
    private final int SERVICE_RUNNING_NOTIFICATION_ID = 0x01;
    private final int NOTICE_PUSH_WORD_ID = 0x02;

    private Timer mPushTimer = null;
    private NotificationManager mNotificationManager = null;

    /*
    * 定时推送服务
    * 每30分钟推送一个新的单词
    * */
    private TimerTask mPushRunnable = new TimerTask() {
        @Override
        public void run() {

            if ((new ServiceConfig(SPushWord.this)).isClose("push_new_word"))
            {
                return;
            }

            long lastDateTime, nowDateTime;

            //获取上次通知的时间
            BaseConfig baseConfig = new BaseConfig(SPushWord.this, "notification.properties");
            try {
                lastDateTime = Long.parseLong(baseConfig.getProperty("new_word"));
            } catch (Exception e) {
                lastDateTime = 0;
            }

            //上次通知的时间与当前时间进行对比，若超过1800000（30分钟），则继续
            nowDateTime = (new Date()).getTime();
            if (nowDateTime - lastDateTime < 1800000) return;

            //写入当前时间
            baseConfig.setProperty("new_word", nowDateTime+"");
            baseConfig.save();

            Log.d("SPushWord", "正在抽取单词");
            WordsDataBase wordsDataBase = new WordsDataBase(SPushWord.this);
            Words words = wordsDataBase.randomGet(false);
            if (words != null)
            {
                Notification.Builder builder = new Notification.Builder(SPushWord.this);

                //Android8.0要求Notification必须有ChannelID
                if (Build.VERSION.SDK_INT > 26)
                {
                    builder.setChannelId("NewWorld");
                }
                builder.setSmallIcon(R.drawable.ic_book);
                builder.setContentTitle(words.getWords());
                builder.setContentText(words.getDescribe());

                //点击Notification后进入程序
                builder.setContentIntent(TaskStackBuilder.create(SPushWord.this)
                        .addNextIntent(new Intent(SPushWord.this, MainActivity.class))
                        .getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT, 0));

                builder.setAutoCancel(true);
                mNotificationManager.notify(NOTICE_PUSH_WORD_ID, builder.build());
            }

            wordsDataBase.close();

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (mNotificationManager == null)
        {
            mNotificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

            //Android8.0以上要求Service必须在通知栏显示，将Service运行在前台，否者出错
            if (Build.VERSION.SDK_INT > 26)
            {
                NotificationChannel channel;
                channel = new NotificationChannel("NOTIFICATION", "SERVICE_RUNNING", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(false);
                channel.setSound(null, null);
                mNotificationManager.createNotificationChannel(channel);

                Notification.Builder builder  = new Notification.Builder(SPushWord.this, channel.getId());
                builder.setContentText("Service running...");
                builder.setSmallIcon(R.drawable.ic_book);

                startForeground(SERVICE_RUNNING_NOTIFICATION_ID, builder.build());

                channel = new NotificationChannel("NewWorld", "Notice", NotificationManager.IMPORTANCE_DEFAULT);
                mNotificationManager.createNotificationChannel(channel);
            }

        }

        if (mPushTimer == null)
        {
            mPushTimer = new Timer();
        }

        //一分钟检测一次
        mPushTimer.schedule(mPushRunnable, 1000, 60000);

    }

}
