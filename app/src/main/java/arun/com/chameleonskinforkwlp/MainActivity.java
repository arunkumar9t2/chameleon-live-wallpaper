package arun.com.chameleonskinforkwlp;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
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

import java.io.File;
import java.io.IOException;

import arun.com.chameleonskinforkwlp.databinding.ActivityMainBinding;
import arun.com.chameleonskinforkwlp.engine.ExtractorService;
import arun.com.chameleonskinforkwlp.preferences.Preferences;
import arun.com.chameleonskinforkwlp.util.Util;
import arun.com.chameleonskinforkwlp.wallpaper.ChameleonWallpaperService;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 112;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandlers(this);

        initToolbar();
        initThemeSelector();
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
                        new DividerDrawerItem()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            switch (drawerItem.getIdentifier()) {
                                case 1:
                                    final Intent setWallpaper = new Intent();
                                    setWallpaper.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                                    setWallpaper.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(getPackageName(), ChameleonWallpaperService.class.getName()));
                                    startActivityForResult(setWallpaper, 0);
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
                            }
                        }
                        return false;
                    }
                })
                .build();
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

    private void checkPermissionAndLaunchCamera() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Snackbar.make(binding.coordinatorLayout, R.string.camera_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.grant, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        }
    }

    private void launchCamera() {
        final Intent capturePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Check if any app can handle this intent for us.
        final ComponentName componentName = capturePicIntent.resolveActivity(getPackageManager());
        if (componentName != null) {
            File imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException ex) {
                Timber.e("Error while creating image file", ex);
            }
            if (imageFile != null) {
                final Uri photoURI = FileProvider.getUriForFile(this,
                        "arun.com.chameleonskinforkwlp.fileprovider",
                        imageFile);
                capturePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(capturePicIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Snackbar.make(binding.coordinatorLayout, R.string.could_not_launch_camera, Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(binding.coordinatorLayout, R.string.camera_not_found, Snackbar.LENGTH_LONG).show();
        }
    }

    private File createImageFile() throws IOException {
        final String imageFileName = "CAPTURED_IMAGE";
        final File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        final File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Glide.with(this)
                    .load(currentPhotoPath)
                    .into(binding.backdrop);
            ExtractorService.startExtraction(this, currentPhotoPath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionAndLaunchCamera();
                } else {
                    Snackbar.make(binding.coordinatorLayout, R.string.need_camera_permssion, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }
}
