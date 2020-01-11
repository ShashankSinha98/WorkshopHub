package com.example.workshophub.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.workshophub.Model.User;
import com.example.workshophub.R;
import com.example.workshophub.Helper.UserDatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mRegisterFullnameLayout;
    private TextInputLayout mRegisterEmailLayout;
    private TextInputLayout mRegisterPasswordLayout;
    private TextInputLayout mRegisterConfirmPasswordLayout;
    private MaterialButton mRegisterLoginButton;
    private ConstraintLayout mRegisterLayout;

    private UserDatabaseHelper mUserDatabaseHelper;


    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        mUserDatabaseHelper = new UserDatabaseHelper(this);

        mRegisterFullnameLayout = findViewById(R.id.register_fullname_text_input_layout);
        mRegisterEmailLayout = findViewById(R.id.register_email_text_input_layout);
        mRegisterPasswordLayout = findViewById(R.id.register_password_text_input_layout);
        mRegisterConfirmPasswordLayout = findViewById(R.id.register_confirm_password_text_input_layout);
        mRegisterLayout = findViewById(R.id.register_layout);
        mRegisterLoginButton = findViewById(R.id.register_login_material_button);

        mRegisterLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void registerButton(View view){

        hideKeyboard();

        String fullname = mRegisterFullnameLayout.getEditText().getText().toString().trim();
        String email = mRegisterEmailLayout.getEditText().getText().toString().trim();
        String password = mRegisterPasswordLayout.getEditText().getText().toString().trim();
        String confirm_password = mRegisterConfirmPasswordLayout.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(fullname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm_password))
            Snackbar.make(mRegisterLayout, getResources().getString(R.string.empty_feild_error), Snackbar.LENGTH_SHORT).show();

        else if(!validateEmail(email) || !validatePassword(password) || !validatePassword(confirm_password)){

            if(!validateEmail(email))
                mRegisterEmailLayout.setError(getResources().getString(R.string.email_error));
            else
                mRegisterEmailLayout.setErrorEnabled(false);


            if(!validatePassword(password))
                mRegisterPasswordLayout.setError(getResources().getString(R.string.password_error));
            else
                mRegisterPasswordLayout.setErrorEnabled(false);

            if(!validatePassword(confirm_password))
                mRegisterConfirmPasswordLayout.setError(getResources().getString(R.string.password_error));
            else
                mRegisterConfirmPasswordLayout.setErrorEnabled(false);



        } else {

            mRegisterPasswordLayout.setErrorEnabled(false);
            mRegisterConfirmPasswordLayout.setErrorEnabled(false);
            mRegisterEmailLayout.setErrorEnabled(false);

            if (!password.equals(confirm_password))
                Snackbar.make(mRegisterLayout, getResources().getString(R.string.password_not_matching), Snackbar.LENGTH_SHORT).show();

            else if(mUserDatabaseHelper.checkIfEmailExist(email)) {

                Snackbar.make(mRegisterLayout, getResources().getString(R.string.email_already_registered), Snackbar.LENGTH_SHORT).show();
                mRegisterEmailLayout.setError(getResources().getString(R.string.email_already_registered));


            } else {

                User new_user = new User(fullname, password, email);
                if(mUserDatabaseHelper.addUser(new_user) > 0){

                    Toast.makeText(this, getResources().getString(R.string.registeration_successful), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.registeration_error), Toast.LENGTH_SHORT).show();
                }

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
