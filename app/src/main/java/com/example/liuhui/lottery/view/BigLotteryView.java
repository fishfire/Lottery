package com.example.liuhui.lottery.view;

import java.util.List;

import android.view.ViewGroup;

/**
 * 大乐透
 * Date: 2017-05-23
 *
 * @author liuhui8
 */
public interface BigLotteryView {
    void getLocationsOnScreen(ViewGroup viewGroup, List<int[]> locationsList);
}
