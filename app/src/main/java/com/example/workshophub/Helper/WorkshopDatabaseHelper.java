package com.example.workshophub.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.workshophub.Model.Workshop;

import java.util.ArrayList;

public class WorkshopDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "workshop.db";
    private static final String TABLE_NAME = "tblWorkshop";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String LOCATION = "location";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String USER_STATUS = "user_status";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";
    private static final String VENUE = "venue";


    public WorkshopDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" TEXT, "+LOCATION+" TEXT, "+DATE+
                " TEXT, "+TIME+" TEXT, "+USER_STATUS+" TEXT, "+DESCRIPTION+" TEXT, "+IMAGE+" INTEGER, "+VENUE+" TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long addWorkshop(Workshop workshop){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, workshop.getName());
        contentValues.put(LOCATION, workshop.getLocation());
        contentValues.put(DATE, workshop.getLocation());
        contentValues.put(TIME, workshop.getTime());
        contentValues.put(USER_STATUS, workshop.getUserStatus());
        contentValues.put(DESCRIPTION,workshop.getDescription());
        contentValues.put(IMAGE, workshop.getImage());
        contentValues.put(VENUE, workshop.getVenue());


        long res = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return  res;
    }

    public long getWorkshopCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }


    public ArrayList<Workshop> getAllWorkshopData(){

        ArrayList<Workshop> workshopArrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(NAME));
                String location = cursor.getString(cursor.getColumnIndex(LOCATION));
                String date = cursor.getString(cursor.getColumnIndex(DATE));
                String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
                String id = cursor.getString(cursor.getColumnIndex(ID));
                String time = cursor.getString(cursor.getColumnIndex(TIME));
                String userStatus = cursor.getString(cursor.getColumnIndex(USER_STATUS));
                int image = cursor.getInt(cursor.getColumnIndex(IMAGE));
                String venue = cursor.getString(cursor.getColumnIndex(VENUE));


                Workshop workshop = new Workshop(id,name,image,description,location,date,time,userStatus, venue);
                workshopArrayList.add(workshop);

                cursor.moveToNext();
            }
        }

        db.close();

        return workshopArrayList;

    }
}
