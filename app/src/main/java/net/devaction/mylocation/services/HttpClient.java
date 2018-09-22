package net.devaction.mylocation.services;

import android.util.Log;

import net.devaction.mylocation.config.ConfigData;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

/**
 * @author Víctor Gil
 *
 * since June 2018
 */
public class HttpClient{
    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    public static void send(String jsonString){
        final String URL = ConfigData.getInstance().getLocationDataRemoteUrl();
        send(URL, jsonString);
    }

    public static void send(String url, String jsonString){
        Log.d("mylocation.HttpClient","Going to send JSON string: " + jsonString +
                "\nRemote URL: " + url);

        OkHttpClient client = new OkHttpClient();
        Request request = buildRequest(url, jsonString);
        Response response = null;

        try{
            response = client.newCall(request).execute();
            Log.d("mylocation.HttpClient", "HTTP Response received: " + response.body().string());
        } catch(IOException ex){
            Log.e("mylocation.HttpClient", "Could not receive HTTP response from URL " + url, ex);
        }
    }

    static Request buildRequest(String url, String jsonString){

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, jsonString);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return request;
    }
}

