package com.project.tk.o0613_friendlychat.activity.user_list;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.project.tk.o0613_friendlychat.R;
import com.project.tk.o0613_friendlychat.activity.chatroom.ChatRoomActivity;
import com.project.tk.o0613_friendlychat.model.ChatRoom;
import com.project.tk.o0613_friendlychat.model.User;

/**
 * Created by conscious on 2017-06-19.
 */
class UserListAdapter extends FirebaseRecyclerAdapter<User, UserListViewHolder> {

    private Context mContext;

    UserListAdapter(Context context, Class<User> modelClass, int modelLayout, Class<UserListViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
    }

    @Override
    protected User parseSnapshot(DataSnapshot snapshot) {
        User user = super.parseSnapshot(snapshot);
        if (user != null) {
            user.setKey(snapshot.getKey());
        } else {
            Toast.makeText(mContext, "no Users", Toast.LENGTH_LONG).show();
        }
        return user;
    }

    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserListViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        viewHolder.setCustomClickListener(new UserListViewHolder.MyViewHolderClickListener() {
            @Override
            public void onListClick(View view, int position) {
                Toast.makeText(mContext, "List Click : " + position, Toast.LENGTH_SHORT).show();
                test(position);
            }

            @Override
            public void onFaceClick(View view, int position) {
                /*TextView tv1 = (TextView) ((Activity) mContext).findViewById(R.id.tv1);
                tv1.setText(getItem(position).getUserName());
                Toast.makeText(mContext,""+position,Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNameClick(View view, int position) {
//                Toast.makeText(mContext,getItem(position).getUserName()+position,Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;

    }

    private void test(int position) {
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String anotherUid = getItem(position).getuID();

        ChatRoom chatRoom = new ChatRoom(null, anotherUid, null, null);
        final DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child(ChatRoom.CHILD_CHAT_ROOM)
                .child(myUid)
                .child(anotherUid)
                .setValue(chatRoom)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mContext.startActivity(new Intent(mContext.getApplicationContext(),ChatRoomActivity.class));
                    }
                });
    }

    @Override
    protected void populateViewHolder(UserListViewHolder viewHolder, User user, int position) {
        if (user.getUserName() != null) {
            viewHolder.tvUserName.setText(user.getUserName());
        }
        if (user.getInsertedDate() != null) {
            viewHolder.tvInsertedDate.setText(user.getInsertedDate());
        }
        if (user.getFaceUrl() == null) {
            viewHolder.civUserFace.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_account_circle_black_36dp));
        } else {
            Glide.with(mContext)
                    .load(user.getFaceUrl())
                    .into(viewHolder.civUserFace);
        }
    }

}