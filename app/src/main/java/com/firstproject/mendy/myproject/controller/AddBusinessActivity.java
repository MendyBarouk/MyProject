package com.firstproject.mendy.myproject.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.controller.fragmentregister.SecondFragment;
import com.firstproject.mendy.myproject.controller.fragmentregister.ThirdFragment;
import com.firstproject.mendy.myproject.model.backend.DBManager;
import com.firstproject.mendy.myproject.model.backend.DBManagerFactory;
import com.firstproject.mendy.myproject.model.entities.Business;

import java.io.ByteArrayOutputStream;


public class AddBusinessActivity extends AppCompatActivity implements SecondFragment.OnSecondFragmentInteractionListener, ThirdFragment.OnThirdFragmentInteractionListener {

    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;

    private TextView steppers;
    private String nameOfCompany;
    private String typeOfCompany;
    private double longitud;
    private double latitude;
    private String phone;
    private String email;
    private String link;
    private Uri logoUri;
    private String logoName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setupFragment();
        showNextFragment(secondFragment);

        findViewById();
    }

    private void findViewById() {
        steppers = (TextView) findViewById(R.id.activity_add_business_steppers_view);
    }

    private void setupFragment() {
        final FragmentManager fm = getSupportFragmentManager();

        this.secondFragment = (SecondFragment) fm.findFragmentByTag(SecondFragment.TAG);
        if (this.secondFragment == null) {
            this.secondFragment = new SecondFragment();
        }

        this.thirdFragment = (ThirdFragment) fm.findFragmentByTag(ThirdFragment.TAG);
        if (this.thirdFragment == null) {
            this.thirdFragment = new ThirdFragment();
        }
    }

    private void showNextFragment(Fragment fragment) {
        if (fragment == null)
            return;

        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

        ft.replace(R.id.activity_add_business_fragment_layout, fragment);

        ft.commit();
    }

    private void showPreviousFragment(String tag) {
        if (tag == null)
            return;

        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        ft.replace(R.id.activity_add_business_fragment_layout, this.secondFragment);

        ft.commit();
    }

    private void changeStepper(boolean nextFragment) {
        if (nextFragment) {
            changeShapeStepper(R.id.steppers_two_step_bar1, R.id.steppers_two_step_rond2, ContextCompat.getColor(this,R.color.colorIcons));
        } else {
            changeShapeStepper(R.id.steppers_two_step_bar1, R.id.steppers_two_step_rond2, ContextCompat.getColor(this, R.color.colorPrimary_light));
        }
    }

    private void changeShapeStepper(int bar1, int rond1, int color1) {
        LayerDrawable stepper = (LayerDrawable) this.getResources().getDrawable(R.drawable.steppers_two_step, null);

        GradientDrawable gradientDrawablebar1 = (GradientDrawable) stepper.findDrawableByLayerId(bar1);
        gradientDrawablebar1.setStroke(5, color1);

        GradientDrawable gradientDrawablerond1 = (GradientDrawable) stepper.findDrawableByLayerId(rond1);
        gradientDrawablerond1.setColor(color1);


        steppers.setBackground(stepper);
    }

    @Override
    public void previousFragment(String tag) {
        showPreviousFragment(tag);
        changeStepper(false);
    }

    @Override
    public void moveToThirdFragment(String nameCompagny, String typeOfCompany, Uri logoUri, String logoName) {
        this.nameOfCompany = nameCompagny;
        this.typeOfCompany = typeOfCompany;
        this.logoUri = logoUri;
        this.logoName = logoName;
        showNextFragment(thirdFragment);
        changeStepper(true);
    }

    @Override
    public void registerFinish(double longitude, double latitude, String phone, String email, String link) {
        this.longitud = longitude;
        this.latitude = latitude;
        this.phone = phone;
        this.email = email;
        this.link = link;
        Bitmap bitmap = BitmapFactory.decodeFile(logoUri.getPath());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] array = stream.toByteArray();
        String encoded_string = Base64.encodeToString(array, 0);
        Business business = new Business(this.nameOfCompany, this.longitud, this.latitude, this.phone,
                this.email, this.link, this.typeOfCompany, encoded_string, this.logoName);
        business.setLogoBitmap(array);
        addBusiness(business);
    }


    private void addBusiness(final Business business) {
        new AsyncTask<Void, Void, Boolean>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(AddBusinessActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle("please wait...");
                progressDialog.setMessage("businesses is loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            @Override
            protected Boolean doInBackground(Void... params) {
                DBManager dbManager = DBManagerFactory.getManager();
                boolean success = false;
                try {
                    success = dbManager.addBusiness(business);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return success;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                progressDialog.dismiss();
                if (aBoolean) {
                    setResult(RESULT_OK, new Intent());
                    finish();
                } else{
                    Toast.makeText(AddBusinessActivity.this, "problem white server", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        secondFragment.onActivityResult(requestCode, resultCode, data);
    }
}
