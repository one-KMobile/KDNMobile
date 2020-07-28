package com.kdn.mtps.mobile.custom;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.kdn.mtps.mobile.util.Logg;

public class CustomDatePickerDialog extends DatePickerDialog implements OnDateChangedListener {

	private DatePickerDialog mDatePicker;
	private final OnDateSetListener mCallBack;
	
	int year;
	int monthOfYear;
	int dayOfMonth;

	@SuppressLint("NewApi")
	public CustomDatePickerDialog(Context context,int theme, OnDateSetListener callBack,
	        int year, int monthOfYear, int dayOfMonth) {
	    super(context, theme,callBack, year, monthOfYear, dayOfMonth);

	    mCallBack = callBack;
	    
	    this.year = year;
	    this.monthOfYear = monthOfYear;
	    this.dayOfMonth = dayOfMonth;
	    
//	    mDatePicker = new DatePickerDialog(context,theme,callBack, year, monthOfYear, dayOfMonth);
	    mDatePicker = new DatePickerDialog(context,callBack, year, monthOfYear, dayOfMonth);
	
	    mDatePicker.getDatePicker().init(year, monthOfYear, dayOfMonth, this);
	
	    mDatePicker.setButton(BUTTON_POSITIVE, "전체", this);
	    mDatePicker.setButton(BUTTON_NEUTRAL,   "선택" , this);
	    mDatePicker.setButton(BUTTON_NEGATIVE,  "취소", this);
        
	    updateTitle(year, monthOfYear);
	
	}
	public void onDateChanged(DatePicker view, int year,
	        int month, int day) {
	    updateTitle(year, month);
	}
	private void updateTitle(int year, int month) {
	    Calendar mCalendar = Calendar.getInstance();
	    mCalendar.set(Calendar.YEAR, year);
	    mCalendar.set(Calendar.MONTH, month);
	//       mCalendar.set(Calendar.DAY_OF_MONTH, day);
	        mDatePicker.setTitle(getFormat().format(mCalendar.getTime()));
	
	}   
	
	public DatePickerDialog getPicker(){
	
	    return this.mDatePicker;
	}
	    /*
	     * the format for dialog tile,and you can override this method
	     */
	public SimpleDateFormat getFormat(){
	    return new SimpleDateFormat("yyyy, MMM");
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		DatePicker datePicker = mDatePicker.getDatePicker();
		if (mCallBack != null) {
            if (which == BUTTON_NEUTRAL) {
//                mCallBack.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            	mCallBack.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), 99);
            } else if (which == BUTTON_POSITIVE) {
                mCallBack.onDateSet(datePicker, 0, 0, 99);
            } else
            	mCallBack.onDateSet(datePicker, year, monthOfYear, 99);
        }

	};   
	
}