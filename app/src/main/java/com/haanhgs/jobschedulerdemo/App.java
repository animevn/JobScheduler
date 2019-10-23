package com.haanhgs.jobschedulerdemo;

import android.app.Application;
import android.content.Context;
import java.lang.ref.WeakReference;

public class App extends Application {

    private static WeakReference<Context> context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = new WeakReference<>(getApplicationContext());
    }

    public static Context context() {
        return context.get();
    }
}
