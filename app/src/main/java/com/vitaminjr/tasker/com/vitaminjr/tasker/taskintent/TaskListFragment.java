package com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.vitaminjr.tasker.R;
import com.vitaminjr.tasker.Task;
import com.vitaminjr.tasker.TaskFragment;
import com.vitaminjr.tasker.TaskPagerActivity;
import java.util.ArrayList;

/**
 * Created by admin on 07.05.2015.
 */
public class TaskListFragment extends ListFragment {
    private boolean mSubtitleVisible;
    private static final String TAG = "TaskListFragment";
    public ArrayList<Task> mTasker;
    private Context mcontext;
    Intent   taskService;
    boolean run =false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.task_title);
        mTasker = TaskLab.get(getActivity()).getmTasker();

        TaskAdapter adapter = new TaskAdapter(mTasker);
        setListAdapter(adapter);

        setRetainInstance(true);
        mSubtitleVisible = false;

        if(run == false) {
            runService();
            run = true;
        }




    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_list, menu);
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible && showSubtitle != null){
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        getActivity().getMenuInflater().inflate(R.menu.task_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        TaskAdapter adapter = (TaskAdapter) getListAdapter();
        Task task = adapter.getItem(position);

        switch (item.getItemId()){
            case R.id.menu_item_delete_task:
                TaskLab.get(getActivity()).deleteTask(task);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_item_new_task:
            Task task = new Task();
            TaskLab.get(getActivity()).addTask(task);
            Intent i = new Intent(getActivity(), TaskPagerActivity.class);
            i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getmId());
            startActivityForResult(i,0);
            return  true;
            case R.id.menu_item_show_subtitle:

                Log.d(TAG,  "was clicked");
                 mcontext = TaskListFragment.this.getActivity();
                 taskService = new Intent(mcontext, TaskService.class);
                taskService.putParcelableArrayListExtra("list",mTasker);
                mcontext.startService(taskService);

                return  true;
        default:
            return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Task t = ((TaskAdapter)getListAdapter()).getItem(position);
        // Log.d(TAG, t.getmTitle() + "was clicked");

        Intent i = new Intent(getActivity(), TaskPagerActivity.class);
        i.putExtra(TaskFragment.EXTRA_TASK_ID, t.getmId());
        startActivity(i);
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
            dateTextView.setText(t.stringDate());
            CheckBox statusCheckBox = (CheckBox) convertView.findViewById(R.id.task_list_item_statusCheckBox);
            statusCheckBox.setChecked(t.isStatus());

            return convertView;
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        runService();
        ((TaskAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
      //  mcontext = TaskListFragment.this.getActivity();
      //  mcontext.stopService(new Intent(mcontext, TaskService.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mSubtitleVisible) {
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
        }



        ListView listView = (ListView) v.findViewById(android.R.id.list);
        registerForContextMenu(listView);



        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            registerForContextMenu(listView);
        }else {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater menuInflater = mode.getMenuInflater();
                    menuInflater.inflate(R.menu.task_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_task:
                            TaskAdapter adapter = (TaskAdapter) getListAdapter();
                            TaskLab taskLab = TaskLab.get(getActivity());
                            for (int i = adapter.getCount() -1; i >= 0 ; i--) {
                                if(getListView().isItemChecked(i)){
                                    taskLab.deleteTask(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                }
            });
        }
        return v;
    }

    public void runService(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mcontext = TaskListFragment.this.getActivity();
                taskService = new Intent(mcontext, TaskService.class);
                taskService.putParcelableArrayListExtra("list",mTasker);
                mcontext.startService(taskService);
            }
        }).start();

    }
}
