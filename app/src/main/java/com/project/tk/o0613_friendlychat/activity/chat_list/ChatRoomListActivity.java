package com.project.tk.o0613_friendlychat.activity.chat_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.project.tk.o0613_friendlychat.R;
import com.project.tk.o0613_friendlychat.activity.chatroom.ChatRoomActivity;
import com.project.tk.o0613_friendlychat.util.MyTime;

public class ChatRoomListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.chat_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatRoomListActivity.this, ChatRoomActivity.class));
            }
        });

    }

}
