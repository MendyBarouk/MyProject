package com.firstproject.mendy.myproject.controller;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by Mendy on 18/01/2017.
 */

public class CustomDatePicker extends TextView {
    private Calendar calendar;

    public CustomDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomDatePicker(Context context) {
        super(context);
        init();
    }

    private void init() {
        calendar = Calendar.getInstance();
        this.setFocusable(false);
        setDate(calendar.getTime());

        initOnClick();
    }

    private void initOnClick() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = CustomDatePicker.this.getContext();
                DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        CustomDatePicker.this.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                };
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, callBack, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void setDate(Date time) {
        calendar.setTime(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        this.setText(day + "/" + (month + 1) + "/" + year);
    }


}
