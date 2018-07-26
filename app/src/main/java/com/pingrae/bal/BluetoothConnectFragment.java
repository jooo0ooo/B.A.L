package com.pingrae.bal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import static android.content.Context.MODE_PRIVATE;

public class BluetoothConnectFragment extends Fragment {

    private static final UUID MY_UUID = UUID.fromString("00001108-0000-1000-8000-00805F9B34FB");
    private Context fragment_context;
    final static int BLUETOOTH_REQUEST_CODE = 100;
    BluetoothAdapter mBluetoothAdapter;
    TextView txtState;
    Button btnSearch;
    CheckBox chkFindme;
    ListView listPaired;
    ListView listDevice;
    SimpleAdapter adapterPaired;
    SimpleAdapter adapterDevice;
    List<Map<String,String>> dataPaired;
    List<Map<String,String>> dataDevice;
    List<BluetoothDevice> bluetoothDevices;
    SharedPreferences pref;
    int selectDevice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final ViewGroup rootGroup =(ViewGroup)inflater.inflate(R.layout.fragment_bluetooth_connect,container,false);
        pref = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        fragment_context = container.getContext();
        txtState = (TextView) rootGroup.findViewById(R.id.txtState);
        btnSearch = (Button) rootGroup.findViewById(R.id.btnSearch);
        listPaired = (ListView) rootGroup.findViewById(R.id.listPaired);
        listDevice = (ListView) rootGroup.findViewById(R.id.listDevice);
        btnSearch.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "RobotoMono-Italic.ttf"));
        dataPaired = new ArrayList<>();
        adapterPaired = new SimpleAdapter(fragment_context, dataPaired, android.R.layout.simple_list_item_2, new String[]{"name","address"}, new int[]{android.R.id.text1, android.R.id.text2});
        listPaired.setAdapter(adapterPaired);
        dataDevice = new ArrayList<>();
        adapterDevice = new SimpleAdapter(fragment_context, dataDevice, android.R.layout.simple_list_item_2, new String[]{"name","address"}, new int[]{android.R.id.text1, android.R.id.text2});
        listDevice.setAdapter(adapterDevice);
        bluetoothDevices = new ArrayList<>();
        selectDevice = -1;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null){
            Toast.makeText(fragment_context, "블루투스를 지원하지 않는 단말기 입니다.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return rootGroup;
        }

        IntentFilter stateFilter = new IntentFilter();
        stateFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(mBluetoothStateReceiver, stateFilter);
        IntentFilter searchFilter = new IntentFilter();
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        searchFilter.addAction(BluetoothDevice.ACTION_FOUND);
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        searchFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        getActivity().registerReceiver(mBluetoothSearchReceiver, searchFilter);
        IntentFilter scanmodeFilter = new IntentFilter();
        scanmodeFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        getActivity().registerReceiver(mBluetoothScanmodeReceiver, scanmodeFilter);

        if(!mBluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BLUETOOTH_REQUEST_CODE);
        }else{
            GetListPairedDevice();
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearch.setEnabled(false);
                if(mBluetoothAdapter.isDiscovering()){
                    mBluetoothAdapter.cancelDiscovery();
                }
                mBluetoothAdapter.startDiscovery();
            }
        });

        listPaired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(fragment_context, "'검색된 목록'을 이용해서 장치와 연결해 주세요", Toast.LENGTH_LONG).show();
            }
        });

        listDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BluetoothDevice device = bluetoothDevices.get(position);
                BluetoothDevice dv = mBluetoothAdapter.getRemoteDevice(device.getAddress());

                try {
                    BluetoothSocket mmSocket = dv.createRfcommSocketToServiceRecord(MY_UUID);
                    mmSocket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                SaveData.connectTodevice = true;
                Log.d("WTF_WTF!!!", device.getAddress());
                Fragment newFragment = new LockStateFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        return  rootGroup;
    }

    BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);

            if(state == BluetoothAdapter.STATE_ON){
                txtState.setText("블루투스 활성화");
            }
            else if(state == BluetoothAdapter.STATE_TURNING_ON){
                txtState.setText("블루투스 활성화 중...");
            }
            else if(state == BluetoothAdapter.STATE_OFF){
                txtState.setText("블루투스 비활성화");
            }
            else if(state == BluetoothAdapter.STATE_TURNING_OFF){
                txtState.setText("블루투스 비활성화 중...");
            }
        }
    };

    BroadcastReceiver mBluetoothSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            switch(action){
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    dataDevice.clear();
                    bluetoothDevices.clear();
                    Toast.makeText(fragment_context, "블루투스 검색 시작", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Map map = new HashMap();
                    map.put("name", device.getName());
                    map.put("address", device.getAddress());
                    dataDevice.add(map);
                    adapterDevice.notifyDataSetChanged();
                    bluetoothDevices.add(device);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    btnSearch.setEnabled(true);
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    BluetoothDevice paired = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(paired.getBondState()==BluetoothDevice.BOND_BONDED){
                        Map map2 = new HashMap();
                        map2.put("name", paired.getName());
                        map2.put("address", paired.getAddress());
                        dataPaired.add(map2);
                        adapterPaired.notifyDataSetChanged();
                        if(selectDevice != -1){
                            bluetoothDevices.remove(selectDevice);
                            dataDevice.remove(selectDevice);
                            adapterDevice.notifyDataSetChanged();
                            selectDevice = -1;
                        }
                    }
                    break;
            }
        }
    };

    BroadcastReceiver mBluetoothScanmodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1);
            switch (state){
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                case BluetoothAdapter.SCAN_MODE_NONE:
                    chkFindme.setChecked(false);
                    chkFindme.setEnabled(true);
                    Toast.makeText(fragment_context, "검색응답 모드 종료", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                    Toast.makeText(fragment_context, "다른 블루투스 기기에서 내 휴대폰을 찾을 수 있습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void mOnBluetoothSearch(View v){
        btnSearch.setEnabled(false);
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    public void GetListPairedDevice(){

        Set<BluetoothDevice> pairedDevice = mBluetoothAdapter.getBondedDevices();
        dataPaired.clear();
        if(pairedDevice.size() > 0){
            for(BluetoothDevice device : pairedDevice){
                Map map = new HashMap();
                map.put("name", device.getName());
                map.put("address", device.getAddress());
                dataPaired.add(map);
            }
        }
        adapterPaired.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case BLUETOOTH_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    GetListPairedDevice();
                }
                else{
                    Toast.makeText(fragment_context, "블루투스를 활성화해야 합니다.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    return;
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mBluetoothStateReceiver);
        getActivity().unregisterReceiver(mBluetoothSearchReceiver);
        getActivity().unregisterReceiver(mBluetoothScanmodeReceiver);
        super.onDestroy();
    }
}