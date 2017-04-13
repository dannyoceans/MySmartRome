package activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.Date;

import config.AppConfig;
import config.AppController;

import static android.content.ContentValues.TAG;

/**
 * Created by Daniele on 08/03/17.
 */

public class Personal_tracker extends AppCompatActivity {
Context context;
   public Personal_tracker(Context c){
       this.context= c;
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
}
