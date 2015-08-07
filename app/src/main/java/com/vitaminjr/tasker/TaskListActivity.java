package com.vitaminjr.tasker;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent.SingleFragmentActivity;
import com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent.TaskLab;
import com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent.TaskListFragment;
import com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent.TaskService;

/**
 * Created by admin on 08.05.2015.
 */
public class TaskListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        return new TaskListFragment();
    }
}
