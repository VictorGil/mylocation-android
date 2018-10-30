package net.devaction.mylocation.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import net.devaction.mylocation.db.LocationServiceController;
import net.devaction.mylocation.processors.LocationDataProcessor;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * @author Víctor Gil
 *
 * since June 2018
 */
//this is defined in the Android manifest XML file
public class LocationJobService extends JobService{
    public static final String LOCATION_DATA_KEY = "locationdata";

    //this is called by Android
    @Override
    public boolean onStartJob(JobParameters locationJobParams){
        Log.i("mylocation.LocationJobService", "Job started");

        if (LocationServiceController.isServiceEnabled(this)){
            Log.d(this.getClass().getSimpleName(),
                    "The location service is enabled, going to send the location data");
            LocationDataProcessor.process(this, locationJobParams);
            //we are doing work in some other thread
            return true;
        } else{
            Log.d(this.getClass().getSimpleName(),
                    "The location service is disabled, nothing to do");
            //nothing to do because the LocationService is not enabled
            return false;
        }
    }

    @Override
    public boolean onStopJob(JobParameters params){
        Log.i("mylocation.LocationJobService", "Job stopped");
        return false;
    }
}

