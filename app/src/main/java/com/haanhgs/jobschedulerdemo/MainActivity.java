package com.haanhgs.jobschedulerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int JOB_ID = 1979;
    private static final int DELAY = 5555;
    private JobScheduler scheduler;
    private int networkOption;

    private Button bnSchedule;
    private Button bnCancel;
    private RadioGroup rgNetwork;
    private Switch swIdle;
    private Switch swCharge;
    private SeekBar sbrDuration;
    private TextView tvDuration;

    private void initViews(){
        scheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        bnCancel = findViewById(R.id.bnCancel);
        bnSchedule = findViewById(R.id.bnSchedule);
        rgNetwork = findViewById(R.id.rgNetwork);
        swIdle = findViewById(R.id.swIdle);
        swCharge = findViewById(R.id.swCharge);
        sbrDuration = findViewById(R.id.sbrDuration);
        tvDuration = findViewById(R.id.tvDuration);
    }

    private void handleSeekBar(){
        sbrDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 0){
                    tvDuration.setText(getString(R.string.form, progress));
                }else {
                    tvDuration.setText(R.string.tvDuration);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void handleRadioGroup(){
        int option = rgNetwork.getCheckedRadioButtonId();
        if (option == R.id.rbnNone){
            networkOption = JobInfo.NETWORK_TYPE_NONE;
        }else if (option == R.id.rbnAny){
            networkOption = JobInfo.NETWORK_TYPE_ANY;
        }else if (option == R.id.rbnWifi){
            networkOption = JobInfo.NETWORK_TYPE_UNMETERED;
        }else {
            networkOption = JobInfo.NETWORK_TYPE_NONE;
        }
    }

    private static ComponentName createService(){
        return new ComponentName(App.context().getPackageName(), Jobs.class.getName());
    }

    private JobInfo.Builder createBuilder(){
        return new JobInfo.Builder(JOB_ID, createService())
                .setRequiredNetworkType(networkOption)
                .setRequiresDeviceIdle(swIdle.isChecked())
                .setRequiresCharging(swCharge.isChecked())
                .setMinimumLatency(sbrDuration.getProgress())
                .setOverrideDeadline(sbrDuration.getProgress() + DELAY);
    }

    private boolean requirement(){
        return networkOption != JobInfo.NETWORK_TYPE_NONE
                || swIdle.isChecked()
                || swCharge.isChecked()
                || sbrDuration.getProgress() > 0;
    }

    private void buildJob(){
        if (requirement()){
            JobInfo jobInfo = createBuilder().build();
            if (scheduler == null) {
                scheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
            }
            if (scheduler != null){
                scheduler.schedule(jobInfo);
                Toast.makeText(this, R.string.job_scheduled, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.no_constraint_toast, Toast.LENGTH_LONG).show();
        }
    }

    private void handleScheduleButton(){
        bnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildJob();
            }
        });
    }

    private void handleCancelButton(){
        bnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scheduler != null){
                    scheduler.cancelAll();
                    scheduler = null;
                    Toast.makeText(App.context(), R.string.job_cancelled, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        handleSeekBar();
        handleRadioGroup();
        handleScheduleButton();
        handleCancelButton();
    }
}
