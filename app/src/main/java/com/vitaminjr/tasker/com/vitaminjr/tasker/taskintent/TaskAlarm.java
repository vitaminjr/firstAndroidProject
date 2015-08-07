package com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.vitaminjr.tasker.R;
import com.vitaminjr.tasker.Task;
import com.vitaminjr.tasker.TaskFragment;
import com.vitaminjr.tasker.TaskListActivity;
import com.vitaminjr.tasker.TaskPagerActivity;

import java.net.URISyntaxException;
import java.util.ArrayList;

import android.widget.ArrayAdapter;


/**
 * Created by vitaminjr on 24.07.15.
 */
public class TaskAlarm extends BroadcastReceiver {
    NotificationManager nm;
    ArrayList<Task> taskListAlarm;
    public static int count = 0;
    Context context1;


/*    public TaskAlarm(A){

    }*/

    @Override
    public void onReceive(Context context, Intent intent) {
        taskListAlarm = intent.getParcelableArrayListExtra("data");
        Bundle extras = intent.getExtras();
        int position = extras.getInt("position");
         Task task = taskListAlarm.get(position);


        intent  = new Intent(context, AlertActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        extras.putInt("position", position);
        intent.putExtras(extras);
        context.startActivity(intent);


        String message = "Нагадування";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha, "Нагадування", (System.currentTimeMillis())+1000*5);




        intent = new Intent(context, TaskPagerActivity.class);
        intent.putExtra(TaskFragment.EXTRA_TASK_ID, task.getmId());

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification.setLatestEventInfo(context,task.getmTitle(),task.getmId().toString(), pIntent);

        long[] vibrate = new long[] { 1000, 1000, 1000, 1000, 1000 };
        notification.vibrate = vibrate;

        Uri ringURI =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.sound = ringURI;

        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        nm.notify(count++, notification);

    }



}
