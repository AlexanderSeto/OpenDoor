package com.alexanderseto.streetlyfe;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.alexanderseto.streetlyfe.MainActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
//import com.google.android.gms.l
import android.app.FragmentManager;

import java.util.List;


public class ShowerFragment extends Fragment {

    public String category = "showers";



    MapView mMapView;
    private GoogleMap googleMap;


    public static LatLng userLocation;
    public LatLng locationGet(){
        return userLocation;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_bathroom, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        googleMap.setMyLocationEnabled(true);
        // latitude and longitude
        double latitude = 10.385044;
        double longitude = 10.486671;


        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                userLocation = new LatLng(latitude, longitude);
            }
        });

        if (userLocation!=null){
            ParseQuery<ParseObject> locationQuery = ParseQuery.getQuery("Waypoint");
            locationQuery.whereEqualTo("category", category);
            locationQuery.whereNear("location", new ParseGeoPoint(locationGet().latitude, locationGet().longitude));
            locationQuery.setLimit(10);
            locationQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    for (ParseObject parseObject : parseObjects) {
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(parseObject.getParseGeoPoint("location").getLatitude(),
                                                parseObject.getParseGeoPoint("location").getLongitude()))
                                        .title(parseObject.getString("title"))
                                        .snippet(parseObject.getString("category"))

                        );
                    }
                }
            });


            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    //                String temp = marker.getTitle();

                    ParseGeoPoint temp = new ParseGeoPoint(marker.getPosition().latitude, marker.getPosition().longitude);
                    ParseQuery<ParseObject> singleLocationQuery = ParseQuery.getQuery("Waypoint");
                    singleLocationQuery.whereNear("location", temp);
                    singleLocationQuery.setLimit(1);
                    singleLocationQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), ViewComments.class);
                            intent.putExtra("postID", parseObjects.get(0).getObjectId());

                            getActivity().startActivity(intent);
                        }
                    });
//                    singleLocationQuery

                    return true;
                }
            });



            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(this.locationGet()).zoom(12).build();
            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        } else {
            LatLng latLng = new LatLng(37.2, -121.4);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(10).build();
            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }


        // create marker
//        MarkerOptions marker = new MarkerOptions().position(
//                new LatLng(latitude, longitude)).title("Hello Maps");

        // Changing marker icon
//        marker.icon(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
//        googleMap.addMarker(marker);



//
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(this.locationGet()).zoom(12).build();
//        googleMap.moveCamera(CameraUpdateFactory
//                .newCameraPosition(cameraPosition));



        // Perform any camera updates here
        return v;
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
    //
//	public BathroomFragment(){}
//
//    private static View view;
//
//    private static GoogleMap mMap;
//    private static Double latitude, longitude;
//
//
//	@Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//
//        if (container == null) {
//            return null;
//        }
//        view = (RelativeLayout) inflater.inflate(R.layout.fragment_bathroom, container, false);
//        latitude = 26.78;
//        longitude = 72.56;
//        setUpMapIfNeeded();
//        return view;
//
////        View rootView = inflater.inflate(R.layout.fragment_bathroom, container, false);
////
////        return rootView;
//    }
//    public static void setUpMapIfNeeded() {
//        if(mMap == null) {
//            mMap = ((MapFragment) MainActivity.fragmentManager.findFragmentById(R.id.location_map)).getMap();
//        }
//        if(mMap != null) {
//            setUpMap();
//        }
//    }
//    private static void setUpMap() {
//        mMap.setMyLocationEnabled(true);
//        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("test").snippet("test"));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        if (mMap != null)
//            setUpMap();
//
//        if (mMap == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            mMap = ((MapFragment) MainActivity.fragmentManager
//                    .findFragmentById(R.id.location_map)).getMap(); // getMap is deprecated
//            // Check if we were successful in obtaining the map.
//            if (mMap != null)
//                setUpMap();
//        }
//    }
//
//    /**** The mapfragment's id must be removed from the FragmentManager
//     **** or else if the same it is passed on the next time then
//     **** app will crash ****/
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (mMap != null) {
//            MainActivity.fragmentManager.beginTransaction()
//                    .remove(MainActivity.fragmentManager.findFragmentById(R.id.location_map)).commit();
//            mMap = null;
//        }
//    }
    public void setString(String cat) {
        category = cat;
    }
}
