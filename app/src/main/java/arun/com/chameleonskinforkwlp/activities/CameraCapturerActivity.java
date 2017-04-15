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

package arun.com.chameleonskinforkwlp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

public abstract class CameraCapturerActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 112;
    /**
     * Temporary location of the photo that was taken.
     */
    protected String takenPhotoPath;

    /**
     * Called when the user has successfully captured a photo. Use {@link #takenPhotoPath} to
     * retrieve the location of the stored photo and do something with it.
     */
    protected abstract void onCapturedSuccessfully();

    /**
     * User cancelled taking the picture.
     */
    protected abstract void onCapturingCancelled();

    /**
     * User denied the permission to camera, we have to explain why the camera permission is needed
     * as {@link ActivityCompat#requestPermissions(Activity, String[], int)} would no longer accept
     * our requests.
     */
    protected abstract void onRequiredCameraPermissionRationale();

    /**
     * We found no compatible camera apps found on the system.
     */
    protected abstract void onNoCameraDetected();

    /**
     * Trying to launch the camera app failed.
     */
    protected abstract void onCameraLaunchingFailed();

    /**
     * User denied the permission after we requested the permission.
     */
    protected abstract void onUserDeniedPermission();


    /**
     * This is the API method which will initiate the camera picture taking process.
     */
    protected void checkPermissionAndLaunchCamera() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                onRequiredCameraPermissionRationale();
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
                imageFile = createTemporaryFile();
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
                onCameraLaunchingFailed();
            }
        } else {
            onNoCameraDetected();
        }
    }

    /**
     * Create a temporary file on our storage directory for the camera app to write the photo to.
     * We also save the location in {@link #takenPhotoPath} so that we can use it after the user
     * takes the photo.
     *
     * @return The file that was created.
     * @throws IOException
     */
    private File createTemporaryFile() throws IOException {
        final String imageFileName = "CAPTURED_IMAGE";
        final File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        final File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        takenPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                onCapturedSuccessfully();
            } else if (resultCode == RESULT_CANCELED) {
                onCapturingCancelled();
            }
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
                    onUserDeniedPermission();
                }
            }
        }
    }
}
