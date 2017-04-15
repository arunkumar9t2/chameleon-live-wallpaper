/*
 * Copyright 2017 Arunkumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package arun.com.chameleonskinforkwlp.engine;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import arun.com.chameleonskinforkwlp.R;
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
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ExtractorService.this, R.string.extraction_complete, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
