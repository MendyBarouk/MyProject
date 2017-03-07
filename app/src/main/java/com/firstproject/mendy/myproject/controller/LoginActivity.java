package com.firstproject.mendy.myproject.controller;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.model.SharedPreference.SaveSharedPreference;
import com.firstproject.mendy.myproject.model.backend.DBManagerFactory;
import com.firstproject.mendy.myproject.model.entities.User;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText userNameEditText = (EditText) findViewById(R.id.activity_login_username_editText);
        final EditText passwordEditText = (EditText) findViewById(R.id.activity_login_password_editText);

        findViewById(R.id.activity_login_signIn_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(userNameEditText.getText().toString(), passwordEditText.getText().toString());
            }

            private void signIn(final String username, final String password) {


                new AsyncTask<Void, Void, Boolean>() {

                    ProgressDialog progressDialog;

                    @Override
                    protected void onPreExecute() {
                        progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setTitle("please wait...");
                        progressDialog.setMessage("businesses is loading...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    }

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        return DBManagerFactory.getManager().initUserIdentification(username, password);
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        if (aBoolean){
                                moveToListBusinessActivity();
                            } else {
                                findViewById(R.id.activity_login_incorect_textView).setVisibility(View.VISIBLE);
                            }
                        progressDialog.dismiss();
                    }
                }.execute();
            }
        });

        findViewById(R.id.activity_login_forgot_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 09/02/2017 fair le dialog fragment pour les mot de pass oublier.
                EmailDialogFragment emailDialogFragment = new EmailDialogFragment();
                emailDialogFragment.show(getFragmentManager(), null);
            }
        });
    }


    public void moveToRegisterActivity(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }


    private void moveToListBusinessActivity() {
        try {
            SaveSharedPreference.UserSharedPreferences.saveUser(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, ListBusinessActivity.class));

        finish();
    }
}
