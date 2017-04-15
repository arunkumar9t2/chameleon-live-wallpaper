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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import arun.com.chameleonskinforkwlp.R;
import arun.com.chameleonskinforkwlp.engine.ExtractorService;

public class ShortcutCameraActivity extends CameraCapturerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissionAndLaunchCamera();
    }

    @Override
    protected void onCapturedSuccessfully() {
        Toast.makeText(this, R.string.picture_captured_successfully, Toast.LENGTH_SHORT).show();
        ExtractorService.startExtraction(this, takenPhotoPath);
        finish();
    }

    @Override
    protected void onCapturingCancelled() {
        Toast.makeText(this, R.string.user_cancelled_taking_picture, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onRequiredCameraPermissionRationale() {
        Toast.makeText(this, R.string.camera_permission_rationale, Toast.LENGTH_LONG).show();
        final Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNoCameraDetected() {
        Toast.makeText(this, R.string.camera_not_found, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onCameraLaunchingFailed() {
        Toast.makeText(this, R.string.could_not_launch_camera, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onUserDeniedPermission() {
        Toast.makeText(this, R.string.need_camera_permssion, Toast.LENGTH_LONG).show();
        finish();
    }
}
