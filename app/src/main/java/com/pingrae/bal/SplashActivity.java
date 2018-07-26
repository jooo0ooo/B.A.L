package com.pingrae.bal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(4000);
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}