package com.example.liuhui.lottery.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 计算recycler的高度
 * Date: 2017-05-24
 *
 * @author liuhui8
 */
public class CalcHeightGridLayoutManager extends GridLayoutManager {

    private int[] mMeasuredDimension = new int[2];

    public CalcHeightGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);

    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        int rows = (int) Math.ceil(getItemCount() * 1.0 / getSpanCount());
        if (getItemCount() > 0) {
            measureScrapChild(recycler, 0,
                    widthSpec,
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    mMeasuredDimension);
        }

        setMeasuredDimension(widthSize, mMeasuredDimension[1] * rows);
    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {
//        try {
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        View view = recycler.getViewForPosition(position);
        if (view != null) {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                    getPaddingTop() + getPaddingBottom(), p.height);
            view.measure(widthSpec, childHeightSpec);
            measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
            measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
            recycler.recycleView(view);
        }

    }
}

