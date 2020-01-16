package com.haanhgs.jobschedulerdemo;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rbnNone)
    RadioButton rbnNone;
    @BindView(R.id.rbnAny)
    RadioButton rbnAny;
    @BindView(R.id.rbnWifi)
    RadioButton rbnWifi;
    @BindView(R.id.rgNetwork)
    RadioGroup rgNetwork;
    @BindView(R.id.swIdle)
    Switch swIdle;
    @BindView(R.id.swCharge)
    Switch swCharge;
    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @BindView(R.id.sbrDuration)
    SeekBar sbrDuration;
    @BindView(R.id.bnSchedule)
    Button bnSchedule;
    @BindView(R.id.bnCancel)
    Button bnCancel;

    private static final int JOB_ID = 1979;
    private static final int DELAY = 5555;
    private JobScheduler scheduler;
    private int networkOption;

    private void handleSeekBar(){
        sbrDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 0){
                    tvDuration.setText(getResources().getString(R.string.form, progress));
                }else {
                    tvDuration.setText(getResources().getString(R.string.tvDuration));
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void initNetworkOptionValue(){
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

    private void handleRadioButtons(){
        rgNetwork.setOnCheckedChangeListener((group, checkedId) -> {
             switch (checkedId){
                 case R.id.rbnAny:
                     networkOption = JobInfo.NETWORK_TYPE_ANY;
                     break;
                 case R.id.rbnNone:
                     networkOption = JobInfo.NETWORK_TYPE_NONE;
                     break;
                 case R.id.rbnWifi:
                     networkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                     break;
             }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        handleSeekBar();
        initNetworkOptionValue();
        handleRadioButtons();
    }

    private JobInfo.Builder initJobBuilder(){
        return new JobInfo.Builder(JOB_ID, new ComponentName(getPackageName(), Job.class.getName()))
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
            JobInfo jobInfo = initJobBuilder().build();
            if (scheduler == null) {
                scheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
            }
            if (scheduler != null) scheduler.schedule(jobInfo);
            Toast.makeText(this, R.string.job_scheduled, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, R.string.no_constraint_toast, Toast.LENGTH_LONG).show();
        }
    }

    private void cancelJob(){
        if (scheduler != null){
            scheduler.cancelAll();
            scheduler = null;
            Toast.makeText(this, R.string.job_cancelled, Toast.LENGTH_LONG).show();
        }
    }

    @OnClick({R.id.bnSchedule, R.id.bnCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bnSchedule:
                buildJob();
                break;
            case R.id.bnCancel:
                cancelJob();
                break;
        }
    }
}
