package com.example.pics;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {


    private FusedLocationProviderClient fusedLocationClient;
    private View popup = null;
    boolean canLocate = false;
    private GoogleMap mMap;
    final public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Activity act = this;
    Bitmap imageBitmap;
    String numStr = "0";
    int numInt = 0;
    double lat = 6, lng = 9;

    Bitmap thePics[] = new Bitmap[100];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Say no and you lose out big time!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    // Permissions not granted
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                }
                else {
                    LaunchCamera();
                }
            }
        });
    }

    void LaunchCamera(){
        fusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        Log.d("lat:", "is" + location.getLatitude());
                        Log.d("lng:", "is" + location.getLongitude());
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                    }
                }
            });
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    void DropMarker(){

        LatLng latLng;

        latLng = new LatLng(lat, lng);
        mMap.setOnMarkerClickListener(this);
        numStr = String.valueOf(numInt);
        MarkerOptions options =  new MarkerOptions()
                .snippet("mark")
                .position(latLng)
                .title(numStr)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            numInt++;

            Log.d("hmm", "title" + numInt);
            thePics[numInt] = imageBitmap;
            DropMarker();
            //imageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    canLocate = true;

                    // Launch camera
                    LaunchCamera();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String titleStr = marker.getTitle();
        int titleInt = Integer.parseInt(titleStr);

        if (marker.getSnippet().equals("mark")) {
            MarkerOptions options = new MarkerOptions()
                    .title(titleStr)
                    .snippet("icon")
                    .position(marker.getPosition())
                    .icon(BitmapDescriptorFactory.fromBitmap(thePics[titleInt]));

            marker.remove();
            mMap.addMarker(options);
        } else {
            MarkerOptions options = new MarkerOptions()
                    .snippet("mark")
                    .title(titleStr)
                    .position(marker.getPosition())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            marker.remove();
            mMap.addMarker(options);
        }



        return false;
    }
}
