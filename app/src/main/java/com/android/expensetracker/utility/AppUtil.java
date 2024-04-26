package com.android.expensetracker.utility;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class AppUtil {

    public static void navigateTo(Context context , Class<? extends AppCompatActivity> to){
        Intent intent = new Intent(context ,to);
        context.startActivity(intent);
    }
}
