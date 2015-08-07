package com.vitaminjr.tasker;

import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent.TaskLab;
import com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent.TaskService;

import java.nio.channels.CancelledKeyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by vitaminjr on 26.06.15.
 */
public class TaskDateFragment extends Fragment {


    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    public static final int REQUEST_DATE = 0;
    public static final String EXTRA_TASK_ID = "com.vitaminjr.tasker.taskintent.task_id";
    public static final String EXTRA_DATE = "com.vitaminjr.tasker.taskintent.date";
    public static final String EXTRA_TIME = "com.vitaminjr.tasker.time";
    Task task;
    DatePickerFragment dialog;
    TimePickerFragment timeDialog;
    DatePicker datePicker;
    Date mDate;
    Calendar today;
    private Button buttonDatePicker;
     int year;
     int month;
     int day;
    private Button buttonTimePicker;
    private  Button buttonStartService;
    Date dateTime;
    Intent intent;
    public static int  count;




    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.date_fragment,container, true);
        intent = new Intent(getActivity().getApplicationContext(),TaskService.class);
        buttonDatePicker = (Button) v.findViewById(R.id.button_date);
        datePicker = (DatePicker) v.findViewById(R.id.date_picker);
        buttonTimePicker = (Button) v.findViewById(R.id.button_time);
        buttonStartService = (Button) v.findViewById(R.id.button_start);

        dateTime = new Date();


        UUID taskId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_TASK_ID);
        task = TaskLab.get(getActivity()).getTask(taskId);
        updateDate();
        updateTime();
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dateTime = new Date();
                dateTime.setHours(task.getmDate().getHours());
                dateTime.setMinutes(task.getmDate().getMinutes());
                FragmentManager fm = getActivity().getSupportFragmentManager();
                dialog = DatePickerFragment.newInstance(task.getmDate());
                dialog.show(fm, DIALOG_DATE);
                dialog.setTargetFragment(TaskDateFragment.this, REQUEST_DATE);
            }
        });
        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                timeDialog = TimePickerFragment.newInstacnce(task.getmDate());
                timeDialog.show(fragmentManager, DIALOG_TIME);
                //timeDialog.setTargetFragment(TaskDateFragment.this, REQUEST_DATE);
                timeDialog.setOnTimeListener(new TimePickerFragment.OnTimeListener() {
                    @Override
                    public void onReturnTimeResult(Date result) {
                        Log.d("log1", result.toString());
                        task.setmDate(result);
                        updateTime();

/*                        Context context = getActivity();
                        context.startService(intent);*/

                    }
                });
            }
        });


        buttonStartService.setText("Запустити таймер");

        buttonStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count%5==0) {
                    Context context = TaskDateFragment.this.getActivity();
                    String message = "Не ламай сенсор";
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    count = 0;
                }
            }
        });

        updateCalendar();

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
               today.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                        task.setmDate(mDate);
                        updateDate();
                    }
                });
        return v;
    }

    public void updateDate(){
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        String date = format1.format(task.getmDate());
        buttonDatePicker.setText(date);
        today = Calendar.getInstance();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        //    Date time = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            date.setHours(dateTime.getHours());
            date.setMinutes(dateTime.getMinutes());
            task.setmDate(date);
           //task.setmDate();
            updateDate();
            updateCalendar();
            updateTime();

        }
    }

    public void updateCalendar(){
        today = Calendar.getInstance();
        today.setTime(task.getmDate());
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH);
        day = today.get(Calendar.DAY_OF_MONTH);
        datePicker.updateDate(year,month,day);
    }
    public void updateTime(){

        SimpleDateFormat format1 = new SimpleDateFormat("kk:mm");
        String date = format1.format(task.getmDate());
        buttonTimePicker.setText(date);
    }

}
