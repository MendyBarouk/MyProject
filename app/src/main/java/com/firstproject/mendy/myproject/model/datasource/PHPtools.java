package com.firstproject.mendy.myproject.model.datasource;

import android.content.ContentValues;
import android.net.Uri;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mendy on 01/01/2017.
 */

class PHPtools {

    public static String GET(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            return "";
        }
    }

    static String POST(String url, ContentValues values) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        Uri.Builder builder = new Uri.Builder();
        for (String value : values.keySet()) {
            builder.appendQueryParameter(value, values.getAsString(value));
        }

        String query = builder.build().getEncodedQuery();

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();
        conn.connect();

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null){
                response.append(line);
            }

            in.close();
            return response.toString();
        } else {
            return "";
        }
    }


}
