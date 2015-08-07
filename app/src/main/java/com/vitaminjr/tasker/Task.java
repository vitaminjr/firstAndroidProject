package com.vitaminjr.tasker;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by admin on 06.05.2015.
 */
public class Task implements Parcelable {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean status;

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public Task(){
        mId = UUID.randomUUID();
        mDate = new Date();

       // format1.format(mDate);
        mDate.getTime();
        //Date mDate1 = new Date();

    }

    public ContentValues saveToCollection()
    {
        ContentValues cv = new ContentValues();
        cv.put("id", mId.toString());
        cv.put("title", mTitle);
        cv.put("status",status);
        cv.put("date", stringDate());
        return cv;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public UUID getmId() {
        return mId;
    }

    @Override
    public String toString() {
        return mTitle;
    }


    public String stringDate(){
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy kk:mm");
        //this.mDate = mDate;
        String date = format1.format(this.mDate);
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getmTitle());
        dest.writeLong(mDate.getTime());
        dest.writeByte((byte) (status ? 1 : 0));
    }


    private static   Task readFromParcel(Parcel parcel) {
        Task field = new Task();
        field.mTitle= parcel.readString();
        field.mDate = new Date(parcel.readLong());
        field.status = parcel.readByte() == 1;
        return field;
    }



    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public Task createFromParcel(Parcel parcel)
        {
            return readFromParcel(parcel);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

}
