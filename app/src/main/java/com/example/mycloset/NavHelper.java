package com.example.mycloset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NavHelper {

    /**
     * move
     * moves to a given activity with intent (no args)
     * @param c context
     * @param anyActivity some activity to move to
     */
    public static void move(Context c, Class<? extends AppCompatActivity> anyActivity) {
        Intent i = new Intent(c,anyActivity);
        c.startActivity(i);
    }

    /**
     * move
     * moves to a given activity with intent (with args)
     * @param c context
     * @param anyActivity some activity to move to
     */
    public static void move(Context c, Class<? extends AppCompatActivity> anyActivity, Bundle args) {
        Intent i = new Intent(c,anyActivity);
        i.putExtras(args);
        c.startActivity(i);
    }
}