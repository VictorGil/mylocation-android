package net.devaction.mylocation.processors;

import android.util.Log;

import net.devaction.mylocation.config.ConfigData;
import net.devaction.mylocation.config.MaskedArea;

import java.math.BigDecimal;

/**
 * @author Víctor Gil
 *
 * since November 2018
 */
public class LocationDataShouldBeMaskedChecker{

    static boolean check(final double latitude, final double longitude){
        return check(BigDecimal.valueOf(latitude), BigDecimal.valueOf(longitude));
    }

    static boolean check(final BigDecimal latitude, final BigDecimal longitude){
        //I am aware that this is not pretty, I plan to switch to using Dependency Injection (Dagger 2)
        //in the future
        final MaskedArea[] maskedAreas = ConfigData.getInstance().getMaskedAreas();

        return check(latitude, longitude, maskedAreas);
    }

    static boolean check(final BigDecimal latitude, final BigDecimal longitude, final MaskedArea[] maskedAreas){

        for (int i = 0; i < maskedAreas.length; i++){
            if (check(latitude, longitude, maskedAreas[i])){
                Log.i(LocationDataShouldBeMaskedChecker.class.getSimpleName(), "Current location is in a masked area");
                return true;
            }
        }

        Log.i(LocationDataShouldBeMaskedChecker.class.getSimpleName(), "Current location must not be masked");
        return false;
    }

    static boolean check(final BigDecimal latitude, final BigDecimal longitude, final MaskedArea maskedArea){
        if (latitude.compareTo(new BigDecimal(maskedArea.getMinLatitude())) > 0 &&
                latitude.compareTo(new BigDecimal(maskedArea.getMaxLatitude())) < 0  &&
                longitude.compareTo(new BigDecimal(maskedArea.getMinLongitude())) > 0 &&
                longitude.compareTo(new BigDecimal(maskedArea.getMaxLongitude())) < 0)
            return true;
        return false;
    }
}
