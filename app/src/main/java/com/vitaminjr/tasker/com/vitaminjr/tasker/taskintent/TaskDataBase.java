package com.vitaminjr.tasker.com.vitaminjr.tasker.taskintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;


/**
 * Created by vitaminjr on 25.06.15.
 */
public class TaskDataBase extends SQLiteOpenHelper {

    public TaskDataBase(Context context){
        //super(context, Environment.getExternalStorageDirectory()
           //     + File.separator+ MYDATABASE_NAME, null, MYDATABASE_VERSION);
        super(context,"/mnt/sdcard2/MyDB",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS task ("
                + "id   text,"
                + "title    text,"
                + "status   integer,"
                + "date     text);"
        );
        Log.d("log1", "таблиця створена");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  /*      db.execSQL("CREATE TABLE IF NOT EXISTS task ("
                        + "id   integer,"
                        + "title    text,"
                        + "status   text,"
                        + "date     text);"
        );
        Log.d("log1", "таблиця створена");*/
    }
}
