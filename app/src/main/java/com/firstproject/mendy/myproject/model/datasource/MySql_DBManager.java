package com.firstproject.mendy.myproject.model.datasource;


import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.firstproject.mendy.myproject.model.backend.DBManager;
import com.firstproject.mendy.myproject.model.backend.PHPContract;
import com.firstproject.mendy.myproject.model.entities.Business;
import com.firstproject.mendy.myproject.model.entities.BusinessActivity;
import com.firstproject.mendy.myproject.model.entities.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Mendy on 24/01/2017.
 */
public class MySql_DBManager implements DBManager {

    private final String SERVER = "mendybarouk.hol.es";
    private final String WEB_URL = "http://" + SERVER + "/MyProjectPHP";

    private boolean updateFlag = false;


    @Override
    public boolean addUser(String username, String password) {
        try {
            ContentValues values = new ContentValues();
            values.put(PHPContract.UserContract.USERNAME, username);
            values.put(PHPContract.UserContract.PASSWORD, password);

            String result = PHPtools.POST(WEB_URL + "/addUser.php", values);
            long id = Long.parseLong(result);
            if (id > 0) {
                setUpdate();
                User.initInstance(id, username, password);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean removeUser() throws Exception {
        return false;
    }

    @Override
    public boolean updateUser(String username, String password) throws Exception {
        return false;
    }

    @Override
    public boolean initUserIdentification(String username, String password) {

        try {
            ContentValues values = new ContentValues();
            values.put(PHPContract.UserContract.USERNAME, username);
            values.put(PHPContract.UserContract.PASSWORD, password);

            String user = PHPtools.POST(WEB_URL + "/initUserIdentification.php", values);
            JSONArray jsonArray = new JSONArray(user);
            if (jsonArray.length() > 0){
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                User.initInstance(jsonObject.getLong(PHPContract.UserContract.ID), username, password);
                getAllBusinessFromUser();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addBusiness(Business business) throws Exception {
        try {

            ContentValues values = new ContentValues();
            values.put(PHPContract.BusinessContract.USER_K , User.getInstance().getId());
            values.put(PHPContract.BusinessContract.NAME, business.getName());
            values.put(PHPContract.BusinessContract.LONGITUDE, business.getLongitude());
            values.put(PHPContract.BusinessContract.LATITUDE, business.getLatitude());
            values.put(PHPContract.BusinessContract.PHONE, business.getPhone());
            values.put(PHPContract.BusinessContract.EMAIL, business.getEmail());
            values.put(PHPContract.BusinessContract.LINK, business.getLink());
            values.put(PHPContract.BusinessContract.TYPE, business.getType());
            values.put(PHPContract.BusinessContract.LOGO_PATH, business.getEncodedLogo());
            values.put(PHPContract.BusinessContract.LOGO_NAME, business.getLogoName());

            String result = PHPtools.POST(WEB_URL + "/addBusiness.php", values);
            long idBusiness = Long.parseLong(result);
            if (idBusiness > 0) {
                business.setId(idBusiness);
                User.getInstance().addBusiness(business);
                setUpdate();
                return true;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeBusiness(Business business) throws Exception {
        try {
            ContentValues values = new ContentValues();
            values.put(PHPContract.BusinessContract.ID, business.getId());

            String result = PHPtools.POST(WEB_URL + "/removeBusiness.php", values);
            long id = Long.parseLong(result);
            if (id > 0) {
                setUpdate();
                User.getInstance().removeBusiness(business);
                return true;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateBusiness(Business business) throws Exception {
        try {

            ContentValues values = new ContentValues();
            values.put(PHPContract.BusinessContract.NAME, business.getName());
            values.put(PHPContract.BusinessContract.LONGITUDE, business.getLongitude());
            values.put(PHPContract.BusinessContract.LATITUDE, business.getLatitude());
            values.put(PHPContract.BusinessContract.PHONE, business.getPhone());
            values.put(PHPContract.BusinessContract.EMAIL, business.getEmail());
            values.put(PHPContract.BusinessContract.LINK, business.getLink());
            values.put(PHPContract.BusinessContract.TYPE, business.getType());
            //values.put(PHPContract.BusinessContract.LOGO, business.getEncodedLogo());
            values.put(PHPContract.BusinessContract.ID, business.getId());

            String result = PHPtools.POST(WEB_URL + "/updateBusiness.php", values);
            long id = Long.parseLong(result);
            if (id > 0) {
                setUpdate();
                User.getInstance().updateBusiness(business);
                return true;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getAllBusinessFromUser() {

        try {
            ContentValues values = new ContentValues();
            values.put(PHPContract.BusinessContract.USER_K, User.getInstance().getId());

            String result = PHPtools.POST(WEB_URL + "/getBusinessesFromUser.php", values);
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                long id = jsonObject.getLong(PHPContract.BusinessContract.ID);
                String name = jsonObject.getString(PHPContract.BusinessContract.NAME);
                double longitude = jsonObject.getDouble(PHPContract.BusinessContract.LONGITUDE);
                double latitude = jsonObject.getDouble(PHPContract.BusinessContract.LATITUDE);
                String phone = jsonObject.getString(PHPContract.BusinessContract.PHONE);
                String email = jsonObject.getString(PHPContract.BusinessContract.EMAIL);
                String link = jsonObject.getString(PHPContract.BusinessContract.LINK);
                String type = jsonObject.getString(PHPContract.BusinessContract.TYPE);

                // TODO: 12/02/2017
                String logoPath = jsonObject.getString(PHPContract.BusinessContract.LOGO_PATH);
                String[] logoNames = logoPath.split("/");
                String logoName = logoNames[logoNames.length - 1];
                Business business = new Business(name, longitude, latitude, phone, email, link, type, logoPath, logoName);
                business.setId(id);
                Bitmap bitmap = BitmapFactory.decodeStream(new URL(WEB_URL + "/" + business.getLogoPath()).openConnection().getInputStream());
                //bitmap.compress(Bitmap.CompressFormat.JPEG, 70, null);
                business.setLogoBitmap(bitmapToByteArray(bitmap));
                User.getInstance().addBusiness(business);
                getAllBusinessActivityFromBusiness(business.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private void getAllBusinessActivityFromBusiness(long idBusiness) {

        try {
            ContentValues values = new ContentValues();
            values.put(PHPContract.BusinessActivityContract.BUSINESS_K, idBusiness);

            String result = PHPtools.POST(WEB_URL + "/getActivitiesFromBusiness.php", values);
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                long id = jsonObject.getLong(PHPContract.BusinessActivityContract.ID);
                long fromDate = jsonObject.getLong(PHPContract.BusinessActivityContract.FROM_DATE);
                long toDate = jsonObject.getLong(PHPContract.BusinessActivityContract.TO_DATE);
                float price = (float) jsonObject.getDouble(PHPContract.BusinessActivityContract.PRICE);
                String description = jsonObject.getString(PHPContract.BusinessActivityContract.DESCRIPTION);

                BusinessActivity businessActivity = new BusinessActivity(fromDate, toDate, price, description);
                businessActivity.setId(id);
                User.getInstance().getBusiness(idBusiness).addBusinessActivity(businessActivity);
                getAllImageFromBusinessActivity(idBusiness, businessActivity.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllImageFromBusinessActivity(long idBusiness, long idBusinessActivity) {
/*        PreparedStatement preparedStatement3 = null;
        ResultSet resultSet3 = null;
        try {
            String sql = "SELECT * FROM " + PHPContract.ImageActivityContract.IMAGEACTIVITY_TABLE +
                    " WHERE " + PHPContract.ImageActivityContract.BUSINESSACTIVITY_K + "=?";
            preparedStatement3 = connection.prepareStatement(sql);
            preparedStatement3.setLong(1, idBusinessActivity);

            resultSet3 = preparedStatement3.executeQuery();

            while (resultSet3.next()) {

                String image = resultSet3.getString(PHPContract.ImageActivityContract.IMAGE);

                User.getInstance().getBusiness(idBusiness).getBusinessActivity(idBusinessActivity).addImage(idBusinessActivity, image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet3.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                preparedStatement3.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public boolean addBusinessActivity(long idBusiness, BusinessActivity businessActivity) throws Exception {
        try {
            ContentValues values = new ContentValues();
            values.put(PHPContract.BusinessActivityContract.BUSINESS_K, idBusiness);
            values.put(PHPContract.BusinessActivityContract.FROM_DATE, businessActivity.getFromDate());
            values.put(PHPContract.BusinessActivityContract.TO_DATE, businessActivity.getToDate());
            values.put(PHPContract.BusinessActivityContract.PRICE, businessActivity.getPrice());
            values.put(PHPContract.BusinessActivityContract.DESCRIPTION, businessActivity.getDescription());

            String result = PHPtools.POST(WEB_URL + "/addBusinessActivity.php", values);
            long id = Long.parseLong(result);
            if (id > 0) {
                setUpdate();
                businessActivity.setId(id);
                User.getInstance().getBusiness(idBusiness).addBusinessActivity(businessActivity);
                return true;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeBusinessActivity(long idBusiness, BusinessActivity businessActivity) throws Exception {
        try {
            ContentValues values = new ContentValues();
            values.put(PHPContract.BusinessActivityContract.ID, businessActivity.getId());

            String result = PHPtools.POST(WEB_URL + "/removeBusinessActivity.php", values);
            long id = Long.parseLong(result);
            if (id > 0) {
                setUpdate();
                User.getInstance().getBusiness(idBusiness).removeBusinessActivity(businessActivity);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateBusinessActivity(long idBusiness, BusinessActivity businessActivity) throws Exception {
        try {
            ContentValues values = new ContentValues();
            values.put(PHPContract.BusinessActivityContract.ID, businessActivity.getId());
            values.put(PHPContract.BusinessActivityContract.FROM_DATE, businessActivity.getFromDate());
            values.put(PHPContract.BusinessActivityContract.TO_DATE, businessActivity.getToDate());
            values.put(PHPContract.BusinessActivityContract.PRICE, businessActivity.getPrice());
            values.put(PHPContract.BusinessActivityContract.DESCRIPTION, businessActivity.getDescription());

            String result = PHPtools.POST(WEB_URL + "/updateBusinessActivity.php", values);
            long rowCount = Long.parseLong(result);
            if (rowCount > 0) {
                setUpdate();
                User.getInstance().getBusiness(idBusiness).updateBusinessActivity(businessActivity);
                return true;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addImageToBusinessActivity(long idBusiness, long idBusinessActivity, String pathImage) throws Exception {
        User.getInstance().getBusiness(idBusiness).getBusinessActivity(idBusinessActivity).addImage(idBusinessActivity, pathImage);
        return false;
    }

    @Override
    public boolean removeImageToBusinessActivity(long idBusiness, long idBusinessActivity, String image) throws Exception {

        User.getInstance().getBusiness(idBusiness).getBusinessActivity(idBusinessActivity).removeImage(idBusinessActivity, image);
        return true;

    }

    @Override
    public String sendEmailForgotPassword(String email){
        try {
            ContentValues values = new ContentValues();
            values.put(PHPContract.BusinessContract.EMAIL, email);
            return PHPtools.POST(WEB_URL + "/sendEmailForgotUser.php", values);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    private void setUpdate() {
        updateFlag = true;
    }
}


