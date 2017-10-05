package info.androidhive.firebase;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by KITTAPORN on 10/5/2017.
 */

public class PostTask extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... data) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://gcm-http.googleapis.com/gcm/send%20Content-Type:application/json%20Authorization:key=AAAAQ-ScEdo:APA91bGnwim61bK8Z2VueMiqCCcGBScl72ilsBrgYr9uv4pGllKrrATDc-3mBnI7Cq5jRshuK1ghAU9-NlSbWsiKfQkbXSm2hFbREWziZXkBsDw4j2lOVSqymBfBISMpLqhZNe1Hh1Mt%20%7B%20\"to\":%20\"/topics/user_puf\",%20\"data\":%20%7B%20\"message\":%20\"This%20is%20a%20GCM%20Topic%20Message!\",%20%7D%20%7D");

        try {
            //add data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("data", data[0]));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //execute http post
            HttpResponse response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }
        return "5555";
    }
}