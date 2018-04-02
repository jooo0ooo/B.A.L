package com.pingrae.bal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyinfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CircleImageView nav_header_user_img, main_user_img;
    String user_nickname, user_email, user_picture, user_picture_big;
    AQuery aQuery;

    BackPressClose back_pressed;

    //private View content;

    Button logoutButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        user_nickname = pref.getString("my_nickname", "nothing");
        user_email = pref.getString("my_email", "nothing");
        user_picture = pref.getString("picture_url", "nothing");
        user_picture_big = pref.getString("picture_url_big", "nothing");

        View nav_header_view = navigationView.getHeaderView(0);

        nav_header_user_img =(CircleImageView) nav_header_view.findViewById(R.id.user_img);
        aQuery = new AQuery(this);
        aQuery.id(nav_header_user_img).image(user_picture); // <- profile small image , userProfile.getProfileImagePath() <- big image

        TextView nav_header_user_nickname = (TextView) nav_header_view.findViewById(R.id.user_nickname);
        nav_header_user_nickname.setText(user_nickname);

        TextView nav_header_user_email = (TextView) nav_header_view.findViewById(R.id.user_email);
        nav_header_user_email.setText(user_email);

        //content = getLayoutInflater().inflate(R.layout.content_myinfo_main, null, false);

        //content.setVisibility(View.GONE);

        main_user_img =(CircleImageView) findViewById(R.id.user_img);
        aQuery = new AQuery(this);
        aQuery.id(main_user_img).image(user_picture_big); // <- profile small image , userProfile.getProfileImagePath() <- big image

        TextView main_user_nickname = (TextView) findViewById(R.id.user_nickname);
        main_user_nickname.setText(user_nickname);

        TextView main_user_email = (TextView) findViewById(R.id.user_email);
        main_user_email.setText(user_email);

        logoutButton = (Button) findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Session.getCurrentSession().isOpened()) {
                    requestLogout();
                }
            }
        });

        back_pressed = new BackPressClose(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            back_pressed.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bluetooth) {
            Intent intent = new Intent(MyinfoActivity.this, BluetoothActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_gps) {
            Intent intent = new Intent(MyinfoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_test1) {

        } else if (id == R.id.state) {
            Intent intent = new Intent(MyinfoActivity.this, LockStateActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_myinfo) {

        } else if (id == R.id.nav_homepage) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://219.255.221.94"));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void requestLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyinfoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(MyinfoActivity.this, "logout successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
