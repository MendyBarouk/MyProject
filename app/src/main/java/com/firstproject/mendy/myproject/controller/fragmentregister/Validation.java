package com.firstproject.mendy.myproject.controller.fragmentregister;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.firstproject.mendy.myproject.model.entities.Business;
import com.firstproject.mendy.myproject.model.entities.TypeOfBusiness;

/**
 * Created by Mendy on 12/01/2017.
 */

public class Validation {

    public static boolean validateFirstFragment(EditText userName, EditText password) {
        return true;
    }

    public static boolean validateSecondFragment(EditText nameOfCompany, TypeOfBusiness typeOfCompany) {
        return true;
    }

    public static boolean validateThirdFragment(AutoCompleteTextView location, EditText email, EditText phone, EditText link){
        return true;
    }
}
