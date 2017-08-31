# ADViewPager 本文重点介绍了自定义ViewPager，在以后的项目中进行快速开发，避免了写很多繁琐的代码，使得维护起来更便利。

![截图](https://github.com/GitPhoenix/ADViewPager/blob/master/screen/Screenshot_20170510-151058-059.jpg)
依赖：compile 'com.alley:ADViewPager:1.6.5'

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
        .startPlay(3 * 1000);

@Override
protected void onResume() {
    super.onResume();
    if (adViewPager.getViewPager() != null) {
         adViewPager.restartPlay();
    }
}

@Override
protected void onPause() {
    super.onPause();
    adViewPager.stopPlay();
}
        
adViewPager.addADViewPagerListener(new ADViewPager.OnCurrentPageListener() {
    @Override
    public void onPageSelected(int position) {
        //每次轮播时被调用的方法
    }

    @Override
    public void onClickPage(@NonNull List<String> imageUrl, @Nullable List<String> imageHref, int position) {
        //点击图片师被调用的方法
        Toast.makeText(MainActivity.this, imageUrl.get(position), Toast.LENGTH_LONG).show();
    }
});
```
当没有轮播图时，只需设置ADViewPager的背景图即可，此时界面上看到的就只有这张背景图
```
<com.alley.ad.widget.ADViewPager
    android:id="@+id/viewPager_main_ad"
    android:layout_width="match_parent"
    android:layout_alignParentTop="true"
    android:background="@mipmap/ic_launcher"
    android:layout_height="180dp"/>
```
