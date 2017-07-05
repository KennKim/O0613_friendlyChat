package com.project.tk.o0613_friendlychat.util;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by conscious on 2017-06-09.
 */

public class MyFirebaseDatabase extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


}
