package com.gn.demo.nyarticlesearchclient.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import com.gn.demo.nyarticlesearchclient.R;
import com.gn.demo.nyarticlesearchclient.activity.SearchFilterFragment;

import java.util.Calendar;

/***
 * class for date picker
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Activity needs to implement this interface
        //DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(),this,  year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SearchFilterFragment sF =(SearchFilterFragment) getParentFragment();
        EditText et = (EditText) sF.getView().getRootView().findViewById(R.id.beginDate);
        et.setText(dayOfMonth + "." + monthOfYear + "." + year);
    }

}