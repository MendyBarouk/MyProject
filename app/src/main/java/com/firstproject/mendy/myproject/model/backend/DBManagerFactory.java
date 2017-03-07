package com.firstproject.mendy.myproject.model.backend;

import com.firstproject.mendy.myproject.model.datasource.MySql_DBManager;

/**
 * Created by Mendy on 29/01/2017.
 */

public class DBManagerFactory {

    static DBManager manager = null;

    public static DBManager getManager() {
        if (manager == null)
            manager = new MySql_DBManager();
        return manager;
    }

}
