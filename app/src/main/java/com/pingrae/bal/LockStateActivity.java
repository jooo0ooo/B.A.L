package com.pingrae.bal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class LockStateActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //for_bt
    Handler h;
    private static final String TAG = "bluetooth2";
    final int RECIEVE_MESSAGE = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();
    Boolean flag = Boolean.TRUE;
    Boolean flag2 = Boolean.FALSE;
    private ConnectedThread mConnectedThread;
    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address;
    //

    CircleImageView nav_header_user_img;
    String user_nickname, user_email, user_picture;
    AQuery aQuery;

    BackPressClose back_pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockstate_main);
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

        View nav_header_view = navigationView.getHeaderView(0);

        nav_header_user_img =(CircleImageView) nav_header_view.findViewById(R.id.user_img);
        aQuery = new AQuery(this);
        aQuery.id(nav_header_user_img).image(user_picture); // <- profile small image , userProfile.getProfileImagePath() <- big image

        TextView nav_header_user_nickname = (TextView) nav_header_view.findViewById(R.id.user_nickname);
        nav_header_user_nickname.setText(user_nickname);

        TextView nav_header_user_email = (TextView) nav_header_view.findViewById(R.id.user_email);
        nav_header_user_email.setText(user_email);

        //for_bt
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        //


        final ImageView lockstate = (ImageView) findViewById(R.id.lock_state);


        lockstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);


                if(pref.getString("lock_state", "nothing").equals("lock")){

                    /*
                    lockstate.setImageResource(R.drawable.unlock_image);

                    pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("lock_state", "unlock");
                    editor.commit();
                    */


                    //
                    if(!btAdapter.isEnabled()){
                        Toast.makeText(LockStateActivity.this, "You Should Turn on the Bluetooth", Toast.LENGTH_SHORT).show();
                    }else{
                        if (flag) {
                            Toast.makeText(LockStateActivity.this, "You Should Connect Our Device", Toast.LENGTH_SHORT).show();
                            Log.d("plz", "l-1");
                        }else{
                            if(flag2) {
                                Toast.makeText(LockStateActivity.this, "You Should Connect Our Device", Toast.LENGTH_SHORT).show();
                                Log.d("plz", "l-2");
                            }else{
                                Log.d("plz", "l-3");
                                mConnectedThread.write("1");
                                Toast.makeText(LockStateActivity.this, "now unlock", Toast.LENGTH_SHORT).show();

                                lockstate.setImageResource(R.drawable.unlock_image);

                                pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("lock_state", "unlock");
                                editor.commit();

                            }
                        }
                    }


                    //

                    //for_bt

                    //


                } else if(pref.getString("lock_state", "nothing").equals("unlock")) {

                    /*
                    lockstate.setImageResource(R.drawable.lock_image);

                    pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("lock_state", "lock");
                    editor.commit();
                    */

                    //

                    if(!btAdapter.isEnabled()){
                        Toast.makeText(LockStateActivity.this, "You Should Turn on the Bluetooth", Toast.LENGTH_SHORT).show();
                    }else{
                        if (flag) {
                            Toast.makeText(LockStateActivity.this, "You Should Connect Our Device", Toast.LENGTH_SHORT).show();
                            Log.d("plz", "ul-1");
                        }else{
                            if(flag2) {
                                Toast.makeText(LockStateActivity.this, "You Should Connect Our Device", Toast.LENGTH_SHORT).show();
                                Log.d("plz", "ul-2");
                            }else{
                                Log.d("plz", "ul-3");
                                mConnectedThread.write("2");
                                Toast.makeText(LockStateActivity.this, "now lock", Toast.LENGTH_SHORT).show();

                                lockstate.setImageResource(R.drawable.lock_image);

                                pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("lock_state", "lock");
                                editor.commit();
                            }
                        }
                    }


                    //

                    //for_bt

                    //


                } else {
                    Toast.makeText(LockStateActivity.this, "Error fuck shit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_pressed = new BackPressClose(this);

    }

    //for_bt
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }
    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.

        //test
        SharedPreferences pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        address = pref.getString("bt_address", "00:00:00:00:00:00");
        //test

        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "....Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        flag = Boolean.FALSE;

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
                flag2 = Boolean.FALSE;
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
                flag2 = Boolean.TRUE;
            }
        }
    }
    //

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
            Intent intent = new Intent(LockStateActivity.this, BluetoothActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_gps) {
            Intent intent = new Intent(LockStateActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_test1) {

        } else if (id == R.id.state) {
        } else if (id == R.id.nav_myinfo) {
            Intent intent = new Intent(LockStateActivity.this, MyinfoActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_homepage) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://219.255.221.94"));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
