package com.example.liuhui.lottery.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;

import com.example.liuhui.lottery.LotteryApplication;

/**
 * <pre>
 *     desc  : SpannableString相关工具类
 * </pre>
 */
public final class SpannableUtils {

    private SpannableUtils() {
    }

    /**
     * 获取建造者
     *
     * @param text 样式字符串文本
     * @return {@link Builder}
     */
    public static Builder getBuilder(@NonNull CharSequence text) {
        return new Builder(LotteryApplication.getAppContext(), text);
    }

    public static final class Builder {
        private Context mContext;

        private int mDefaultColorValue = 0xff000000;
        private int mDefaultTextSize;

        private CharSequence mText;

        private int mTextSize;
        private int mFlag;
        @ColorInt
        private int mForegroundColor;
        @ColorInt
        private int mBackgroundColor;
        @ColorInt
        private int mQuoteColor;

        private boolean mIsLeadingMargin;
        private int mFirst;
        private int mRest;

        private boolean mIsBullet;
        private int mGapWidth;
        private int mBulletColor;

        private float mProportion;
        private float mXProportion;
        private boolean mIsStrikethrough;
        private boolean mIsUnderline;
        private boolean mIsSuperscript;
        private boolean mIsSubscript;
        private boolean mIsBold;
        private boolean mIsItalic;
        private boolean mIsBoldItalic;
        private String mFontFamily;
        private Layout.Alignment mAlign;

        private boolean mImageIsBitmap;
        private Bitmap mBitmap;
        private boolean mImageIsDrawable;
        private Drawable mDrawable;
        private boolean mImageIsUri;
        private Uri mUri;
        private boolean mImageIsResourceId;
        @DrawableRes
        private int mResourceId;

        private ClickableSpan mClickSpan;
        private String mUrl;

        private boolean mIsBlur;
        private float mRadius;
        private BlurMaskFilter.Blur mStyle;

        private SpannableStringBuilder mBuilder;


        private Builder(@NonNull Context context, @NonNull CharSequence text) {
            this.mContext = context;
            this.mText = text;
            mFlag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
            mDefaultTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, mContext.getResources().getDisplayMetrics());
            mTextSize = mDefaultTextSize;
            mForegroundColor = mDefaultColorValue;
            mBackgroundColor = mDefaultColorValue;
            mQuoteColor = mDefaultColorValue;
            mProportion = -1;
            mXProportion = -1;
            mBuilder = new SpannableStringBuilder();
        }


        /**
         * 设置标识
         *
         * @param flag <ul>
         *             <li>{@link Spanned#SPAN_INCLUSIVE_EXCLUSIVE}</li>
         *             <li>{@link Spanned#SPAN_INCLUSIVE_INCLUSIVE}</li>
         *             <li>{@link Spanned#SPAN_EXCLUSIVE_EXCLUSIVE}</li>
         *             <li>{@link Spanned#SPAN_EXCLUSIVE_INCLUSIVE}</li>
         *             </ul>
         * @return {@link Builder}
         */
        public Builder setFlag(int flag) {
            this.mFlag = flag;
            return this;
        }

        /**
         * 设置前景色
         *
         * @param color 前景色
         * @return {@link Builder}
         */
        public Builder setForegroundColor(@ColorInt int color) {
            this.mForegroundColor = color;
            return this;
        }

        public Builder setForegroundColorId(@ColorRes int color) {
            this.mForegroundColor = LotteryApplication.getAppContext().getResources().getColor(color);
            return this;
        }

        /**
         * 设置背景色
         *
         * @param color 背景色
         * @return {@link Builder}
         */
        public Builder setBackgroundColor(@ColorInt int color) {
            this.mBackgroundColor = color;
            return this;
        }

        /**
         * 设置引用线的颜色
         *
         * @param color 引用线的颜色
         * @return {@link Builder}
         */
        public Builder setQuoteColor(@ColorInt int color) {
            this.mQuoteColor = color;
            return this;
        }

        /**
         * 设置字体大小
         *
         * @param textSize
         */
        public Builder setTextSize(int textSize) {
            this.mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, mContext.getResources().getDisplayMetrics());
            return this;
        }

        /**
         * 设置缩进
         *
         * @param first 首行缩进
         * @param rest  剩余行缩进
         * @return {@link Builder}
         */
        public Builder setLeadingMargin(int first, int rest) {
            this.mFirst = first;
            this.mRest = rest;
            mIsLeadingMargin = true;
            return this;
        }

        /**
         * 设置列表标记
         *
         * @param gapWidth 列表标记和文字间距离
         * @param color    列表标记的颜色
         * @return {@link Builder}
         */
        public Builder setBullet(int gapWidth, int color) {
            this.mGapWidth = gapWidth;
            mBulletColor = color;
            mIsBullet = true;
            return this;
        }

        /**
         * 设置字体比例
         *
         * @param proportion 比例
         * @return {@link Builder}
         */
        public Builder setProportion(float proportion) {
            this.mProportion = proportion;
            return this;
        }

        /**
         * 设置字体横向比例
         *
         * @param proportion 比例
         * @return {@link Builder}
         */
        public Builder setXProportion(float proportion) {
            this.mXProportion = proportion;
            return this;
        }

        /**
         * 设置删除线
         *
         * @return {@link Builder}
         */
        public Builder setStrikethrough() {
            this.mIsStrikethrough = true;
            return this;
        }

        /**
         * 设置下划线
         *
         * @return {@link Builder}
         */
        public Builder setUnderline() {
            this.mIsUnderline = true;
            return this;
        }

        /**
         * 设置上标
         *
         * @return {@link Builder}
         */
        public Builder setSuperscript() {
            this.mIsSuperscript = true;
            return this;
        }

        /**
         * 设置下标
         *
         * @return {@link Builder}
         */
        public Builder setSubscript() {
            this.mIsSubscript = true;
            return this;
        }

        /**
         * 设置粗体
         *
         * @return {@link Builder}
         */
        public Builder setBold() {
            mIsBold = true;
            return this;
        }

        /**
         * 设置斜体
         *
         * @return {@link Builder}
         */
        public Builder setItalic() {
            mIsItalic = true;
            return this;
        }

        /**
         * 设置粗斜体
         *
         * @return {@link Builder}
         */
        public Builder setBoldItalic() {
            mIsBoldItalic = true;
            return this;
        }

        /**
         * 设置字体
         *
         * @param fontFamily 字体
         *                   <ul>
         *                   <li>monospace</li>
         *                   <li>serif</li>
         *                   <li>sans-serif</li>
         *                   </ul>
         * @return {@link Builder}
         */
        public Builder setFontFamily(@Nullable String fontFamily) {
            this.mFontFamily = fontFamily;
            return this;
        }

        /**
         * 设置对齐
         *
         * @param align 对其方式
         *              <ul>
         *              <li>{@link Layout.Alignment#ALIGN_NORMAL}正常</li>
         *              <li>{@link Layout.Alignment#ALIGN_OPPOSITE}相反</li>
         *              <li>{@link Layout.Alignment#ALIGN_CENTER}居中</li>
         *              </ul>
         * @return {@link Builder}
         */
        public Builder setAlign(@Nullable Layout.Alignment align) {
            this.mAlign = align;
            return this;
        }

        /**
         * 设置图片
         *
         * @param bitmap 图片位图
         * @return {@link Builder}
         */
        public Builder setBitmap(@NonNull Bitmap bitmap) {
            this.mBitmap = bitmap;
            mImageIsBitmap = true;
            return this;
        }

        /**
         * 设置图片
         *
         * @param drawable 图片资源
         * @return {@link Builder}
         */
        public Builder setDrawable(@NonNull Drawable drawable) {
            this.mDrawable = drawable;
            mImageIsDrawable = true;
            return this;
        }

        /**
         * 设置图片
         *
         * @param uri 图片uri
         * @return {@link Builder}
         */
        public Builder setUri(@NonNull Uri uri) {
            this.mUri = uri;
            mImageIsUri = true;
            return this;
        }

        /**
         * 设置图片
         *
         * @param resourceId 图片资源id
         * @return {@link Builder}
         */
        public Builder setResourceId(@DrawableRes int resourceId) {
            this.mResourceId = resourceId;
            mImageIsResourceId = true;
            return this;
        }

        /**
         * 设置点击事件
         * <p>需添加view.setMovementMethod(LinkMovementMethod.getInstance())</p>
         *
         * @param clickSpan 点击事件
         * @return {@link Builder}
         */
        public Builder setClickSpan(@NonNull ClickableSpan clickSpan) {
            this.mClickSpan = clickSpan;
            return this;
        }

        /**
         * 设置超链接
         * <p>需添加view.setMovementMethod(LinkMovementMethod.getInstance())</p>
         *
         * @param url 超链接
         * @return {@link Builder}
         */
        public Builder setUrl(@NonNull String url) {
            this.mUrl = url;
            return this;
        }

        /**
         * 设置模糊
         * <p>尚存bug，其他地方存在相同的字体的话，相同字体出现在之前的话那么就不会模糊，出现在之后的话那会一起模糊</p>
         * <p>推荐还是把所有字体都模糊这样使用</p>
         *
         * @param radius 模糊半径（需大于0）
         * @param style  模糊样式<ul>
         *               <li>{@link BlurMaskFilter.Blur#NORMAL}</li>
         *               <li>{@link BlurMaskFilter.Blur#SOLID}</li>
         *               <li>{@link BlurMaskFilter.Blur#OUTER}</li>
         *               <li>{@link BlurMaskFilter.Blur#INNER}</li>
         *               </ul>
         * @return {@link Builder}
         */
        public Builder setBlur(float radius, BlurMaskFilter.Blur style) {
            this.mRadius = radius;
            this.mStyle = style;
            this.mIsBlur = true;
            return this;
        }

        /**
         * 追加样式字符串
         *
         * @param text 样式字符串文本
         * @return {@link Builder}
         */
        public Builder append(@NonNull CharSequence text) {
            setSpan();
            this.mText = text;
            return this;
        }

        public Builder appendStringId(@StringRes int id, Object... args) {
            setSpan();
            if (args == null || args.length == 0) {
                this.mText = LotteryApplication.getAppContext().getString(id);
            } else {
                this.mText = LotteryApplication.getAppContext().getString(id, args);
            }
            return this;
        }

        /**
         * 创建样式字符串
         *
         * @return 样式字符串
         */
        public SpannableStringBuilder create() {
            setSpan();
            return mBuilder;
        }

        /**
         * 设置样式
         */
        private void setSpan() {
            int start = mBuilder.length();
            if (this.mText == null) {
                return;
            }
            mBuilder.append(this.mText);
            int end = mBuilder.length();
            if (mForegroundColor != mDefaultColorValue) {
                mBuilder.setSpan(new ForegroundColorSpan(mForegroundColor), start, end, mFlag);
                mForegroundColor = mDefaultColorValue;
            }
            if (mBackgroundColor != mDefaultColorValue) {
                mBuilder.setSpan(new BackgroundColorSpan(mBackgroundColor), start, end, mFlag);
                mBackgroundColor = mDefaultColorValue;
            }
            if (mTextSize != mDefaultTextSize) {
                mBuilder.setSpan(new AbsoluteSizeSpan(mTextSize), start, end, mFlag);
                mTextSize = mDefaultTextSize;
            }

            if (mIsLeadingMargin) {
                mBuilder.setSpan(new LeadingMarginSpan.Standard(mFirst, mRest), start, end, mFlag);
                mIsLeadingMargin = false;
            }
            if (mQuoteColor != mDefaultColorValue) {
                mBuilder.setSpan(new QuoteSpan(mQuoteColor), start, end, 0);
                mQuoteColor = mDefaultColorValue;
            }
            if (mIsBullet) {
                mBuilder.setSpan(new BulletSpan(mGapWidth, mBulletColor), start, end, 0);
                mIsBullet = false;
            }
            if (mProportion != -1) {
                mBuilder.setSpan(new RelativeSizeSpan(mProportion), start, end, mFlag);
                mProportion = -1;
            }
            if (mXProportion != -1) {
                mBuilder.setSpan(new ScaleXSpan(mXProportion), start, end, mFlag);
                mXProportion = -1;
            }
            if (mIsStrikethrough) {
                mBuilder.setSpan(new StrikethroughSpan(), start, end, mFlag);
                mIsStrikethrough = false;
            }
            if (mIsUnderline) {
                mBuilder.setSpan(new UnderlineSpan(), start, end, mFlag);
                mIsUnderline = false;
            }
            if (mIsSuperscript) {
                mBuilder.setSpan(new SuperscriptSpan(), start, end, mFlag);
                mIsSuperscript = false;
            }
            if (mIsSubscript) {
                mBuilder.setSpan(new SubscriptSpan(), start, end, mFlag);
                mIsSubscript = false;
            }
            if (mIsBold) {
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, mFlag);
                mIsBold = false;
            }
            if (mIsItalic) {
                mBuilder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, mFlag);
                mIsItalic = false;
            }
            if (mIsBoldItalic) {
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, mFlag);
                mIsBoldItalic = false;
            }
            if (mFontFamily != null) {
                mBuilder.setSpan(new TypefaceSpan(mFontFamily), start, end, mFlag);
                mFontFamily = null;
            }
            if (mAlign != null) {
                mBuilder.setSpan(new AlignmentSpan.Standard(mAlign), start, end, mFlag);
                mAlign = null;
            }
            if (mImageIsBitmap || mImageIsDrawable || mImageIsUri || mImageIsResourceId) {
                if (mImageIsBitmap) {
                    mBuilder.setSpan(new ImageSpan(mContext, mBitmap), start, end, mFlag);
                    mBitmap = null;
                    mImageIsBitmap = false;
                } else if (mImageIsDrawable) {
                    mBuilder.setSpan(new ImageSpan(mDrawable), start, end, mFlag);
                    mDrawable = null;
                    mImageIsDrawable = false;
                } else if (mImageIsUri) {
                    mBuilder.setSpan(new ImageSpan(mContext, mUri), start, end, mFlag);
                    mUri = null;
                    mImageIsUri = false;
                } else {
                    mBuilder.setSpan(new ImageSpan(mContext, mResourceId), start, end, mFlag);
                    mResourceId = 0;
                    mImageIsResourceId = false;
                }
            }
            if (mClickSpan != null) {
                mBuilder.setSpan(mClickSpan, start, end, mFlag);
                mClickSpan = null;
            }
            if (mUrl != null) {
                mBuilder.setSpan(new URLSpan(mUrl), start, end, mFlag);
                mUrl = null;
            }
            if (mIsBlur) {
                mBuilder.setSpan(new MaskFilterSpan(new BlurMaskFilter(mRadius, mStyle)), start, end, mFlag);
                mIsBlur = false;
            }
            mFlag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        }
    }
}


