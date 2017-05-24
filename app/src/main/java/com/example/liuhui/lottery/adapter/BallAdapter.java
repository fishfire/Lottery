package com.example.liuhui.lottery.adapter;

import java.util.List;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import com.example.liuhui.lottery.R;
import com.example.liuhui.lottery.bean.Ball;
import com.example.liuhui.lottery.event.BigLottoEvent;
import com.example.liuhui.lottery.utils.ExtendUtils;

/**
 * 大乐透适配器
 * Date: 2017-05-24
 *
 * @author liuhui8
 */
public class BallAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<Ball> mList;
    private int mBgDrawableRes;
    private int mTextColorRes;

    public BallAdapter(Context context, List<Ball> list, int mBgDrawableRes, @ColorRes int colorRes) {
        mContext = context;
        mList = list;
        this.mBgDrawableRes = mBgDrawableRes;
        this.mTextColorRes = colorRes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mContext, LayoutInflater.from(mContext).inflate(R.layout.list_item_ball, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (ExtendUtils.listIsNullOrEmpty(mList) || mList.get(position) == null) {
            return;
        }
        Ball ball = mList.get(position);
        holder.setText(R.id.tv_ball, ball.name);
        holder.setTextColorStateList(R.id.tv_ball, ContextCompat.getColorStateList(mContext, mTextColorRes));
        holder.setBackgroundRes(R.id.tv_ball, mBgDrawableRes);
        holder.setSelected(R.id.tv_ball, ball.isSelect);
        holder.setTag(R.id.tv_ball, ball);
        holder.setTag(R.id.tv_ball, R.id.item_holder, holder);
        holder.setOnClickListener(R.id.tv_ball, this);
    }

    @Override
    public int getItemCount() {
        return ExtendUtils.listIsNullOrEmpty(mList) ? 0 : mList.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_ball && v.getTag() instanceof Ball && v.getTag(R.id.item_holder) instanceof ViewHolder) {
            Ball ball = (Ball) v.getTag();
            ball.isSelect = !ball.isSelect;
            ViewHolder holder = (ViewHolder) v.getTag(R.id.item_holder);
            holder.setSelected(R.id.tv_ball, ball.isSelect);
            //跳转到大乐透，BigLottoActivity界面，计算可能数目
            EventBus.getDefault().post(new BigLottoEvent());
        }
    }
}

