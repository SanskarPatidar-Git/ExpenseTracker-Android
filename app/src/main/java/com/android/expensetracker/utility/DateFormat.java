package com.android.expensetracker.utility;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormat {

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are zero-based
        int year = calendar.get(Calendar.YEAR);
        @SuppressLint("DefaultLocale")
        String formattedDate = String.format("%02d-%02d-%04d", day, month, year);
        System.out.println("Current Date: " + formattedDate);
        return formattedDate;
    }

    public static void getDateFromCalender(Context context, DatePickerListener datePickerListener) {

        Calendar calendar;
        int year, day, month;

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "-" + month + "-" + year;
                datePickerListener.onSelectDate(date);
            }
        }, year, month, day);
        dialog.show();
    }

    public static Date getDateFromString(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMonthNameByDigit(int month){
        switch (month){
            case 1 : return  "Jan";
            case 2 : return  "Feb";
            case 3 : return  "Mar";
            case 4 : return  "Apr";
            case 5 : return  "May";
            case 6 : return  "Jun";
            case 7 : return  "Jul";
            case 8 : return  "Aug";
            case 9 : return  "Sep";
            case 10 : return  "Oct";
            case 11 : return  "Nov";
            case 12 : return  "Dec";
            default: return "";
        }
    }
}
