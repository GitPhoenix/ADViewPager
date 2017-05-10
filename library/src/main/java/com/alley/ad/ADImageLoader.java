package com.alley.ad;


import android.content.Context;
import android.widget.ImageView;

public interface ADImageLoader {

    /**
     * 自定义图片加载策略
     *
     * @param context
     * @param url 图片路径
     * @param imageView
     * @param width
     * @param height
     */
    void displayImage(Context context, String url, ImageView imageView, int width, int height);
}
