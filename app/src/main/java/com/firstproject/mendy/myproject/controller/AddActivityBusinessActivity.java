package com.firstproject.mendy.myproject.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.model.backend.DBManager;
import com.firstproject.mendy.myproject.model.backend.DBManagerFactory;
import com.firstproject.mendy.myproject.model.backend.PHPContract;
import com.firstproject.mendy.myproject.model.entities.BusinessActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddActivityBusinessActivity extends AppCompatActivity {

    public static final String TO_EDIT = "toEdit";
    private EditText descriptionEditText, priceEditText;
    private TextView fromTextView, toTextView;

    private long idBusiness;
    private boolean ifEdit;
    private BusinessActivity businessActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity_business);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        idBusiness = getIntent().getLongExtra(PHPContract.BusinessContract.ID, -1);


        findViewById();
        ifEdit = getIntent().getBooleanExtra(TO_EDIT, false);
        if (ifEdit){
            businessActivity = (BusinessActivity) getIntent().getSerializableExtra(PHPContract.BusinessActivityContract.BUSINESSACTIVITY_TABLE);
            editBusiness(businessActivity);
        }
    }

    private void editBusiness(BusinessActivity businessActivity) {
        descriptionEditText.setText(businessActivity.getDescription());
        priceEditText.setText(String.valueOf(businessActivity.getPrice()));
        fromTextView.setText(getDate(businessActivity.getFromDate()));
        toTextView.setText(getDate(businessActivity.getToDate()));
    }

    public static String getDate(long milliSeconds)
    {
        // Create a DateFormatter object for displaying date in specified format.
        android.icu.text.SimpleDateFormat formatter = new android.icu.text.SimpleDateFormat("dd/MM/yyyy");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void findViewById() {
        descriptionEditText = (EditText) findViewById(R.id.activity_add_activity_business_desc_EditText);
        priceEditText = (EditText) findViewById(R.id.activity_add_activity_business_price_editText);
        fromTextView = (TextView) findViewById(R.id.activity_add_activity_business_from_textView);
        toTextView = (TextView) findViewById(R.id.activity_add_activity_business_to_textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_activity_business, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: finish();
                return true;
            case R.id.menu_add_activity_business_save: saveActivityBusiness();
                return true;
        }
        return false;
    }

    private void saveActivityBusiness() {
        String date = fromTextView.getText().toString();
        long date1 = 0;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        date = toTextView.getText().toString();
        long date2 = 0;
        try {
            date2 = new SimpleDateFormat("dd/MM/yyyy").parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        if (ifEdit){
            businessActivity.setFromDate(date1);
            businessActivity.setToDate(date2);
            businessActivity.setPrice(Float.parseFloat(priceEditText.getText().toString()));
            businessActivity.setDescription(descriptionEditText.getText().toString());
            editBusinessActivity(businessActivity);
        }else {
            businessActivity = new BusinessActivity(
                    date1,
                    date2,
                    Float.parseFloat(priceEditText.getText().toString()),
                    descriptionEditText.getText().toString()
            );
            addBusinessActivity(businessActivity);
        }

    }

    private void editBusinessActivity(final BusinessActivity businessActivity) {
        new AsyncTask<Void, Void, Boolean>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(AddActivityBusinessActivity.this);
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
                    success = dbManager.updateBusinessActivity(idBusiness, businessActivity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return success;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                progressDialog.dismiss();
                if (aBoolean) {
                    Intent intent = new Intent();
                    intent.putExtra(TO_EDIT, true);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        }.execute();
    }

    private void addBusinessActivity(final BusinessActivity businessActivity) {

        new AsyncTask<Void, Void, Boolean>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(AddActivityBusinessActivity.this);
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
                    success = dbManager.addBusinessActivity(idBusiness, businessActivity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return success;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                progressDialog.dismiss();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute();
    }
}
