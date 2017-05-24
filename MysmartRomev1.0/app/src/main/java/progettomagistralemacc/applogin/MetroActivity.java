package progettomagistralemacc.applogin;
        import android.Manifest;
        import android.app.ProgressDialog;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.Spinner;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.Volley;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.location.LocationListener;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.MapFragment;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.LatLngBounds;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.location.LocationServices;
        import com.google.firebase.messaging.FirebaseMessaging;


        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;

        import activity.R;
        import config.AppConfig;

public class MetroActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 13;
    private GoogleMap mMap;

    LatLng roma;
    Marker marker;
    ArrayList<Marker> markerList = new ArrayList<Marker>();
    String loc;
    Double lat, lng;
    Bundle savedIS;

    private Spinner spinner1;
    private Spinner spinner2;
    private Button button2;
    private Button attr;
    // Progress dialog
    private ProgressDialog pDialog;


    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    protected Location mLastLocation;
    private LocationRequest mLocationRequest;

    //private boolean mapReady = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro);
        FirebaseMessaging.getInstance().subscribeToTopic(AppConfig.TOPIC_GLOBAL);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);


        spinner1 = (Spinner) findViewById(R.id.spinner1);
        //spinner1.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this,
                R.array.station_array, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);


        spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> dataAdapter2 = ArrayAdapter.createFromResource(this,
                R.array.attractions_array, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);


        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

        attr = (Button) findViewById(R.id.button4);
        attr.setOnClickListener(this);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        buildGoogleApiClient();


        if (savedInstanceState != null) {
            savedIS = savedInstanceState;
        }

    }


    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
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
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //mapReady = true;

    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("Permission not granted", "--->>>>");


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            //return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //startLocationUpdates();
        //while(mLastLocation == null){
        //}

        if (mLastLocation == null) {
            Toast.makeText(this, "No location detected", Toast.LENGTH_LONG).show();
        }


        roma = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(roma).title("Your position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(roma));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //LatLng near = new LatLng(lat, lng);
        //LatLngBounds bounds = new LatLngBounds(roma, roma).including(near);
        //bounds.including(near);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
        //mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(roma,near));

        if(savedIS != null){
            loc = savedIS.getString("loc");
            lat = savedIS.getDouble("lat");
            lng = savedIS.getDouble("lng");
            LatLng nextStation = new LatLng(lat, lng);
            if(marker != null) {
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions().position(nextStation).title(loc).icon(BitmapDescriptorFactory.defaultMarker()));
            marker.showInfoWindow();

            //LatLng near = new LatLng(lat, lng);
            LatLngBounds bounds = new LatLngBounds(roma, roma).including(nextStation);
            //bounds.including(near);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }


    }


    /*protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5)
                .setFastestInterval(4);
        // Request location updates
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }*/

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_LONG).show();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Toast.makeText(this, "Connection failed: ConnectionResult.getErrorCode() = " +
                connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();
    }









    @Override
    public void onClick(View v) {

        if (!pDialog.isShowing())
            pDialog.show();

        if((Button) v == button2){
            String selectedSpinner = spinner1.getSelectedItem().toString();
            String linea = selectedSpinner.substring(selectedSpinner.length() -1);
            String url = "http://nextstationrome.altervista.org/distance.php?linea="+linea+"&lat="+
                    mLastLocation.getLatitude()+"&lng="+mLastLocation.getLongitude();

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                loc = response.getString("loc");
                                lat = response.getDouble("lat");
                                lng = response.getDouble("lng");
                                Toast.makeText(getApplicationContext(),"The nearest station is "+loc,Toast.LENGTH_LONG).show();

                                LatLng nextStation = new LatLng(lat, lng);
                                if(marker != null) {
                                    marker.remove();
                                }
                                marker = mMap.addMarker(new MarkerOptions().position(nextStation).title(loc).icon(BitmapDescriptorFactory.defaultMarker()));
                                marker.showInfoWindow();

                                //LatLng near = new LatLng(lat, lng);
                                LatLngBounds bounds = new LatLngBounds(roma, roma).including(nextStation);
                                //bounds.including(near);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                                //mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(roma,nextStation));



                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(nextStation));
                                //mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error try again",Toast.LENGTH_LONG).show();
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        }
                    });

            Volley.newRequestQueue(this).add(jsonRequest);
        }else if((Button) v == attr){
            //museum, art gallery, park, stadium, zoo, church

            String selectedSpinner2 = spinner2.getSelectedItem().toString();
            String sel = "museum";

            switch (selectedSpinner2){
                case "Museum" :
                    sel = "museum";
                    break;
                case "Art galleries" :
                    sel = "art_gallery";
                    break;
                case "Parks" :
                    sel = "park";
                    break;
                case "Stadiums" :
                    sel = "stadium";
                    break;
                case "Zoos" :
                    sel = "zoo";
                    break;
                case "Churches" :
                    sel = "church";
                    break;
                case "Amusement parks" :
                    sel = "amusement_park";
                    break;
                case "Bars" :
                    sel = "bar";
                    break;
                case "Car rentals" :
                    sel = "car_rental";
                    break;
                case "Embassies" :
                    sel = "embassy";
                    break;
                case "Laundries" :
                    sel = "laundry";
                    break;
                case "Pharmacies" :
                    sel = "pharmacy";
                    break;
                case "Police stations" :
                    sel = "police";
                    break;
                case "Restaturants" :
                    sel = "restaurant";
                    break;
                case "Spas" :
                    sel = "spa";
                    break;
                case "Taxi stands" :
                    sel = "taxi_stand";
                    break;
                default :

            }



            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+mLastLocation.getLatitude()+","
                    +mLastLocation.getLongitude()+"&radius=1000&type="+sel+"&key=AIzaSyDxFgFgou-z90VdXpgRq6wkxxqSQADAV9s";
            System.out.println(url);

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                if (!markerList.isEmpty()){
                                    for (int j = markerList.size()-1; j>=0;j--){
                                        markerList.get(j).remove();
                                        markerList.remove(j);
                                    }
                                }
                                //JSONArray array = new JSONArray();
                                JSONArray array = response.getJSONArray("results");
                                for(int i=0; i< array.length(); i++ ){
                                    JSONObject resObj = array.getJSONObject(i);
                                    JSONObject geoObj = resObj.getJSONObject("geometry");
                                    JSONObject locObj = geoObj.getJSONObject("location");
                                    loc = resObj.getString("name");
                                    lat = locObj.getDouble("lat");
                                    lng = locObj.getDouble("lng");


                                    Toast.makeText(getApplicationContext(),loc,Toast.LENGTH_LONG).show();

                                    LatLng nextAttr = new LatLng(lat, lng);
                                    //if(marker != null) {
                                    //  marker.remove();
                                    //}
                                    Marker tempMarker = mMap.addMarker(new MarkerOptions().position(nextAttr).title(loc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                    //marker.showInfoWindow();
                                    markerList.add(tempMarker);

                                    //LatLng near = new LatLng(lat, lng);
                                    //LatLngBounds bounds = new LatLngBounds(roma, roma).including(nextAttr);
                                    //bounds.including(nextAttr);
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                                    //mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(roma,nextStation));


                                }
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(roma,16));




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error try again",Toast.LENGTH_LONG).show();
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        }
                    });

            Volley.newRequestQueue(this).add(jsonRequest);
        }




    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(lat!=null && lng!=null && loc != null) {
            outState.putDouble("lat", lat);
            outState.putDouble("lng", lng);
            outState.putString("loc", loc);
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }
}