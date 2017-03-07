package com.firstproject.mendy.myproject.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.model.SharedPreference.SaveSharedPreference;
import com.firstproject.mendy.myproject.model.entities.User;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AsyncTask<Void, Void, Void>() {
            User user = null;
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle("please wait...");
                progressDialog.setMessage("businesses is loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            @Override
            protected Void doInBackground(Void... params) {
                user = SaveSharedPreference.UserSharedPreferences.getUser(MainActivity.this);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressDialog.dismiss();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, ListBusinessActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.execute();

    }

}
