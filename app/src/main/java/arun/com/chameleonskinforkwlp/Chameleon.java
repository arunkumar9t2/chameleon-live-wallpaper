package arun.com.chameleonskinforkwlp;

import android.app.Application;

import timber.log.Timber;

public class Chameleon extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
