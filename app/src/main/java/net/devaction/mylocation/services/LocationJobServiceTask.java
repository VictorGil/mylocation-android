package net.devaction.mylocation.services;

import android.app.job.JobParameters;
import android.os.AsyncTask;
import android.util.Log;

import net.devaction.mylocation.api.data.LocationData;
import net.devaction.mylocation.serialization.PojoToJsonStringSerializer;

/**
 * @author Víctor Gil
 *
 * since June 2018
 */
public class LocationJobServiceTask extends AsyncTask<JobParameters, Void, JobParameters>{
    private final byte[] keyStoreBytes;
    private final LocationData locationData;

    public LocationJobServiceTask(byte[] keyStoreBytes, LocationData locationData) {
        this.keyStoreBytes =  keyStoreBytes;
        this.locationData = locationData;
    }

    @Override
    protected JobParameters doInBackground(JobParameters... jobParametersArray){
        String locationDataJsonString = PojoToJsonStringSerializer.serialize(locationData);
        Log.d("mylocation." + this.getClass().getSimpleName(), "LocationData JSON string: " + locationDataJsonString);
        if (locationDataJsonString != null)
            HttpClient.send(locationDataJsonString, keyStoreBytes);

        if (jobParametersArray[0] == null)
            return null;

        return jobParametersArray[0];
    }

    @Override
    protected void onPostExecute(JobParameters jobParameters){
        Log.d("mylocation." + this.getClass().getSimpleName(), "Task execution finished");
    }
}

