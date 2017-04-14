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

package arun.com.chameleonskinforkwlp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Util {
    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return color != 0 ? new ColorDrawable(color) : null;
    }

    public static boolean isPackageInstalled(@NonNull Context c, @Nullable String pkgName) {
        if (pkgName == null) return false;

        PackageManager pm = c.getApplicationContext().getPackageManager();
        try {
            pm.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void openPlayStore(@NonNull Context context, @NonNull String appPackageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static void openApp(Activity activity, String packageName) {
        if (isPackageInstalled(activity, packageName)) {
            final Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                activity.startActivity(intent);
            }
        }
    }
}
