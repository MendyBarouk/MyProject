package com.firstproject.mendy.myproject.model.entities;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Mendy on 25/01/2017.
 */
public class BusinessActivity implements Serializable {

    private long id = 0;
    private long fromDate = 0;
    private long toDate = 0;
    private float price = 0.0f;
    private String description = "";
    private Map<Long, List<String>> listImage = new HashMap<>();

    public BusinessActivity(long fromDate, long toDate, float price, String description) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.price = price;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFromDate() {
        return fromDate;
    }


    public long getToDate() {
        return toDate;
    }


    public float getPrice() {
        return price;
    }


    public String getDescription() {
        return description;
    }


    public void addImage(long idBusinessActivity, String image){

        if (!this.listImage.containsKey(idBusinessActivity))
            this.listImage.put(idBusinessActivity, new ArrayList<String>());
        this.listImage.get(idBusinessActivity).add(image);
    }

    public void removeImage(long idBusinessActivity, String image){
        this.listImage.get(idBusinessActivity).remove(image);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getId() == ((BusinessActivity) obj).getId();
    }

    @Override
    public String toString() {
        return "BusinessActivity:: id: " + this.id + ", fromDate: " + this.fromDate + ", toDate: " + this.toDate + ", price: " + this.price + ", description: " + this.description
                + ", listImage: " + this.listImage.toString();
    }
}
