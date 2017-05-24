package com.example.liuhui.lottery.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;

import com.example.liuhui.lottery.bean.Ball;
import com.example.liuhui.lottery.constant.GlobalConstant;
import com.example.liuhui.lottery.utils.ExtendUtils;
import com.example.liuhui.lottery.view.BigLotteryView;


/**
 * 大乐透实现类
 * Date: 2017-05-23
 *
 * @author liuhui8
 */
public class BigLotteryPresenterImpl implements BigLotteryPresenter {
    private Context mContext;
    private BigLotteryView mView;

    public BigLotteryPresenterImpl(Context context, BigLotteryView view) {
        this.mContext = context;
        mView = view;
    }

    @Override
    public void generaterSource(List<Ball> ballList, int max) {
        for (int i = 1; i <= max; i++) {
            ballList.add(generatorBall(i, max == GlobalConstant.BigLottery.RED ? Ball.RED : Ball.BLUE));
        }
    }

    @Override
    public void resetSourceDataSet(List<Ball> ballList) {
        if (ExtendUtils.listIsNullOrEmpty(ballList)) {
            return;
        }
        for (Ball ball : ballList) {
            if (ball != null) {
                ball.isSelect = false;
            }
        }
    }

    @Override
    public void randomSource(List<Ball> list) {
        if (list == null) {
            return;
        }
        List<Integer> reds = new ArrayList<>();
        randomSourceIndex(reds, GlobalConstant.BigLottery.RED, 5);
        List<Integer> blues = new ArrayList<>();
        randomSourceIndex(blues, GlobalConstant.BigLottery.BLUE, 2);
        list.clear();
        list.addAll(generaterBalls(reds, blues));
    }

    @Override
    public void randomSourceIndex(List<Integer> reds, int max, int count) {
        if (reds == null || max <= 0 || count <= 0) {
            return;
        }
        Random random = new Random();
        while (reds.size() < count) {
            int i = random.nextInt(max) + 1;
            if (!reds.contains(i)) {
                reds.add(i);
            }
        }
        Collections.sort(reds);
    }

    private List<Ball> generaterBalls(List<Integer> reds, List<Integer> blues) {
        List<Ball> list = new ArrayList<>();
        for (Integer red : reds) {
            list.add(generatorBall(red, Ball.RED));
        }

        for (Integer blue : blues) {
            list.add(generatorBall(blue, Ball.BLUE));
        }
        return list;
    }

    private Ball generatorBall(int i, @Ball.BallType int type) {
        StringBuilder builder = new StringBuilder();
        Ball ball = new Ball(type);
        //前9个球，添加0
        builder.append(i <= 9 ? "0" : "");
        builder.append(String.valueOf(i));
        ball.name = builder.toString();
        return ball;
    }
}
