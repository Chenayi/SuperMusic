package com.chenayi.supermusic.adapter

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chenayi.supermusic.R
import com.chenayi.supermusic.utils.NotNullUtils
import com.chenayi.supermusic.widget.DiscView

/**
 * Created by Chenwy on 2018/4/28.
 */
@BindingAdapter("app:cover", "app:isPlaying")
fun setCover(discView: DiscView, cover: String?, isPlaying: Boolean?) {
    NotNullUtils.ifNotNull(cover, isPlaying, { cover, isPlaying ->
        Glide.with(discView.context)
                .asBitmap()
                .load(cover)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                        discView.setCover(bitmap, isPlaying)
                    }
                })
    })

}