package arun.com.chameleonskinforkwlp;

import android.content.ComponentName;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

import arun.com.chameleonskinforkwlp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setHandlers(this);
        setSupportActionBar(mainBinding.toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainBinding.unbind();
    }

    /**
     * Handler for FAB click event. Used to initiate taking picture flow.
     *
     * @param view FAB
     */
    public void onFabClick(View view) {
        final Intent capturePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Check if any app can handle this intent for us.
        final ComponentName componentName = capturePicIntent.resolveActivity(getPackageManager());
        if (componentName != null) {
            File imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (imageFile != null) {
                final Uri photoURI = FileProvider.getUriForFile(this,
                        "arun.com.chameleonskinforkwlp.fileprovider",
                        imageFile);
                capturePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(capturePicIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Snackbar.make(mainBinding.coordinatorLayout, R.string.could_not_launch_camera, Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(mainBinding.coordinatorLayout, R.string.camera_not_found, Snackbar.LENGTH_LONG).show();
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
                    .into(mainBinding.backdrop);
        }
    }
}
