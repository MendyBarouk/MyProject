package com.firstproject.mendy.myproject.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.controller.fragmentregister.FirstFragment;
import com.firstproject.mendy.myproject.controller.fragmentregister.SecondFragment;
import com.firstproject.mendy.myproject.controller.fragmentregister.ThirdFragment;
import com.firstproject.mendy.myproject.model.SharedPreference.SaveSharedPreference;
import com.firstproject.mendy.myproject.model.backend.DBManager;
import com.firstproject.mendy.myproject.model.backend.DBManagerFactory;
import com.firstproject.mendy.myproject.model.entities.Business;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity implements FirstFragment.OnFirstFragmentInteractionListener,
        SecondFragment.OnSecondFragmentInteractionListener, ThirdFragment.OnThirdFragmentInteractionListener {

    private TextView steppers;

    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;

    private String userName;
    private String password;
    private String nameOfCompany;
    private String phone;
    private String email;
    private String link;
    private String typeOfCompany;
    private Uri logoUri;
    private String logoName;
    private double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupFragments();
        showNextFragment(firstFragment);

        findViewById();
        changeStepper(FirstFragment.TAG, false);

    }

    private void findViewById() {
        findViewById(R.id.activity_register_haveAccount_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        steppers = (TextView) findViewById(R.id.activity_register_steppers_view);
    }

    private void setupFragments() {
        final FragmentManager fm = getSupportFragmentManager();

        this.firstFragment = (FirstFragment) fm.findFragmentByTag(FirstFragment.TAG);
        if (this.firstFragment == null) {
            this.firstFragment = new FirstFragment();
        }

        this.secondFragment = (SecondFragment) fm.findFragmentByTag(SecondFragment.TAG);
        if (this.secondFragment == null) {
            this.secondFragment = new SecondFragment();
        }

        this.thirdFragment = (ThirdFragment) fm.findFragmentByTag(ThirdFragment.TAG);
        if (this.thirdFragment == null) {
            this.thirdFragment = new ThirdFragment();
        }
    }

    private void showNextFragment(final Fragment fragment) {
        if (fragment == null)
            return;

        getSupportFragmentManager()
                .beginTransaction().
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.activity_register_fragment_layout, fragment)
                .commit();


    }

    private void showPreviousFragment(String tag) {
        if (tag == null)
            return;


        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        switch (tag) {
            case FirstFragment.TAG:
                ft.replace(R.id.activity_register_fragment_layout, this.firstFragment);
                break;
            case SecondFragment.TAG:
                ft.replace(R.id.activity_register_fragment_layout, this.secondFragment);
                break;
        }


        ft.commit();
    }

    private void changeStepper(String tag, boolean nextFragment) {
        if (nextFragment) {
            switch (tag) {
                case SecondFragment.TAG:
                    changeShapeStepper(R.id.steppers_bar1, R.id.steppers_rond2, ContextCompat.getColor(this, R.color.colorIcons), R.id.steppers_bar2, R.id.steppers_rond3, ContextCompat.getColor(this, R.color.colorPrimary_light));
                    break;
                case ThirdFragment.TAG:
                    changeShapeStepper(R.id.steppers_bar2, R.id.steppers_rond3, ContextCompat.getColor(this, R.color.colorIcons), R.id.steppers_bar1, R.id.steppers_rond2, ContextCompat.getColor(this, R.color.colorIcons));
                    break;
            }
        } else {
            switch (tag) {
                case FirstFragment.TAG:
                    int color1 = ContextCompat.getColor(this, R.color.colorPrimary_light);// getColor(R.color.colorPrimary_light);
                    int color2 = ContextCompat.getColor(this, R.color.colorPrimary_light);//getColor(R.color.colorPrimary_light);
                    changeShapeStepper(R.id.steppers_bar1, R.id.steppers_rond2, color1, R.id.steppers_bar2, R.id.steppers_rond3, color2);
                    break;
                case SecondFragment.TAG:
                    changeShapeStepper(R.id.steppers_bar1, R.id.steppers_rond2, ContextCompat.getColor(this, R.color.colorIcons), R.id.steppers_bar2, R.id.steppers_rond3, ContextCompat.getColor(this, R.color.colorPrimary_light));
                    break;
            }
        }

    }

    private void changeShapeStepper(int bar1, int rond1, int color1, int bar2, int rond2, int color2) {
        LayerDrawable stepper = (LayerDrawable) this.getResources().getDrawable(R.drawable.steppers_three_step, null);

        GradientDrawable gradientDrawablebar1 = (GradientDrawable) stepper.findDrawableByLayerId(bar1);
        gradientDrawablebar1.setStroke(4, color1);

        GradientDrawable gradientDrawablerond1 = (GradientDrawable) stepper.findDrawableByLayerId(rond1);
        gradientDrawablerond1.setColor(color1);

        GradientDrawable gradientDrawablebar2 = (GradientDrawable) stepper.findDrawableByLayerId(bar2);
        gradientDrawablebar2.setStroke(4, color2);

        GradientDrawable gradientDrawablerond2 = (GradientDrawable) stepper.findDrawableByLayerId(rond2);
        gradientDrawablerond2.setColor(color2);

        steppers.setBackground(stepper);
    }

    @Override
    public void previousFragment(String tag) {
        showPreviousFragment(tag);
        changeStepper(tag, false);
    }

    @Override
    public void moveToSecondFragment(String userName, String password) {
        this.userName = userName;
        this.password = password;
        showNextFragment(secondFragment);
        changeStepper(SecondFragment.TAG, true);
    }

    @Override
    public void moveToThirdFragment(String nameCompagny, String typeOfCompany, Uri logoUri, String logoName) {
        this.nameOfCompany = nameCompagny;
        this.typeOfCompany = typeOfCompany;
        this.logoUri = logoUri;
        this.logoName = logoName;
        showNextFragment(thirdFragment);
        changeStepper(ThirdFragment.TAG, true);
    }

    @Override
    public void registerFinish(double longitude, double latitude, String phone, String email, String link) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.phone = phone;
        this.email = email;
        this.link = link;
        addRegister();
    }

    private void addRegister() {
        new AsyncTask<Void, Void, Void>() {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle("please wait...");
                progressDialog.setMessage("businesses is loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            @Override
            protected Void doInBackground(Void... params) {
                DBManager dbManager = DBManagerFactory.getManager();
                try {
                    boolean success = dbManager.addUser(RegisterActivity.this.userName, RegisterActivity.this.password);
                    if (success) {
                        Bitmap bitmap = BitmapFactory.decodeFile(logoUri.getPath());

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                        byte[] array = stream.toByteArray();
                        String encoded_string = Base64.encodeToString(array, 0);

                        Business business = new Business(RegisterActivity.this.nameOfCompany, RegisterActivity.this.longitude,
                                RegisterActivity.this.latitude, RegisterActivity.this.phone, RegisterActivity.this.email,
                                RegisterActivity.this.link, RegisterActivity.this.typeOfCompany, encoded_string, logoName);
                        business.setLogoBitmap(array);
                        dbManager.addBusiness(business);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressDialog.dismiss();
                moveToListBusinessActivity();
            }
        }.execute();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        secondFragment.onActivityResult(requestCode, resultCode, data);
    }
}
