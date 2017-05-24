package com.example.liuhui.lottery.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.liuhui.lottery.R;
import com.example.liuhui.lottery.bean.Ball;
import com.example.liuhui.lottery.utils.ExtendUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorInflater;

/**
 * TODO: description
 * Date: 2017-05-24
 *
 * @author liuhui8
 */
public class RandomView extends LinearLayout {
    public RandomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addView(Ball ball) {
        if (ball == null) {
            return;
        }
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ExtendUtils.dip2px(getContext(), 40), ExtendUtils.dip2px(getContext(), 40));
        layoutParams.leftMargin = ExtendUtils.dip2px(getContext(), 16);
        textView.setLayoutParams(layoutParams);
        textView.setText(ball.name);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(ContextCompat.getColorStateList(getContext(), ball.type == Ball.RED ? R.color.text_color_red_and_white : R.color.text_color_blue_and_white));
        textView.setBackgroundResource(ball.type == Ball.RED ? R.drawable.bg_select_circle_white_red : R.drawable.bg_select_circle_white_blue);
        textView.setSelected(ball.isSelect);
        addView(textView);

    }

    public void startAnim() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            Animator anim = AnimatorInflater.loadAnimator(getContext(), R.anim.anim_0_1);
            anim.setTarget(view);
            anim.setDuration(400 * i);
            anim.start();

        }
    }
}
