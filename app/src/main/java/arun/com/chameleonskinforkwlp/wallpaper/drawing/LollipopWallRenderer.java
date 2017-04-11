package arun.com.chameleonskinforkwlp.wallpaper.drawing;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.util.HashMap;

import arun.com.chameleonskinforkwlp.util.Size;
import arun.com.chameleonskinforkwlp.wallpaper.ChameleonWallpaperService;

public class LollipopWallRenderer implements Renderer {
    private Paint paint;

    public LollipopWallRenderer() {
    }

    @Override
    public void draw(Canvas canvas, Paint paint, Size size, HashMap<String, Integer> colorMap) {
        final int width = size.width;
        final int height = size.height;
        this.paint = paint;

        // divide into blocks
        int block = height / 40;

        // set default colors just in case
        int muted = Color.parseColor("#E91E63");
        int darkMuted = Color.parseColor("#880E4F");
        int vibrant = Color.parseColor("#00BCD4");
        int darkVibrant = Color.parseColor("#00ACC1");
        int lightMuted = Color.parseColor("#80DEEA");

        // set the colors;
        if (colorMap != null) {
            muted = colorMap.get(ChameleonWallpaperService.MUTED) != null ? colorMap.get(ChameleonWallpaperService.MUTED) : muted;
            darkMuted = colorMap.get(ChameleonWallpaperService.DARK_MUTED) != null ? colorMap.get(ChameleonWallpaperService.DARK_MUTED) : darkMuted;
            vibrant = colorMap.get(ChameleonWallpaperService.VIBRANT) != null ? colorMap.get(ChameleonWallpaperService.VIBRANT) : vibrant;
            darkVibrant = colorMap.get(ChameleonWallpaperService.DARK_VIBRANT) != null ? colorMap.get(ChameleonWallpaperService.DARK_VIBRANT) : darkVibrant;
            lightMuted = colorMap.get(ChameleonWallpaperService.LIGHT_MUTED) != null ? colorMap.get(ChameleonWallpaperService.LIGHT_MUTED) : lightMuted;
        }

        // declare points for quadrilateral
        Point point1;
        Point point2;
        Point point3;
        Point point4;
        Point point5;

        // The path we draw on
        Path path;

        // set the background color.
        canvas.drawColor(darkMuted);
        this.paint.setShadowLayer(8.0f, 1.0f, 2.0f, 0x85000000);

        this.paint.setColor(muted);
        point1 = new Point(9 * block, 16 * block);
        point2 = new Point(0, 7 * block);
        point3 = new Point(0, 25 * block);
        // Paths
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(point1.x, point1.y);
        path.lineTo(point2.x, point2.y);
        path.lineTo(point3.x, point3.y);
        path.lineTo(point1.x, point1.y);
        path.close();
        canvas.drawPath(path, this.paint);

        // draw quadrilateral root bg
        this.paint.setColor(vibrant);
        point1 = new Point(0, 11 * block);
        point2 = new Point(0, 0);
        point3 = new Point(width, 0);
        point4 = new Point(width, 16 * block);
        point5 = new Point(12 * block, 24 * block);
        // Paths
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(point1.x, point1.y);
        path.lineTo(point2.x, point2.y);
        path.lineTo(point3.x, point3.y);
        path.lineTo(point4.x, point4.y);
        path.lineTo(point5.x, point5.y);
        path.lineTo(point1.x, point1.y);
        path.close();
        canvas.drawPath(path, this.paint);
        // quadrilateral end

        this.paint.setColor(lightMuted);
        point1 = new Point(0, 0);
        point2 = new Point(width, 24 * block);
        point3 = new Point(width, 27 * block);
        point4 = new Point(0, 3 * block);
        // Paths
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(point1.x, point1.y);
        path.lineTo(point2.x, point2.y);
        path.lineTo(point3.x, point3.y);
        path.lineTo(point4.x, point4.y);
        path.lineTo(point1.x, point1.y);
        path.close();
        canvas.drawPath(path, this.paint);
        // pipe end

        // draw triangle final
        this.paint.setColor(lightMuted);
        point1 = new Point(width, 27 * block);
        point2 = new Point(12 * block, height);
        point3 = new Point(width, height);
        // Paths
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(point1.x, point1.y);
        path.lineTo(point2.x, point2.y);
        path.lineTo(point3.x, point3.y);
        path.lineTo(point1.x, point1.y);
        path.close();
        canvas.drawPath(path, this.paint);
        // triangle end

        this.paint.setColor(darkVibrant);
        point1 = new Point(12 * block, 24 * block);
        point2 = new Point(width, 11 * block);
        point3 = new Point(width, 36 * block);
        // Paths
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(point1.x, point1.y);
        path.lineTo(point2.x, point2.y);
        path.lineTo(point3.x, point3.y);
        path.lineTo(point1.x, point1.y);
        path.close();
        canvas.drawPath(path, this.paint);
        // triangle end
    }
}
