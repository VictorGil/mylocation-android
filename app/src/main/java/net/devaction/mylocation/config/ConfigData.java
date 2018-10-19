package net.devaction.mylocation.config;

/**
 * @author Víctor Gil
 *
 * since September 2018
 */
public class ConfigData{
    private static ConfigData INSTANCE = null;

    // example value could be "http://192.168.0.101:8091/api/locationdata"
    private String locationDataRemoteUrl;

    public static ConfigData getInstance(){
        return INSTANCE;
    }

    public ConfigData(){
        assert INSTANCE == null;
        INSTANCE = this;
    }

    @Override
    public String toString() {
        return "ConfigData{" +
                    "locationDataRemoteUrl='" + locationDataRemoteUrl + '\'' +
                '}';
    }

    public String getLocationDataRemoteUrl(){
        return locationDataRemoteUrl;
    }

    public void setLocationDataRemoteUrl(String locationDataRemoteUrl){
        this.locationDataRemoteUrl = locationDataRemoteUrl;
    }
}

