/*
 * illumina, a pilight remote
 *
 * Copyright (c) 2014 Peter Heisig <http://google.com/+PeterHeisig>
 *
 * illumina is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * illumina is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with illumina. If not, see <http://www.gnu.org/licenses/>.
 */

package nl.pilight.illumina.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import nl.pilight.Illumina;
import nl.pilight.illumina.BuildConfig;
import nl.pilight.illumina.R;
import nl.pilight.illumina.fragment.SettingsFragment;

public class PreferenceActivity extends FragmentActivity implements
        SettingsFragment.SettingsListener {

    public static final Logger log = LoggerFactory.getLogger(PreferenceActivity.class);

    private String mCurrentTheme;

    private SharedPreferences getPreferences() {
        return ((Illumina) getApplication()).getSharedPreferences();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {
        log.info("onCreate");

        mCurrentTheme = getPreferences().getString(
                Illumina.PREF_THEME, getString(R.string.theme_default));

        setTheme(getResources().getIdentifier(mCurrentTheme, "style", getPackageName()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            requestWindowFeature(Window.FEATURE_ACTION_BAR);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_fragment);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getActionBar() != null) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void refreshTheme() {
        final String possiblyUpdatedTheme = getPreferences().getString(
                Illumina.PREF_THEME, getString(R.string.theme_default));

        if (!TextUtils.equals(mCurrentTheme, possiblyUpdatedTheme)) {
            finish();
            startActivity(new Intent(this, getClass()));
        }
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setMessage(String.format(Locale.getDefault(),
                            "Version: %s\nVariant: %s\nBuild: %s",
                            BuildConfig.VERSION_NAME,
                            BuildConfig.FLAVOR,
                            BuildConfig.BUILD_TYPE))
                    .setCancelable(true)
                    .create()
                    .show();
        }

        return super.onKeyLongPress(keyCode, event);
    }

}
