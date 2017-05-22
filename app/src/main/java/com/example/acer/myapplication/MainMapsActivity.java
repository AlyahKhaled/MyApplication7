package com.example.acer.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.acer.myapplication.Utility.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static double latitude=24.692474;
    static double longitude=46.687318;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gpsTracker=new GPSTracker(this);

        //check the premision and get the location if not show the GPS is not enabled and go to settings
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (gpsTracker.canGetLocation()) {
                gpsTracker = new GPSTracker(this);
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);

        }
    }


    //leave the page with saving the new location
    public void OnButtonClick(View view){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainMapsActivity.this);
        //alertDialog.setCancelable(false);
        //TITLE, message, the "yes" button and the "No" button
        alertDialog.setTitle("مغادرة الصفحة!")

                .setMessage("هل تريد مغادرة الصفحة؟")

                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // return to invitation Creation Page with the intent loaded with the latlng
                        Intent data = new Intent();
                        data.putExtra("latitude",latitude);
                        data.putExtra("longitude",longitude);
                        setResult(RESULT_OK,data);
                        finish();
                    }
                })

                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.cancel();
                    }
                }).show();

    }

    //retrun without saving anything
    public void OnButtonClickReturn(View view)
    {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainMapsActivity.this);
        //alertDialog.setCancelable(false);
        //TITLE, message, the "yes" button and the "No" button
        alertDialog.setTitle("مغادرة الصفحة")

                .setMessage("هل تريد مغادرة الصفحة من دون حفظ المعلومات؟" )

                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // return to invitation Creation Page without the intent loaded with the latlng

                        mMap.clear();
                        finish();
                    }
                })

                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.cancel();
                    }
                }).show();

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

        final Context context=this;

//        for changing the type of the map
//        mMap.setMapType(googleMap.MAP_TYPE_HYBRID);
//        get the user Location but before need to check of there is permission or not
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;

        }
        //enable the user location
        mMap.setMyLocationEnabled(true);
        //to let the user zooM in and out
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //get the location from location saved before(if not zero) or the one sent with the intent (for update,default 0)
        if(getIntent().getDoubleExtra("latitude",0) == 0 && getIntent().getDoubleExtra("longitude",0) == 0){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        }
        else{
            latitude = getIntent().getDoubleExtra("latitude",0);
            longitude = getIntent().getDoubleExtra("longitude",0);
        }

        Log.i("AppInfo", "Latitude: " + String .valueOf(latitude));
        Log.i("AppInfo", "longitude: " + String .valueOf(longitude));


        //add the marker
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Marker on current location"));

        //change the location of the camera for the place of the location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 11));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10, 10), 14));

        //this method will be called every time the map is Clicked
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //remove the previous marker
                mMap.clear();

                //add the marker
                mMap.addMarker(new MarkerOptions().position(latLng).title("المكان المحدد"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

                latitude=latLng.latitude;
                longitude=latLng.longitude;
                Toast.makeText(context,latLng.latitude + "-" + latLng.longitude, Toast.LENGTH_LONG);

            }
        });

    }
}
