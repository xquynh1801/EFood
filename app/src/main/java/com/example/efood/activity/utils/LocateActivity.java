package com.example.efood.activity.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.efood.R;
import com.example.efood.activity.home.HomeActivity;
import com.example.efood.db.UserDB;
import com.example.efood.model.User;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocateActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private GPSTracker gps;
    private MapView map = null;
    private MyLocationNewOverlay myLocationNewOverlay;
    private EditText txtLocation;
    private Button btnSearchLocation, btnSelectLocation;
    private double mylatitude, mylongitude, curlatitude, curlongitude;
    private String endPosition="20.980356447957945;105.78690022230148";
    private UserDB userDB;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Thông tin vị trí");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();

        userDB = new UserDB(getApplicationContext());
        this.user = (User)getIntent().getSerializableExtra("user");

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        gps = new GPSTracker(LocateActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){
            mylatitude = gps.getLatitude();
            mylongitude = gps.getLongitude();
            endPosition = mylatitude+";"+mylongitude;
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        txtLocation = findViewById(R.id.txtLocation);
        btnSearchLocation = findViewById(R.id.btnSearchLocation);
        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(19.0);
        GeoPoint myLocation = new GeoPoint(mylatitude, mylongitude);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(myLocation);
        startMarker.setIcon(getResources().getDrawable(R.drawable.marker));
        map.getOverlays().add(startMarker);

        mapController.setCenter(myLocation);


        Geocoder geocoder = new Geocoder(ctx);
        String theAddress;
        theAddress = getAddressFromLatLongt(geocoder, mylatitude, mylongitude);
        if(theAddress.length()>0) {
            txtLocation.setText(theAddress);
        }else{
            Toast.makeText(getApplicationContext(), "Không lấy được thông tin vị trí", Toast.LENGTH_LONG).show();
        }

        btnSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String theAddress = txtLocation.getText().toString().trim() + ", Việt Nam";
                try {
                    List<Address> addresses = geocoder.getFromLocationName(theAddress, 1);
                    curlatitude = addresses.get(0).getLatitude();
                    curlongitude = addresses.get(0).getLongitude();
                    endPosition = curlatitude+";"+curlongitude;
                    mapController.setCenter(new GeoPoint(curlatitude,curlongitude));
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Không lấy được thông tin vị trí", Toast.LENGTH_LONG).show();
                }
            }
        });

        map.getOverlays().add(new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                startMarker.setPosition(p);
                mapController.setCenter(p);
                String addr = getAddressFromLatLongt(geocoder, p.getLatitude(), p.getLongitude());
                if(addr.length()>0) {
                    endPosition = p.getLatitude()+";"+p.getLongitude();
                    txtLocation.setText(addr);
                }else{
                    Toast.makeText(getApplicationContext(), "Không lấy được thông tin vị trí", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        }));

        btnSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int res = userDB.updateAddress(user, endPosition);
                if(res>0){
                    user.setAddress(endPosition);
                    startActivity(new Intent(LocateActivity.this, HomeActivity.class).putExtra("user", user));
                }else{
                    Toast.makeText(getApplicationContext(), "Không lưu được vị trí", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String getAddressFromLatLongt(Geocoder geocoder, double lat, double longt){
        String theAddress;
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, longt, 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                int n = address.getMaxAddressLineIndex();
                for (int i=0; i<=n; i++) {
                    if (i!=0)
                        sb.append(", ");
                    sb.append(address.getAddressLine(i));
                }
                theAddress = sb.toString();
            } else {
                theAddress = null;
            }
        } catch (IOException e) {
            theAddress = null;
        }
        if (theAddress != null) {
            return theAddress;
        } else {
            return "";
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();

                // Call account fragment
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("user",this.user);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
