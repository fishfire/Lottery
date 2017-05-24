package com.example.liuhui.lottery.bean;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.support.annotation.IntDef;

/**
 * 所有球类模型（双色球，大乐透）
 * Date: 2017-05-24
 *
 * @author liuhui8
 */
public class Ball implements Serializable {
    public static final int RED = 1;
    public static final int BLUE = 2;

    @IntDef({RED, BLUE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BallType {
    }

    public Ball(@BallType int type) {
        this.type = type;
    }

    public String name;
    public int type;
    public boolean isSelect;
}
