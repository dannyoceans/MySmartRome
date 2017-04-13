/**
 * Created by francesconi on 08/03/2017.
 */
package activity.chat;


import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import helper.SQLiteHandler;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import progettomagistralemacc.applogin.Main2Activity;

import static progettomagistralemacc.applogin.Main2Activity.tel2;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();

        registerToken(token,tel2);
    }

    private void registerToken(String token,String tel) {
     //   SQLiteHandler db= new SQLiteHandler(getApplicationContext());
       // Toast.makeText(getApplicationContext(),"sss"+db.getUserDetails().get("uid"),Toast.LENGTH_LONG).show();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token)
                .add("telefono",tel)
               // .add("telefono", db.getUserDetails().get("uid"))
                //     .add("id_receiver",id_receiver);
                .build();

        Request request = new Request.Builder()
                .url("http://progettomagistrale.altervista.org/registertoken.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
