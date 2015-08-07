package com.vitaminjr.tasker;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent.TaskLab;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by vitaminjr on 13.05.15.
 */
public class TaskPagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ArrayList<Task> mTasker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mTasker = TaskLab.get(this).getmTasker();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

            @Override
            public Fragment getItem(int position) {
                Task tasker = mTasker.get(position);
                return TaskFragment.newInstance(tasker.getmId());
            }


            @Override
            public int getCount() {
                return mTasker.size();
            }
        });

       UUID taskId = (UUID)getIntent().getSerializableExtra(TaskFragment.EXTRA_TASK_ID);
       for (int i = 0; i < mTasker.size() ; i++) {
            if(mTasker.get(i).getmId().equals(taskId))
           {
               mViewPager.setCurrentItem(i);
                break;
           }
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               Task task = mTasker.get(position);
                if(task.getmTitle()!= null){
                    setTitle(task.getmTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}

