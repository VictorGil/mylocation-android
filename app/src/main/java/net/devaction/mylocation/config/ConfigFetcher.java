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
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CONFIG_DATA = objectMapper.readValue(configInputStream, ConfigData.class);
        } catch (IOException ex) {
            Log.wtf(ConfigFetcher.class.getSimpleName(), "Could not read config file", ex);
            Toast toast = Toast.makeText(context, R.string.could_not_read_config_file, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, -150);
            toast.show();
            return;
        }

        if (CONFIG_DATA.getLocationDataRemoteUrl() == null || CONFIG_DATA.getLocationDataRemoteUrl().isEmpty()){
            Log.wtf(ConfigFetcher.class.getSimpleName(),
                    "The value of the remote URL is not set in the configuration file");
            Toast toast = Toast.makeText(context, R.string.url_missing, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, -150);
            toast.show();
            return;
        }
        Log.d(ConfigFetcher.class.getSimpleName(), "Configuration data has just been read from file: " + CONFIG_DATA);
    }
}
