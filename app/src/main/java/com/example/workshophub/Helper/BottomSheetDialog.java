package com.example.workshophub.Helper;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.workshophub.Activity.MainActivity;
import com.example.workshophub.Adapter.AvailableWorkshopAdapter;
import com.example.workshophub.Model.User;
import com.example.workshophub.Model.Workshop;
import com.example.workshophub.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private static TextView workshopName;
    private static TextView workshopDate;
    private static TextView workshopTime;
    private static TextView workshopLocation;
    private static TextView workshopVenue;
    private static TextView userName;
    private static TextView userEmail;
    private static ImageView workshopImage;
    private static MaterialButton confirmRegBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout,container,false);

        workshopName = view.findViewById(R.id.dialog_workshop_name);
        workshopDate = view.findViewById(R.id.dialog_workshop_date);
        workshopTime = view.findViewById(R.id.dialog_workshop_time);
        workshopLocation = view.findViewById(R.id.dialog_workshop_location);
        workshopVenue = view.findViewById(R.id.dialog_workshop_venue);
        userEmail = view.findViewById(R.id.dialog_user_email);
        userName = view.findViewById(R.id.dialog_user_name);
        workshopImage = view.findViewById(R.id.dialog_workshop_iv);
        confirmRegBtn = view.findViewById(R.id.confirm_reg_btn);

        populateDialog(MainActivity.workshopSelected, MainActivity.sUser);

        confirmRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvailableWorkshopAdapter.confirmRegistration();
            }
        });


        return view;
    }

    public static void populateDialog(Workshop workshop, User user) {

        workshopName.setText(workshop.getName());
        workshopDate.setText(workshop.getDate());
        workshopTime.setText(workshop.getTime());
        workshopLocation.setText(workshop.getLocation());
        userEmail.setText(user.getEmail());
        userName.setText(user.getUsername());
        workshopImage.setImageResource(workshop.getImage());
        workshopVenue.setText(workshop.getVenue());

    }
}
