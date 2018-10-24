package com.ibbumobile;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, LocationListener {

    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationClient;
    View mapView;

    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (googleServiceAvailable()) {
            setContentView(R.layout.activity_map);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


            initMap();
//            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            // no google map contentview
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }
            }

            ;
        };


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.mapTypeNone:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;

            case R.id.mapTypeNormal:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;

            case R.id.mapTypeTerrain:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;

            case R.id.mapTypeSatellite:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.mapTypeHybrid:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initMap() {
        // MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
    }

    public boolean googleServiceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to play service", Toast.LENGTH_LONG);
        }
        return false;
    }

    private void gotoLocationZoom(double lat, double lon, float zoom) {

        LatLng ll = new LatLng(lat, lon);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(mGoogleMap.MAP_TYPE_HYBRID);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;


        gotoLocationZoom(9.0691721, 6.570296, 18.0f);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //        int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//        }
//        mGoogleMap.setMyLocationEnabled(true);
//        mGoogleMap.setOnMyLocationButtonClickListener(this);
//        mGoogleMap.setOnMyLocationClickListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mGoogleMap.setBuildingsEnabled(true);

        mGoogleMap.addMarker(getMarker("Hostel A", 9.073297, 6.569160, 0, "Females Hostel"));
        mGoogleMap.addMarker(getMarker("Hostel B", 9.072618, 6.568579, 0, "Male Hostel"));
        mGoogleMap.addMarker(getMarker("Hostel C", 9.072017, 6.569353, 0, "Females Hostel"));
        mGoogleMap.addMarker(getMarker("Central Mosque", 9.072942, 6.571705, 0, "University Central Mosque"));
        mGoogleMap.addMarker(getMarker("Auditorium", 9.069348, 6.569579, 0, "University Main Auditorium"));
        mGoogleMap.addMarker(getMarker("Abubakar Gimba Library", 9.069178, 6.570860, 0, "University Library"));
        mGoogleMap.addMarker(getMarker("Computer Science Department", 9.067188, 6.571754, 0, "Department of Computer Science"));

        mGoogleMap.addMarker(getMarker("ICT Center", 9.068196, 6.573010, 0, "Information and Comunication Center"));
        mGoogleMap.addMarker(getMarker("Faculty of Agriculture B", 9.070331, 6.573166, 0, "Male Hostel"));
        mGoogleMap.addMarker(getMarker("Mathematics Department", 9.070331, 6.573166, 0, "Mathematics Department"));
        mGoogleMap.addMarker(getMarker("SLT Hall", 9.065625, 6.570905, 0, "Science Lecture Theatre"));
        mGoogleMap.addMarker(getMarker("Department of Biology", 9.065506, 6.571242, 0, "Biological Science Department"));
        mGoogleMap.addMarker(getMarker("Department of Physics", 9.065103, 6.570712, 0, "Physics Department"));
        mGoogleMap.addMarker(getMarker("Department of Chemistry", 9.065350, 6.570425, 0, "Chemistry "));

        mGoogleMap.addMarker(getMarker("Department of Geography", 9.065728, 6.570422, 0, "Geography"));
        mGoogleMap.addMarker(getMarker("SUG Secretariate", 9.065363, 6.570757, 0, "Student Union Government Secretariate"));
        mGoogleMap.addMarker(getMarker("NANISS Secretariate", 9.065431, 6.570683, 0, "NANNIS"));
        mGoogleMap.addMarker(getMarker("Twin Lecture Theatre B", 9.063829, 6.571018, 0, "TLT B"));
        mGoogleMap.addMarker(getMarker("Twin Lecture Theatre A", 9.063697, 6.571199, 0, "TLT A"));
        mGoogleMap.addMarker(getMarker("Cafteria A", 9.065742, 6.571146, 0, "Cafteria"));
        mGoogleMap.addMarker(getMarker("Cafteria B", 9.063249, 6.571578, 0, "h "));

        mGoogleMap.addMarker(getMarker("MLT", 9.064336, 6.572491, 0, "Modupe Folorunsho Alakija Lecture Theatre"));
        mGoogleMap.addMarker(getMarker("Department of Political Science", 9.064622, 6.572233, 0, "Student Union Government Secretariate"));
        mGoogleMap.addMarker(getMarker("Department of Business Administration", 9.064139, 6.572819, 0, "NANNIS"));
        mGoogleMap.addMarker(getMarker("Department of Economics", 9.064303, 6.571959, 0, "TLT B"));
        mGoogleMap.addMarker(getMarker("Twin Lecture Theatre A", 9.063697, 6.571199, 0, "TLT A"));
        mGoogleMap.addMarker(getMarker("FEA", 9.066863, 6.574582, 0, "Cafteria"));
        mGoogleMap.addMarker(getMarker("NLT A", 9.065542, 6.574512, 0, "h "));
        mGoogleMap.addMarker(getMarker("TEMP Admin", 9.060137, 6.573005, 0, "h "));
        mGoogleMap.addMarker(getMarker("Hostel C", 9.071996, 6.569342, 0, "h "));
        mGoogleMap.addMarker(getMarker("Senate Building", 9.067705, 6.570423, 0, "h "));
        mGoogleMap.addMarker(getMarker("ATM",  9.066648, 6.572073, 0, "h "));


    }

    private MarkerOptions getMarker(String title, double lat, double lng, int icon, String snippet) {

        return new MarkerOptions()
                .title(title)
                .position(new LatLng(lat, lng))
                .snippet(snippet);
        //.icon(BitmapDescriptorFactory.fromResource(icon));

    }


   /* private void geoLocate(View view) throws IOException {

        String location = null;

        Geocoder geocoder = new Geocoder(this);
        List<Address> list = geocoder.getFromLocationName(location, 1);
        Address address = list.get(0);
        String locality = address.getLocality();

        double lat = address.getLatitude();
        double lng = address.getLongitude();
        gotoLocationZoom(lat, lng, 15);


    }*/

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(this, "Cant get current locaton", Toast.LENGTH_LONG).show();
        } else {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mGoogleMap.animateCamera(update);

        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}