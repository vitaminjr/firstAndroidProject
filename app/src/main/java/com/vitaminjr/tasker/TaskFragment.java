package com.vitaminjr.tasker;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent.TaskLab;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by admin on 06.05.2015.
 */
public class TaskFragment extends Fragment {
    private Task mtask;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mStatusCheckBox;
    public static final String EXTRA_TASK_ID = "com.vitaminjr.tasker.taskintent.task_id";

    private static final String DIALOG_DATE = "date";
    public static final int REQUEST_DATE = 0;
    TaskAdapter adapter;

    public TaskAdapter getTaskAdapter() {
        return adapter;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        //UUID taskId = (UUID)getArguments().getSerializable(EXTRA_TASK_ID);
        UUID taskId = (UUID) getArguments().getSerializable(EXTRA_TASK_ID);
        //проблема запитати
        // UUID taskId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_TASK_ID);
        mtask = TaskLab.get(getActivity()).getTask(taskId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup parent,  Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_task, parent, false);
        mTitleField = (EditText) v.findViewById(R.id.task_title);
        mTitleField.setText(mtask.getmTitle());
        mDateButton = (Button) v.findViewById(R.id.task_date);
        updateDate();
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                if(NavUtils.getParentActivityName(getActivity())!=null){
                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                }*/
/*            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }*/
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mtask.setmTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mDateButton = (Button) v.findViewById(R.id.task_date);
       mDateButton.setText(mtask.stringDate());
        //  mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mtask.getmDate());
               dialog.show(fm, DIALOG_DATE);
                dialog.setTargetFragment(TaskFragment.this, REQUEST_DATE);*/
                Intent intent = new Intent(TaskFragment.this.getActivity(),DateActivity.class);
                intent.putExtra(EXTRA_TASK_ID, mtask.getmId());
               startActivity(intent);

                //updateDate();
            }
        });

        mStatusCheckBox = (CheckBox) v.findViewById(R.id.task_status);
        mStatusCheckBox.setChecked(mtask.isStatus());
        mStatusCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mtask.setStatus(b);
            }
        });

            return v;
        }


    private class TaskAdapter extends ArrayAdapter<Task> {
        public TaskAdapter(ArrayList<Task> taskers) {
            super(getActivity(), 0, taskers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_task, null);
            }
            Task t = getItem(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.task_list_item_titleTextView);
            titleTextView.setText(t.getmTitle());
            TextView dateTextView = (TextView) convertView.findViewById(R.id.task_list_item_dateTextView);
            dateTextView.setText(t.getmDate().toString());
            CheckBox statusCheckBox = (CheckBox) convertView.findViewById(R.id.task_list_item_statusCheckBox);
            statusCheckBox.setChecked(t.isStatus());

            return convertView;
        }
    }



    //  @Override
    public  static TaskFragment newInstance(UUID taskId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TASK_ID, taskId);
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return  fragment;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null){
                NavUtils.navigateUpFromSameTask(getActivity());
            }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateDate(){
        mDateButton.setText(mtask.stringDate());
    }

    @Override
    public void onPause() {
        super.onPause();
        TaskLab.get(getActivity()).saveTask();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDate();
    }
}



