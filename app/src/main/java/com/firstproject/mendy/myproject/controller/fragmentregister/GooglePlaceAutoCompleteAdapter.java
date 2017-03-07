package com.firstproject.mendy.myproject.controller.fragmentregister;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mendy on 07/02/2017.
 */
public class GooglePlaceAutoCompleteAdapter extends ArrayAdapter {

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyARh9Se-l-cDdrTLH4x3vB6KPJK9R2vmx4";
    private ArrayList resulstList;

    Context context;

    public GooglePlaceAutoCompleteAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        this.context = context;
    }

    @Override
    public int getCount() {
        return resulstList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return (String) resulstList.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null){
                    resulstList = autoComplete(constraint.toString());
                    filterResults.values = resulstList;
                    filterResults.count = resulstList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0){
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private static ArrayList autoComplete(String input){
        ArrayList resultList = null;
        HttpURLConnection connection = null;
        StringBuilder jsonResult = new StringBuilder();

        try {

            URL url = new URL(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?key=" + API_KEY + "&input=" + URLEncoder.encode(input, "utf8"));
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(connection.getInputStream());

            int read;
            char[] buffer = new char[1024];
            while ((read = in.read(buffer)) != -1){
                jsonResult.append(buffer, 0, read);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonResult.toString());
            JSONArray predsJsonArray = jsonObject.getJSONArray("predictions");

            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultList;
    }


    public LatLng setLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address != null) {
                Address location = address.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
