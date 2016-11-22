package com.example.player.fragment;

import com.example.player.R;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by LiJiaZhi on 16/11/22.
 * 唱片
 */
public class QueueFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_queue;
    }

    @Override
    protected void initView(View rootView, Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.playing_queue);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
