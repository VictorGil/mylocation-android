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

import net.devaction.mylocation.R;
import net.devaction.mylocation.api.data.LocationData;
import net.devaction.mylocation.config.ConfigFetcher;
import net.devaction.mylocation.services.LocationJobServiceTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * @author Víctor Gil
 *
 * since June 2018
 */
public class LocationDataProcessor{

    //it is called from the UI
    public static void process(Context context){
        process(context, null);
    }

    //it may be called by the service or by the UI
    public static void process(final Context context, final JobParameters jobParameters){
        ConfigFetcher.fetch(context);

        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED){
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location){
                            // GPS location can be null if GPS is switched off
                            process(location, jobParameters, context);
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

    static void process(Location location, JobParameters jobParameters, Context context){
        LocationData locationData = LocationDataConstructor.construct(location);
        Log.i(LocationDataProcessor.class.getSimpleName(), "LocationData: " + locationData);

        if (locationData == null){
            Log.e("mylocation." + LocationDataProcessor.class.getSimpleName(),
                    "Could not get the current location");
            return;
        }

        if (locationData.getLatitude().equals(LocationData.MASKED)){
            Log.w("mylocation." + LocationDataProcessor.class.getSimpleName(), "Current location is masked" +
            ", not going to send it to the backend sever");
            return;
        }

        JobParameters[] jobParametersArray = new JobParameters[1];
        jobParametersArray[0] = jobParameters;

        InputStream keyStoreInputStream = context.getResources().openRawResource(R.raw.mylocationkeystore01);
        byte[] keyStoreBytes =  readBytes(keyStoreInputStream);
        Log.d(LocationDataProcessor.class.getSimpleName(), "Size of the keyStore in bytes: " + keyStoreBytes.length);

        LocationJobServiceTask locationJobServiceTask = new LocationJobServiceTask(keyStoreBytes, locationData);
        locationJobServiceTask.execute(jobParametersArray);
    }

    static byte[] readBytes(InputStream inputStream){
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        try {
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch(IOException ex){
            Log.e(LocationDataProcessor.class.getSimpleName(), "Exception when reading from input stream: " + ex.toString(), ex);
            return null;
        } finally{
            try {
                inputStream.close();
            } catch (IOException ex){
                Log.e(LocationDataProcessor.class.getSimpleName(), "Exception when closing the input stream: " + ex.toString(), ex);
            }
        }

        return buffer.toByteArray();
    }
}

