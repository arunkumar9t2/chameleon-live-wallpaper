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

package arun.com.chameleonskinforkwlp.wallpaper.drawing;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.NonNull;

import java.util.HashMap;

import arun.com.chameleonskinforkwlp.wallpaper.ChameleonWallpaperService;

public class LollipopWallRenderer implements Renderer {

    public LollipopWallRenderer() {
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull HashMap<String, Integer> colorMap) {
        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        // divide into blocks
        int block = height / 40;

        int muted = colorMap.get(ChameleonWallpaperService.MUTED);
        int darkMuted = colorMap.get(ChameleonWallpaperService.DARK_MUTED);
        int vibrant = colorMap.get(ChameleonWallpaperService.VIBRANT);
        int darkVibrant = colorMap.get(ChameleonWallpaperService.DARK_VIBRANT);
        int lightMuted = colorMap.get(ChameleonWallpaperService.LIGHT_MUTED);

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
        paint.setShadowLayer(14.0f, 1.0f, 2.0f, 0x85000000);

        paint.setColor(muted);
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
        canvas.drawPath(path, paint);

        // draw quadrilateral root bg
        paint.setColor(vibrant);
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
        canvas.drawPath(path, paint);
        // quadrilateral end

        paint.setColor(lightMuted);
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
        canvas.drawPath(path, paint);
        // pipe end

        // draw triangle final
        paint.setColor(lightMuted);
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
        canvas.drawPath(path, paint);
        // triangle end

        paint.setColor(darkVibrant);
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
        canvas.drawPath(path, paint);
        // triangle end
    }
}
