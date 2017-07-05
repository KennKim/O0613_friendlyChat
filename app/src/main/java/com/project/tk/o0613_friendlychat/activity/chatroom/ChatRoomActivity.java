/**
 * Copyright Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.project.tk.o0613_friendlychat.activity.chatroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.builders.PersonBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.tk.o0613_friendlychat.R;
import com.project.tk.o0613_friendlychat.activity.SignInActivity;
import com.project.tk.o0613_friendlychat.fcm.MyStr;
import com.project.tk.o0613_friendlychat.model.FriendlyMessage;
import com.project.tk.o0613_friendlychat.model.User;
import com.project.tk.o0613_friendlychat.util.SharedPre;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    public static class MessageViewHolder1Basic extends RecyclerView.ViewHolder {

        interface MyViewHolderClickListener {
            public void onImageClick(View view, int position);

            public void onFaceClick(View view, int position);

            public void onNameClick(View view, int position);
        }

        private MyViewHolderClickListener mListener;
        TextView tvUserName;
        CircleImageView civUserFace;
        TextView tvMsgBody;
        ImageView ivImgBody;

        public MessageViewHolder1Basic(View v) {
            super(v);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onNameClick(v, getAdapterPosition());
                }
            });
            civUserFace = (CircleImageView) itemView.findViewById(R.id.civ_user_face);
            tvMsgBody = (TextView) itemView.findViewById(R.id.tv_msg_body);
            ivImgBody = (ImageView) itemView.findViewById(R.id.iv_img_body);
            ivImgBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onImageClick(v, getAdapterPosition());
                }
            });
        }

        void setCustomClickListener(MyViewHolderClickListener listener) {
            mListener = listener;
        }
    }

    public static class MessageViewHolder2User extends RecyclerView.ViewHolder {
        interface MyViewHolderClickListener {
            public void onImageClick(View view, int position);

            public void onFaceClick(View view, int position);

            public void onNameClick(View view, int position);
        }

        private MyViewHolderClickListener mListener;
        TextView tvMsgBody;
        ImageView ivImgBody;
        TextView tvUserName;
        CircleImageView civUserFace;

        public MessageViewHolder2User(View v) {
            super(v);
            tvMsgBody = (TextView) itemView.findViewById(R.id.tv_msg_body);
            ivImgBody = (ImageView) itemView.findViewById(R.id.iv_img_body);
            ivImgBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onImageClick(v, getAdapterPosition());
                }
            });
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            civUserFace = (CircleImageView) itemView.findViewById(R.id.civ_user_face);
        }

        void setCustomClickListener(MyViewHolderClickListener listener) {
            mListener = listener;
        }
    }

    public static class MessageViewHolder3 extends RecyclerView.ViewHolder {

        public MessageViewHolder3(View v) {
            super(v);
        }
    }

    private static final String TAG = "ChatRoomActivity.class";
    public static final String CHILD_MESSAGES = "messages";
    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_IMAGE = 2;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
    public static final String ANONYMOUS = "anonymous";
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private static final String MESSAGE_URL = "http://friendlychat.firebase.google.com/message/";
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    private static final int USER_TYPE_1_basic = 10;
    private static final int USER_TYPE_2_user = 20;
    private static final int USER_TYPE_3 = 30;
    private int selectedView;

    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;

    private ImageButton mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<FriendlyMessage, RecyclerView.ViewHolder> mFirebaseAdapter;
    public static final String IMAGE_URL = "imageUrl";

    private ProgressBar mProgressBar;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAnalytics mFirebaseAnalytics;
    private EditText mMessageEditText;
    private ImageView mAddMessageImageView;
    private AdView mAdView;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUsername = ANONYMOUS;

        // Initialize Firebase Auth ------------------------
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        setRecyclerView();
        setEtcFirebase();

        initView();
    }

    private void setRecyclerView() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage, RecyclerView.ViewHolder>(
                FriendlyMessage.class,
                R.layout.item_message1_basic,
                RecyclerView.ViewHolder.class,
                mFirebaseDatabaseReference.child(CHILD_MESSAGES)) {

            @Override
            protected FriendlyMessage parseSnapshot(DataSnapshot snapshot) {
                FriendlyMessage friendlyMessage = super.parseSnapshot(snapshot);
                if (friendlyMessage != null) {
                    friendlyMessage.setId(snapshot.getKey());
                }
                return friendlyMessage;
            }

            @Override
            public int getItemViewType(int position) {
                FriendlyMessage friendlyMessage = getItem(position);
                if (!friendlyMessage.getName().equals(mUsername)) {
                    return selectedView = USER_TYPE_1_basic;
                } else {
                    return selectedView = USER_TYPE_2_user;
                }
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                switch (viewType) {
                    case USER_TYPE_1_basic:
                        View userType1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message1_basic, parent, false);
                        MessageViewHolder1Basic viewHolder1 = new MessageViewHolder1Basic(userType1);

                        viewHolder1.setCustomClickListener(new MessageViewHolder1Basic.MyViewHolderClickListener() {
                            @Override
                            public void onImageClick(View view, int position) {
                                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFaceClick(View view, int position) {

                            }

                            @Override
                            public void onNameClick(View view, int position) {
                                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
                            }
                        });
                        return viewHolder1;
                    case USER_TYPE_2_user:
                        View userType2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message2_user, parent, false);
                        MessageViewHolder2User viewHolder2 = new MessageViewHolder2User(userType2);
                        viewHolder2.setCustomClickListener(new MessageViewHolder2User.MyViewHolderClickListener() {
                            @Override
                            public void onImageClick(View view, int position) {
                                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(ChatRoomActivity.this, ImageActivity.class);
                                intent.putExtra(IMAGE_URL, getItem(position).getImageUrl());
                                startActivity(intent);
                            }

                            @Override
                            public void onFaceClick(View view, int position) {

                            }

                            @Override
                            public void onNameClick(View view, int position) {

                            }
                        });
                        return viewHolder2;
                    case USER_TYPE_3:
                        View userType3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message3, parent, false);
                        return new MessageViewHolder3(userType3);
                }
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void populateViewHolder(final RecyclerView.ViewHolder viewHolder, FriendlyMessage friendlyMessage, int position) {
                switch (selectedView) {
                    case USER_TYPE_1_basic:
                        populateType1_basic((MessageViewHolder1Basic) viewHolder, friendlyMessage, position);
                        return;
                    case USER_TYPE_2_user:
                        populateType2_user((MessageViewHolder2User) viewHolder, friendlyMessage, position);
                        return;
                    case USER_TYPE_3:
                        return;
                    default:
                        populateType1_basic((MessageViewHolder1Basic) viewHolder, friendlyMessage, position);
                }
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
    }

    private void setEtcFirebase() {
        // Initialize and request AdMob ad.
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Initialize Firebase Measurement.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Initialize Firebase Remote Config.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Define Firebase Remote Config Settings.
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(true)
                        .build();

        // Define default config values. Defaults are used when fetched config values are not
        // available. Eg: if an error occurred fetching values from the server.
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put("friendly_msg_length", 10L);

        // Apply config settings and default values.
        mFirebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap);

        // Fetch remote config.
        fetchConfig();
    }

    private void initView() {
        mMessageEditText = (EditText) findViewById(R.id.et_msg_body);
        // remote Config
        /*mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSharedPreferences
                .getInt(CodelabPreferences.FRIENDLY_MSG_LENGTH, DEFAULT_MSG_LENGTH_LIMIT))});*/
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mAddMessageImageView = (ImageView) findViewById(R.id.addMessageImageView);
        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String text = mMessageEditText.getText().toString();
                FriendlyMessage friendlyMessage = new FriendlyMessage(text, mUsername, mPhotoUrl, null);
                mFirebaseDatabaseReference.child(CHILD_MESSAGES)
                        .push()
                        .setValue(friendlyMessage)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mMessageEditText.setText("");
                                getAllUID(text);
                            }
                        });
                mFirebaseAnalytics.logEvent(MESSAGE_SENT_EVENT, null);
            }
        });
    }

    private void getAllUID(final String message) {
        mFirebaseDatabase.getReference(User.CHILD_USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    String result = child.getKey();
                    if (!result.equals(SharedPre.getInstance().getString(SharedPre.UID, null))) {
                    }
                    sendPostToFCM(result, message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendPostToFCM(String receiveUID, final String message) {
        mFirebaseDatabase.getReference(User.CHILD_USERS)
                .child(receiveUID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User user = dataSnapshot.getValue(User.class);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // FMC 메시지 생성 start
                                    JSONObject root = new JSONObject();
                                    JSONObject notification = new JSONObject();
                                    notification.put("body", message);
                                    notification.put("title", "titleTest");
                                    root.put("notification", notification);
                                    root.put("to", user.getFcmToken());
                                    // FMC 메시지 생성 end

                                    URL Url = new URL(MyStr.FCM_MESSAGE_URL);

                                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                                    conn.setRequestMethod("POST");
                                    conn.setDoOutput(true);
                                    conn.setDoInput(true);
                                    conn.addRequestProperty("Authorization", "key=" + MyStr.SERVER_KEY);
                                    conn.setRequestProperty("Accept", "application/json");
                                    conn.setRequestProperty("Content-type", "application/json");

                                    OutputStream os = conn.getOutputStream();
                                    os.write(root.toString().getBytes("utf-8"));
                                    os.flush();
                                    conn.getResponseCode();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void populateType1_basic(final MessageViewHolder1Basic viewHolder, FriendlyMessage friendlyMessage, int position) {
        if (friendlyMessage.getText() != null) {
            viewHolder.tvMsgBody.setText(friendlyMessage.getText());
            viewHolder.tvMsgBody.setVisibility(TextView.VISIBLE);
            viewHolder.ivImgBody.setVisibility(ImageView.GONE);
        } else {
            String imageUrl = friendlyMessage.getImageUrl();
            if (imageUrl.startsWith("gs://")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                storageReference.getDownloadUrl().addOnCompleteListener(
                        new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    String downloadUrl = task.getResult().toString();
                                    Glide.with(viewHolder.ivImgBody.getContext())
                                            .load(downloadUrl)
                                            .into(viewHolder.ivImgBody);
                                } else {
                                    Log.w(TAG, "Getting download url was not successful.", task.getException());
                                }
                            }
                        });
            } else {
                Glide.with(viewHolder.ivImgBody.getContext())
                        .load(friendlyMessage.getImageUrl())
                        .into(viewHolder.ivImgBody);
            }
            viewHolder.ivImgBody.setVisibility(ImageView.VISIBLE);
            viewHolder.tvMsgBody.setVisibility(TextView.GONE);
        }

        viewHolder.tvUserName.setText(friendlyMessage.getName());
        if (friendlyMessage.getPhotoUrl() == null) {
            viewHolder.civUserFace.setImageDrawable(ContextCompat.getDrawable(ChatRoomActivity.this, R.drawable.ic_account_circle_black_36dp));
        } else {
            Glide.with(ChatRoomActivity.this)
                    .load(friendlyMessage.getPhotoUrl())
                    .into(viewHolder.civUserFace);
        }

        if (friendlyMessage.getText() != null) {
            // write this message to the on-device index
            FirebaseAppIndex.getInstance().update(getMessageIndexable(friendlyMessage));
        }

        // log a view action on it
        FirebaseUserActions.getInstance().end(getMessageViewAction(friendlyMessage));
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

    }

    private void populateType2_user(final MessageViewHolder2User viewHolder, FriendlyMessage friendlyMessage, int position) {
        if (friendlyMessage.getText() != null) {
            viewHolder.tvMsgBody.setText(friendlyMessage.getText());
            viewHolder.tvMsgBody.setVisibility(TextView.VISIBLE);
            viewHolder.ivImgBody.setVisibility(ImageView.GONE);
        } else {
            String imageUrl = friendlyMessage.getImageUrl();
            if (imageUrl.startsWith("gs://")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String downloadUrl = task.getResult().toString();
                            Glide.with(viewHolder.ivImgBody.getContext())
                                    .load(downloadUrl)
                                    .into(viewHolder.ivImgBody);
                        } else {
                            Log.w(TAG, "Getting download url was not successful.", task.getException());
                        }
                    }
                });
            } else {
                Glide.with(viewHolder.ivImgBody.getContext())
                        .load(friendlyMessage.getImageUrl())
                        .into(viewHolder.ivImgBody);
            }
            viewHolder.ivImgBody.setVisibility(ImageView.VISIBLE);
            viewHolder.tvMsgBody.setVisibility(TextView.GONE);
        }

        viewHolder.tvUserName.setText(friendlyMessage.getName());
        if (friendlyMessage.getPhotoUrl() == null) {
            viewHolder.civUserFace.setImageDrawable(ContextCompat.getDrawable(ChatRoomActivity.this,
                    R.drawable.ic_account_circle_black_36dp));
        } else {
            Glide.with(ChatRoomActivity.this)
                    .load(friendlyMessage.getPhotoUrl())
                    .into(viewHolder.civUserFace);
        }

        if (friendlyMessage.getText() != null) {
            // write this message to the on-device index
            FirebaseAppIndex.getInstance().update(getMessageIndexable(friendlyMessage));
        }

        // log a view action on it
        FirebaseUserActions.getInstance().end(getMessageViewAction(friendlyMessage));
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

    }

    private Action getMessageViewAction(FriendlyMessage friendlyMessage) {
        return new Action.Builder(Action.Builder.VIEW_ACTION)
                .setObject(friendlyMessage.getName(), MESSAGE_URL.concat(friendlyMessage.getId()))
                .setMetadata(new Action.Metadata.Builder().setUpload(false))
                .build();
    }

    private Indexable getMessageIndexable(FriendlyMessage friendlyMessage) {
        PersonBuilder sender = Indexables.personBuilder()
                .setIsSelf(mUsername.equals(friendlyMessage.getName()))
                .setName(friendlyMessage.getName())
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId() + "/sender"));

        PersonBuilder recipient = Indexables.personBuilder()
                .setName(mUsername)
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId() + "/recipient"));

        Indexable messageToIndex = Indexables.messageBuilder()
                .setName(friendlyMessage.getText())
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId()))
                .setSender(sender)
                .setRecipient(recipient)
                .build();

        return messageToIndex;
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invite_menu:
                sendInvitation();
                return true;
            case R.id.crash_menu:
                FirebaseCrash.logcat(Log.ERROR, TAG, "crash caused");
                causeCrash();
                return true;
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mFirebaseUser = null;
                mUsername = ANONYMOUS;
                mPhotoUrl = null;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            case R.id.fresh_config_menu:
                fetchConfig();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void causeCrash() {
        throw new NullPointerException("Fake null pointer exception");
    }

    private void sendInvitation() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    // Fetch the config to determine the allowed length of messages.
    public void fetchConfig() {
        long cacheExpiration = 3600; // 1 hour in seconds
        // If developer mode is enabled reduce cacheExpiration to 0 so that each fetch goes to the
        // server. This should not be used in release builds.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Make the fetched config available via FirebaseRemoteConfig get<type> calls.
                        mFirebaseRemoteConfig.activateFetched();
                        applyRetrievedLengthLimit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // There has been an error fetching the config
                        Log.w(TAG, "Error fetching config", e);
                        applyRetrievedLengthLimit();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());

                    FriendlyMessage tempMessage = new FriendlyMessage(null, mUsername, mPhotoUrl, LOADING_IMAGE_URL);
                    mFirebaseDatabaseReference.child(CHILD_MESSAGES).push()
                            .setValue(tempMessage, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError,
                                                       DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        String key = databaseReference.getKey();
                                        StorageReference storageReference =
                                                FirebaseStorage.getInstance()
                                                        .getReference(mFirebaseUser.getUid())
                                                        .child(key)
                                                        .child(uri.getLastPathSegment());

                                        putImageInStorage(storageReference, uri, key);
                                    } else {
                                        Log.w(TAG, "Unable to write message to database.",
                                                databaseError.toException());
                                    }
                                }
                            });
                }
            }
        } else if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Use Firebase Measurement to log that invitation was sent.
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "inv_sent");

                // Check how many invitations were sent and log.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                Log.d(TAG, "Invitations sent: " + ids.length);
            } else {
                // Use Firebase Measurement to log that invitation was not sent
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "inv_not_sent");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, payload);

                // Sending failed or it was canceled, show failure message to the user
                Log.d(TAG, "Failed to send invitation.");
            }
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri).addOnCompleteListener(ChatRoomActivity.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            FriendlyMessage friendlyMessage = new FriendlyMessage(null, mUsername, mPhotoUrl, task.getResult().getDownloadUrl().toString());
                            mFirebaseDatabaseReference.child(CHILD_MESSAGES).child(key).setValue(friendlyMessage);
                        } else {
                            Log.w(TAG, "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }

    /**
     * Apply retrieved length limit to edit text field. This result may be fresh from the server or it may be from
     * cached values.
     */
    private void applyRetrievedLengthLimit() {
        Long friendly_msg_length = mFirebaseRemoteConfig.getLong("friendly_msg_length");
//        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(friendly_msg_length.intValue())});
        Log.d(TAG, "FML is: " + friendly_msg_length);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

}
