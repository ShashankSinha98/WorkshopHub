package com.example.workshophub.Helper;

import android.util.Log;

import com.example.workshophub.Model.Workshop;

import java.util.ArrayList;

public class WorkshopCart {

    private static final String TAG = WorkshopCart.class.getSimpleName();

    private static WorkshopCart workshopCart = null;

    public ArrayList<Workshop> workshopArrayList;

    private WorkshopCart(){
        workshopArrayList = new ArrayList<>();
    }

    public static WorkshopCart getInstance(){

        if(workshopCart == null)
            workshopCart = new WorkshopCart();

        return workshopCart;
    }

    public static void displayWorkshops(){

        WorkshopCart workshopCart = WorkshopCart.getInstance();

        for(int i=0; i<workshopCart.workshopArrayList.size(); i++){
            Log.d(TAG, workshopCart.workshopArrayList.get(i).getName()+" : "+workshopCart.workshopArrayList.get(i).getLocation()+
                    " : "+workshopCart.workshopArrayList.get(i).getDate()+" : "+workshopCart.workshopArrayList.get(i).getTime()+" : "+
                    workshopCart.workshopArrayList.get(i).getUserStatus()+" : "+workshopCart.workshopArrayList.get(i).getDescription()+" : "+
                    workshopCart.workshopArrayList.get(i).getImage()+" : I: "+workshopCart.workshopArrayList.get(i).getId());
        }
    }


}
