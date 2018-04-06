/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.amohnacs.moviemenu.mainref.ui;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.amohnacs.moviemenu.R;

/*
 * MainActivity class that loads {@link MainFragment}.
 */
public class MainActivity extends Activity {
    public static final String TAG = "DeviceTypeRuntimeCheck";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //because we are using leanback launcher, checking for TV is not necessary but it is good habit
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            //take action to update config
            Log.d(TAG, "Running on a TV Device");
        } else {
            Log.d(TAG, "Running on a non-TV Device");
        }

    }
}
