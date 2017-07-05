package com.project.tk.o0613_friendlychat.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by conscious on 2017-06-20.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private final static String TAG = "FCM_ID";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);

//        생성등록된 토큰을 개인 앱서버에 보내 저장해 두었다가 추가 뭔가를 하고 싶으면 할 수 있도록 한다.
        sendRegistrationToServer(token);
    }
    private void sendRegistrationToServer(String token) {
        /*// Add custom implementation, as needed.
        Log.d(TAG, "sendRegistrationToServer token: " + token);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .build();

        //request
        Request request = new Request.Builder()
                .url("http://jip.dothome.co.kr/myhome/test/fcm/register.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }
}
