package com.firstproject.mendy.myproject.model.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.firstproject.mendy.myproject.model.backend.DBManagerFactory;
import com.firstproject.mendy.myproject.model.backend.MySqlContract;
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
            editor.putLong(MySqlContract.UserContract.ID, User.getInstance().getId());
            editor.putString(MySqlContract.UserContract.USERNAME, User.getInstance().getUsername());
            editor.putString(MySqlContract.UserContract.PASSWORD, User.getInstance().getPassword());
            editor.apply();
        }

        public static User getUser(Context context) {
            sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            long id = sharedPreferences.getLong(MySqlContract.UserContract.ID, -1);
            String username = sharedPreferences.getString(MySqlContract.UserContract.USERNAME, null);
            String password = sharedPreferences.getString(MySqlContract.UserContract.PASSWORD, null);

            if (id != -1 && username != null && password != null){
                Log.d("Mendy", "getUser: " + username + ", " + password);
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
            editor.remove(MySqlContract.UserContract.ID);
            editor.remove(MySqlContract.UserContract.USERNAME);
            editor.remove(MySqlContract.UserContract.PASSWORD);
            editor.apply();
        }

    }
}
