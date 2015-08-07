package com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vitaminjr.tasker.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by vitaminjr on 07.07.15.
 */
public class TaskintentDBSerializer extends Activity {
    final String LOG_TAG = "myLogs";
    private Context mcontext;
    TaskDataBase taskDataBaseHelper;
    public TaskintentDBSerializer(Context mcontext){
        this.mcontext = mcontext;
    }

    public void saveTasks(ArrayList<Task> tasks){

        ContentValues content = new ContentValues();
        taskDataBaseHelper = new TaskDataBase(mcontext);
        SQLiteDatabase db = taskDataBaseHelper.getWritableDatabase();
        db.delete("task", null, null);
        for (Task t : tasks) {
            content.putAll(t.saveToCollection());
            db.insert("task", null, content);

        }
        taskDataBaseHelper.close();
        db.close();
    }



    public ArrayList<Task> loadTasks() throws ParseException {
        taskDataBaseHelper = new TaskDataBase(mcontext);
        SQLiteDatabase db = taskDataBaseHelper.getWritableDatabase();
        ArrayList<Task> arrayList =  new ArrayList<Task>();
        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("task", null, null, null, null, null, null);
            while (c.moveToNext())
            {
                Task task1 = new Task();
                task1.setmTitle(c.getString(c.getColumnIndex("title")));
                String bool = c.getString(c.getColumnIndex("status"));
                int i_bool = Integer.parseInt(bool);
                if(i_bool==1)
                    task1.setStatus(true);
                else
                    task1.setStatus(false);
                String idstr = (c.getString(c.getColumnIndex("id")));
                task1.setmId(UUID.fromString(idstr));

                String datestr = (c.getString(c.getColumnIndex("date")));
                SimpleDateFormat format = new SimpleDateFormat();
                Date docDate = format.parse(datestr);
                task1.setmDate(docDate);
                arrayList.add(task1);
            }
        c.close();
        db.close();
        return arrayList;
    }
}