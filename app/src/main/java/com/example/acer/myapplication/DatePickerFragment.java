package com.example.acer.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //object whose calendar fields have been initialized with the current date and time
        final Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int DayOfMonth= calendar.get(Calendar.DAY_OF_MONTH);
        //Create and return a new instance of DatePickerDialog
        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(),this,Year,Month,DayOfMonth);
        //set the min time for the date
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());

        return mDatePicker;
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        TextView DateText=(TextView) getActivity().findViewById(R.id.Datetext);

        DateText.setText("التاريخ الذي تم اختياره:" + year+ "-" + month + "-" + dayOfMonth);

    }
}