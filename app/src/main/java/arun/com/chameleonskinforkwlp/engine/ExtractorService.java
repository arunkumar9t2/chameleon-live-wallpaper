package arun.com.chameleonskinforkwlp.engine;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;

import timber.log.Timber;

public class ExtractorService extends IntentService {
    private static final String ACTION_FOO = "arun.com.chameleonskinforkwlp.engine.action.EXTRACT";
    private static final String EXTRA_PHOTO_PATH = "arun.com.chameleonskinforkwlp.engine.extra.PHOTO_PATH";

    public ExtractorService() {
        super("ExtractorService");
    }

    public static void startExtraction(Context context, String photoPath) {
        Intent intent = new Intent(context, ExtractorService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PHOTO_PATH, photoPath);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String photoPath = intent.getStringExtra(EXTRA_PHOTO_PATH);
                doExtraction(photoPath);
            }
        }
    }

    private void doExtraction(String photoPath) {
        try {
            final Bitmap bitmap = Glide.with(this)
                    .load(photoPath)
                    .asBitmap()
                    .centerCrop()
                    .into(720, 720)
                    .get();
            if (bitmap != null) {
                Extractor.forBitmap(this, bitmap).doExtraction();
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
