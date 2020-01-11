package com.example.workshophub.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshophub.Activity.LoginActivity;
import com.example.workshophub.Activity.MainActivity;
import com.example.workshophub.Fragment.AvailableWorkshopFragment;
import com.example.workshophub.Helper.BottomSheetDialog;
import com.example.workshophub.Helper.UserDatabaseHelper;
import com.example.workshophub.Model.User;
import com.example.workshophub.Model.Workshop;
import com.example.workshophub.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Set;

public class AvailableWorkshopAdapter extends RecyclerView.Adapter<AvailableWorkshopAdapter.ViewHolder> {

    private static Context mContext;
    private static ArrayList<Workshop> mWorkshopArrayList;
    private Activity mActivity;
    private static AlertDialog.Builder  builder;
    private Set<String> workshopRegistered;
    private static BottomSheetDialog bottomSheetDialog;
    private static int  posSelected = -1;

    public AvailableWorkshopAdapter(Context mContext,  ArrayList<Workshop> mWorkshopArrayList, Set<String> workshopRegistered) {
        this.mContext = mContext;
        this.workshopRegistered = workshopRegistered;
        this.mWorkshopArrayList = mWorkshopArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_single_row_available_workshop, viewGroup, false);
        return new AvailableWorkshopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {



        Log.d("xlr8_check","ID: "+mWorkshopArrayList.get(position).getId()+" : Set- "+workshopRegistered);

            holder.registerButton.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent, null));
            holder.registerButton.setText(mContext.getResources().getString(R.string.register));
            holder.registerButton.setEnabled(true);

            holder.workshopImage.setBackground(mContext.getDrawable(mWorkshopArrayList.get(position).getImage()));
            holder.workshopName.setText(mWorkshopArrayList.get(position).getName());
            holder.workshopDate.setText(mWorkshopArrayList.get(position).getDate()+",");
            holder.workshopDescription.setText(mWorkshopArrayList.get(position).getDescription());
            holder.workshopLocation.setText(mWorkshopArrayList.get(position).getLocation());
            holder.workshopTime.setText(mWorkshopArrayList.get(position).getTime());




        holder.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MainActivity.isLoggedBool) {

                    posSelected = position;

                    bottomSheetDialog = new BottomSheetDialog();
                    MainActivity.workshopSelected = mWorkshopArrayList.get(position);
                    bottomSheetDialog.show(((MainActivity) mContext).getSupportFragmentManager(), "bottom_nav");
                } else {
                    builder.setTitle(mContext.getResources().getString(R.string.login_required)).setMessage(mContext.getResources().getString(R.string.login_required_msg))
                            .setCancelable(false)
                            .setPositiveButton(mContext.getResources().getString(R.string.login), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    ((MainActivity)mContext).finish();
                                    mContext.startActivity(intent);
                                }
                            })
                            .setNegativeButton(mContext.getResources().getString(R.string.dissmiss), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mWorkshopArrayList.size();
    }

    public static void confirmRegistration(){
        builder.setTitle(mContext.getResources().getString(R.string.register)).setMessage(mContext.getResources().getString(R.string.register_message))
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Toast.makeText(mContext, mContext.getResources().getString(R.string.registeration_successful), Toast.LENGTH_SHORT).show();

                        bottomSheetDialog.dismiss();
                        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(mContext);
                        userDatabaseHelper.updateData(MainActivity.sUser, mWorkshopArrayList.get(posSelected).getId());
                        MainActivity.adminUpdateList();
                        AvailableWorkshopFragment.populateAvailableWorkshopList();
                        //notifyDataSetChanged();

                        ArrayList<User> allUser = userDatabaseHelper.getAllUserData();

                        for(User user : allUser){
                            Log.d("xlr8_all_user","I: "+user.getId()+" N: "+user.getUsername()+" P: "+user.getPassword()+" E: "+user.getEmail()
                                    +" : W: "+user.getWorkshopRegistered());
                        }
                    }
                })
                .setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView workshopName, workshopDate, workshopTime, workshopDescription, workshopLocation;
        private ImageView workshopImage;
        private MaterialButton registerButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            workshopName = itemView.findViewById(R.id.workshop_name);
            workshopDate = itemView.findViewById(R.id.workshop_date);
            workshopTime = itemView.findViewById(R.id.workshop_time);
            workshopDescription = itemView.findViewById(R.id.workshop_description);
            workshopLocation = itemView.findViewById(R.id.workshop_location);
            workshopImage = itemView.findViewById(R.id.workshop_img);
            registerButton = itemView.findViewById(R.id.workshop_register_btn);
        }
    }
}
