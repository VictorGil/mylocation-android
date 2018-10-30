package net.devaction.mylocation.processors;

import android.app.job.JobParameters;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import net.devaction.mylocation.api.data.LocationData;
import net.devaction.mylocation.config.ConfigFetcher;
import net.devaction.mylocation.services.LocationJobService;
import net.devaction.mylocation.services.LocationJobServiceTask;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * @author Víctor Gil
 *
 * since June 2018
 */
public class LocationDataProcessor{

    //it is called by the MainActivity (UI)
    public static void process(Context context){
        process(context, null);
    }

    //it is called by the service
    public static void process(final Context context, final JobParameters jobParameters){
        ConfigFetcher.fetch(context);

        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED){
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location){
                            LocationJobService locationJobService = null;
                            if (jobParameters != null)
                                locationJobService = (LocationJobService) context;

                            // GPS location can be null if GPS is switched off
                            process(location, jobParameters, locationJobService);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception ex) {
                            Log.d("mylocation." + LocationDataProcessor.class.getSimpleName(),
                                    "Error trying to get last GPS location", ex);
                        }
                    });

        } else{
            Log.e("mylocation." + LocationDataProcessor.class.getSimpleName(),
                    "We do not have the permision to retrieve the location");
        }
    }

    static void process(Location location, JobParameters jobParameters, LocationJobService locationJobService){
        LocationData locationData = LocationDataConstructor.construct(location);

        if (locationData == null){
            Log.e("mylocation." + LocationDataProcessor.class.getSimpleName(),
                    "Could not get the current location");
        } else {
            JobParameters[] jobParametersArray = new JobParameters[1];
            LocationJobServiceTask locationJobServiceTask;
            if (jobParameters == null){
                locationJobServiceTask = new LocationJobServiceTask(locationData);
            } else{
                jobParametersArray[0] = jobParameters;
                locationJobServiceTask = new LocationJobServiceTask(locationJobService, locationData);
            }
            locationJobServiceTask.execute(jobParametersArray);
        }
    }
}

