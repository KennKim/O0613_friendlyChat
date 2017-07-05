package com.project.tk.o0613_friendlychat.activity.chatroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.project.tk.o0613_friendlychat.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends AppCompatActivity {
    private ImageView iv;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        String imgUrl = getIntent().getStringExtra(ChatRoomActivity.IMAGE_URL);

        iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.photo_example);
        Glide.with(ImageActivity.this)
                .load(imgUrl)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(iv);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(iv);
        photoViewAttacher.update();

        progressBar=(ProgressBar)findViewById(R.id.progressBar);

    }
}
