package com.smlnskgmail.jaman.hashchecker;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smlnskgmail.jaman.hashchecker.components.BaseActivity;
import com.smlnskgmail.jaman.hashchecker.navigation.fragments.BaseFragment;
import com.smlnskgmail.jaman.hashchecker.navigation.fragments.FeedbackFragment;
import com.smlnskgmail.jaman.hashchecker.navigation.fragments.MainFragment;
import com.smlnskgmail.jaman.hashchecker.navigation.fragments.SettingsFragment;
import com.smlnskgmail.jaman.hashchecker.navigation.fragments.history.HistoryFragment;
import com.smlnskgmail.jaman.hashchecker.navigation.states.AppResumeTarget;
import com.smlnskgmail.jaman.hashchecker.navigation.states.BackClickTarget;
import com.smlnskgmail.jaman.hashchecker.support.prefs.SettingsHelper;
import com.smlnskgmail.jaman.hashchecker.utils.UIUtils;

public class MainActivity extends BaseActivity {

    public static final String URI_FROM_EXTERNAL_APP = "com.smlnskgmail.jaman.hashchecker.URI_FROM_EXTERNAL_APP";

    @Override
    public void initialize() {
        Intent intent = getIntent();
        String scheme = null;
        ClipData clipData = null;
        if (intent != null) {
            scheme = intent.getScheme();
            clipData = intent.getClipData();
        }
        Uri externalFileUri = null;
        if (clipData != null) {
            externalFileUri = clipData.getItemAt(0).getUri();
        }

        MainFragment mainFragment = new MainFragment();
        if (scheme != null && scheme.compareTo(ContentResolver.SCHEME_CONTENT) == 0) {
            mainFragment.setArguments(getConfiguredBundleWithDataUri(intent.getData()));
            SettingsHelper.setGenerateFromShareIntentMode(this, true);
        } else if (externalFileUri != null) {
            mainFragment.setArguments(getConfiguredBundleWithDataUri(externalFileUri));
            SettingsHelper.setGenerateFromShareIntentMode(this, true);
        } else {
            mainFragment.setArguments(getBundleForShortcutAction(intent.getAction()));
            SettingsHelper.setGenerateFromShareIntentMode(this, false);
        }

        UIUtils.showFragment(getSupportFragmentManager(), mainFragment);
    }

    @NonNull
    private Bundle getConfiguredBundleWithDataUri(@NonNull Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putString(URI_FROM_EXTERNAL_APP, uri.toString());
        return bundle;
    }

    @NonNull
    private Bundle getBundleForShortcutAction(@Nullable String action) {
        Bundle shortcutArguments = new Bundle();
        if (action != null && action.equals(App.ACTION_START_WITH_TEXT)) {
            shortcutArguments.putBoolean(App.ACTION_START_WITH_TEXT, true);
        } else if (action != null && action.equals(App.ACTION_START_WITH_FILE)) {
            shortcutArguments.putBoolean(App.ACTION_START_WITH_FILE, true);
        }
        return shortcutArguments;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        UIUtils.hideKeyboard(this, findViewById(android.R.id.content));
        switch (item.getItemId()) {
            case R.id.menu_main_section_settings:
                UIUtils.showFragment(getSupportFragmentManager(), new SettingsFragment());
                break;
            case R.id.menu_main_section_feedback:
                UIUtils.showFragment(getSupportFragmentManager(), new FeedbackFragment());
                break;
            case R.id.menu_main_section_history:
                UIUtils.showFragment(getSupportFragmentManager(), new HistoryFragment());
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(BaseFragment.CURRENT_FRAGMENT_TAG);
        if (fragment instanceof BackClickTarget) {
            ((BackClickTarget) fragment).onBackClick();
        }
        for (Fragment fragmentInApp: getSupportFragmentManager().getFragments()) {
            if (fragmentInApp instanceof AppResumeTarget) {
                ((AppResumeTarget) fragmentInApp).onAppResume();
            }
        }
    }

}
