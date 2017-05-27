package progettomagistralemacc.applogin;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;

import activity.GPSTracker;
import activity.R;
import config.AppConfig;
import config.AppController;
import helper.SQLiteHandler;


public class NotificationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View mCustomMarkerView;
    private ImageView mMarkerImageView;
    Double lat;
    Double lng;
    String body;
    String title;

    private static final String TAG = NotificationActivity.class.getSimpleName();
        private BroadcastReceiver mRegistrationBroadcastReceiver;
        private TextView txtRegId, txtMessage;
        GPSTracker gps;
        int personal_traker=0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);
            Bundle bundle = getIntent().getExtras();
             lat = bundle.getDouble("lat");
             lng = bundle.getDouble("lng");
            body = bundle.getString("body");
            title = bundle.getString("title");

            mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
            mMarkerImageView = (ImageView) mCustomMarkerView.findViewById(R.id.profile_image);


            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            //txtRegId = (TextView) findViewById(R.id.txt_reg_id);
            //txtMessage = (TextView) findViewById(R.id.txt_push_message);

           /* mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // checking for type intent filter
                    if (intent.getAction().equals(AppConfig.REGISTRATION_COMPLETE)) {
                        // gcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                        FirebaseMessaging.getInstance().subscribeToTopic(AppConfig.TOPIC_GLOBAL);
                        String message = intent.getStringExtra("message");
                        txtMessage.setText(message);

                    } else if (intent.getAction().equals(AppConfig.PUSH_NOTIFICATION)) {
                        // new push notification is received

                        String message = intent.getStringExtra("message");

                        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                        txtMessage.setText(message);
                    }
                }
            };*/

        }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gps = new GPSTracker(NotificationActivity.this);
        mMap = googleMap;
        if(gps.canGetLocation()) {
            personal_traker = 0;
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();


            LatLng myposition2=new LatLng(latitude,longitude);
            mMap.addMarker(new MarkerOptions().position(myposition2).title("You are Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();

            mMap.moveCamera(CameraUpdateFactory.newLatLng(myposition2));
        }


            LatLng myposition=new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(myposition).title(title).snippet(body)).showInfoWindow();

            mMap.moveCamera(CameraUpdateFactory.newLatLng(myposition));
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);






    }
        // Fetches reg id from shared preferences
        // and displays on the screen


        @Override
        protected void onResume() {
            super.onResume();

            // register GCM registration complete receiver
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(AppConfig.REGISTRATION_COMPLETE));

            // register new push message receiver
            // by doing this, the activity will be notified each time a new message arrives
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(AppConfig.PUSH_NOTIFICATION));

            // clear the notification area when the app is opened
            NotificationUtils.clearNotifications(getApplicationContext());
        }

        @Override
        protected void onPause() {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            super.onPause();
        }
    }