package com.example.liuhui.lottery.presenter;

import java.util.List;

import com.example.liuhui.lottery.bean.Ball;


/**
 * 大乐透
 * Date: 2017-05-23
 *
 * @author liuhui8
 */
public interface BigLotteryPresenter {

    void generaterSource(List<Ball> ballList, int max);

    void resetSourceDataSet(List<Ball> ballList);

    void randomSource(List<Ball> list);

    void randomSourceIndex(List<Integer> reds, int max, int count);

}
