package net.devaction.mylocation.services;

import android.util.Log;

import net.devaction.mylocation.config.ConfigData;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

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

    public static void send(String jsonString, byte[] keyStoreBytes){
        final String URL = ConfigData.getInstance().getLocationDataRemoteUrl();
        final String keyStorePassword = ConfigData.getInstance().getKeyStorePassword();
        send(URL, jsonString, keyStoreBytes, keyStorePassword);
    }

    static void send(String url, String jsonString, byte[] keyStoreBytes, String keyStorePassword){
        Log.d("mylocation.HttpClient","Going to send JSON string: " + jsonString +
                "\nRemote URL: " + url);

        SSLSocketFactory sslSocketFactory = SslSocketFactoryCreator.create(keyStoreBytes, keyStorePassword);

        //TO DO: use a different method because sslSocketFactory(sslSocketFactory) is deprecated
        OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory).hostnameVerifier(
                new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session){
                        //I do not verify the hostname
                        //because I am going to deploy to AWS so I am not sure about the hostname
                        //where the Vert.x code will run
                        return true;
                    }
                }
        ).build();


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

