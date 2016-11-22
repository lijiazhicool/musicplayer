package com.example.player.activity;

import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.example.player.R;
import com.example.player.fragment.MainFragment;
import com.example.player.fragment.PlaylistFragment;
import com.example.player.fragment.QueueFragment;
import com.example.player.utils.NavigationUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements ATEActivityThemeCustomizer {
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private TextView songtitle, songartist;
    private ImageView albumart;
    private boolean isDarkTheme;
    private Runnable runnable;
    private Handler navDrawerRunnable = new Handler();

    MainFragment mainFragment;
    PlaylistFragment playlistFragment;
    QueueFragment queueFragment;
    Fragment currentFragment;
    private Runnable navigateLibrary = new Runnable() {
        public void run() {
            navigationView.getMenu().findItem(R.id.nav_library).setChecked(true);
            if (null == mainFragment) {
                mainFragment = new MainFragment();
            }
            if (null != currentFragment) {
                hideFragment(currentFragment);
            }
            if (!mainFragment.isAdded()) {
                addFragment(R.id.fragment_container, mainFragment);
            } else {
                showFragment(mainFragment);
            }
            currentFragment = mainFragment;
        }
    };
    Runnable navigatePlaylist = new Runnable() {
        public void run() {
            navigationView.getMenu().findItem(R.id.nav_playlists).setChecked(true);
            if (null == playlistFragment) {
                playlistFragment = new PlaylistFragment();
            }
            if (null != currentFragment) {
                hideFragment(currentFragment);
            }
            if (!playlistFragment.isAdded()) {
                addFragment(R.id.fragment_container, playlistFragment);
            } else {
                showFragment(playlistFragment);
            }
            currentFragment = playlistFragment;
        }
    };
    Runnable navigateQueue = new Runnable() {
        public void run() {
            navigationView.getMenu().findItem(R.id.nav_queue).setChecked(true);
            if (null == queueFragment) {
                queueFragment = new QueueFragment();
            }
            if (null != currentFragment) {
                hideFragment(currentFragment);
            }
            if (!queueFragment.isAdded()) {
                addFragment(R.id.fragment_container, queueFragment);
            } else {
                showFragment(queueFragment);
            }
            currentFragment = queueFragment;
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initBundleExtra() {

    }

    @Override
    protected void findViewById() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.inflateHeaderView(R.layout.nav_header);
        albumart = (ImageView) header.findViewById(R.id.album_art);
        songtitle = (TextView) header.findViewById(R.id.song_title);
        songartist = (TextView) header.findViewById(R.id.song_artist);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        isDarkTheme = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false);

        navDrawerRunnable.postDelayed(new Runnable() {
            @Override
            public void run() {
                setupDrawerContent(navigationView);
                setupNavigationIcons(navigationView);
            }
        }, 700);

        navigateLibrary.run();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (isNavigatingMain()) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else
                    super.onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNavigatingMain() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return (currentFragment instanceof MainFragment || currentFragment instanceof QueueFragment
            || currentFragment instanceof PlaylistFragment);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
                updatePosition(menuItem);
                return true;

            }
        });
    }

    private void setupNavigationIcons(NavigationView navigationView) {

        // material-icon-lib currently doesn't work with navigationview of design support library 22.2.0+
        // set icons manually for now
        // https://github.com/code-mc/material-icon-lib/issues/15
        if (!isDarkTheme) {
            navigationView.getMenu().findItem(R.id.nav_library).setIcon(R.drawable.library_music);
            navigationView.getMenu().findItem(R.id.nav_playlists).setIcon(R.drawable.playlist_play);
            navigationView.getMenu().findItem(R.id.nav_queue).setIcon(R.drawable.music_note);
            navigationView.getMenu().findItem(R.id.nav_nowplaying).setIcon(R.drawable.bookmark_music);
            navigationView.getMenu().findItem(R.id.nav_settings).setIcon(R.drawable.settings);
            navigationView.getMenu().findItem(R.id.nav_help).setIcon(R.drawable.help_circle);
            navigationView.getMenu().findItem(R.id.nav_about).setIcon(R.drawable.information);
            // navigationView.getMenu().findItem(R.id.nav_donate).setIcon(R.drawable.payment_black);
        } else {
            navigationView.getMenu().findItem(R.id.nav_library).setIcon(R.drawable.library_music_white);
            navigationView.getMenu().findItem(R.id.nav_playlists).setIcon(R.drawable.playlist_play_white);
            navigationView.getMenu().findItem(R.id.nav_queue).setIcon(R.drawable.music_note_white);
            navigationView.getMenu().findItem(R.id.nav_nowplaying).setIcon(R.drawable.bookmark_music_white);
            navigationView.getMenu().findItem(R.id.nav_settings).setIcon(R.drawable.settings_white);
            navigationView.getMenu().findItem(R.id.nav_help).setIcon(R.drawable.help_circle_white);
            navigationView.getMenu().findItem(R.id.nav_about).setIcon(R.drawable.information_white);
            // navigationView.getMenu().findItem(R.id.nav_donate).setIcon(R.drawable.payment_white);
        }
    }

    private void updatePosition(final MenuItem menuItem) {
        runnable = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_library:
                runnable = navigateLibrary;
                break;
            case R.id.nav_playlists:
                runnable = navigatePlaylist;
                break;
            case R.id.nav_nowplaying:
                // NavigationUtils.navigateToNowplaying(MainActivity.this, false);
                break;
            case R.id.nav_queue:
                runnable = navigateQueue;

                break;
            case R.id.nav_settings:
                 NavigationUtils.navigateToSettings(MainActivity.this);
                break;
            case R.id.nav_help:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:lijiazhicool@gmail.com");
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.nav_about:
                mDrawerLayout.closeDrawers();
                // Handler handler = new Handler();
                // handler.postDelayed(new Runnable() {
                // @Override
                // public void run() {
                // Helpers.showAbout(MainActivity.this);
                // }
                // }, 350);

                break;
            case R.id.nav_donate:
                // startActivity(new Intent(MainActivity.this, DonateActivity.class));
                break;
        }

        if (runnable != null) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }, 350);
        }
    }

    @Override
    public int getActivityTheme() {
        return isDarkTheme ? R.style.AppThemeNormalDark : R.style.AppThemeNormalLight;
    }
}
