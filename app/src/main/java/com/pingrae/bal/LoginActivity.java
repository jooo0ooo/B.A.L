package com.pingrae.bal;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback callback;
    LinearLayout login_layout;
    LoginButton loginButton;
    BackPressClose back_pressed;

    @Override
    public void onBackPressed() {
        back_pressed.onBackPressed();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //aQuery = new AQuery(this);
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        // kakaotalk login button
        loginButton = (LoginButton)findViewById(R.id.com_kakao_login);
        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(!isConnected()){
                        Toast.makeText(LoginActivity.this,"Please check your Internet connection",Toast.LENGTH_SHORT).show();
                    }
                }

                if(isConnected()){
                    return false;
                }else{
                    return true;
                }
            }
        });

        login_layout = (LinearLayout)findViewById(R.id.login_layout);

        if(Session.getCurrentSession().isOpened()){
            requestMe();
        }else{
            //success_layout.setVisibility(View.GONE);
            login_layout.setVisibility(View.VISIBLE);
        }

    }


    //checking internet connection
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            //access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태. 일반적으로 로그인 후의 다음 activity로 이동한다.
            if(Session.getCurrentSession().isOpened()){ // 한 번더 세션을 체크해주었습니다.
                requestMe();
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }


    public void requestMe() {
        //success_layout.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e("onFailure", errorResult + "");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("onSessionClosed",errorResult + "");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.e("onSuccess",userProfile.toString());

                SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("my_nickname", userProfile.getNickname());
                editor.putString("my_email", userProfile.getEmail());
                editor.putString("picture_url", userProfile.getThumbnailImagePath());
                editor.putString("picture_url_big", userProfile.getProfileImagePath());
                editor.putString("lock_state", "lock");
                editor.commit();

                editor.commit();

                Intent intent = new Intent(LoginActivity.this, LockStateActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onNotSignedUp() {
                Log.e("onNotSignedUp","onNotSignedUp");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }


}