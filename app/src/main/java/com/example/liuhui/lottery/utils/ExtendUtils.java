package com.example.liuhui.lottery.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Date: 2017-05-24
 *
 */
public class ExtendUtils {
    private static final String TAG = "ExtendUtils";

    /**
     * dp -> px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px -> dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取状态栏/通知栏的高度
     *
     * @return px 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        if (context instanceof Activity) {
            Rect frame = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            if (statusBarHeight > 0) {
                return statusBarHeight;
            }

            // 反射获取高度
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
                return statusBarHeight;
            } catch (Exception e) {
                Log.e(TAG, "get status bar height error.");
            }
        }

        // 以上均失效时，使用默认高度为25dp。
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) Math.ceil(25 * metrics.density);
    }

    /**
     * 对RecyclerView 中的GridLayoutManager设置特殊列在有的列数
     * @param adapter
     * @param gridLayoutManager
     * @param size 在有多少列
     */
    private void setSpanCount(final RecyclerView.Adapter adapter, GridLayoutManager gridLayoutManager, final int size) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = adapter.getItemViewType(position);
                if (type == 2) { //真实开发中类型自己定义
                    return 1;//只占一行中的一列，
                } else {
                    return size;//独占一行
                }
            }
        });
    }


    /**
     * 删除列表中为空的数据
     *
     * @param dataList 列表数据
     */
    public static void removeNullDataInList(List dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        Iterator iterator = dataList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == null) {
                iterator.remove();
            }
        }
    }

    /**
     * 判断列表是否为空
     *
     * @param collection 列表数据
     * @return true: 为空 false: 不为空
     */
    public static boolean listIsNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 去掉List中的null
     *
     * @param list 原始List
     * @return 去null以后的list
     */
    public static <T> List<T> removeNull(List<T> list) {
        if (list != null) {
            for (int pos = list.size() - 1; pos >= 0; pos--) {
                if (list.get(pos) == null) {
                    list.remove(pos);
                }
            }
        }
        return list;
    }


}
