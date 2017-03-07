package com.firstproject.mendy.myproject.model.entities;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mendy on 24/01/2017.
 */
public class User implements Serializable {

    private long id = 0;
    private String username = "";
    private String password = "";
    private List<Business> listBusiness;

    private static User instance = null;
    public static User getInstance() throws Exception {
        if (instance == null)
            throw new Exception("First reinitialize the instance");

        return instance;
    }
    public static void initInstance(long id, String username, String password){
        if (instance == null) {
            instance = new User(id, username, password);
            instance.listBusiness = new ArrayList<>();
        }
        else {
            instance = null;
            initInstance(id, username, password);
        }
    }
    public static void removeInstance(){
        instance = null;
    }

    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Business> getListBusiness() {
        return listBusiness;
    }

    public void setId(long id) {
        if (this.id == 0)
            this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addBusiness(Business business){
        this.listBusiness.add(business);
    }

    public void removeBusiness(Business business){
        this.listBusiness.remove(business);
    }

    public void updateBusiness(Business business) {
        this.listBusiness.set(this.listBusiness.indexOf(business), business);
    }

    public Business getBusiness(long idBusiness){
        for (Business b :
                listBusiness) {
            if (b.getId() == idBusiness) {
                return b;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getId() == ((User) obj).getId();
    }

    @Override
    public String toString() {
        return "User:: Id: " + this.id + ", username: " + this.username + ", password: " + this.password + ", listBusiness: " + listBusiness.toString();
    }

}

