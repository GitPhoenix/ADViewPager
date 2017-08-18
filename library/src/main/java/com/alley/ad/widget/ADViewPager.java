package com.alley.ad.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alley.ad.ADImageLoader;

import java.util.List;

/**
 * 轮播图控件封装
 *
 * @author Phoenix
 * @date 2017/4/24 9:54
 */
public class ADViewPager extends FrameLayout implements View.OnClickListener {
    private Context context;
    private ImageView[] allPage;
    private ViewPager viewPager;
    private LinearLayout dotLayout;

    //自定义轮播图的资源
    private List<String> imageUrls;
    private List<String> imageHref;
    private int pageIndicatorGravity = Gravity.RIGHT;
    private int indicatorDrawableChecked;
    private int indicatorDrawableUnchecked;
    private int indicatorBackground = Color.TRANSPARENT;
    //自动轮播启用开关
    private boolean isAutoPlay = true;
    //轮播指示器启用开关
    private boolean isDisplayIndicator = true;
    //banner轮播周期
    private long period = 6 * 1000L;
    //轮播小点之间的距离
    private int dotMargin = 4;
    private int currentIndex = 0;
    private int[] distance = new int[4];
    //图片加载
    private ADImageLoader adImageLoader;
    //页面切换动画
    private ViewPager.PageTransformer pageTransformer;
    //刷新当前页回调
    private OnCurrentPageListener onCurrentPageListener;

    private static final int HANDLE_TEXT_CHANGED_MSG = 0x001;

    private Handler viewPagerHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem((currentIndex + 1) % allPage.length);

            viewPagerHandler.sendEmptyMessageDelayed(HANDLE_TEXT_CHANGED_MSG, period);
        }
    };

    public ADViewPager(Context context) {
        this(context, null);
    }

    public ADViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ADViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    /**
     * 初始化ADViewPager
     */
    private void initADViewPager() {
        if (imageUrls == null || imageUrls.size() < 1) {
            return;
        }
        LayoutInflater.from(context).inflate(R.layout.view_ad_pager, this, true);
        dotLayout = (LinearLayout) findViewById(R.id.ll_dot);
        viewPager = (ViewPager) findViewById(R.id.viewPager_ad);
        dotLayout.removeAllViews();

        if (adImageLoader == null) {
            throw new NullPointerException("adLoader == null");
        }
        allPage = new ImageView[imageUrls.size()];
        // 热点个数与图片特殊相等
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView pageView = new ImageView(context);
            allPage[i] = pageView;
            pageView.setTag(R.id.AD_ImageView, i);
            pageView.setScaleType(ImageView.ScaleType.FIT_XY);
            adImageLoader.displayImage(context, imageUrls.get(i), pageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            pageView.setOnClickListener(this);
        }

        if (isDisplayIndicator) {
            drawPageIndicator();
        }

        PagerAdapter adapter = new ADViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setFocusable(true);
        if (pageTransformer != null) {
            viewPager.setPageTransformer(true, pageTransformer);
        }
        viewPager.addOnPageChangeListener(new ADViewPagerChangeListener());
    }

    /**
     * 绘制指示器
     */
    private void drawPageIndicator() {
        if (imageUrls.size() <= 1) {
            return;
        }
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = dotMargin;
            params.rightMargin = dotMargin;
            dotView.setBackgroundResource(indicatorDrawableUnchecked);
            dotLayout.addView(dotView, params);
        }
        dotLayout.setGravity(pageIndicatorGravity);
        dotLayout.setPadding(distance[0], distance[1], distance[2], distance[3]);
        dotLayout.setBackgroundColor(indicatorBackground);
        dotLayout.getChildAt(0).setBackgroundResource(indicatorDrawableChecked);
    }

    /**
     * 填充ViewPager的页面适配器
     */
    private class ADViewPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(allPage[position]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = allPage[position];
            container.addView(imageView);
            return imageView;
        }

        @Override
        public int getCount() {
            return allPage == null ? 0 : allPage.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }
    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class ADViewPagerChangeListener implements ViewPager.OnPageChangeListener {
        private boolean isScrolled;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:// 手势滑动
                    isScrolled = false;
                    break;

                case 2:// 界面切换
                    isScrolled = true;
                    break;

                case 0:// 滑动结束
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isScrolled) {// 当前为最后一张，此时从右向左滑，则切换到第一张
                        viewPager.setCurrentItem(0);
                    } else if (viewPager.getCurrentItem() == 0 && !isScrolled) {// 当前为第一张，此时从左向右滑，则切换到最后一张
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageSelected(int arg0) {
            setCurrentDot(arg0);
        }
    }

    /**
     * 设置当前选中的指示器
     *
     * @param position
     */
    private void setCurrentDot(int position) {
        currentIndex = position;
        //设置回调
        if (onCurrentPageListener != null) {
            onCurrentPageListener.onPageSelected(position);
        }
        //如果设置了不显示指示器，那就返回
        if (!isDisplayIndicator) {
            return;
        }
        for (int i = 0; i < dotLayout.getChildCount(); i++) {
            if (i == position) {
                dotLayout.getChildAt(position).setBackgroundResource(indicatorDrawableChecked);
            } else {
                dotLayout.getChildAt(i).setBackgroundResource(indicatorDrawableUnchecked);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (onCurrentPageListener != null) {
            onCurrentPageListener.onClickPage(imageUrls, imageHref, (int) view.getTag(R.id.AD_ImageView));
        }
    }

    /**
     * 初始化page 并且开始执行轮播
     *
     * @param period 轮播的周期，单位millisecond
     */
    public void startPlay(long period) {
        this.period = period;

        initADViewPager();

        restartPlay();
    }

    /**
     * 重启轮播
     */
    public void restartPlay() {
        if (isAutoPlay && imageUrls.size() > 1) {
            stopPlay();
            viewPagerHandler.sendEmptyMessageDelayed(HANDLE_TEXT_CHANGED_MSG, period);
        }
    }

    /**
     * 停止轮播
     */
    public void stopPlay() {
        if (viewPagerHandler != null) {
            viewPagerHandler.removeMessages(HANDLE_TEXT_CHANGED_MSG);
        }
    }

    public interface OnCurrentPageListener {
        /**
         * 刷新当前页
         *
         * @param position 当前页索引
         */
        void onPageSelected(int position);

        /**
         * 点击图片时进行回调
         *
         * @param imageUrl  轮播图资源链接
         * @param imageHref 点击轮播图所跳转的链接
         * @param position  当前轮播图在所有轮播图中的索引
         * @return
         */
        void onClickPage(@NonNull List<String> imageUrl, @Nullable List<String> imageHref, int position);
    }

    /**
     * 对外开放的方法
     *
     * @param onCurrentPageListener
     */
    public void addADViewPagerListener(OnCurrentPageListener onCurrentPageListener) {
        this.onCurrentPageListener = onCurrentPageListener;
    }

    /**
     * 设置轮播图的Uri
     *
     * @param imageUrls
     */
    public ADViewPager setBannerUrl(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        return this;
    }

    /**
     * 设置点击轮播图跳转的路径
     *
     * @param imageHref
     */
    public ADViewPager setBannerHref(List<String> imageHref) {
        this.imageHref = imageHref;
        return this;
    }

    /**
     * 图片加载
     *
     * @param adImageLoader
     * @return
     */
    public ADViewPager setADLoader(ADImageLoader adImageLoader) {
        this.adImageLoader = adImageLoader;
        return this;
    }

    /**
     * 设置是否自动播放
     *
     * @param isAutoPlay
     * @return
     */
    public ADViewPager setAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    /**
     * 设置页面切换动画
     *
     * @param transformer
     * @return
     */
    public ADViewPager setPageTransformer(ViewPager.PageTransformer transformer) {
        this.pageTransformer = transformer;
        return this;
    }

    /**
     * 设置是否显示指示器
     *
     * @param enable
     * @return
     */
    public ADViewPager setIndicatorEnable(boolean enable) {
        this.isDisplayIndicator = enable;
        return this;
    }

    /**
     * 设置指示器的位置
     *
     * @param gravity
     * @return
     */
    public ADViewPager setIndicatorGravity(int gravity) {
        this.pageIndicatorGravity = gravity;
        return this;
    }

    /**
     * 设置指示器小点之间的距离
     *
     * @param margin
     * @return
     */
    public ADViewPager setDotMargin(int margin) {
        this.dotMargin = margin;
        return this;
    }

    /**
     * 设置指示器选中时的drawable
     *
     * @param resId
     * @return
     */
    public ADViewPager setIndicatorDrawableChecked(@DrawableRes int resId) {
        this.indicatorDrawableChecked = resId;
        return this;
    }

    /**
     * 设置指示器未选中时的drawable
     *
     * @param resId
     * @return
     */
    public ADViewPager setIndicatorDrawableUnchecked(@DrawableRes int resId) {
        this.indicatorDrawableUnchecked = resId;
        return this;
    }

    /**
     * 设置指示器背景色，默认透明
     *
     * @param resId
     * @return
     */
    public ADViewPager setIndicatorBackground(int resId) {
        this.indicatorBackground = resId;
        return this;
    }

    /**
     * 设置指示器距离上下左右的距离
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public ADViewPager setIndicatorPadding(int left, int top, int right, int bottom) {
        distance[0] = left;
        distance[1] = top;
        distance[2] = right;
        distance[3] = bottom;
        return this;
    }

    /**
     * 对外开放ViewPager
     *
     * @return
     */
    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopPlay();
    }
}
