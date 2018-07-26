package com.pingrae.bal;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import static android.content.Context.MODE_PRIVATE;

public class LockStateFragment extends Fragment {

    SharedPreferences pref;
    private Context context;
    private static final String TAG = "bluetooth2";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();
    Boolean flag = Boolean.TRUE;
    Boolean flag2 = Boolean.FALSE;
    private ConnectedThread mConnectedThread;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address;
    Thread mWorkerThread = null;
    byte[] readBuffer;
    int readBufferPosition;
    InputStream mInputStream = null;
    String mStrDelimiter = "\n";
    char mCharDelimiter =  '\n';

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final ViewGroup rootGroup =(ViewGroup)inflater.inflate(R.layout.fragment_lock_state,container,false);
        pref = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        context = container.getContext();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        Toast.makeText(context, "자물쇠를 길게 누르시면 이전 화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show();
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels * 7 / 10;
        int height = dm.heightPixels * 5 / 10;
        final ImageView lockstate = (ImageView) rootGroup.findViewById(R.id.lock_state);
        lockstate.getLayoutParams().width = width;
        lockstate.getLayoutParams().height = height;

        lockstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getString("lock_state", "nothing").equals("lock")){
                    if(!btAdapter.isEnabled()){
                        Toast.makeText(context, "You Should Turn on the Bluetooth", Toast.LENGTH_SHORT).show();
                    }else{
                        if (flag) {
                            Toast.makeText(context, "You Should Connect Our Device", Toast.LENGTH_SHORT).show();
                            Log.d("plz", "l-1");
                        }else{
                            if(flag2) {
                                Toast.makeText(context, "You Should Connect Our Device", Toast.LENGTH_SHORT).show();
                                Log.d("plz", "l-2");
                            }else{
                                Log.d("plz", "l-3");
                                mConnectedThread.write("1");
                                Toast.makeText(context, "now unlock", Toast.LENGTH_SHORT).show();
                                lockstate.setBackgroundResource(R.drawable.unlock_image);
                                pref = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("lock_state", "unlock");
                                editor.commit();
                            }
                        }
                    }
                } else if(pref.getString("lock_state", "nothing").equals("unlock")) {
                    if(!btAdapter.isEnabled()){
                        Toast.makeText(context, "You Should Turn on the Bluetooth", Toast.LENGTH_SHORT).show();
                    }else{
                        if (flag) {
                            Toast.makeText(context, "You Should Connect Our Device", Toast.LENGTH_SHORT).show();
                            Log.d("plz", "ul-1");
                        }else{
                            if(flag2) {
                                Toast.makeText(context, "You Should Connect Our Device", Toast.LENGTH_SHORT).show();
                                Log.d("plz", "ul-2");
                            }else{
                                Log.d("plz", "ul-3");
                                mConnectedThread.write("2");
                                Toast.makeText(context, "now lock", Toast.LENGTH_SHORT).show();
                                lockstate.setBackgroundResource(R.drawable.lock_image);
                                pref = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("lock_state", "lock");
                                editor.commit();
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Error fuck shit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lockstate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Fragment newFragment = new BluetoothConnectFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }
        });


        return  rootGroup;
    }

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
        pref = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        address = pref.getString("bt_address", "00:00:00:00:00:00");
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        if(btAdapter.isEnabled()){
            try {
                btSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
                getActivity().finish();
            }
            btAdapter.cancelDiscovery();
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
            Log.d(TAG, "...Create Socket...");
            flag = Boolean.FALSE;
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        if(btAdapter.isEnabled()){
            try     {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
            }
        }
    }

    private void checkBTState() {
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {

            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(context, title + " - " + message, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] readBuffer = new byte[256];
            int bytes;

            readBufferPosition = 0;

            Log.d("why", "why");
            while (true) {
                try {
                    bytes = mmInStream.read(readBuffer);
                    Log.d("plz~~~", bytes+"");

                    int bytesAvailable = mmInStream.available();
                    if(bytesAvailable >0){
                        byte[] packetBytes = new byte[bytesAvailable];
                        mmInStream.read(packetBytes);

                        for(int i=0;i<bytesAvailable;i++) {

                            byte b = packetBytes[i];
                            if(b == '\n')
                            {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0,
                                        encodedBytes.length);
                                final String recvMessage = new String(encodedBytes, "UTF-8");

                                readBufferPosition = 0;

                                Log.d(TAG, "recv message: " + recvMessage);
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable(){
                                    // 수신된 문자열 데이터에 대한 처리.
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, recvMessage, Toast.LENGTH_LONG).show();
                                    }

                                });
                            }
                            else
                            {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }

                    }
                } catch (IOException e) {
                    break;
                }
            }
        }

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

    void beginListenForData() {
        final Handler handler = new Handler();

        readBufferPosition = 0;
        readBuffer = new byte[1024];
        mWorkerThread = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()) {
                    try {
                        int byteAvailable = mInputStream.available();
                        if(byteAvailable > 0) {
                            byte[] packetBytes = new byte[byteAvailable];
                            mInputStream.read(packetBytes);
                            for(int i=0; i<byteAvailable; i++) {
                                byte b = packetBytes[i];
                                if(b == mCharDelimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable(){
                                        // 수신된 문자열 데이터에 대한 처리.
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                            alertDialogBuilder.setTitle("전송받은 데이터");
                                            alertDialogBuilder.setMessage(data+mStrDelimiter);
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();
                                        }

                                    });
                                }
                                else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }

                    } catch (Exception e) {
                        Toast.makeText(context, "데이터 수신 중 오류가 발생 했습니다.", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                }
            }

        });

    }
}