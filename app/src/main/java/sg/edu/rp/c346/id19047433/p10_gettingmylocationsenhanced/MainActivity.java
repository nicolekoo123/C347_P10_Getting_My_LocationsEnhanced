package sg.edu.rp.c346.id19047433.p10_gettingmylocationsenhanced;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnStartDetector, btnStopDetector, btnCheckRecords;
    TextView tvLastKnownLocation, tvLatitude, tvLongitude;
    ToggleButton btnMusic;
    private GoogleMap map;
    String folderLocation_I;
    FusedLocationProviderClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartDetector = findViewById(R.id.btnStartDetector);
        btnStopDetector = findViewById(R.id.btnStopDetector);
        btnCheckRecords = findViewById(R.id.btnCheckRecords);
        btnMusic = findViewById(R.id.btnMusic);
        tvLastKnownLocation = findViewById(R.id.tvLastKnownLocation);
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)
                fm.findFragmentById(R.id.map);


        mapFragment.getMapAsync(new OnMapReadyCallback(){
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                LatLng poi_Singapore = new LatLng(1.3521, 103.8198);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(poi_Singapore, 11));
                UiSettings ui = map.getUiSettings();
                ui.setCompassEnabled(true);
                ui.setZoomControlsEnabled(true);

                if (checkPermission() == true) {
                    map.setMyLocationEnabled(true);
                } else {
                    Log.e("GMap - Permission", "GPS access has not been granted");
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }

            }
        });

        //Folder creation
        folderLocation_I = getFilesDir().getAbsolutePath() + "/MyFolder";

        File folder = new File(folderLocation_I);
        if (folder.exists() == false){
            boolean result = folder.mkdir();
            if (result == true){
                Log.d("File Read/Write", "Folder created");
            }
        }
        if (checkPermission() == false){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
        //Show the last known location when I turn on.
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        checkPermission();
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    //String msg = "Latitude: " + location.getLatitude() + "\n" + "Longitude: " + location.getLongitude();
                    tvLatitude.setText("Latitude: " +location.getLatitude());
                    tvLongitude.setText("Longitude: " + location.getLongitude());

                }
                else {
                    String msg = "No Last known Location known";
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnStartDetector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyService2.class);
                startService(i);


            }
        });
        btnStopDetector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyService2.class);
                stopService(i);

            }
        });

        btnCheckRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, checkRecord.class);
                startActivity(intent);

            }
        });

        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnMusic.isChecked() == true){
                    startService(new Intent(MainActivity.this, MyService.class));
                }
                else {
                    stopService(new Intent(MainActivity.this, MyService.class));

                }
            }
        });

    }
    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck_Write = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                && permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED && permissionCheck_Write == PermissionChecker.PERMISSION_GRANTED) {

            return true;
        } else {
            return false;
        }
    }
}