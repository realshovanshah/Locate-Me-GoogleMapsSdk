package com.realshovanshah.locateme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.realshovanshah.locateme.models.User;
import com.realshovanshah.locateme.models.UserLocation;

import java.util.ArrayList;

import static com.realshovanshah.locateme.utils.Constants.MAPVIEW_BUNDLE_KEY;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private View mapView;
    private ArrayList<User> mUserList = new ArrayList<>();
    private ArrayList<UserLocation> mUserLocations = new ArrayList<>();
    private FirebaseFirestore db;
    private static final String TAG = "MapsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = view.findViewById(R.id.map_view);
        db = FirebaseFirestore.getInstance();
        getAllUsers();

//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps_fragment);
//        mapView = mapFragment.getView();

        initGoogleMaps(savedInstanceState);

        return view;
    }

//    private void getAllUsers() {
//        CollectionReference userRef = db.collection("users");
//        userRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (queryDocumentSnapshots != null){
//                    mUserList.clear();
//                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                        User user = documentSnapshot.toObject(User.class);
//                        mUserList.add(user);
//                        Log.d(TAG, "onSuccess: "+user.getId());
//                    }
//                    Log.d(TAG, "onSuccess: " + mUserList);
//                }
//            }
//        });
//        getUserLocation();
//    }
//
//    private void getUserLocation(){
//        Log.d(TAG, "getUserLocation: "+mUserList);
//        for (User user: mUserList){
//            DocumentReference locationRef = db.collection("UserLocation").document(user.getId());
//            locationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if(task.isSuccessful()){
//                        if (task.getResult().toObject(UserLocation.class) != null){
//                            mUserLocations.add(task.getResult().toObject(UserLocation.class));
//                        }
//                    }else {
//                        Log.d(TAG, "onComplete: Task Failed");
//                    }
//                }
//            });
//        }
//        Log.d(TAG, "getUserLocation: "+ mUserLocations);
//    }

    private void getAllUsers() {
        CollectionReference userRef = db.collection("users");
        userRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null){
                    mUserList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        User user = documentSnapshot.toObject(User.class);
                        mUserList.add(user);
                        String id = user.getId();
                        Log.d(TAG, "onSuccess: "+user.getId());
                        getUserLocation(id);
                    }
                    Log.d(TAG, "onSuccess: "+mUserLocations + mUserList);
                }
            }
        });
    }

    private void getUserLocation(String id){
        DocumentReference locationRef = db.collection("UserLocation").document(id);
        locationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if (task.getResult().toObject(UserLocation.class) != null){
                        mUserLocations.add(task.getResult().toObject(UserLocation.class));
                    }
                }else {
                    Log.d(TAG, "onComplete: Task Failed");
                }
            }
        });
    }

    // MAPS SDK METHODS NEEDED FOR MAP VIEW

    private void initGoogleMaps(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
//        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null){
//
//            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
//            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//// position on right bottom
//            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//            rlp.setMargins(0, 180, 180, 0);
//        }
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}