package com.alley.adviewpager.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.alley.ad.widget.ADViewPager;
import com.alley.adviewpager.R;
import com.alley.adviewpager.helper.DepthPageTransformer;
import com.alley.adviewpager.helper.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> imageUrl = new ArrayList<>();
    private List<String> imageHref = new ArrayList<>();

    private ADViewPager adViewPager1;
    private ADViewPager adViewPager2;
    private ADViewPager adViewPager3;

    private TextView tvTitle;
    private TextView tvDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setSubView();
        initEvent();
    }

    private void initView() {
        adViewPager1 = (ADViewPager) findViewById(R.id.viewPager_ad1);
        adViewPager2 = (ADViewPager) findViewById(R.id.viewPager_ad2);
        adViewPager3 = (ADViewPager) findViewById(R.id.viewPager_ad4);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDes = (TextView) findViewById(R.id.tv_des);
    }

    private void setSubView() {
        imageUrl.add("http://img13.360buyimg.com/n8/jfs/t2494/199/2974963318/623734/7b6fe7e5/572c1618N7ee89e63.jpg");
        imageUrl.add("http://img13.360buyimg.com/n8/jfs/t2827/183/199606439/285948/d507257d/5707b5f3N0df2f00b.jpg");
        imageUrl.add("http://img12.360buyimg.com/cms/jfs/t3259/133/6926144546/163293/aae4cc75/58ae9f21Nbe8a5ecb.png");

        imageHref.add("https://www.baidu.com");
        imageHref.add("http://img13.360buyimg.com/n8/jfs/t2827/183/199606439/285948/d507257d/5707b5f3N0df2f00b.jpg");

        adViewPager1.setIndicatorDrawableChecked(R.mipmap.img_banner_dot_focused)
                .setIndicatorDrawableUnchecked(R.mipmap.img_banner_dot_normal)
                .setAutoPlay(false)
                .setDotMargin(0)
                .setPageTransformer(new DepthPageTransformer())
                .setIndicatorEnable(true)
                .setIndicatorGravity(Gravity.CENTER)
                .setIndicatorBackground(Color.TRANSPARENT)
                .setIndicatorPadding(0, 0, 0, 24)
                .setBannerUrl(imageUrl)
                .setBannerHref(imageHref)
                .setADLoader(new ImageLoader())
                .start(3 * 1000);

        //点击轮播图时，若直接跳转到activity，则设置setTargetActivity(WebActivity.class) 就可以了，无需注册监听adViewPager.addADViewPagerListener
        adViewPager2.setIndicatorDrawableChecked(R.mipmap.img_banner_dot_focused)
                .setIndicatorDrawableUnchecked(R.mipmap.img_banner_dot_normal)
                .setAutoPlay(true)
                .setDotMargin(0)
                .setIndicatorEnable(true)
                .setIndicatorGravity(Gravity.RIGHT)
                .setIndicatorBackground(Color.parseColor("#a0000000"))
                .setIndicatorPadding(0, 24, 24, 24)
                .setBannerUrl(imageUrl)
                .setBannerHref(imageHref)
//                .setTargetActivity(WebActivity.class)
                .setADLoader(new ImageLoader())
                .start(3 * 1000);

        adViewPager3.setIndicatorDrawableChecked(R.mipmap.img_banner_dot_focused)
                .setIndicatorDrawableUnchecked(R.mipmap.img_banner_dot_normal)
                .setAutoPlay(true)
                .setDotMargin(0)
                .setIndicatorEnable(false)
                .setIndicatorGravity(Gravity.RIGHT)
                .setIndicatorBackground(Color.TRANSPARENT)
                .setIndicatorPadding(0, 0, 24, 24)
                .setBannerUrl(imageUrl)
                .setBannerHref(imageHref)
                .setADLoader(new ImageLoader())
                .start(3 * 1000);
    }

    private void initEvent() {
        adViewPager2.addADViewPagerListener(new ADViewPager.OnCurrentPageListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onClickPage(@NonNull List<String> imageUrl, @Nullable List<String> imageHref, int position) {
                //点击图片被调用的方法，若在此方法中处理了跳转业务，则返回值应为TRUE，可以跳转到Web页面，这里就不演示了
                Toast.makeText(MainActivity.this, "点击图片所跳转的路径" + imageUrl.get(position), Toast.LENGTH_LONG).show();
            }
        });

        adViewPager3.addADViewPagerListener(new ADViewPager.OnCurrentPageListener() {
            @Override
            public void onPageSelected(int position) {
                //每次轮播时被调用的方法
                if (position == 0) {
                    tvTitle.setText("这是标题 " + position);
                    tvDes.setText("每次轮播时被调用的方法 " + position);
                } else if (position == 1) {
                    tvTitle.setText("点击图片被调用的方法 " + position);
                    tvDes.setText("若在此方法中处理了跳转业务 " + position);
                } else {
                    tvTitle.setText("若在此方法中 " + position);
                    tvDes.setText("则返回值应为TRUE " + position);
                }
            }

            @Override
            public void onClickPage(@NonNull List<String> imageUrl, @Nullable List<String> imageHref, int position) {
                imageUrl.add("http://img12.360buyimg.com/cms/jfs/t3259/133/6926144546/163293/aae4cc75/58ae9f21Nbe8a5ecb.png");

                imageHref.add("https://www.baidu.com");

                adViewPager3.setBannerUrl(imageUrl)
                        .setBannerHref(imageHref)
                        .start(3 * 1000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adViewPager3.restart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adViewPager3.stop();
    }
}
