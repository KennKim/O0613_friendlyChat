package com.project.tk.o0613_friendlychat.util;

/**
 * Created by conscious on 2017-06-22.
 */

public class FcmSendMessage {

    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAAZ-ng5y0:APA91bHBwuWgu00b_z_hD8CXAJ4jI6LZ2OWSZ-zqq7La9UXGOUJLeBRxmafE_tyZh3MLLb8nTEcy04zS7Oy8dCAG5AZ7-meM4b1qJX-G32Vk4fTkwrx5-RwHXeMQNGNKt0s4xXW8nV0w";

    public FcmSendMessage() {
    }

    private static class Singleton{
        private static FcmSendMessage instance = new FcmSendMessage();
    }

    public static FcmSendMessage getInstance() {
        return Singleton.instance;
    }
/*
    private void sendPostToFCM(final String message) {
        mFirebaseDatabase.getReference(User.CHILD_USERS)
                .child(SharedPre.getInstance().getString(SharedPre.UID,null))
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
                                    notification.put("title", getString(R.string.app_name));
                                    root.put("notification", notification);
                                    root.put("to", user.getFcmToken());
                                    // FMC 메시지 생성 end

                                    URL Url = new URL(FCM_MESSAGE_URL);
                                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                                    conn.setRequestMethod("POST");
                                    conn.setDoOutput(true);
                                    conn.setDoInput(true);
                                    conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
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
    }*/


}
