package arun.com.chameleonskinforkwlp.engine;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;
import java.util.List;

public class Extractor {
    private final Bitmap bitmap;
    private int noOfColors = 0;

    public Extractor(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Extractor(Bitmap bitmap, int noOfColors) {
        this.bitmap = bitmap;
        this.noOfColors = noOfColors;
    }

    @WorkerThread
    public List<Swatch> getColorSwatches() {
        List<Swatch> colorSwatches = new ArrayList<>();

        List<Palette.Swatch> generatedSwatches = extractSwatches();

        // Copy all the swatches to our own POJO
        for (final Palette.Swatch paletteSwatch : generatedSwatches) {
            colorSwatches.add(paletteSwatch != null ? new Swatch(paletteSwatch) : null);
        }
        recycleBitmap();
        return colorSwatches;
    }

    private void recycleBitmap() {
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    @NonNull
    @WorkerThread
    private List<Palette.Swatch> extractSwatches() {
        final List<Palette.Swatch> swatchList = new ArrayList<>();
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

            swatchList.add(dominantSwatch);
            swatchList.add(vibrantSwatch);
            swatchList.add(vibrantDarkSwatch);
            swatchList.add(vibrantLightSwatch);
            swatchList.add(mutedSwatch);
            swatchList.add(mutedDarkSwatch);
            swatchList.add(mutedLightSwatch);
        }
        return swatchList;
    }
}
