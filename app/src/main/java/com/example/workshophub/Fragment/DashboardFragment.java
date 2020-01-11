package com.example.workshophub.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workshophub.Activity.MainActivity;
import com.example.workshophub.Adapter.AvailableWorkshopAdapter;
import com.example.workshophub.Adapter.DashboardWorkshopAdapter;
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
public class DashboardFragment extends Fragment {

    public static RecyclerView dashboard_list;
    public static DashboardWorkshopAdapter dashboardWorkshopAdapter;

    public static View rootView;
    private UserDatabaseHelper userDatabaseHelper;
    public  static  Set<String> workshopRegisteredSet;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboard_list = rootView.findViewById(R.id.dashboard_workshop_list);

        userDatabaseHelper = new UserDatabaseHelper(rootView.getContext());

        populateAvailableWorkshopList();


        return  rootView;
    }

    public static void populateAvailableWorkshopList(){

        dashboardWorkshopAdapter =new DashboardWorkshopAdapter(rootView.getContext(), MainActivity.dashboardWorkshopAdminList, MainActivity.workshopRegisteredSet);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(),LinearLayoutManager.VERTICAL,false);
        dashboard_list.setLayoutManager(layoutManager);
        dashboard_list.setAdapter(dashboardWorkshopAdapter);


    }

}
