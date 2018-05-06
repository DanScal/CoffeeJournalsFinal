package danscal.coffeejournals;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    MapView mapView;
    MapView mMapView;
    private GoogleMap googleMap;

    ChildEventListener mChildEventListener;
    DatabaseReference mProfileRef = FirebaseDatabase.getInstance().getReference("Profile");
    private GoogleMap mMap;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_view_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap=googleMap;

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



}