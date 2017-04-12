package arun.com.chameleonskinforkwlp.util;

import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;

public class Util {
    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return color != 0 ? new ColorDrawable(color) : null;
    }
}
