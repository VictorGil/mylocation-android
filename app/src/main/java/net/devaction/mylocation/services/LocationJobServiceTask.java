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
    private final LocationJobService locationJobService;
    private final LocationData locationData;

    //this is used when it is not submitted by the LocationJobService
    //but by a button on the UI for example
    public LocationJobServiceTask(LocationData locationData) {
        this.locationJobService = null;
        this.locationData = locationData;
    }

    public LocationJobServiceTask(LocationJobService jobService, LocationData locationData) {
        this.locationJobService = jobService;
        this.locationData = locationData;
    }

    @Override
    protected JobParameters doInBackground(JobParameters... jobParametersArray){
        String locationDataJsonString = PojoToJsonStringSerializer.serialize(locationData);
        Log.d("mylocation." + this.getClass().getSimpleName(), "LocationData JSON string: " + locationDataJsonString);
        if (locationDataJsonString != null)
            HttpClient.send(locationDataJsonString);

        if (jobParametersArray[0] == null)
            return null;

        return jobParametersArray[0];
    }

    @Override
    protected void onPostExecute(JobParameters jobParameters){
        if (locationJobService != null && jobParameters != null){
            Log.d("mylocation." + this.getClass().getSimpleName(), "Job with id " +
                    jobParameters.getJobId() + " finished");
            locationJobService.jobFinished(jobParameters, false);
        }

        Log.d("mylocation." + this.getClass().getSimpleName(), "Task execution finished");
    }
}

