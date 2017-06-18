package com.project.tk.o0613_friendlychat.activity.user_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.project.tk.o0613_friendlychat.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by conscious on 2017-06-19.
 */

public class UserListViewHolder  extends RecyclerView.ViewHolder {

    interface MyViewHolderClickListener {
        public void onFaceClick(View view, int position);
        public void onNameClick(View view, int position);
    }

    private MyViewHolderClickListener mListener;
    CircleImageView civUserFace;
    TextView tvUserName, tvInsertedDate;

    public UserListViewHolder(View v) {
        super(v);
        civUserFace = (CircleImageView)v.findViewById(R.id.civ_user_face);
        tvUserName = (TextView)v.findViewById(R.id.tv_user_name);
        tvInsertedDate = (TextView)v.findViewById(R.id.tv_inserted_date);

        civUserFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFaceClick(v,getAdapterPosition());
            }
        });
        tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNameClick(v,getAdapterPosition());
            }
        });
    }

    void setCustomClickListener(MyViewHolderClickListener listener){
        mListener=listener;
    }
}