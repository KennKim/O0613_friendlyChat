package com.project.tk.o0613_friendlychat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.project.tk.o0613_friendlychat.R;
import com.project.tk.o0613_friendlychat.activity.user_list.UserListActivity;
import com.project.tk.o0613_friendlychat.util.SharedPreferenceUtil;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferenceUtil.getInstance().init(getApplicationContext());
        String userName = SharedPreferenceUtil.getInstance().getString(SharedPreferenceUtil.DISPLAY_NAME, null);


        if (userName == null || userName.equals("")) {
            startActivity(new Intent(StartActivity.this, SignInActivity.class));
            finish();
        } else {
            startActivity(new Intent(StartActivity.this, UserListActivity.class));
            finish();
        }

    }


}
