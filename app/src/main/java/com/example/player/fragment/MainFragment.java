package com.example.player.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.player.R;
import com.example.player.utils.PreferencesUtility;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by LiJiaZhi on 16/11/22.
 * 主界面fragment
 */

public class MainFragment extends BaseFragment {
    //为了标记当前的tabindex 切到后台再且回来保持状态
    private PreferencesUtility mPreferences;
    private ViewPager mViewPager;
    private Adapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = PreferencesUtility.getInstance(getActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View rootView, Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        if (mViewPager != null) {
            mAdapter = new Adapter(getActivity(), getChildFragmentManager());
            mAdapter.addFragment(new SongsFragment(), this.getString(R.string.songs));
            mAdapter.addFragment(new AlbumFragment(), this.getString(R.string.albums));
            mAdapter.addFragment(new ArtistFragment(), this.getString(R.string.artists));
            mViewPager.setAdapter(mAdapter);
            mViewPager.setOffscreenPageLimit(2);
        }

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mAdapter.getTabView(i));
        }
    }

    @Override
    protected void initData() {
        mViewPager.setCurrentItem(mPreferences.getStartPageIndex());
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPreferences.lastOpenedIsStartPagePreference()) {
            mPreferences.setStartPageIndex(mViewPager.getCurrentItem());
        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        private Context context;

        public Adapter(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

        public View getTabView(int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_custom_tableview, null);
            TextView tv = (TextView) view.findViewById(R.id.item_smartlayout_tableview_tv);
            tv.setText(mFragmentTitles.get(position));
            return view;
        }

    }
}
