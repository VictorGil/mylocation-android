package net.devaction.mylocation.processors;

import android.location.Location;
import android.util.Log;

import static android.location.Location.FORMAT_DEGREES;

import net.devaction.mylocation.api.data.LocationData;

import java.util.Date;

/**
 * @author Víctor Gil
 *
 * since June 2018
 */
public class LocationDataConstructor{

    public static LocationData construct(Location location){
        if (location == null){
            Log.e("mylocation." + LocationDataConstructor.class.getSimpleName(),
                    "Location is null, maybe the mobile phone's location switch is off?");
            return null;
        }

        if (LocationDataShouldBeMaskedChecker.check(location.getLatitude(), location.getLongitude()))
            return constructMaskedLocationData();

        return constructRegularLocationData(location);
    }

    static LocationData constructRegularLocationData(Location location){
        LocationData locationData = new LocationData();

        locationData.setLatitude(Location.convert(location.getLatitude(), FORMAT_DEGREES));
        locationData.setLongitude(Location.convert(location.getLongitude(), FORMAT_DEGREES));
        locationData.setHorizontalAccuracy(Float.toString(location.getAccuracy()));

        locationData.setAltitude(Double.toString(location.getAltitude()));
        //this requires API level 26 and because of the implicit broadcast band we are stuck with API level 25
        //locationData.setVerticalAccuracy(Float.toString(location.getVerticalAccuracyMeters()));
        locationData.setVerticalAccuracy(LocationData.NA);

        //from Java time to UNIX time, i.e., from milliseconds to seconds
        locationData.setTimeChecked(new Date().getTime() / 1000);
        //from Java time to UNIX time, i.e., from milliseconds to seconds
        locationData.setTimeMeasured(location.getTime() / 1000);

        Log.d("mylocation." + LocationDataConstructor.class.getSimpleName(),
                "LocationData: " + locationData);

        return locationData;
    }

    static LocationData constructMaskedLocationData(){
        LocationData locationData = new LocationData();
        locationData.setLatitude(LocationData.MASKED);
        locationData.setLongitude(LocationData.MASKED);

        return locationData;
    }
}

