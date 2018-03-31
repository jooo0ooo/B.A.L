package com.pingrae.bal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by pingrae on 2017. 10. 17..
 */

public class SplashActivity extends Activity {
    String user_name, user_id, user_picture_url, user_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(4000);

            /*
            SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            user_flag = pref.getString("user_flag", "nothing");

            user_id = pref.getString("my_id", "nothing");
            user_name = pref.getString("my_name", "nothing");
            user_picture_url = pref.getString("picture_url", "nothing");

            MyInfo.my_name = user_name;
            MyInfo.my_id = user_id;
            MyInfo.user_flag = user_flag;
            MyInfo.user_picture_url = user_picture_url;
*/

            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}