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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;

public class Swatch implements Parcelable {
    @ColorInt
    private int backgroundColor;
    @ColorInt
    private int foregroundColor;

    public Swatch(@ColorInt int backgroundColor, @ColorInt int foregroundColor) {
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    Swatch(@Nullable Palette.Swatch paletteSwatch) {
        if (paletteSwatch != null) {
            this.backgroundColor = paletteSwatch.getRgb();
            this.foregroundColor = paletteSwatch.getTitleTextColor();
        }
    }

    private Swatch(Parcel in) {
        backgroundColor = in.readInt();
        foregroundColor = in.readInt();
    }

    public static final Creator<Swatch> CREATOR = new Creator<Swatch>() {
        @Override
        public Swatch createFromParcel(Parcel in) {
            return new Swatch(in);
        }

        @Override
        public Swatch[] newArray(int size) {
            return new Swatch[size];
        }
    };

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(@ColorInt int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Swatch swatch = (Swatch) o;

        return backgroundColor == swatch.backgroundColor && foregroundColor == swatch.foregroundColor;

    }

    @Override
    public int hashCode() {
        int result = backgroundColor;
        result = 31 * result + foregroundColor;
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(backgroundColor);
        dest.writeInt(foregroundColor);
    }

    @Override
    public String toString() {
        return "Swatch{" +
                "backgroundColor=" + backgroundColor +
                ", foregroundColor=" + foregroundColor +
                '}';
    }

}