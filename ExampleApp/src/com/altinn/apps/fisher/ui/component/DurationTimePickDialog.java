package com.altinn.apps.fisher.ui.component;
/**
 * This class is used to support a TimePicker dialog which will shows the customized Minute interval
 */
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class DurationTimePickDialog extends TimePickerDialog
{
    final OnTimeSetListener mCallback;
    TimePicker mTimePicker;
    final int increment;

    public DurationTimePickDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView, int increment)
    {
        super(context, callBack, hourOfDay, minute/increment, is24HourView);
        this.mCallback = callBack;
        this.increment = increment;
        minute = (minute/increment)*increment;
        setTitle(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
            if (mCallback != null && mTimePicker!=null) {
                mTimePicker.clearFocus();
                mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                        mTimePicker.getCurrentMinute()*increment);
            }
    }

    @Override
    protected void onStop()
    {
        // override and do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            Class<?> rClass = Class.forName("com.android.internal.R$id");
            Field timePicker = rClass.getField("timePicker");
            this.mTimePicker = (TimePicker)findViewById(timePicker.getInt(null));
            Field m = rClass.getField("minute");

            NumberPicker mMinuteSpinner = (NumberPicker)mTimePicker.findViewById(m.getInt(null));
            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue((60/increment)-1);
            List<String> displayedValues = new ArrayList<String>();
            for(int i=0;i<60;i+=increment)
            {
                displayedValues.add(String.format("%02d", i));
            }
            mMinuteSpinner.setDisplayedValues(displayedValues.toArray(new String[0]));
           
            TimePicker.OnTimeChangedListener onTimeChangedListener =  new TimePicker.OnTimeChangedListener(){

				@Override
				public void onTimeChanged(TimePicker view, int hourOfDay,int minute) {
					minute = minute*increment;
					DurationTimePickDialog.this.setTitle(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
					
				}
            	
            };           
            mTimePicker.setOnTimeChangedListener(onTimeChangedListener);
           
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}