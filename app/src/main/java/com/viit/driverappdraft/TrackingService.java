package com.viit.driverappdraft;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrackingService extends Service {

    public static final String ARG_FIREBASE_URL = "reference_url";
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(), mChildRef;
    public TrackingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        String mChildRefURL = intent.getStringExtra(ARG_FIREBASE_URL);
        String[] mSegments = mChildRefURL.split("/");
        mChildRef = mRootRef.child(mSegments[mSegments.length-2]).child(mSegments[mSegments.length-1]);
        return START_STICKY;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        requestLocationUpdates();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        mChildRef.child("Online").setValue(false);
        Toast.makeText(this, "GPS Tracking Disabled", Toast.LENGTH_SHORT).show();
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permission == PackageManager.PERMISSION_GRANTED){
            client.requestLocationUpdates(request,new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult){
                    Location location = locationResult.getLastLocation();
                    if(location != null) {
                        mChildRef.child("Latitude").setValue(location.getLatitude());
                        mChildRef.child("Longitude").setValue(location.getLongitude());
                    }
                }
            }, null);
        }
    }
}
