package com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.vitaminjr.tasker.Task;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by admin on 07.05.2015.
 */
public class TaskLab {
    private static TaskLab sTaskLab;
    private ArrayList<Task> mTasker;
    private Context mAppContext;

    private static final String TAG = "TaskLab";

    private TaskintentDBSerializer mSerializer;

    private TaskLab(Context appContext) {
        mAppContext = appContext;

        mSerializer  = new TaskintentDBSerializer(mAppContext);

        try {
            mTasker = mSerializer.loadTasks();
        }catch (Exception ex){
            mTasker = new ArrayList<>();
            Log.e(TAG, "Помилка зчитування завдань: ", ex);
        }



}
   public boolean saveTask(){
        try {
            mSerializer.saveTasks(mTasker);
            Log.d(TAG, "Дані збереженні");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean saveTaskAlert(ArrayList<Task> list){
        try {
            mSerializer.saveTasks(list);
            Log.d(TAG, "Дані збереженні");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static TaskLab get(Context c) {
        if (sTaskLab == null) {
            sTaskLab = new TaskLab(c.getApplicationContext());
        }
        return sTaskLab;
    }

    public ArrayList<Task> getmTasker() {
        return mTasker;
    }

    public Task getTask(UUID id) {
        for (Task t : mTasker) {
            if (t.getmId().equals(id))
                return t;
        }
        return null;
    }

    public void addTask(Task t) {
        mTasker.add(t);
    }


    public  void deleteTask(Task t){
        mTasker.remove(t);
        TaskDataBase taskDataBaseHelper = new TaskDataBase(mAppContext);
        SQLiteDatabase db = taskDataBaseHelper.getWritableDatabase();
      //  Log.d("sql","DELETE FROM task WHERE id="+t.getmId().toString());
       // Log.d("sql2","DELETE FROM task WHERE id=?");
        db.execSQL("DELETE FROM task WHERE id=\"" + t.getmId().toString() +"\"");

        Log.d("MyLog", "Запис видалено");
        taskDataBaseHelper.close();
        db.close();

    }
}

