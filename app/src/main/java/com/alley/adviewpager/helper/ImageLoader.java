package com.alley.adviewpager.helper;

import android.content.Context;
import android.widget.ImageView;

import com.alley.ad.ADImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class ImageLoader implements ADImageLoader {

    @Override
    public void displayImage(Context context, String url, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
}
