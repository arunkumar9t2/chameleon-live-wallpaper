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

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class Extractor {
    private final Bitmap bitmap;
    private final Context context;
    private int noOfColors = 0;

    private Extractor(Context context, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.context = context.getApplicationContext();
    }

    public Extractor(Context context, Bitmap bitmap, int noOfColors) {
        this.bitmap = bitmap;
        this.context = context.getApplicationContext();
        this.noOfColors = noOfColors;
    }

    public static Extractor forBitmap(Context context, Bitmap bitmap) {
        return new Extractor(context, bitmap);
    }

    public void doExtraction() {
        final List<Swatch> colorSwatches = extractSwatches();
        Timber.d("Extracted colors : %s", colorSwatches);
        Broadcaster.broadcastColors(context, colorSwatches);
    }

    private void recycleBitmap() {
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    @NonNull
    @WorkerThread
    private List<Swatch> extractSwatches() {
        final List<Swatch> colorSwatches = new ArrayList<>();
        final List<Palette.Swatch> generatedSwatches = new ArrayList<>();
        if (bitmap != null) {
            final Palette palette;
            if (noOfColors != 0) {
                palette = Palette.from(bitmap).maximumColorCount(noOfColors).generate();
            } else {
                palette = Palette.from(bitmap).generate();
            }
            Palette.Swatch dominantSwatch = palette.getDominantSwatch();
            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
            Palette.Swatch vibrantDarkSwatch = palette.getDarkVibrantSwatch();
            Palette.Swatch vibrantLightSwatch = palette.getLightVibrantSwatch();
            Palette.Swatch mutedSwatch = palette.getMutedSwatch();
            Palette.Swatch mutedDarkSwatch = palette.getDarkMutedSwatch();
            Palette.Swatch mutedLightSwatch = palette.getLightMutedSwatch();

            if (vibrantSwatch != null && mutedSwatch == null) {
                mutedSwatch = vibrantSwatch;
            } else if (vibrantSwatch == null && mutedSwatch != null) {
                vibrantSwatch = mutedSwatch;
            }
            if (vibrantDarkSwatch != null && mutedDarkSwatch == null) {
                mutedDarkSwatch = vibrantDarkSwatch;
            } else if (vibrantDarkSwatch == null && mutedDarkSwatch != null) {
                vibrantDarkSwatch = mutedDarkSwatch;
            }
            if (vibrantLightSwatch != null && mutedLightSwatch == null) {
                mutedLightSwatch = vibrantLightSwatch;
            } else if (vibrantLightSwatch == null && mutedLightSwatch != null) {
                vibrantLightSwatch = mutedLightSwatch;
            }

            generatedSwatches.add(dominantSwatch);
            generatedSwatches.add(vibrantSwatch);
            generatedSwatches.add(vibrantDarkSwatch);
            generatedSwatches.add(vibrantLightSwatch);
            generatedSwatches.add(mutedSwatch);
            generatedSwatches.add(mutedDarkSwatch);
            generatedSwatches.add(mutedLightSwatch);

            for (final Palette.Swatch paletteSwatch : generatedSwatches) {
                colorSwatches.add(paletteSwatch != null ? new Swatch(paletteSwatch) : null);
            }
            recycleBitmap();
        }
        return colorSwatches;
    }
}
