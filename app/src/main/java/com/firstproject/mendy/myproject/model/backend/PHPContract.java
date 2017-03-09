package com.firstproject.mendy.myproject.model.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Mendy on 25/01/2017.
 */
public class PHPContract {

    public static class UserContract {
        public static final String USER_TABLE = "users";
        public static final String ID = "_id";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }

    public static class BusinessContract {
        public static final String BUSINESS_TABLE = "business";
        public static final String ID = "_id";
        public static final String USER_K = "user_k";
        public static final String NAME = "name";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String PHONE = "phone";
        public static final String EMAIL = "email";
        public static final String LINK = "link";
        public static final String TYPE = "type";
        public static final String LOGO_PATH = "logo_path";
        public static final String LOGO_NAME = "logo_name";
    }

    public static class BusinessActivityContract {
        public static final String BUSINESSACTIVITY_TABLE = "businessactivity";
        public static final String ID = "_id";
        public static final String BUSINESS_K = "business_k";
        public static final String FROM_DATE = "from_date";
        public static final String TO_DATE = "to_date";
        public static final String PRICE = "price";
        public static final String DESCRIPTION = "description";
    }

    public static class ImageActivityContract {
        public static final String IMAGEACTIVITY_TABLE = "image_ba";
        public static final String BUSINESSACTIVITY_K = "businessactivity_k";
        public static final String IMAGE = "path_image";
    }

}
