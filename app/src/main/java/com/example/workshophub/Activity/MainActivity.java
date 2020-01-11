package com.example.workshophub.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workshophub.Fragment.AvailableWorkshopFragment;
import com.example.workshophub.Fragment.DashboardFragment;
import com.example.workshophub.Helper.WorkshopCart;
import com.example.workshophub.Helper.WorkshopDatabaseHelper;
import com.example.workshophub.Model.User;
import com.example.workshophub.Model.Workshop;
import com.example.workshophub.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static User sUser;
    private Gson mGson;
    private SharedPreferences mSharedPreferences;
    private TextView mToolbarName;
    private ImageView mToolbarLogout;
    private AlertDialog.Builder builder;
    private WorkshopDatabaseHelper workshopDatabaseHelper;

    public static User currentUser;

    public static  Set<String> workshopRegisteredSet;



    private static final String LOGIN_STATUS = "login_status";
    private static final String IS_LOGGED = "is_logged";


    private WorkshopCart workshopCart;

    private ArrayList<Workshop> workshopArrayList;

    private BottomNavigationView mBottomNavigationView;

    public static Context mContext;

    public static ArrayList<Workshop> availableWorkshopAdminList;
    public static ArrayList<Workshop> dashboardWorkshopAdminList;

    public static Workshop workshopSelected = null;

    public static boolean isLoggedBool = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        availableWorkshopAdminList = new ArrayList<>();
        dashboardWorkshopAdminList = new ArrayList<>();

        mToolbarName = findViewById(R.id.username_toolbar);
        mToolbarLogout = findViewById(R.id.logout_toolbar);

        mBottomNavigationView = findViewById(R.id.bottom_nav);

        mContext = getApplicationContext();

        mBottomNavigationView.setOnNavigationItemSelectedListener(navListener);



        workshopDatabaseHelper = new WorkshopDatabaseHelper(this);

        builder = new AlertDialog.Builder(this);

        workshopCart = WorkshopCart.getInstance();
        workshopArrayList = workshopCart.workshopArrayList;

        if(workshopDatabaseHelper.getWorkshopCount() == 0){
            addDataToWorkshopDatabase();
        } else {
            WorkshopCart.getInstance().workshopArrayList = workshopDatabaseHelper.getAllWorkshopData();
        }
        WorkshopCart.displayWorkshops();






        mGson = new Gson();
        mSharedPreferences = getApplicationContext().getSharedPreferences(LOGIN_STATUS,0);
        String json = mSharedPreferences.getString("UserObject","");



        if(mSharedPreferences.getBoolean(IS_LOGGED,false)) {
            Log.d("xlr8_log_stat","LOGGED IN");
            isLoggedBool = true;
            sUser = mGson.fromJson(json, User.class);
            mToolbarName.setText(getResources().getString(R.string.welcome) + " " + sUser.getUsername());
            mToolbarLogout.setVisibility(View.VISIBLE);
            adminUpdateList();

        } else {
            Log.d("xlr8_log_stat","LOGGED OUT");
            isLoggedBool = false;
            mToolbarLogout.setVisibility(View.INVISIBLE);
            mToolbarName.setText(getResources().getString(R.string.app_name));
            availableWorkshopAdminList = WorkshopCart.getInstance().workshopArrayList;

            Log.d("xlr8_log","LOGGED OUT LIST");
            displayWorkshops(availableWorkshopAdminList);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AvailableWorkshopFragment()).addToBackStack(null).commit();
        }




        mToolbarLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle(getResources().getString(R.string.logout)).setMessage(getResources().getString(R.string.logout_message))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
                                prefsEditor.putBoolean(IS_LOGGED, false);
                                prefsEditor.commit();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });










    }


    public static void adminUpdateList() {

        availableWorkshopAdminList = new ArrayList<>();
        dashboardWorkshopAdminList = new ArrayList<>();

        String workshopRegisteredRawString = sUser.getWorkshopRegistered();

        String[] workshopRegisteredArray = workshopRegisteredRawString.split("#");

        workshopRegisteredSet = new HashSet<>();

        for(String workshopRegistered : workshopRegisteredArray){
            if(!workshopRegistered.equals(""))
                workshopRegisteredSet.add(workshopRegistered);
        }

        Log.d("xlr8_WORKS_R_S: ", String.valueOf(workshopRegisteredSet));

        for(int i=0; i<WorkshopCart.getInstance().workshopArrayList.size(); i++){
            if(!workshopRegisteredSet.contains(WorkshopCart.getInstance().workshopArrayList.get(i).getId()))
                availableWorkshopAdminList.add(WorkshopCart.getInstance().workshopArrayList.get(i));
        }

        Log.d("xlr8","AVAILABLE W: "+availableWorkshopAdminList.size());
        displayWorkshops(availableWorkshopAdminList);



        for(int i=0; i<WorkshopCart.getInstance().workshopArrayList.size(); i++){
            if(workshopRegisteredSet.contains(WorkshopCart.getInstance().workshopArrayList.get(i).getId()))
                dashboardWorkshopAdminList.add(WorkshopCart.getInstance().workshopArrayList.get(i));
        }

        Log.d("xlr8","DASHBOARD W: "+dashboardWorkshopAdminList.size());
        displayWorkshops(dashboardWorkshopAdminList);

        Log.d("xlr8_admin_W_L_S","S: "+ WorkshopCart.getInstance().workshopArrayList.size());

    }

    public static void displayWorkshops(ArrayList<Workshop> workshops){

        for(int i=0; i<workshops.size(); i++){
            Log.d("xlr8_workshops: ","I: "+workshops.get(i).getId()+" N: "+workshops.get(i).getName());
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_available:
                            selectedFragment = new AvailableWorkshopFragment();
                            break;

                        case R.id.nav_dashboard:
                            if(isLoggedBool)
                                selectedFragment = new DashboardFragment();
                            else {
                                Toast.makeText(MainActivity.this, "Please Login First.", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).addToBackStack(null).commit();

                    return true;
                }
            };

    private void addDataToWorkshopDatabase() {

        String uri = "@drawable/android";
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Workshop engifest = new Workshop("1",getResources().getString(R.string.android),imageResource, getResources().getString(R.string.android_desc),"New Delhi","20 Feburary 2020","8 AM - 4 PM","N","DTU College, Rithala");
        if(workshopDatabaseHelper.addWorkshop(engifest) > 0){
            Log.d(TAG,"Workshop Data Added Successfully");
        }
        WorkshopCart.getInstance().workshopArrayList.add(engifest);

        uri = "@drawable/ml";
        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Workshop moksha = new Workshop("2",getResources().getString(R.string.ml),imageResource, getResources().getString(R.string.ml_desc),"New Delhi","15 March 2020","8 AM - 2 PM","N","NSIT College, Dwarka Mor");
        if(workshopDatabaseHelper.addWorkshop(moksha) > 0){
            Log.d(TAG,"Workshop Data Added Successfully");
        }

        WorkshopCart.getInstance().workshopArrayList.add(moksha);


        uri = "@drawable/python";
        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Workshop odyssey = new Workshop("3",getResources().getString(R.string.python),imageResource, getResources().getString(R.string.python_desc),"New Delhi","9 March 2020","9 AM - 4 PM","N","IIITD College, Okhla");
        if(workshopDatabaseHelper.addWorkshop(odyssey) > 0){
            Log.d(TAG,"Workshop Data Added Successfully");
        }

        WorkshopCart.getInstance().workshopArrayList.add(odyssey);


        uri = "@drawable/web";
        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Workshop rendezvous = new Workshop("4",getResources().getString(R.string.web),imageResource, getResources().getString(R.string.web_desc),"New Delhi","12 March 2020","9 AM - 4 PM","N","IIT Delhi College, Hauz Khas");
        if(workshopDatabaseHelper.addWorkshop(rendezvous) > 0){
            Log.d(TAG,"Workshop Data Added Successfully");
        }

        WorkshopCart.getInstance().workshopArrayList.add(rendezvous);


        uri = "@drawable/javascript";
        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Workshop taarangana = new Workshop("5",getResources().getString(R.string.js),imageResource, getResources().getString(R.string.js_desc),"New Delhi","28 Feburary 2020","9 AM - 4 PM","N","IGDTUW College, Kashmiri Gate");
        if(workshopDatabaseHelper.addWorkshop(taarangana) > 0){
            Log.d(TAG,"Workshop Data Added Successfully");
        }

        WorkshopCart.getInstance().workshopArrayList.add(taarangana);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);    }
}
