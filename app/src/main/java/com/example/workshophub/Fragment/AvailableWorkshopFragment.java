package com.example.workshophub.Fragment;


import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workshophub.Activity.MainActivity;
import com.example.workshophub.Adapter.AvailableWorkshopAdapter;
import com.example.workshophub.Helper.UserDatabaseHelper;
import com.example.workshophub.Helper.WorkshopCart;
import com.example.workshophub.Model.User;
import com.example.workshophub.Model.Workshop;
import com.example.workshophub.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableWorkshopFragment extends Fragment {

    private static AvailableWorkshopAdapter availableWorkshopAdapter;
    private static View rootView;

    private static RecyclerView mAvailableWorkshopList;
    private UserDatabaseHelper userDatabaseHelper;


    public AvailableWorkshopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_available_workshop, container, false);

        mAvailableWorkshopList = rootView.findViewById(R.id.available_workshop_list);


        userDatabaseHelper = new UserDatabaseHelper(rootView.getContext());

        Log.d("xlr8_size","C: "+WorkshopCart.getInstance().workshopArrayList.size());

        ArrayList<User> allUser = userDatabaseHelper.getAllUserData();

        for(User user : allUser){
           Log.d("xlr8_all_user","I: "+user.getId()+" N: "+user.getUsername()+" P: "+user.getPassword()+" E: "+user.getEmail()
           +" : W: "+user.getWorkshopRegistered());
        }

        populateAvailableWorkshopList();



        return rootView;
    }

    public static void populateAvailableWorkshopList(){

        Log.d("xlr8_log","List populate called, S: "+MainActivity.availableWorkshopAdminList.size());
        MainActivity.displayWorkshops(MainActivity.availableWorkshopAdminList);

        availableWorkshopAdapter =new AvailableWorkshopAdapter(rootView.getContext(), MainActivity.availableWorkshopAdminList, MainActivity.workshopRegisteredSet);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(),LinearLayoutManager.VERTICAL,false);
        mAvailableWorkshopList.setLayoutManager(layoutManager);
        mAvailableWorkshopList.setAdapter(availableWorkshopAdapter);


    }

}
