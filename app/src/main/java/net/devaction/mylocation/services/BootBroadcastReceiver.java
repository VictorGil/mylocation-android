package net.devaction.mylocation.services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author Víctor Gil
 *
 * since June 2018
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final int JOB_ID = 77012731;
    private static final int FIFTEEN_MINUTES_IN_MILLIS = 15 * 60 * 1000;
    private static final int FIVE_MINUTES_IN_MILLIS = 5 * 60 * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        scheduleJob(context);
    }

    static void scheduleJob(Context context){

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        JobInfo jobInfo = jobScheduler.getPendingJob(JOB_ID);

        if (jobInfo == null){
            Log.i("mylocation.BootBroadcastReceiver", "Going to schedule the job");
            ComponentName serviceComponent = new ComponentName(context, LocationJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
            //builder.setMinimumLatency(1 * 1000); // wait at least
            //java.lang.IllegalArgumentException: Can't call setOverrideDeadline() on a periodic job.
            //builder.setOverrideDeadline(3 * 1000); // maximum delay
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setRequiresCharging(false);
            builder.setRequiresDeviceIdle(false);

            //these are the minimum values (highest frequency) allowed
            builder.setPeriodic(FIFTEEN_MINUTES_IN_MILLIS, FIVE_MINUTES_IN_MILLIS);

            jobScheduler.schedule(builder.build());
        } else{
            Log.w("mylocation.BootBroadcastReceiver", "The job with id " + JOB_ID +
                    " is either running or already scheduled: " + jobInfo);
        }
    }
}

