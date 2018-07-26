package com.pingrae.bal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BottomBarActivity extends AppCompatActivity {

    BluetoothConnectFragment fragment1;
    GpsFragment fragment2;
    ManualFragment fragment3;
    MoreFragment fragment4;
    LockStateFragment fragment5;
    BackPressClose back_pressed;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_lock:

                    if(SaveData.connectTodevice){
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment5).commit();
                    }else{
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
                    }
                    return true;
                case R.id.navigation_gps:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
                    return true;
                case R.id.navigation_manual:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment3).commit();
                    return true;
                case R.id.navigation_more:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment4).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        fragment1 = new BluetoothConnectFragment();
        fragment2 = new GpsFragment();
        fragment3 = new ManualFragment();
        fragment4 = new MoreFragment();
        fragment5 = new LockStateFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment1).commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        back_pressed = new BackPressClose(this);
    }

    @Override
    public void onBackPressed() {
        back_pressed.onBackPressed();
    }

}