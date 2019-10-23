package com.haanhgs.jobschedulerdemo;

import android.app.job.JobParameters;
import android.app.job.JobService;

//add these lines to manifest
//<service
//      android:name=".Jobs"
//      android:permission="android.permission.BIND_JOB_SERVICE">
// </service>

public class Jobs extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Notification.createNotification(1979);
        //return false, run job in background
        //return true, run job in main
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {


        return false;
    }
}
