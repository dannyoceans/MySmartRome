package progettomagistralemacc.applogin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;

import activity.GPSTracker;
import activity.LoginActivity;

import activity.R;
import config.AppConfig;
import config.AppController;
import helper.SQLiteHandler;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View mCustomMarkerView;
    private ImageView mMarkerImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        mMarkerImageView = (ImageView) mCustomMarkerView.findViewById(R.id.profile_image);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public String nome;
    public String cognome;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        Intent i=getIntent();

        if(i.getIntExtra("personal",0)==0){
        double mylatitudine=i.getDoubleExtra("latitudine",0);
        double mylongitudine=i.getDoubleExtra("longitudine",0);

        StringRequest strReq = new StringRequest(Method.PUBLIC,
                AppConfig.URL_GETAMICI, new Response.Listener<String>(){
            public void onResponse(String response) {

                try {
                    JSONArray jObj1 = new JSONArray(response);
                    for(int i=0;i<jObj1.length();i++)
                    {
                        JSONObject j=jObj1.getJSONObject(i);
                        final String nome=j.getString("nome")+" "+j.getString("cognome");
                        final String telefono=j.getString("telefono");
                        double latitudine=j.getDouble("latitudine");
                        double longitudine=j.getDouble("longitudine");
                        final LatLng position= new LatLng(latitudine,longitudine);
                     //  mMap.addMarker(new MarkerOptions().position(position).title(telefono)

               //         .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, R.drawable.avatar))));
                        // adding a marker with image from URL using glide image loading library
                        Glide.with(getApplicationContext()).
                                load(AppConfig.ImageUrl+telefono+".png")
                                .asBitmap()
                                .fitCenter()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                        mMap.addMarker(new MarkerOptions()
                                                .position(position).snippet(nome)
                                                .title(telefono)
                                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, bitmap))));
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13f));


                                    }
                                });

                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) ;

        AppController.getInstance().addToRequestQueue(strReq, "req_update");

        LatLng myposition=new LatLng(mylatitudine,mylongitudine);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myposition));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String markerName = marker.getTitle();
                String nome= marker.getSnippet();

                Intent i=new Intent(MapsActivity.this,ProfileActivity.class);
                i.putExtra("position",marker.getPosition());
                i.putExtra("nome",nome);
                i.putExtra("telefono",markerName);
                startActivity(i);

            }
        });
    }else{

            // SqLite database handler
            SQLiteHandler db;
            db=new SQLiteHandler(getApplicationContext());
           HashMap<String, String> user = db.getUserDetails();
            String telefono = user.get("uid");//uid è il telefono nel sqlite porcodio
            String nome = user.get("name");
            String cognome = user.get("email");
          //  Toast.makeText(getApplicationContext(),telefono,Toast.LENGTH_LONG).show();
            sendData(telefono);


        }


    }

    public void sendData(String telefono){
        StringRequest strReq = new StringRequest(Method.PUBLIC,
                AppConfig.URL_PERSONAL+"?telefono='"+telefono+"'", new Response.Listener<String>(){
            public void onResponse(String response) {
                Log.d("", "Login Response: " + response.toString());
                try {
                    JSONArray jObj1= new JSONArray(response);


                    for(int i=0;i<jObj1.length();i++){
                        JSONObject j=jObj1.getJSONObject(i);
                        String data=j.getString("data");
                        double latitudine=j.getDouble("latitudine");
                        double longitudine=j.getDouble("longitudine");
                        String telefono=j.getString("id_utente");

                        LatLng position= new LatLng(latitudine,longitudine);
                        mMap.addMarker(new MarkerOptions().position(position).title(data));


                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                             }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) ;

        AppController.getInstance().addToRequestQueue(strReq, "req_update");

    }



    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {

        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }


    private Bitmap getMarkerBitmapFromView(View view, @DrawableRes int resId) {

        mMarkerImageView.setImageResource(resId);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }


}
