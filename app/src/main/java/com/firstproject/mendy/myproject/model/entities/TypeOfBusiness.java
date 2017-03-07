package com.firstproject.mendy.myproject.model.entities;

/**
 * Created by Mendy on 29/01/2017.
 */

public enum TypeOfBusiness {
    HOTEL("Hotel"),
    TRAVEL_AGENCY("Travel agency"),
    ENTERTAINMENT("Entertainment"),
    CAR_RENTAL("Car rental");

    private String desc;

    TypeOfBusiness(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }

    public static TypeOfBusiness fromString(String parameterName) {
        if (parameterName != null) {
            for (TypeOfBusiness objType : TypeOfBusiness.values()) {
                if (parameterName.equalsIgnoreCase(objType.desc)) {
                    return objType;
                }
            }
        }
        return null;
    }
}
