# ADViewPager 本文重点介绍了自定义ViewPager，在以后的项目中进行快速开发，避免了写很多繁琐的代码，使得维护起来更便利。
##效果图:
![image](https://github.com/GitPhoenix/ADViewPager/blob/master/screen/screen.gif)<br>

1.在布局文件中加入ADViewPager
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.alley.ad.widget.ADViewPager
        android:id="@+id/viewPager_main_ad"
        android:layout_width="match_parent"
        android:layout_alignParentTop="true"
        android:layout_height="180dp"/>

</RelativeLayout>
```
2.待轮播图路径请求成功时，根据需求在代码中设置参数
```
adViewPager.setIndicatorDrawableChecked(R.mipmap.img_banner_dot_focused) //当前指示点
        .setIndicatorDrawableUnchecked(R.mipmap.img_banner_dot_normal) //非当前指示点
        .setAutoPlay(true) //是否开启自动轮播
        .setDotMargin(0)  //指示器小点之间的距离
        .setPageTransformer(new ZoomOutPageTransformer()) //页面滑动动画
        .setIndicatorEnable(true) //是否显示指示器
        .setIndicatorGravity(Gravity.CENTER) //指示器位置
        .setIndicatorBackground(Color.TRANSPARENT) //指示器背景色
        .setIndicatorPadding(0, 0, 0, 24)
        .setBannerUrl(imageUrl) //图片路径
        .setBannerHref(imageHref) //点击图片跳转的路径
        .setADLoader(new ImageLoader()) // 图片加载配置
        .setTargetActivity(WebActivity.class) //点击图片跳转的webView页面
        .startPlay(3, 3);
        
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
```
当轮播图资源路径为空时设置默认显示的图片，只需设置ADViewPager的背景图即可
```
<com.alley.ad.widget.ADViewPager
        android:id="@+id/viewPager_main_ad"
        android:layout_width="match_parent"
        android:layout_alignParentTop="true"
        android:background="@mipmap/ic_launcher"
        android:layout_height="180dp"/>
```
