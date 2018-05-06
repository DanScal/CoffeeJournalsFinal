package danscal.coffeejournals;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapViewFragment extends  Fragment /*implements OnMapReadyCallback*/ {
    private static final String TAG = "MapViewFragment";

    public String lat(String s){
        int a=s.length();
        int b=s.indexOf(",");
        return s.substring(b+1,a);

    }
    public String lon(String s){
        int a=s.length();
        int b=s.indexOf(",");
        return s.substring(0,b-1);

    }

    List<CoffeeShop> shops=new ArrayList<>();

    MapView mMapView;
    private GoogleMap googleMap;
    private Activity mActivity;

    private final static int REQUEST_CODE = 18; //any number that is greater than or equal to 0


    ChildEventListener mChildEventListener;
    DatabaseReference mProfileRef = FirebaseDatabase.getInstance().getReference("Profile");
    private GoogleMap mMap;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_view_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mActivity = getActivity();

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkPermissions();




        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {


                mMap=googleMap;

                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){

                }
                mMap.setMyLocationEnabled(true);
                DatabaseReference myRef;
                myRef = FirebaseDatabase.getInstance()
                        .getReference().child("coffee shops");
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        String lat=lat(dataSnapshot.child("location").getValue().toString());
                        String lon=lon(dataSnapshot.child("location").getValue().toString());
                        String vibe=("Vibe: "+dataSnapshot.child("vibe").getValue().toString());
                        String coffee=("Coffee: "+dataSnapshot.child("coffee").getValue().toString());



                        System.out.println(dataSnapshot.child("name").getValue().toString()+lon+" " +lat);
                        Log.d("YourTag", lon);
                        Log.d("YourTag", lat);
                        double latI=Double.parseDouble(lat);
                        double lonI=Double.parseDouble(lon);
                        LatLng newLocation = new LatLng(lonI,latI);

                        mMap.addMarker(new MarkerOptions()
                                .position(newLocation)
                                .title(dataSnapshot.child("name").getValue().toString())
                                .snippet(coffee+" "+vibe));

                        LatLng losA = new LatLng( 34.0522222,-118.2427778);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(losA,10));

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                        String lat=lat(dataSnapshot.child("location").getValue().toString());
                        String lon=lon(dataSnapshot.child("location").getValue().toString());
                        String vibe=("Vibe: "+dataSnapshot.child("vibe").getValue().toString());
                        String coffee=("Coffee: "+dataSnapshot.child("coffee").getValue().toString());

                        Log.d("YourTag", lat);
                        Log.d("YourTag", lon);
                        double latI=Double.parseDouble(lat);
                        double lonI=Double.parseDouble(lon);
                        LatLng newLocation = new LatLng(lonI,latI);

                        mMap.addMarker(new MarkerOptions()
                                .position(newLocation)
                                .title(dataSnapshot.child("name").getValue().toString())
                                .snippet(coffee+" "+vibe));

                        LatLng losA = new LatLng( 34.0522222,-118.2427778);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(losA,10));

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }



        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onStop(){
        if(mChildEventListener != null)
            mProfileRef.removeEventListener(mChildEventListener);
        super.onStop();
    }

    protected void checkPermissions() {
        if(ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.READ_CONTACTS)
                + ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            //show an alert dialogue / popup window with request explinations
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, android.Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, android.Manifest.permission.READ_CONTACTS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    ) {
                //build an alert dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage("Camera, Location, and Write External Storage permissions are required to use this app.");
                builder.setTitle("Please grant these permissions.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(mActivity, new String[]{
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.READ_CONTACTS,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

                    }
                });

                builder.setNegativeButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                //directly request for required permissions
                ActivityCompat.requestPermissions(mActivity, new String[]{
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_CONTACTS,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        }
        else {
            Toast.makeText(getContext(), "permissions already granted", Toast.LENGTH_LONG).show();
        }
    }



}