package com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;

import com.vitaminjr.tasker.Task;
import com.vitaminjr.tasker.TaskFragment;
import com.vitaminjr.tasker.TaskListActivity;
import com.vitaminjr.tasker.TaskPagerActivity;
import com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent.TaskAlarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by vitaminjr on 24.07.15.
 */
public class TaskService extends Service {
    public  static  final String LOGV = "vitaminjrlog";
    AlarmManager alarmManager;
    PendingIntent pIntent1;
    Date date;
    Intent intent1;

    ArrayList<Task> taskArrayList;

    @Override
    public void onCreate() {

        Log.d(LOGV, "Сервіс створено");
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        taskArrayList = intent.getParcelableArrayListExtra("list");

        Log.d(LOGV, "Сервіс запущено");
         intent = new Intent(this,TaskAlarm.class);
        intent.putParcelableArrayListExtra("data", taskArrayList);
        date = new Date();
         final Context context = TaskService.this;

        final Intent finalIntent = intent;

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Bundle extras = new Bundle();
                        for (int i = 0; i < taskArrayList.size(); i++) {
                            Task task = taskArrayList.get(i);

                            if (date.getTime() >= task.getmDate().getTime() && task.isStatus()==false) {
                                extras.putInt("position", i);
                                finalIntent.putExtras(extras);
                                pIntent1 = PendingIntent.getBroadcast(context, 0, finalIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pIntent1);

                            }
                            SystemClock.sleep(3000);

                            if (i==taskArrayList.size()-1)
                                i=0;
                        }
                    }
                    }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);

    }

    @Override
    public void onDestroy() {
        Log.d("stop","Сервіс зупинено");
        super.onDestroy();
    }



}
