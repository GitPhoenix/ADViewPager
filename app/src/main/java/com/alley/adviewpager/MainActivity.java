package com.alley.adviewpager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.alley.ad.widget.ADViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> imageUrl = new ArrayList<>();
    private List<String> imageHref = new ArrayList<>();

    private ADViewPager adViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setSubView();
        initEvent();
    }

    private void initView() {
        adViewPager = (ADViewPager) findViewById(R.id.viewPager_ad_home);
    }

    private void setSubView() {
        imageUrl.add("http://img13.360buyimg.com/n8/jfs/t2494/199/2974963318/623734/7b6fe7e5/572c1618N7ee89e63.jpg");
        imageUrl.add("http://img13.360buyimg.com/n8/jfs/t2827/183/199606439/285948/d507257d/5707b5f3N0df2f00b.jpg");
        imageUrl.add("http://img12.360buyimg.com/cms/jfs/t3259/133/6926144546/163293/aae4cc75/58ae9f21Nbe8a5ecb.png");

        imageHref.add("https://www.baidu.com");
        imageHref.add("http://img13.360buyimg.com/n8/jfs/t2827/183/199606439/285948/d507257d/5707b5f3N0df2f00b.jpg");

        adViewPager.setIndicatorDrawableChecked(R.mipmap.img_banner_dot_focused)
                .setIndicatorDrawableUnchecked(R.mipmap.img_banner_dot_normal)
                .setAutoPlay(true)
                .setDotMargin(0)
//                .setPageTransformer(new ZoomOutPageTransformer())
                .setIndicatorEnable(true)
                .setIndicatorGravity(Gravity.CENTER)
                .setIndicatorBackground(Color.TRANSPARENT)
                .setIndicatorPadding(0, 0, 0, 24)
                .setBannerUrl(imageUrl)
                .setBannerHref(imageHref)
                .setADLoader(new ImageLoader())
//                .setTargetActivity(WebActivity.class)
                .startPlay(3, 3);
    }

    private void initEvent() {
        adViewPager.addADViewPagerListener(new ADViewPager.OnCurrentPageListener() {
            @Override
            public void onPageSelected(int position) {
                //每次轮播时被调用的方法

            }

            @Override
            public boolean onClickPage(@NonNull List<String> imageUrl, @Nullable List<String> imageHref, int position) {
                //点击图片师被调用的方法，若在此方法中处理了跳转业务，则返回值应为TRUE
                Toast.makeText(MainActivity.this, imageUrl.get(position), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }
}
