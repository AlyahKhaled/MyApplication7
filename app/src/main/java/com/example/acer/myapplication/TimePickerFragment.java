package com.example.acer.myapplication;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    public static String passingTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //object whose calendar fields have been initialized with the current date and time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){

        String Date="";
        TextView textView = (TextView) getActivity().findViewById(R.id.timeText);

        //format the time
        Time time = new Time(hourOfDay,minute,0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("hh:mm:ss a");
        Date=formatter.format(time);

        //Display time on TextView
        passingTime=""+Date;
        textView.setText("الوقت المختار هو:" + Date);
        invititioncretion.isEditTime = false;
    }
}