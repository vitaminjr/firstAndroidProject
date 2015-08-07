package com.vitaminjr.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent.TaskService;


/**
 * Created by vitaminjr on 29.06.15.
 */

public class DateActivity extends FragmentActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_activity);
        //startService(new Intent(this, TaskService.class));


    }





}
