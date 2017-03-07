package com.firstproject.mendy.myproject.model.backend;


import com.firstproject.mendy.myproject.model.entities.Business;
import com.firstproject.mendy.myproject.model.entities.BusinessActivity;

/**
 * Created by Mendy on 24/01/2017.
 */
public interface DBManager {

    boolean addUser(String username, String password) throws Exception;
    boolean removeUser() throws Exception;
    boolean updateUser(String username, String password) throws Exception;
    boolean initUserIdentification(String username, String password);

    boolean addBusiness(Business business) throws Exception;
    boolean removeBusiness(Business Business) throws Exception;
    boolean updateBusiness(Business business) throws Exception;

    boolean addBusinessActivity(long idBusiness, BusinessActivity businessActivity) throws Exception;
    boolean removeBusinessActivity(long idBusiness, BusinessActivity businessActivity) throws Exception;
    boolean updateBusinessActivity(long idBusiness, BusinessActivity businessActivity) throws Exception;

    boolean addImageToBusinessActivity(long idBusiness, long idBusinessActivity, String pathImage) throws Exception;
    boolean removeImageToBusinessActivity(long idBusiness, long idBusinessActivity, String image) throws Exception;

    String sendEmailForgotPassword(String email);

}
