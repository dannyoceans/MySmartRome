package progettomagistralemacc.applogin;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import activity.R;
import config.AppConfig;
import config.AppController;
import helper.SQLiteHandler;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle bundle= getIntent().getExtras();
        final String telefono = bundle.getString("telefono");
        final String nome = bundle.getString("nome");
        LatLng pos=bundle.getParcelable("position");
        String _Location="?";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<android.location.Address> listAddresses = geocoder.getFromLocation(pos.latitude, pos.longitude, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                _Location = listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView nomeutenteview= (TextView) findViewById(R.id.tvNumber);
        nomeutenteview.setText(nome);
        TextView v = (TextView) findViewById(R.id.tvNumber1);
        v.setText(telefono);
        TextView pos1 = (TextView) findViewById(R.id.tvNumber5);
        pos1.setText(_Location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
                final EditText edittext = new EditText(getApplicationContext());
                edittext.setTextColor(Color.BLACK);
                edittext.setPadding(20,20,20,20);
                edittext.setWidth(100);
                edittext.setHeight(150);
                alert.setMessage("Inserisci il messaggio");
                alert.setTitle("");

                alert.setView(edittext);


                alert.setPositiveButton("Invia", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String msg = edittext.getText().toString();
                       // Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                        msg= msg.replaceAll(" ","%20");
                        StringRequest strReq = new StringRequest(Method.PUBLIC,
                                AppConfig.URL_PUSHNOTIFY+"?msg="+msg+"&idutente="+telefono, new Response.Listener<String>(){
                            public void onResponse(String response) {
                                Log.d("", "Notify Response: " + response.toString());
                                //      Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                                if(response.equals("OK"));

                                else
                                    Toast.makeText(getApplicationContext(),"Error sending message!",Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("", "Notify Error: " + error.getMessage());
                                Toast.makeText(getApplicationContext(),
                                        error.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }) ;
                        Calendar c = Calendar.getInstance();

                        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String datasend = df.format(c.getTime());
                        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                        int telefonomittente= Integer.parseInt(db.getUserDetails().get("uid"));
                        int telefonodest = Integer.parseInt(telefono);
                       // Toast.makeText(getApplicationContext(),msg+" "+telefonodest+" "+telefonomittente+" "+datasend,Toast.LENGTH_LONG).show();
                        StringRequest strReq2 = new StringRequest(Method.PUBLIC,
                                AppConfig.URL_SENDMSG+"?msg='"+msg.toString()+"'&idM='"+telefonomittente
                                        +"'&idD='"+telefonodest+"'&data='"+datasend.toString().replaceAll(" ","%20")+"'", new Response.Listener<String>(){
                            public void onResponse(String response) {
                                Log.d("", "Notify Response: " + response.toString());
                                //      Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                                if (response.equals("OK"))
                                    Toast.makeText(getApplicationContext(),"Messaggio inviato correttamente",Toast.LENGTH_LONG).show();



                                else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("", "Notify Error: " + error.getMessage());
                                Toast.makeText(getApplicationContext(),
                                        error.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }) ;

                        AppController.getInstance().addToRequestQueue(strReq, "req_update");

                        AppController.getInstance().addToRequestQueue(strReq2, "req_update");
                    }
                });

                alert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();


            }
        });

        imageView = (ImageView) findViewById(R.id.imgProfile);
        new DownloadImage(telefono).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    private class DownloadImage extends AsyncTask<Void, Void, Bitmap>{

        String name;
        public DownloadImage(String name){
            this.name=name;
        }


        @Override
        protected Bitmap doInBackground(Void... params) {

            String url=AppConfig.SERVER_ADDRESS+name+".png";
            try{
                URLConnection connection=new URL(url).openConnection();
                connection.setConnectTimeout(1000*30);
                connection.setReadTimeout(1000*30);

                return BitmapFactory.decodeStream((InputStream) connection.getContent(),null,null);

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }




        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap !=null){

                //tasto dowload premto
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
