package com.vitaminjr.tasker;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

import com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent.TaskService;

import java.util.Date;
import java.util.EventListener;

/**
 * Created by vitaminjr on 20.07.15.
 */
public class TimePickerFragment extends DialogFragment {
    Date mDate;
    Intent intent;
    Context context;
    public static final String EXTRA_TIME = "com.vitaminjr.tasker.time";

    public  static TimePickerFragment newInstacnce(Date date){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIME, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return  fragment;

    }

    private OnTimeListener onTimeListener;

    public interface OnTimeListener extends EventListener
    {
        public void onReturnTimeResult(Date result);

    }

    public void setOnTimeListener(OnTimeListener listener)
    {
        onTimeListener = listener;
    }

/*    private void sendResult(int resultCode){
        if( getTargetFragment()== null)
            return;
        Intent i = new Intent();

        i.putExtra(EXTRA_TIME, mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);;
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);

        context = getActivity();


        mDate = (Date)getArguments().getSerializable(EXTRA_TIME);



        int hour = mDate.getHours();
        final int minute = mDate.getMinutes();

        final TimePicker timepicker = (TimePicker) v.findViewById(R.id.dialog_time_timePicker);
        timepicker.setIs24HourView(true);

        timepicker.setCurrentHour(hour);
        timepicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity()).setView(v).
                setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDate.setHours(timepicker.getCurrentHour());
                        mDate.setMinutes(timepicker.getCurrentMinute());
                        onTimeListener.onReturnTimeResult(mDate);
                     //   sendResult(Activity.RESULT_OK);

                    }
                }).create();
    }
}
