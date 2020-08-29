package com.benahita.slooz;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

/**
 * Created by yvon on 8/29/20.
 */


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("JobServiceSample", "MainJobService start" );
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("JobServiceSample", "MainJobService stop" );
        return true;
    }

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, MainJobService.class);
        JobInfo jobInbo = new JobInfo.Builder(0, serviceComponent)
                .setMinimumLatency(1000)      // Temps d'attente minimal avant déclenchement
                .setOverrideDeadline(3000)    // Temps d'attente maximal avant déclenchement
                .build();

        JobScheduler jobScheduler = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            jobScheduler = context.getSystemService(JobScheduler.class);
        }
        jobScheduler.schedule(jobInbo);
    }
}
