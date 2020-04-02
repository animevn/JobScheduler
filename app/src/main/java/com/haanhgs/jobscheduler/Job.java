package com.haanhgs.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;

/*
add these lines to manifest
<service
     android:name=".Jobs"
     android:permission="android.permission.BIND_JOB_SERVICE">
</service>
*/
public class Job extends JobService {

    public static final int ID = 1979;

    @Override
    public boolean onStartJob(JobParameters params) {
        Notification notification = new Notification(this);
        notification.createNotification(1979);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
