package com.example.workshophub.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.workshophub.Model.User;
import com.example.workshophub.R;
import com.example.workshophub.Helper.UserDatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mLoginEmailTextInputLayout;
    private TextInputLayout mLoginPasswordTextInputLayout;
    private MaterialButton mLoginMaterialButton;
    private MaterialButton mLoginRegisterMaterialButton;
    private ConstraintLayout mLoginLayout;

    private UserDatabaseHelper mUserDatabaseHelper;

    private SharedPreferences mSharedPreferences;



    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static final String LOGIN_STATUS = "login_status";
    private static final String IS_LOGGED = "is_logged";

    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mUserDatabaseHelper = new UserDatabaseHelper(this);

        mLoginEmailTextInputLayout = findViewById(R.id.login_email_text_input_layout);
        mLoginPasswordTextInputLayout = findViewById(R.id.login_password_text_input_layout);
        mLoginMaterialButton = findViewById(R.id.login_material_button);
        mLoginRegisterMaterialButton = findViewById(R.id.login_register_material_button);
        mLoginLayout = findViewById(R.id.login_layout);

        mLoginEmailTextInputLayout.setHint(getResources().getString(R.string.email));
        mLoginPasswordTextInputLayout.setHint(getResources().getString(R.string.password));

        mSharedPreferences = getApplicationContext().getSharedPreferences(LOGIN_STATUS,0);

        if(mSharedPreferences.getBoolean(IS_LOGGED,false)){
                finish();

            SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
            prefsEditor.putBoolean(IS_LOGGED,true);
            Gson gson = new Gson();
            mSharedPreferences = getApplicationContext().getSharedPreferences(LOGIN_STATUS,0);
            String json = mSharedPreferences.getString("UserObject", "");
            User user = gson.fromJson(json, User.class);
            Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
            loginIntent.putExtra("user",user);
            finish();
            startActivity(loginIntent);
        }




        mLoginRegisterMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });





    }

    public void loginButton(View view){
        hideKeyboard();

        String email = mLoginEmailTextInputLayout.getEditText().getText().toString().trim();
        String password = mLoginPasswordTextInputLayout.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
            Snackbar.make(mLoginLayout, getResources().getString(R.string.empty_feild_error), Snackbar.LENGTH_SHORT).show();



        if(!validateEmail(email) || !validatePassword(password)){

            if(!validateEmail(email))
                mLoginEmailTextInputLayout.setError(getResources().getString(R.string.email_error));
            else
                mLoginEmailTextInputLayout.setErrorEnabled(false);


            if(!validatePassword(password))
                mLoginPasswordTextInputLayout.setError(getResources().getString(R.string.password_error));
            else
                mLoginPasswordTextInputLayout.setErrorEnabled(false);

        } else {
            mLoginPasswordTextInputLayout.setErrorEnabled(false);
            mLoginEmailTextInputLayout.setErrorEnabled(false);

            User user = new User(email, password);

            if(mUserDatabaseHelper.checkUser(user)){

                user = mUserDatabaseHelper.getSingleUserInfo(email);

                SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
                prefsEditor.putBoolean(IS_LOGGED,true);
                Gson gson = new Gson();
                String json = gson.toJson(user);
                prefsEditor.putString("UserObject", json);
                prefsEditor.commit();


                Snackbar.make(mLoginLayout, getResources().getString(R.string.login_success), Snackbar.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                loginIntent.putExtra("user",user);
                finish();
                startActivity(loginIntent);


            } else {

                Snackbar.make(mLoginLayout, getResources().getString(R.string.login_error), Snackbar.LENGTH_SHORT).show();

            }
        }
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validatePassword(String password) {
        return password.length() >= 8;
    }
}
