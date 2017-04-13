package progettomagistralemacc.applogin;
import activity.chat.ChatArrayAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import activity.R;
import activity.chat.ChatMessage;
import config.AppConfig;
import config.AppController;

public class ChatActivity extends Activity {
    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;
    public final ArrayList<String> msg = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        final String telefonomittente = bundle.getString("telefonodest");
        final String telefonodest = bundle.getString("telefonomitt");


        setContentView(R.layout.activity_chat);

        final StringRequest strReq = new StringRequest(Method.PUBLIC,
                AppConfig.URL_GETMSG+"?telefono="+telefonodest+"&mittente="+telefonomittente, new Response.Listener<String>(){
            public void onResponse(String response) {
                if(response!=null){
                    try {
                        JSONArray jObj1 = new JSONArray(response);
                        int cont= 0;
                        for (int i = 0; i < jObj1.length(); i++) {
                            JSONObject j = jObj1.getJSONObject(i);
                            if (!j.get("idMitt").toString().equals(j.get("idDest").toString())) {
                                msg.add(j.get("testo").toString());

                                if (j.get("idMitt").toString().equals(telefonomittente) && j.get("idDest").toString().equals(telefonodest) )
                                    chatArrayAdapter.add(new ChatMessage(false, msg.get(cont)));
                                else if(j.get("idMitt").toString().equals(telefonodest) && j.get("idDest").toString().equals(telefonomittente))
                                    chatArrayAdapter.add(new ChatMessage(true, msg.get(cont)));
                                cont++;
                            }
                        }
                   //     Toast.makeText(getApplicationContext(),telefonodest + "mittente: "+telefonomittente,Toast.LENGTH_LONG).show();
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
        }) ;
        AppController.getInstance().addToRequestQueue(strReq, "req_update");
        buttonSend = (Button) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);


        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                String msgtosend = chatText.getText().toString();
                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String datasend = df.format(c.getTime());
                StringRequest strReq2 = new StringRequest(Method.PUBLIC,
                        //è una risposta e quindi
                        // le variabilie telefonodest e telefonomittente si invertono di ruolo
                        // ecco perche sotto idMittente = telefonodestinatario...
                        AppConfig.URL_SENDMSG+"?msg='"+msgtosend.replaceAll(" ","%20")+"'&idM='"+telefonodest
                                +"'&idD='"+telefonomittente+"'&data='"+datasend.toString().replaceAll(" ","%20")+"'", new Response.Listener<String>(){
                    public void onResponse(String response) {
                        Log.d("", "Notify Response: " + response.toString());
                        //      Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                        if (response.equals("OK"))
                        ;


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

                AppController.getInstance().addToRequestQueue(strReq2, "req_update");

                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    private boolean sendChatMessage() {

        chatArrayAdapter.add(new ChatMessage(true, chatText.getText().toString()));
        chatText.setText("");
        side = !side;
        return true;
    }
}