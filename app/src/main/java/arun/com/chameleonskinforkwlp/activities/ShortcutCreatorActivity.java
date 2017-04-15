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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import arun.com.chameleonskinforkwlp.R;

public class ShortcutCreatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The intent we pass on to launcher
        Intent shortcutResult = new Intent();

        // The intent that the launcher should launch
        Intent shortcutCamera = new Intent(this, ShortcutCameraActivity.class);
        shortcutCamera.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        shortcutResult.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutCamera);
        shortcutResult.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.shortcut_name));
        shortcutResult.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(this,
                        R.mipmap.ic_launcher));
        setResult(RESULT_OK, shortcutResult);
        finish();
    }
}
