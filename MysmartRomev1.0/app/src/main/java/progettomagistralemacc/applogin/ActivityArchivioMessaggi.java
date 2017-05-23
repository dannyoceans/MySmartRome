package progettomagistralemacc.applogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import activity.R;
import config.AppConfig;
import config.AppController;

public class ActivityArchivioMessaggi extends AppCompatActivity {
    public String telefonodest;
    public String nome="user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listamex);
        Bundle bundle= getIntent().getExtras();
        telefonodest = bundle.getString("telefono");
        // definisco un ArrayList
        final ArrayList<Person> personList=new ArrayList<Person>(); //lista delle persone che la listview visualizzerà

        StringRequest strReq = new StringRequest(Method.PUBLIC,
                AppConfig.URL_READMSG+"?telefono="+telefonodest, new Response.Listener<String>(){
            public void onResponse(String response) {
                if(response!=null){

                    try {
                        JSONArray jObj1 = new JSONArray(response);
                        Person people[] = new Person[jObj1.length()];
                        for (int i = 0; i < jObj1.length(); i++) {
                            JSONObject j = jObj1.getJSONObject(i);
                            int telefono = j.getInt("idMitt");
                            StringRequest strReq2 = new StringRequest(Method.PUBLIC,
                            AppConfig.URL_GETNOME+"?telefono="+telefono, new Response.Listener<String>(){
                                public void onResponse(String response) {
                                    if(response!=null){
                                        try {
                                            JSONArray jObj1 = new JSONArray(response);

                                            for (int cont = 0; cont < jObj1.length(); cont++) {
                                                JSONObject j = jObj1.getJSONObject(cont);
                                                nome= j.getString("nome").toString()+" "+j.getString("cognome").toString();
                                                   }
                                          } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }


                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(getApplicationContext(),
                                            error.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }) ; // Toast.makeText(getApplicationContext(),nome,Toast.LENGTH_LONG).show();
                            String name = nome;
                            AppController.getInstance().addToRequestQueue(strReq2, "req_update");
                            people[i] = new Person(name, telefono);
                            personList.add(people[i]);


                        }
                      /*  for(int x=0;x<personList.size();x++){
                            Toast.makeText(getApplicationContext(),personList.get(x).getName()+" "+personList.get(x).getTelefono(),Toast.LENGTH_LONG).show();
                        }
*/

                        //Questa è la lista che rappresenta la sorgente dei dati della listview
                        //ogni elemento è una mappa(chiave->valore)
                        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();


                        for (int i = 0; i < personList.size(); i++) {
                            Person p = personList.get(i);// per ogni persona all'inteno della ditta

                            HashMap<String, Object> personMap = new HashMap<String, Object>();//creiamo una mappa di valori
                            personMap.put("name", p.getName()); // per la chiave name,l'informazine sul nome

                            personMap.put("telefono", p.getTelefono()); // per la chiave image, inseriamo la risorsa dell immagine
                            data.add(personMap);  //aggiungiamo la mappa di valori alla sorgente dati
                        }


                        String[] from = {"name","telefono"}; //dai valori contenuti in queste chiavi
                        int[] to = {R.id.personName, R.id.personTel};//agli id delle view

                        //costruzione dell adapter
                        final SimpleAdapter adapter = new SimpleAdapter(
                                getApplicationContext(),
                                data,//sorgente dati
                                R.layout.activity_listamex2, //layout contenente gli id di "to"
                                from,
                                to);
                        ListView listView = (ListView) findViewById(R.id.personListView);
                        //utilizzo dell'adapter
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getItem(position);
                                String name = obj.get("name").toString();
                                String telefono= obj.get("telefono").toString();
                                // When clicked, show a toast with the TextView text
                               /* Intent intent = new Intent(ActivityArchivioMessaggi.this,ChatActivity.class);

                                intent.putExtra("nomemittente",name);
                                intent.putExtra("telefonomitt",telefonodest);
                                intent.putExtra("telefonodest",telefono);
                                //based on item add info to intent
                                startActivity(intent);
*/
                            }


                        });
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }}
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) ;

        AppController.getInstance().addToRequestQueue(strReq, "req_update");








    }
}


