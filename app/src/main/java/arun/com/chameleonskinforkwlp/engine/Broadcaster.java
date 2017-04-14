package arun.com.chameleonskinforkwlp.engine;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import static arun.com.chameleonskinforkwlp.wallpaper.ChameleonWallpaperService.ACTION_COLOR_EXTRACTED;

class Broadcaster {
    private static final String KUSTOM_ACTION = "org.kustom.action.SEND_VAR";
    private static final String KUSTOM_ACTION_EXT_NAME = "org.kustom.action.EXT_NAME";
    private static final String KUSTOM_ACTION_VAR_NAME = "org.kustom.action.VAR_NAME";
    private static final String KUSTOM_ACTION_VAR_VALUE = "org.kustom.action.VAR_VALUE";
    private static final String CHAMELEON = "chameleon";
    private static final String PROMINENTB = "prominentb";
    private static final String PROMINENTT = "prominentt";
    private static final String VIBRANTB = "vibrantb";
    private static final String VIBRANTT = "vibrantt";
    private static final String DARKVIBRANTB = "darkvibrantb";
    private static final String DARKVIBRANTT = "darkvibrantt";
    private static final String LIGHTVIBRANTB = "lightvibrantb";
    private static final String LIGHTVIBRANTT = "lightvibrantt";
    private static final String MUTEDB = "mutedb";
    private static final String MUTEDT = "mutedt";
    private static final String DARKMUTEDB = "darkmutedb";
    private static final String DARKMUTEDT = "darkmutedt";
    private static final String LIGHTMUTEDB = "lightmutedb";
    private static final String LIGHTMUTEDT = "lightmutedt";
    private static final String[][] cham_group = {{PROMINENTB, PROMINENTT}, {VIBRANTB, VIBRANTT}, {DARKVIBRANTB, DARKVIBRANTT},
            {LIGHTVIBRANTB, LIGHTVIBRANTT}, {MUTEDB, MUTEDT},
            {DARKMUTEDB, DARKMUTEDT}, {LIGHTMUTEDB, LIGHTMUTEDT}};

    public static void broadcastColors(Context context, List<Swatch> swatches) {
        if (swatches != null) {
            for (int i = 0; i < swatches.size(); i++) {
                Swatch swatch = swatches.get(i);
                if (swatch != null) {
                    String[] array = cham_group[i];
                    Intent intent = new Intent(KUSTOM_ACTION);
                    intent.putExtra(KUSTOM_ACTION_EXT_NAME, CHAMELEON);
                    intent.putExtra(KUSTOM_ACTION_VAR_NAME, array[0]);
                    intent.putExtra(KUSTOM_ACTION_VAR_VALUE, getHexValueColor(swatch.getBackgroundColor()));
                    context.sendBroadcast(intent);
                    intent = new Intent(KUSTOM_ACTION);
                    intent.putExtra(KUSTOM_ACTION_EXT_NAME, CHAMELEON);
                    intent.putExtra(KUSTOM_ACTION_VAR_NAME, array[1]);
                    intent.putExtra(KUSTOM_ACTION_VAR_VALUE, getHexValueColor(swatch.getForegroundColor()));
                    context.sendBroadcast(intent);
                }
            }
        }
        updateOurLiveWallpaper(context, swatches);
    }

    private static void updateOurLiveWallpaper(Context context, List<Swatch> swatches) {
        final Intent colorUpdateIntent = new Intent();
        colorUpdateIntent.setAction(ACTION_COLOR_EXTRACTED);
        colorUpdateIntent.putParcelableArrayListExtra("Colors", (ArrayList<? extends Parcelable>) swatches);
        context.sendBroadcast(colorUpdateIntent);
    }

    private static String getHexValueColor(int color) {
        return String.format("#%08X", (0xFFFFFFFF & color));
    }
}
