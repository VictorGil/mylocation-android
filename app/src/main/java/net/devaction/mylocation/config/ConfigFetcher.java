package net.devaction.mylocation.config;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import net.devaction.mylocation.R;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Víctor Gil
 *
 * since September 2018
 */
public class ConfigFetcher{
    private static ConfigData CONFIG_DATA;

    public static void fetch(Context context){
        if (CONFIG_DATA != null){
            //we have already read the configuration data
            return;
        }

        InputStream configInputStream = context.getResources().openRawResource(R.raw.config);
         CONFIG_DATA = readConfigData(configInputStream, context);
    }

    static ConfigData readConfigData(InputStream configInputStream, Context context){
        ObjectMapper objectMapper = new ObjectMapper();
        ConfigData configData = null;

        try {
            configData = objectMapper.readValue(configInputStream, ConfigData.class);
        } catch (IOException ex) {
            Log.wtf(ConfigFetcher.class.getSimpleName(), "Could not read config file", ex);
            final Toast toast = Toast.makeText(context, R.string.could_not_read_config_file, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, -150);
            toast.show();
            return null;
        } finally{
            try{
                configInputStream.close();
            } catch(IOException ex){
                Log.e(ConfigFetcher.class.getSimpleName(), "Error when closing the InputStream: " + ex, ex);
            }
        }

        if (configData.getLocationDataRemoteUrl() == null || configData.getLocationDataRemoteUrl().isEmpty()){
            Log.wtf(ConfigFetcher.class.getSimpleName(),
                    "The value of the remote URL is not set in the configuration file");
            final Toast toast = Toast.makeText(context, R.string.url_missing, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, -150);
            toast.show();
            return null;
        }
        Log.d(ConfigFetcher.class.getSimpleName(), "Configuration data has just been read from file: " + configData);
        return configData;
    }
}

