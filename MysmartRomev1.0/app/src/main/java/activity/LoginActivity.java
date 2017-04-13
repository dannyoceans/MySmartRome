package activity;

/**
 * Created by francesconi on 01/03/2017.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import config.AppConfig;
import config.AppController;
import helper.SessionManager;
import helper.SQLiteHandler;
import progettomagistralemacc.applogin.Main2Activity;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputPhone;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private static final int MY_PERMISSIONS_REQUEST_READ_GPS =10;
    GPSTracker gps;
    double latitude=0;
    double longitude=0;
    String telefono;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputPhone = (EditText) findViewById(R.id.phone);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                telefono = inputPhone.getText().toString();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!telefono.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(telefono, password);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public String getTelefono(){
        return  telefono;
    }

    private void updatePosition(final String telefono, final double latitudine, final double longitudine ){


    StringRequest strReq = new StringRequest(Method.PUBLIC,
            AppConfig.URL_UPDATEPOSITION+"?telefono='"+telefono+"'&latitudine='"+latitudine+"'&longitudine='"+longitudine+"'", new Response.Listener<String>(){
    public void onResponse(String response) {
        Log.d(TAG, "Login Response: " + response.toString());
        //      Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
        hideDialog();
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
            hideDialog();
        }
    }) ;

    AppController.getInstance().addToRequestQueue(strReq, "req_update");

}
    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();
     //   Toast.makeText(getApplicationContext(),username,Toast.LENGTH_LONG).show();
        StringRequest strReq = new StringRequest(Method.PUBLIC,
                AppConfig.URL_LOGIN+"?username='"+username+"'&password='"+password+"'", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
          //      Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                hideDialog();
                try {
                    JSONArray jObj1= new JSONArray(response);

                          //boolean error = jObj1.;
                    // Check for error node in json
                 //  if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj1.getString(0);

                       JSONObject user = jObj1.getJSONObject(0);

                    if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(LoginActivity.this,
                                new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
                                MY_PERMISSIONS_REQUEST_READ_GPS);

                    } else {
                        //readGps();
                        gps = new GPSTracker(LoginActivity.this);

                        // check if GPS enabled
                        if(gps.canGetLocation()){

                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();

                            // \n is for new line
                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        }else{
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            gps.showSettingsAlert();
                        }

                    }


                        String name = user.getString("nome");
                        String cognome = user.getString("cognome");
                        String password = user
                                .getString("password");
                        String telefono = user.getString("telefono");


                    updatePosition(telefono,latitude,longitude);
                        double latdb=user.getDouble("latitudine");
                        double longdb=user.getDouble("longitudine");

                        // Inserting row in users table
                      db.addUser(name, cognome,telefono,latdb,longdb);

               //   Toast.makeText(getApplicationContext(), name +" "+Latitudine+" "+inputEmail.getText().toString()+" "+inputPassword.getText().toString(), Toast.LENGTH_LONG).show();
                        if(telefono.equals(inputPhone.getText().toString()) && password.equals(inputPassword.getText().toString())) {
                            // Launch main activity
                            Intent intent = new Intent(LoginActivity.this,
                                    Main2Activity.class);
                            intent.putExtra("telefono",telefono);
                            startActivity(intent);
                            finish();
                            //  } else {
                            // Error in login. Get the error message
                            //  String errorMsg = jObj.getString("error");

                        }
                 //  }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Invalid credentials! Please try again.", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}