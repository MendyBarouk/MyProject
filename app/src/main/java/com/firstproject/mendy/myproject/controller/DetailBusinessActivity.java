package com.firstproject.mendy.myproject.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.controller.fragmentregister.GooglePlaceAutoCompleteAdapter;
import com.firstproject.mendy.myproject.controller.fragmentregister.Validation;
import com.firstproject.mendy.myproject.model.backend.DBManager;
import com.firstproject.mendy.myproject.model.backend.DBManagerFactory;
import com.firstproject.mendy.myproject.model.backend.PHPContract;
import com.firstproject.mendy.myproject.model.entities.Business;
import com.firstproject.mendy.myproject.model.entities.TypeOfBusiness;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Locale;

public class DetailBusinessActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String IF_SAVE = "ifSave";
    private EditText nameOfCompanyEditText, emailEditText, phoneEditText, linkEditText;
    private AutoCompleteTextView locationEditText;
    private TypeOfBusiness typeOfCompany;

    private Business business;
    private GooglePlaceAutoCompleteAdapter adapter;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_business);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        business = (Business) getIntent().getSerializableExtra(PHPContract.BusinessContract.BUSINESS_TABLE);
        findViewById();
        editBusiness();
    }

    private void editBusiness() {
        nameOfCompanyEditText.setText(business.getName());
        Geocoder geocoder;
        Address addresse = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresse = geocoder.getFromLocation(business.getLatitude(), business.getLongitude(), 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresse != null) {
            latLng = adapter.setLocationFromAddress(addresse.getAddressLine(0) + ", " + addresse.getAddressLine(1) + ", " + addresse.getAddressLine(2));
            locationEditText.setText(addresse.getAddressLine(0) + ", " + addresse.getAddressLine(1) + ", " + addresse.getAddressLine(2));
        }
        emailEditText.setText(business.getEmail());
        phoneEditText.setText(business.getPhone());
        linkEditText.setText(business.getLink().substring(7));

        final Spinner descSpinner = (Spinner) findViewById(R.id.activity_detail_business_desc_spinner);


        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TypeOfBusiness.values());
        descSpinner.setAdapter(adapter);
        descSpinner.setSelection(TypeOfBusiness.fromString(business.getType()).ordinal());

        descSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                business.setType(descSpinner.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void findViewById() {
        nameOfCompanyEditText = (EditText) findViewById(R.id.activity_detail_business_nameOfCompany_textView);
        locationEditText = (AutoCompleteTextView) findViewById(R.id.activity_detail_business_loaction_editText);
        emailEditText = (EditText) findViewById(R.id.activity_detail_business_email_editText);
        phoneEditText = (EditText) findViewById(R.id.activity_detail_business_phone_editText);
        linkEditText = (EditText) findViewById(R.id.activity_detail_business_link_editText);

        adapter = new GooglePlaceAutoCompleteAdapter(this);

        locationEditText.setAdapter(adapter);
        locationEditText.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_business, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_detail_save:
                save();
                return true;
            case R.id.menu_detail_delete:
                deleteShowDialog();
                return true;
        }
        return false;
    }

    private void delete() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(DetailBusinessActivity.this);
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
                    dbManager.removeBusiness(business);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if ((progressDialog != null) && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Intent intent = new Intent();
                intent.putExtra(IF_SAVE, false);
                intent.putExtra(RecyclerBusinessAdapter.POSITION, getIntent().getIntExtra(RecyclerBusinessAdapter.POSITION, -1));
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute();
    }

    ProgressDialog progressDialog;
    private void save() {
        // TODO: 17/01/2017
        if (Validation.validateSecondFragment(nameOfCompanyEditText, typeOfCompany) &&
                Validation.validateThirdFragment(locationEditText, emailEditText, phoneEditText, linkEditText)) {
            business.setLatitude(latLng.latitude);
            business.setLongitude(latLng.longitude);
            business.setName(nameOfCompanyEditText.getText().toString());
            business.setPhone(phoneEditText.getText().toString());
            business.setEmail(emailEditText.getText().toString());
            business.setLink("http://" + linkEditText.getText().toString());



            new AsyncTask<Void, Void, Boolean>() {

                @Override
                protected void onPreExecute() {
                    progressDialog = new ProgressDialog(DetailBusinessActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setTitle("please wait...");
                    progressDialog.setMessage("businesses is loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
                @Override
                protected Boolean doInBackground(Void... params) {
                    DBManager dbManager = DBManagerFactory.getManager();
                    try {
                        return dbManager.updateBusiness(business);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    if ((progressDialog != null) && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (aBoolean) {
                        Intent intent = new Intent();

                        intent.putExtra(IF_SAVE, true);

                        //intent.putExtra(PHPContract.BusinessContract.BUSINESS_TABLE, business);

                        //intent.putExtra(RecyclerBusinessAdapter.POSITION, getIntent().getIntExtra(RecyclerBusinessAdapter.POSITION, -1));

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }.execute();

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;
    }


    public void deleteShowDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Are you sure ?")
                .setMessage("This cannot be undone !")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DetailBusinessActivity.this.delete();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String address = (String) parent.getItemAtPosition(position);
        latLng = adapter.setLocationFromAddress(address);
    }
}
