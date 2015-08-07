package com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.vitaminjr.tasker.Task;
import com.vitaminjr.tasker.TaskFragment;
import com.vitaminjr.tasker.TaskPagerActivity;

import java.util.ArrayList;

/**
 * Created by vitaminjr on 04.08.15.
 */
public class AlertActivity extends Activity {

    ArrayList<Task> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent= getIntent();

        list = intent.getParcelableArrayListExtra("data");
        Bundle extras = intent.getExtras();
        final int position = extras.getInt("position");
        final Task task = list.get(position);

        list.remove(position);

        String text = task.getmDate().toString();
        String button1String = "Виконано";
        String button2String = "Перенести";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(task.getmTitle());
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage(text);
        builder.setPositiveButton(button1String,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        task.setStatus(true);
                        list.add(position, task);


                        TaskLab.get(getApplicationContext()).saveTaskAlert(list);
                        Intent taskService = new Intent(AlertActivity.this, TaskService.class);
                        taskService.putParcelableArrayListExtra("list", list);
                        startService(taskService);
                        AlertActivity.this.finish();
                    }
                });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
