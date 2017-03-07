package com.firstproject.mendy.myproject.model.entities;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Mendy on 25/01/2017.
 */
public class Business implements Serializable {

    private long id = 0;
    private String name = "";
    private double longitude = 0;
    private double latitude = 0;
    private String phone = "";
    private String email = "";
    private String link = "";
    private String type = "";
    private String encodedLogo = "";
    private String logoName = "";
    private List<BusinessActivity> listBusinessActivities;
    private byte[] logoBitmap;

    public Business(String name, double longitude, double latitude, String phone, String email, String link, String type, String encodedLogo, String logoName) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phone = phone;
        this.email = email;
        this.link = link;
        this.type = type;
        this.encodedLogo = encodedLogo;
        this.logoName = logoName;
        listBusinessActivities = new ArrayList<>();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public double getLongitude() {
        return longitude;
    }


    public double getLatitude() {
        return latitude;
    }


    public String getPhone() {
        return phone;
    }


    public String getEmail() {
        return email;
    }


    public String getLink() {
        return link;
    }


    public String getType() {
        return type;
    }


    public String getEncodedLogo() {
        return encodedLogo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEncodedLogo(String encodedLogo) {
        this.encodedLogo = encodedLogo;
    }

    public List<BusinessActivity> getListBusinessActivities() {
        return listBusinessActivities;
    }

    public void addBusinessActivity(BusinessActivity businessActivity){
        this.listBusinessActivities.add(businessActivity);
    }

    public void removeBusinessActivity(BusinessActivity businessActivity){
        this.listBusinessActivities.remove(businessActivity);
    }

    public void updateBusinessActivity(BusinessActivity businessActivity){
        this.listBusinessActivities.set(this.listBusinessActivities.indexOf(businessActivity), businessActivity);
    }

    public BusinessActivity getBusinessActivity(long idBusinessActivity){
        for (BusinessActivity b :
                listBusinessActivities) {
            if (b.getId() == idBusinessActivity)
                return b;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getId() == ((Business) obj).getId();
    }

    @Override
    public String toString() {
        return "Business:: id: " + this.id + ", name: " + this.name + ", longitude: " + this.longitude + ", latitude: " + this.latitude + ", phone: " + this.phone + ", email: "
                + this.email + ", link: " + this.link + ", type: " + this.type + ", encodedLogo: " + this.encodedLogo + ", listBusinessActivities: " + this.listBusinessActivities.toString();
    }

    public String getLogoName() {
        return logoName;
    }

    public String getLogoPath() {
        return encodedLogo;
    }

    public void setLogoBitmap(byte[] logoBitmap) {
        this.logoBitmap = logoBitmap;
    }

    public byte[] getLogoBitmap() {
        return logoBitmap;
    }

}


