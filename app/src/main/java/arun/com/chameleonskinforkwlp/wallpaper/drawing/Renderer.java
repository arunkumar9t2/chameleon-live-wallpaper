package arun.com.chameleonskinforkwlp.wallpaper.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.HashMap;

import arun.com.chameleonskinforkwlp.util.Size;

public interface Renderer {
    void draw(Canvas canvas, Paint paint, Size size, HashMap<String, Integer> colorMap);
}
