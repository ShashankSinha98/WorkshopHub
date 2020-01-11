package com.example.workshophub.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.lang.UScript;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.workshophub.Activity.MainActivity;
import com.example.workshophub.Model.User;
import com.example.workshophub.Model.Workshop;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UserDatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "register.db";
    private static final String TABLE_NAME = "tblRegisterUser";
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String FULLNAME = "fullname";
    private static final String WORKSHOP_REGISTERED = "workshop_registered";
    private static final String LOGIN_STATUS = "login_status";
    private static final String IS_LOGGED = "is_logged";

    private SharedPreferences mSharedPreferences;


    public UserDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+EMAIL+" TEXT, "+PASSWORD+" TEXT, "+FULLNAME+" TEXT, "+WORKSHOP_REGISTERED+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EMAIL, user.getEmail());
        contentValues.put(PASSWORD, user.getPassword());
        contentValues.put(FULLNAME, user.getUsername());
        contentValues.put(WORKSHOP_REGISTERED, "");

        long res = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return  res;
    }


    public boolean checkUser(User user){
        String[] columns = {ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = EMAIL+"=? and "+PASSWORD+"=?";
        String[] selectionArgs = {user.getEmail(), user.getPassword()};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    public boolean checkIfEmailExist(String email){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME +" where "+ EMAIL+" = ? ", new String[]{email});
        return cursor.getCount() > 0;

    }

    public User getSingleUserInfo(String email){

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME +" where "+ EMAIL+" = ? ", new String[]{email});
        cursor.moveToFirst();

        User user = new User();
        user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
        user.setUsername(cursor.getString(cursor.getColumnIndex(FULLNAME)));
        user.setId("#C"+cursor.getString(cursor.getColumnIndex(ID)));
        user.setWorkshopRegistered(cursor.getString(cursor.getColumnIndex(WORKSHOP_REGISTERED)));

        cursor.close();
        database.close();

        return user;

    }

    public void updateData(User user, String id){
        ContentValues cv = new ContentValues();
        String current_status = user.getWorkshopRegistered();
        String updated_status = current_status+"#"+id;
        cv.put(WORKSHOP_REGISTERED,updated_status);

        SQLiteDatabase database = this.getWritableDatabase();
        database.update(TABLE_NAME, cv, "id="+user.getId().substring(2),null);

        mSharedPreferences = MainActivity.mContext.getSharedPreferences(LOGIN_STATUS,0);
        user.setWorkshopRegistered(updated_status);

        MainActivity.sUser = user;

        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
        prefsEditor.putBoolean(IS_LOGGED,true);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("UserObject", json);
        prefsEditor.commit();
    }

    public ArrayList<User> getAllUserData(){
        ArrayList<User> userArrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex(ID));
                String name = cursor.getString(cursor.getColumnIndex(FULLNAME));
                String email = cursor.getString(cursor.getColumnIndex(EMAIL));
                String workshod_registered = cursor.getString(cursor.getColumnIndex(WORKSHOP_REGISTERED));
                String password = cursor.getString(cursor.getColumnIndex(PASSWORD));


               User user = new User(id,name,password,email, workshod_registered);
               userArrayList.add(user);

                cursor.moveToNext();
            }
        }

        db.close();

        return userArrayList;
    }

}
