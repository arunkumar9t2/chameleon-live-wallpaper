package arun.com.chameleonskinforkwlp;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by arunk on 12-04-2017.
 */

public class Chameleon extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
