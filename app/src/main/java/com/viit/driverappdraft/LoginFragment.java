package com.viit.driverappdraft;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class LoginFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final int PERMISSION_REQUEST = 100;
    public static final String ARG_FIREBASE_URL = "reference_url";

    Button mLoginButton, mLogoutButton;
    Spinner mSpinnerRoute, mSpinnerDriver;
    ArrayList<String> mRoutesArray;
    ArrayList<String> mDriversRoute1Array;
    ArrayList<String> mDriversRoute2Array;
    ArrayList<String> mDriversRoute3Array;

    ArrayAdapter<String> mRoutesAdapter, mDriverAdapter;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRootref = mDatabase.getReference();
    public DatabaseReference mChildRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mRoutesArray = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.routes_array)));
        mDriversRoute1Array = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.drivers_route1_array)));
        mDriversRoute2Array = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.drivers_route2_array)));
        mDriversRoute3Array = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.drivers_route3_array)));
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginButton = v.findViewById(R.id.confirm_bus);
        mLogoutButton = v.findViewById(R.id.stop_bus);

        mSpinnerRoute = v.findViewById(R.id.spinner_routes);
        mSpinnerDriver = v.findViewById(R.id.spinner_drivers);
        mSpinnerRoute.setPrompt(getString(R.string.route_select_prompt));
        mSpinnerDriver.setPrompt(getString(R.string.driver_select_prompt));

        mRoutesAdapter = new ArrayAdapter<>(Objects.requireNonNull(this.getActivity()), R.layout.spinner_ui, mRoutesArray);
        mRoutesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinnerRoute.setAdapter(mRoutesAdapter);
        mSpinnerRoute.setOnItemSelectedListener(this);
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.spinner_routes:
                String mSelectedRoute = parent.getItemAtPosition(position).toString();
                switch (mSelectedRoute) {
                    case "Route 1":
                        mDriverAdapter = new ArrayAdapter<>(Objects.requireNonNull(this.getActivity()), R.layout.spinner_ui, mDriversRoute1Array);
                        mDriverAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        mSpinnerDriver.setAdapter(mDriverAdapter);
                        mSpinnerDriver.setOnItemSelectedListener(this);
                        break;
                    case "Route 2":
                        mDriverAdapter = new ArrayAdapter<>(Objects.requireNonNull(this.getActivity()), R.layout.spinner_ui, mDriversRoute2Array);
                        mDriverAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        mSpinnerDriver.setAdapter(mDriverAdapter);
                        mSpinnerDriver.setOnItemSelectedListener(this);
                        break;
                    case "Route 3":
                        mDriverAdapter = new ArrayAdapter<>(Objects.requireNonNull(this.getActivity()), R.layout.spinner_ui, mDriversRoute3Array);
                        mDriverAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        mSpinnerDriver.setAdapter(mDriverAdapter);
                        mSpinnerDriver.setOnItemSelectedListener(this);
                        break;
                }
                break;
            case R.id.spinner_drivers:
                mLoginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectChildReference();
                    }
                });
                mLogoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent serviceIntent = new Intent(getActivity(), TrackingService.class);
                        Objects.requireNonNull(getActivity()).stopService(serviceIntent);
                        Log.i("GPSservice", "service stopped");
                    }
                });
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void selectChildReference(){
        switch(mSpinnerRoute.getSelectedItem().toString()){
            case "Route 1":
                switch (mSpinnerDriver.getSelectedItem().toString()){
                    case "Driver 1":
                        mChildRef = mRootref.child("Route_1").child("Driver_1");
                        break;
                    case "Driver 2":
                        mChildRef = mRootref.child("Route_1").child("Driver_2");
                        break;
                    case "Driver 3":
                        mChildRef = mRootref.child("Route_1").child("Driver_3");
                        break;
                }
                break;
            case "Route 2":
                switch (mSpinnerDriver.getSelectedItem().toString()){
                    case "Driver 1":
                        mChildRef = mRootref.child("Route_2").child("Driver_1");
                        break;
                    case "Driver 2":
                        mChildRef = mRootref.child("Route_2").child("Driver_2");
                        break;
                }
                break;
            case "Route 3":
                switch (mSpinnerDriver.getSelectedItem().toString()){
                    case "Driver 1":
                        mChildRef = mRootref.child("Route_3").child("Driver_1");
                        break;
                    case "Driver 2":
                        mChildRef = mRootref.child("Route_3").child("Driver_2");
                        break;
                    case "Driver 3":
                        mChildRef = mRootref.child("Route_3").child("Driver_3");
                        break;
                    case "Driver 4":
                        mChildRef = mRootref.child("Route_3").child("Driver_4");
                        break;
                }
                break;
        }

        mChildRef.child("Online").setValue(true);

        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getActivity().finish();
        }

        int permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(permission == PackageManager.PERMISSION_GRANTED){
            startTrackerService();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
        }
    }

    private void startTrackerService(){
        Intent serviceIntent = new Intent(getActivity(), TrackingService.class);
        serviceIntent.putExtra(ARG_FIREBASE_URL, mChildRef.toString());
        Objects.requireNonNull(getActivity()).startService(serviceIntent);
        Toast.makeText(getActivity(), "GPS tracking enabled", Toast.LENGTH_SHORT).show();
        //getActivity().finish();
    }
}
