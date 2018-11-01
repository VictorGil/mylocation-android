package net.devaction.mylocation.config;

import java.util.Arrays;

/**
 * @author Víctor Gil
 *
 * since September 2018
 */
public class ConfigData{
    private static ConfigData INSTANCE = null;

    // example value when testing could be "https://192.168.0.101:8091/api/locationdata"
    private String locationDataRemoteUrl;

    private String keyStorePassword;

    private MaskedArea[] maskedAreas;

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
                ", keyStorePassword='" + keyStorePassword + '\'' +
                ", maskedAreas=" + Arrays.toString(maskedAreas) +
                '}';
    }

    public String getLocationDataRemoteUrl(){
        return locationDataRemoteUrl;
    }

    //this is called by com.fasterxml.jackson.databind.ObjectMapper
    public void setLocationDataRemoteUrl(String locationDataRemoteUrl){
        this.locationDataRemoteUrl = locationDataRemoteUrl;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    //this method is called by com.fasterxml.jackson.databind.ObjectMapper
    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public MaskedArea[] getMaskedAreas() {
        return maskedAreas;
    }

    public void setMaskedAreas(MaskedArea[] maskedAreas) {
        this.maskedAreas = maskedAreas;
    }
}

