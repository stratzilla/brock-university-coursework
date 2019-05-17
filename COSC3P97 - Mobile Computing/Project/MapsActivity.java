package com.example.b.bc;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    // database objects and references
    FirebaseDatabase db = FirebaseDatabase.getInstance
            ("https://brockcaster-1512252912138.firebaseio.com");
    DatabaseReference ref = db.getReference();
    private Button messageButton;
    // views/google services
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Circle currPos;
    private GoogleMap mMap;
    // room name
    private String room;

    /*
     * method to handle what happens on activity launch
     *
     * @param savedInstanceState - previous activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        // get location via requests
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000) // 1 second
                .setFastestInterval(16); // blazing fast
        // add listeners to button
        addListeners();
    }

    /*
     * method to add button listeners
     */
    private void addListeners() {
        final Context aContext = this;
        messageButton = findViewById(R.id.enterChat);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(aContext, MessageActivity.class);
                messageIntent.putExtra("extra", room);
                // start chat activity on button press
                startActivity(messageIntent);
            }
        });
    }

    /*
     * method to handle map at map load
     *
     * @param googleMap - the googlemap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // keep zoom focused at one level (intentional)
        mMap.setMinZoomPreference(14.75f);
        mMap.setMaxZoomPreference(18.75f);
        // don't allow panning/pinching/etc
        //mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        // dummy location to Null Island
        LatLng dummyLoc = new LatLng(0, 0);
        // print chatroom circles
        printCircles(mMap);
        // make circle around current location, too
        currPos = mMap.addCircle(new CircleOptions()
                .center(dummyLoc)
                .radius(4)
                .strokeColor(0x500000FF)
                .strokeWidth(5)
                .fillColor(0x350000FF));
    }

    /*
     * method to draw chatroom points and radii on map
     *
     * @param map - the Googlemap
     */
    private void printCircles(GoogleMap map) {
        // for method visibility within anonymous inner class
        final GoogleMap mMap = map;
        // single event listener
        ref.child("rooms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    // get all rooms and instantiate Room objects
                    Rooms r = s.getValue(Rooms.class);
                    // add circles corresponding to room radii
                    mMap.addCircle(new CircleOptions()
                        .center(new LatLng(r.getLat(), r.getLng()))
                        .radius(r.getRadius())
                        .strokeColor(0x25000000)
                        .strokeWidth(5)
                        .fillColor(0x15000000));
                    // add nav markers corresponding to room latlong
                    mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                .HUE_AZURE))
                        .position(new LatLng(r.getLat(), r.getLng()))
                        .title(r.getName()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /*
     * method to handle events where a connection is established
     *
     * @param bundle - bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        // AS yells at me if I don't have this
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // get last known location
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // request updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        // handle new location
        handleNewLocation(location);
    }

    /*
     * method to handle events where location services are paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    /*
     * method to handle events where it was paused but is no longer
     */
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    /*
     * method to handle new location on location update
     *
     * @param location - the new location
     */
    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    /*
     * method to handle new location (ie. update camera viewport)
     *
     * @param loc - the new location
     */
    private void handleNewLocation(Location loc) {
        // get latlong of new location
        LatLng newLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
        // instantiate a new camera position based on new location
        CameraPosition cP = new CameraPosition.Builder().target(newLocation).build();
        // position map to new position
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cP));
        // change position of blue circle to new location
        currPos.setCenter(newLocation);
        // check if in room
        checkIfInRoom(loc);
    }

    /*
     * method to check if user is within range of room
     *
     * @param loca - location of user
     */
    private void checkIfInRoom(Location loca) {
        // for variable visibility in anonymous inner class
        final Location loc = loca;
        // single event for fb
        ref.child("rooms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean inRoomFlag = false; // initially not in range
                for (DataSnapshot s : snapshot.getChildren()) {
                    // get room from snapshot
                    Rooms r = s.getValue(Rooms.class);
                    // find distance between room and user
                    double distance = distanceFromChat(r.getLat(), r.getLng(),
                            loc.getLatitude(), loc.getLongitude());
                    /*
                     * don't forget to account for radius
                     * and radius of user (2)
                     */
                    if (distance <= r.getRadius() + 2) {
                        // if within bounds, set flag -> true
                        inRoomFlag = true;
                        // find room of closest chat
                        room = r.getName();
                    }
                }
                // if in room
                if (inRoomFlag) {
                    // allow entry
                    messageButton.setEnabled(true);
                } else {
                    // otherwise keep declining it
                    messageButton.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /*
     * implementation of haversine formula to find distance
     * between two points on Earth in kilometers
     *
     * @params - lat/long pairs of the two points
     * @return - the distance between the two points
     */
    private int distanceFromChat(double lata, double lona, double latb, double lonb) {
        int earthRadius = 6371; // earth's radius (cool!)
        double dLat = degreeToRadian(latb - lata);
        double dLon = degreeToRadian(lonb - lona);
        // haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(degreeToRadian(lata)) * Math.cos(degreeToRadian(latb))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (int) ((earthRadius * c) * 1000); // don't need much precision
    }

    /*
     * method to turn degrees into radians
     *
     * @param in - the degree to "radify"
     * @return - the radians converted from degrees
     */
    private double degreeToRadian(double in) {
        return in * (Math.PI / 180);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
}
