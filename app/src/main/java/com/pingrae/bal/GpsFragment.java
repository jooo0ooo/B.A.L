package com.pingrae.bal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class GpsFragment extends Fragment implements OnMapReadyCallback {

    View rootView;
    MapView mapView;
    double my_longitude = 126.93658499999992, my_latitude = 37.5560909;
    String user_nickname;
    SharedPreferences pref;
    LocationManager locationManager;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gps, container, false);
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        context = container.getContext();

        pref = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        user_nickname = pref.getString("my_nickname", "nothing");

        final LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mOnGPSClick(view);

                try{
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
                }catch(SecurityException ex){
                    Toast.makeText(context, "What the Fuck Shit", Toast.LENGTH_SHORT).show();
                }
            }
        });


        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        return rootView;
    }

    public void mOnGPSClick(View v){
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(context, "어플을 사용하시기 위해 위치 서비스를 켜주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        LatLng my_position = new LatLng(my_latitude, my_longitude);

        Log.d("test", "fuck google혹시몰라 한국어로 인식장애 해결 찡긋_<");

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(my_position);
        if (my_longitude == -157.919177) {
            markerOptions.title("Click the Button below");
            markerOptions.snippet("To find your location");
        } else {
            markerOptions.title(user_nickname);
            markerOptions.snippet("my location");
        }
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(my_position));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d("test", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            //double altitude = location.getAltitude();
            //float accuracy = location.getAccuracy();
            String provider = location.getProvider();

            my_longitude = longitude;
            my_latitude = latitude;

            mapView.getMapAsync(GpsFragment.this);

        }
        public void onProviderDisabled(String provider) {
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };
}
