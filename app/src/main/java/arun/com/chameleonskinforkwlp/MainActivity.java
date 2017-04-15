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

package arun.com.chameleonskinforkwlp;

import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import arun.com.chameleonskinforkwlp.activities.CameraCapturerActivity;
import arun.com.chameleonskinforkwlp.databinding.ActivityMainBinding;
import arun.com.chameleonskinforkwlp.engine.ExtractorService;
import arun.com.chameleonskinforkwlp.preferences.Preferences;
import arun.com.chameleonskinforkwlp.util.Util;
import arun.com.chameleonskinforkwlp.wallpaper.ChameleonWallpaperService;

public class MainActivity extends CameraCapturerActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandlers(this);

        initToolbar();
        initThemeSelector();
        initSetWallpaperPrompt();
    }

    /**
     * Prompt the user to set our app as live wallpaper if the user has not set it.
     */
    private void initSetWallpaperPrompt() {
        final WallpaperManager wm = WallpaperManager.getInstance(getApplicationContext());
        if ((wm.getWallpaperInfo() != null && wm.getWallpaperInfo().getPackageName().equalsIgnoreCase(getPackageName()))) {
            // We are good
        } else {
            // Ask user.
            Snackbar.make(binding.coordinatorLayout, R.string.set_live_wallpaer_promt, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            launchSetWallpaperScreen();
                        }
                    }).show();
        }
    }


    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        Glide.with(this).load(R.drawable.backdrop).centerCrop().into(binding.backdrop);
        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.backdrop)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                .withCompactStyle(true)
                .build();
        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(binding.toolbar)
                .withAccountHeader(header)
                .withSelectedItem(-1)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.set_as_system_wallpaper)
                                .withIdentifier(1)
                                .withSelectable(false)
                                .withIcon(GoogleMaterial.Icon.gmd_wallpaper),
                        new PrimaryDrawerItem().withName(R.string.use_with_kustom)
                                .withSelectable(false)
                                .withIcon(GoogleMaterial.Icon.gmd_palette)
                                .withIdentifier(2),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem()
                                .withName(R.string.rate_this_app)
                                .withSelectable(false)
                                .withIdentifier(3)
                                .withIcon(GoogleMaterial.Icon.gmd_star),
                        new PrimaryDrawerItem()
                                .withName(R.string.privacy_policy)
                                .withIcon(GoogleMaterial.Icon.gmd_insert_drive_file)
                                .withSelectable(false)
                                .withIdentifier(4),
                        new DividerDrawerItem()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            switch (drawerItem.getIdentifier()) {
                                case 1:
                                    launchSetWallpaperScreen();
                                    break;
                                case 2:
                                    if (Util.isPackageInstalled(MainActivity.this, "org.kustom.wallpaper")) {
                                        Util.openApp(MainActivity.this, "org.kustom.wallpaper");
                                    } else {
                                        Snackbar.make(binding.coordinatorLayout, R.string.kustom_not_found, Snackbar.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 3:
                                    Util.openPlayStore(MainActivity.this, getPackageName());
                                    break;
                                case 4:
                                    Intent licenses = new Intent(Intent.ACTION_VIEW, Uri.parse("http://htmlpreview.github.com/?https://github.com/arunkumar9t2/chameleon-live-wallpaper/blob/master/privacy_policy.html"));
                                    startActivity(licenses);
                                    break;
                            }
                        }
                        return false;
                    }
                })
                .build();
    }

    private void launchSetWallpaperScreen() {
        final Intent setWallpaper = new Intent();
        setWallpaper.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        setWallpaper.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(getPackageName(), ChameleonWallpaperService.class.getName()));
        startActivityForResult(setWallpaper, 0);
    }

    private void initThemeSelector() {
        binding.setTheme(Preferences.get(this).getTheme());
        binding.executePendingBindings();
        Glide.with(this).load(R.drawable.lollipop).centerCrop().into(binding.lollipopThemeThumbnail);
        Glide.with(this).load(R.drawable.marshmallow).centerCrop().into(binding.marshmallowThemeThumbnail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    /**
     * Handler for FAB click event. Used to initiate taking picture flow.
     *
     * @param view FAB
     */
    public void onFabClick(View view) {
        checkPermissionAndLaunchCamera();
    }

    /**
     * Handler that will be fired when clicking either of the theme.
     *
     * @param view View that was clicked.
     */
    public void onThemeSelectorClicked(View view) {
        binding.setTheme(Preferences.get(this).toggleTheme());
        binding.executePendingBindings();
    }

    public void onFormulaClicked(View view) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            final CharSequence textToCopy = textView.getText();
            final ClipData clip = ClipData.newPlainText("formula", textToCopy);
            Snackbar.make(binding.coordinatorLayout, String.format(getString(R.string.copied_alert), textToCopy), Snackbar.LENGTH_SHORT).show();
            clipboard.setPrimaryClip(clip);
        }
    }

    @Override
    protected void onCapturedSuccessfully() {
        Glide.with(this)
                .load(takenPhotoPath)
                .into(binding.backdrop);
        ExtractorService.startExtraction(this, takenPhotoPath);
    }

    @Override
    protected void onCapturingCancelled() {
        Snackbar.make(binding.coordinatorLayout, R.string.user_cancelled_taking_picture, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onRequiredCameraPermissionRationale() {
        Snackbar.make(binding.coordinatorLayout, R.string.camera_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.grant, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                }).show();
    }

    @Override
    protected void onNoCameraDetected() {
        Snackbar.make(binding.coordinatorLayout, R.string.camera_not_found, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onCameraLaunchingFailed() {
        Snackbar.make(binding.coordinatorLayout, R.string.could_not_launch_camera, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onUserDeniedPermission() {
        Snackbar.make(binding.coordinatorLayout, R.string.need_camera_permssion, Snackbar.LENGTH_SHORT).show();
    }
}
