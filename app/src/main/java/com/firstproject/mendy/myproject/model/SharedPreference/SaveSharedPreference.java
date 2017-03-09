package com.firstproject.mendy.myproject.model.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.firstproject.mendy.myproject.model.backend.DBManagerFactory;
import com.firstproject.mendy.myproject.model.backend.PHPContract;
import com.firstproject.mendy.myproject.model.entities.User;


/**
 * Created by Mendy on 12/01/2017.
 */

public class SaveSharedPreference {
    private static final String PREFS = "PREFS";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;


    public static class UserSharedPreferences {

        public static void saveUser(Context context) throws Exception {
            sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putLong(PHPContract.UserContract.ID, User.getInstance().getId());
            editor.putString(PHPContract.UserContract.USERNAME, User.getInstance().getUsername());
            editor.putString(PHPContract.UserContract.PASSWORD, User.getInstance().getPassword());
            editor.apply();
        }

        public static User getUser(Context context) {
            sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            long id = sharedPreferences.getLong(PHPContract.UserContract.ID, -1);
            String username = sharedPreferences.getString(PHPContract.UserContract.USERNAME, null);
            String password = sharedPreferences.getString(PHPContract.UserContract.PASSWORD, null);

            if (id != -1 && username != null && password != null){
                DBManagerFactory.getManager().initUserIdentification(username, password);
                try {
                    return User.getInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public static void removeUser() {
            editor = sharedPreferences.edit();
            editor.remove(PHPContract.UserContract.ID);
            editor.remove(PHPContract.UserContract.USERNAME);
            editor.remove(PHPContract.UserContract.PASSWORD);
            editor.apply();
        }

    }
}
