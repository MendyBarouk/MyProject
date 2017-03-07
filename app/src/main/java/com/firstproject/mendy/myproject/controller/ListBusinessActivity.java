package com.firstproject.mendy.myproject.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firstproject.mendy.myproject.R;
import com.firstproject.mendy.myproject.model.SharedPreference.SaveSharedPreference;
import com.firstproject.mendy.myproject.model.entities.Business;
import com.firstproject.mendy.myproject.model.entities.User;

import java.util.ArrayList;

public class ListBusinessActivity extends AppCompatActivity {

    private static final int BACK_FROM_ADD_BUSINESS = 1;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_business);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.activity_list_business_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new RecyclerBusinessAdapter(this);

        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_business, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_list_add: addBusiness();
                return true;
            case R.id.menu_list_logout: logout();
                return true;
        }
        return false;
    }

    private void addBusiness() {
        Intent intent = new Intent(this, AddBusinessActivity.class);
        startActivityForResult(intent, BACK_FROM_ADD_BUSINESS);
    }

    private void logout() {
        SaveSharedPreference.UserSharedPreferences.removeUser();
        User.removeInstance();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BACK_FROM_ADD_BUSINESS){
            if (resultCode == RESULT_OK){
                try {
                    adapter.notifyItemInserted(User.getInstance().getListBusiness().size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == RecyclerBusinessAdapter.BACK_FROM_DETAIL_ACTIVITY){
            if (resultCode == RESULT_OK){
                boolean ifSave = data.getBooleanExtra(DetailBusinessActivity.IF_SAVE, false);
                if (ifSave) {
                    try {
                        Log.d("Mendy", "onActivityResult: " + User.getInstance().getListBusiness());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    int position = data.getIntExtra(RecyclerBusinessAdapter.POSITION, -1);
                    adapter.notifyItemRemoved(position);
                }
            }
        }
    }

}
