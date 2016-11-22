package com.example.player.utils;


import android.app.Activity;
import android.content.Intent;

import com.example.player.activity.SearchActivity;
import com.example.player.activity.SettingsActivity;

public class NavigationUtils {

    public static void navigateToSettings(Activity context) {
        final Intent intent = new Intent(context, SettingsActivity.class);
        if (!PreferencesUtility.getInstance(context).getSystemAnimations()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        intent.setAction(Constants.NAVIGATE_SETTINGS);
        context.startActivity(intent);
    }

    public static void navigateToSearch(Activity context) {
        final Intent intent = new Intent(context, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setAction(Constants.NAVIGATE_SEARCH);
        context.startActivity(intent);
    }

    public static Intent getNavigateToStyleSelectorIntent(Activity context, String what) {
        final Intent intent = new Intent(context, SettingsActivity.class);
        if (!PreferencesUtility.getInstance(context).getSystemAnimations()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        intent.setAction(Constants.SETTINGS_STYLE_SELECTOR);
        intent.putExtra(Constants.SETTINGS_STYLE_SELECTOR_WHAT, what);
        return intent;
    }
}
