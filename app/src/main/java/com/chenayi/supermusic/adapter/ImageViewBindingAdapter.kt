package com.chenayi.supermusic.adapter

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


/**
 * Created by Chenwy on 2018/4/27.
 */
@BindingAdapter("android:src")
fun setSrc(imageView: ImageView, bitmap: Bitmap) {
    imageView.setImageBitmap(bitmap)
}

@BindingAdapter("android:src")
fun setSrc(imageView: ImageView, drawable: Drawable) {
    imageView.setImageDrawable(drawable)
}

@BindingAdapter("android:src")
fun setSrc(imageView: ImageView, resId: Int) {
    imageView.setImageResource(resId)
}


@BindingAdapter("app:imageUrl","app:placeholder")
fun loadImage(imageView: ImageView, url: String,placeholder : Drawable) {
    Glide.with(imageView.context)
            .load(url)
            .apply(RequestOptions()
                    .centerCrop()
                    .placeholder(placeholder))
            .into(imageView)
}