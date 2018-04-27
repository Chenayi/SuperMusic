package com.chenayi.supermusic.adapter

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView

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