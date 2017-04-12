package arun.com.chameleonskinforkwlp.engine;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import static arun.com.chameleonskinforkwlp.wallpaper.ChameleonWallpaperService.ACTION_COLOR_EXTRACTED;

/**
 * Created by Arun on 05/08/2015.
 */
public class Broadcaster {
    private static final String KUSTOM_ACTION = "org.kustom.action.SEND_VAR";
    private static final String KUSTOM_ACTION_EXT_NAME = "org.kustom.action.EXT_NAME";
    private static final String KUSTOM_ACTION_VAR_NAME = "org.kustom.action.VAR_NAME";
    private static final String KUSTOM_ACTION_VAR_VALUE = "org.kustom.action.VAR_VALUE";
    private static final String CHAMELEON = "chameleon";
    private static final String prominentb = "prominentb";
    private static final String prominentt = "prominentt";
    private static final String vibrantb = "vibrantb";
    private static final String vibrantt = "vibrantt";
    private static final String darkvibrantb = "darkvibrantb";
    private static final String darkvibrantt = "darkvibrantt";
    private static final String lightvibrantb = "lightvibrantb";
    private static final String lightvibrantt = "lightvibrantt";
    private static final String mutedb = "mutedb";
    private static final String mutedt = "mutedt";
    private static final String darkmutedb = "darkmutedb";
    private static final String darkmutedt = "darkmutedt";
    private static final String lightmutedb = "lightmutedb";
    private static final String lightmutedt = "lightmutedt";
    private static final String[][] cham_group = {{prominentb, prominentt}, {vibrantb, vibrantt}, {darkvibrantb, darkvibrantt},
            {lightvibrantb, lightvibrantt}, {mutedb, mutedt},
            {darkmutedb, darkmutedt}, {lightmutedb, lightmutedt}};

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
