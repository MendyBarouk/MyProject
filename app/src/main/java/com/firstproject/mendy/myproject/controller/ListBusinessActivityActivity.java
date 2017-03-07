package com.firstproject.mendy.myproject.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.model.backend.DBManager;
import com.firstproject.mendy.myproject.model.backend.DBManagerFactory;
import com.firstproject.mendy.myproject.model.backend.MySqlContract;
import com.firstproject.mendy.myproject.model.entities.BusinessActivity;
import com.firstproject.mendy.myproject.model.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.firstproject.mendy.myproject.controller.AddActivityBusinessActivity.TO_EDIT;

public class ListBusinessActivityActivity extends AppCompatActivity implements RecyclerActivityBusinessAdapter.ViewHolderActivityBusiness.OnEditDeleteInteractionListener {

    public static final int BACK_FROM_ADD_ACTIVITY = 4;
    private RecyclerView.Adapter adapter;
    private List<BusinessActivity> businessActivities;

    private long idBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_business_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        idBusiness = getIntent().getLongExtra(MySqlContract.BusinessContract.ID, -1);

        try {
            businessActivities = User.getInstance().getBusiness(idBusiness).getListBusinessActivities();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.activity_list_business_activity_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new RecyclerActivityBusinessAdapter(this, businessActivities);
        mRecyclerView.setAdapter(adapter);

        //displayListActivitiesBusiness();
    }

//    private void displayListActivitiesBusiness() {
//        new AsyncTask<Void, BusinessActivity, Void>() {
//
//            ProgressDialog progressDialog;
//
//            @Override
//            protected void onPreExecute() {
//                progressDialog = new ProgressDialog(ListBusinessActivityActivity.this);
//                progressDialog.setTitle("please wait...");
//                progressDialog.setMessage("businesses is loading...");
//                progressDialog.setIndeterminate(true);
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                DBManager dbManager = DBManagerFactory.getMySqlManager();
//                String result = dbManager.getActivitiesFromBusiness(idBusiness);
//                try {
//                    JSONArray jsonArray = new JSONArray(result);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jo = jsonArray.getJSONObject(i);
//                        String dateF = jo.getString(MySqlContract.BusinessActivity.BEGGINING);
//                        int yearF = Integer.parseInt(dateF.substring(0,4));
//                        int monthF = Integer.parseInt(dateF.substring(5,7)) - 1;
//                        int dayF = Integer.parseInt(dateF.substring(8,10));
//                        String dateT = jo.getString(MySqlContract.BusinessActivity.FINISH);
//                        int yearT = Integer.parseInt(dateT.substring(0,4));
//                        int monthT = Integer.parseInt(dateT.substring(5,7)) - 1;
//                        int dayT = Integer.parseInt(dateT.substring(8,10));
//
//                        BusinessActivity businessActivity = new BusinessActivity(
//                                idBusiness,
//                                new GregorianCalendar(yearF,monthF,dayF),
//                                new GregorianCalendar(yearT,monthT,dayT),
//                                (float) jo.getDouble(MySqlContract.BusinessActivity.PRICE),
//                                jo.getString(MySqlContract.BusinessActivity.VERBAL_DESCRIPTION)
//                        );
//                        businessActivity.setId(jo.getLong(MySqlContract.BusinessActivity.ID));
//                        publishProgress(businessActivity);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onProgressUpdate(BusinessActivity... values) {
//                businessActivities.add(values[0]);
//                adapter.notifyItemInserted(businessActivities.size()-1);
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                progressDialog.dismiss();
//            }
//        }.execute();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: finish();
                return true;
            case R.id.menu_activity_list_add: addActivity();
                return true;
        }
        return false;
    }

    private void addActivity() {
        Intent intent = new Intent(this, AddActivityBusinessActivity.class);
        intent.putExtra(MySqlContract.BusinessContract.ID, idBusiness);
        startActivityForResult(intent, BACK_FROM_ADD_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BACK_FROM_ADD_ACTIVITY){
            if (resultCode == RESULT_OK){
                if (data.getBooleanExtra(TO_EDIT, false)) {
                    adapter.notifyDataSetChanged();
                }
                else {
                    adapter.notifyItemInserted(businessActivities.size());
                }
            }
        }
    }

    @Override
    public void onDelete(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Are you sure ?")
                .setMessage("This cannot be undone !")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListBusinessActivityActivity.this.delete(position);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void delete(final int position) {
        final BusinessActivity businessActivity = businessActivities.get(position);
        Log.d("Mendy", "delete: " + "Position: " + position + ", " + businessActivity);
        new AsyncTask<Void, Void, Void>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(ListBusinessActivityActivity.this);
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
                    dbManager.removeBusinessActivity(idBusiness, businessActivity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressDialog.dismiss();
                adapter.notifyItemRemoved(position);
            }
        }.execute();


    }

    @Override
    public void onEdit(int position) {
        Intent intent = new Intent(this, AddActivityBusinessActivity.class);
        intent.putExtra(TO_EDIT, true);
        intent.putExtra(MySqlContract.BusinessContract.ID, idBusiness);
        intent.putExtra(MySqlContract.BusinessActivityContract.BUSINESSACTIVITY_TABLE, businessActivities.get(position));
        startActivityForResult(intent, BACK_FROM_ADD_ACTIVITY);
    }
}
