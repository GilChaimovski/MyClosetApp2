package com.example.mycloset;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class MessagingHelper {

    public static void makeSnackBar(Context c, View v, String content) {
       Snackbar.make(c,v,content,Snackbar.LENGTH_SHORT).show();
    }
}
