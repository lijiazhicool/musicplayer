package com.example.player.activity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.ATEActivity;
import com.example.player.model.LPBaseEventBean;
import com.example.player.utils.FragmentAnimUtil;
import com.example.player.R;
import com.example.player.utils.Helpers;
import com.example.player.utils.NavigationUtils;

/**
 * Created by LiJiaZhi on 16/11/7. base
 */

public abstract class BaseActivity extends ATEActivity {

    protected FrameLayout mContentLayout;

    protected View rootView;


    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initBundleExtra();

    protected abstract void findViewById();

    protected abstract void initListeners();

    protected abstract void initData(Bundle savedInstanceState);

    protected boolean isStartEventBus() {
        return false;
    }

    @Nullable
    @Override
    public String getATEKey() {
        return Helpers.getATEKey(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ATE.applyMenu(this, getATEKey(), menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        if (bundle != null) {
            // 如果系统回收的Activity， 但是系统却保留了Fragment， 当Activity被重新初始化， 此时， 系统保存的Fragment 的getActivity为空，
            // 所以要移除旧的Fragment ， 重新初始化新的Fragment
            String FRAGMENTS_TAG = "android:support:fragments";
            bundle.remove(FRAGMENTS_TAG);
        }
        super.onCreate(bundle);
        rootView = LayoutInflater.from(this).inflate(R.layout.lp_activity_base, null);
        setContentView(rootView);

        mContentLayout = (FrameLayout) findViewById(R.id.layout_content);

        int layoutId = getLayoutId();
        if (layoutId != 0) {
            mContentLayout.addView(LayoutInflater.from(this).inflate(layoutId, null));
        }

        initPopupFragment(getPopupFragment());

        initBundleExtra();
        findViewById();
        initListeners();
        initData(bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isStartEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(LPBaseEventBean event) {
    }

    @Override
    public void onStop() {
        if (isStartEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    protected void initPopupFragment(Fragment popupFragment) {
        if (popupFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.layout_content, popupFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    protected Fragment getPopupFragment() {
        return null;
    }

    protected void addFragment(int layoutId, Fragment fragment, boolean addToBackStack, String fragmentTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        int inAnim = FragmentAnimUtil.getInAnim(fragment);
//        int outAnim = FragmentAnimUtil.getOutAnim(fragment);
//        if (inAnim != 0 && outAnim != 0) {
//            transaction.setCustomAnimations(inAnim, outAnim);
//        }
        if (fragmentTag == null) {
            transaction.add(layoutId, fragment);
        } else {
            transaction.add(layoutId, fragment, fragmentTag);
        }
        transaction.commitAllowingStateLoss();
    }

    protected void addFragment(int layoutId, Fragment fragment, boolean addToBackStack) {
        addFragment(layoutId, fragment, addToBackStack, null);
    }

    protected void addFragment(int layoutId, Fragment fragment, String tag) {
        addFragment(layoutId, fragment, false, tag);
    }

    public void addFragment(int layoutId, Fragment fragment) {
        addFragment(layoutId, fragment, false);
    }

    protected Fragment findFragment(int layoutId) {
        FragmentManager fm = getSupportFragmentManager();
        return fm.findFragmentById(layoutId);
    }

    protected void removeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int inAnim = FragmentAnimUtil.getInAnim(fragment);
        int outAnim = FragmentAnimUtil.getOutAnim(fragment);
        if (inAnim != 0 && outAnim != 0) {
            transaction.setCustomAnimations(inAnim, outAnim);
        }
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();
        // getSupportFragmentManager().executePendingTransactions();
    }

    protected void hideFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        int inAnim = FragmentAnimUtil.getInAnim(fragment);
//        int outAnim = FragmentAnimUtil.getOutAnim(fragment);
//        if (inAnim != 0 && outAnim != 0) {
//            transaction.setCustomAnimations(inAnim, outAnim, inAnim, outAnim);
//        }
        transaction.hide(fragment);
        transaction.commitAllowingStateLoss();
    }

    protected void replaceFragment(int layoutId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(layoutId, fragment);
        transaction.commitAllowingStateLoss();
    }

    protected void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        int inAnim = FragmentAnimUtil.getInAnim(fragment);
//        int outAnim = FragmentAnimUtil.getOutAnim(fragment);
//        if (inAnim != 0 && outAnim != 0) {
//            transaction.setCustomAnimations(inAnim, outAnim);
//        }
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }

    public void switchFragment(Fragment from, Fragment to, int layoutId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int inAnim = FragmentAnimUtil.getInAnim(from);
        int outAnim = FragmentAnimUtil.getOutAnim(from);
        if (inAnim != 0 && outAnim != 0) {
            transaction.setCustomAnimations(inAnim, outAnim, inAnim, outAnim);
        }
        if (!to.isAdded()) { // 先判断是否被add过
            transaction.hide(from).add(layoutId, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_settings:
                NavigationUtils.navigateToSettings(this);
                return true;
            case R.id.action_shuffle:
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        MusicPlayer.shuffleAll(BaseActivity.this);
//                    }
//                }, 80);

                return true;
            case R.id.action_search:
                NavigationUtils.navigateToSearch(this);
                return true;
            case R.id.action_equalizer:
//                NavigationUtils.navigateToEqualizer(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
