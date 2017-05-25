package progettomagistralemacc.applogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import activity.GPSTracker;
import activity.GPS_Service;
import activity.LoginActivity;
import activity.Personal_tracker;
import activity.R;
import config.AppConfig;
import config.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;
import progettomagistralemacc.applogin.imgProfilo.Main3Activity;

import static android.content.ContentValues.TAG;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView txtName;
    private TextView txtEmail;
    Personal_tracker p;
    private SQLiteHandler db;

    public static String tel2="5";
    public String tel;

    private SessionManager session;
    private static final int MY_PERMISSIONS_REQUEST_READ_GPS =10;
    GPSTracker gps;
    private BroadcastReceiver broadcastReceiver;
    double lon;
    double lat;
    int personal_traker=0;
    boolean thread_running=true;
    String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    /*    FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();


        Thread t = new Thread(new Runnable() {
            FirebaseInstanceIdService service= new FirebaseInstanceIDService();
            @Override
            public void run() {



                while( thread_running){
                    Token= FirebaseInstanceId.getInstance().getToken();
                    if(Token!=null){
                        System.out.println("****DEVICE TOKEN ISSS "+Token);
                        thread_running=false;
                        service.onTokenRefresh();
                    }
                    else{
                        System.out.println("-TOKEN NOT LOADED***************");
                    }
                    try{
                        Thread.sleep(1000);

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        });t.start();*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());


        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        tel= user.get("uid");
        // start service GPS
        Intent i =new Intent(getApplicationContext(),GPS_Service.class);
        startService(i);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Intent i = new Intent(this,Main3Activity.class);
            i.putExtra("telefono",tel);
            startActivity(i);

        } else if (id == R.id.nav_gallery) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
                        MY_PERMISSIONS_REQUEST_READ_GPS);

            } else {

                readGps();

            }


        } /*else if (id == R.id.nav_slideshow) {
            personal_traker=1;
            Intent i=new Intent(this,MapsActivity.class);
            i.putExtra("personal",personal_traker);
            startActivity(i);


        }*/
        else if (id == R.id.attractions) {
            personal_traker=1;
            Intent i=new Intent(this,MetroActivity.class);
            startActivity(i);


        }
        else if ( id==R.id.btnLogout){
            logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void localizza(View v){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
                    MY_PERMISSIONS_REQUEST_READ_GPS);

        } else {

            readGps();

        }


    }

    private void readGps() {


        gps = new GPSTracker(Main2Activity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){
            personal_traker=0;
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Intent i=new Intent(this,MapsActivity.class);

            i.putExtra("latitudine", gps.getLatitude());
            i.putExtra("longitudine", gps.getLongitude());
            i.putExtra("personal",personal_traker);
            startActivity(i);
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        //getAmici();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_GPS :
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    readGps();

                } else {

                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        new AlertDialog.Builder(this).
                                setTitle("Gps permission").
                                setMessage("You need to grant Gps permission to use read" +
                                        " contacts feature. Retry and grant it !").show();
                    } else {
                        new AlertDialog.Builder(this).
                                setTitle("Gps permission denied").
                                setMessage("You denied Gps permission." +
                                        " So, the feature will be disabled. To enable it" +
                                        ", go on settings and " +
                                        "grant read contacts for the application").show();
                    }

                }

                break;
        }
    }
    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        Intent i = new Intent(getApplicationContext(), GPS_Service.class);
        stopService(i);
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Main2Activity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    lon=intent.getDoubleExtra("longitude",0);
                    lat=intent.getDoubleExtra("latitude",0);

                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c.getTime());

                    HashMap<String, String> user = db.getUserDetails();
                    String telefono = user.get("uid");

                    updatePosition(telefono,lat,lon,formattedDate);
                    Toast.makeText(getApplicationContext(),"longitudine="+lon+"latitudine="+lat+"data di oggi="+formattedDate+"telefono="+telefono,Toast.LENGTH_LONG).show();



                }
            };
        }

        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));

    }
    public void updatePosition(final String telefono, final double latitudine, final double longitudine,String data ){


        StringRequest strReq = new StringRequest(Method.PUBLIC,
                AppConfig.URL_TRACKER+"?telefono='"+telefono+"'&latitudine='"+latitudine+"'&longitudine='"+longitudine+"'&data='"+data+"'", new Response.Listener<String>(){
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                //      Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                if(response.equals("OK"));


                else
                    Toast.makeText(getApplicationContext(),"Error update position!",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) ;

        AppController.getInstance().addToRequestQueue(strReq, "req_update");

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

}
