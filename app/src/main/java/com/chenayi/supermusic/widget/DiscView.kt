package com.chenayi.supermusic.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chenayi.supermusic.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.databinding.DataBindingUtil
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.chenayi.supermusic.databinding.DiscViewBinding


/**
 * Created by Chenwy on 2018/4/18.
 */
class DiscView : RelativeLayout {
    private var binding: DiscViewBinding? = null
    private var discAnimator: ObjectAnimator? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.disc_view,this,true)
    }

    fun setCover(cover: String, isPlaying: Boolean) {
        Glide.with(context)
                .asBitmap()
                .load(cover)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        var backBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_disc)
                        var mergeDrawable = mergeDrawable(backBitmap, resource)
                        binding?.discDrawable = mergeDrawable

                        stopDiscAnimation()
                        binding?.ivDisc?.let {
                            discAnimator = getDiscAnimator(it)
                        }
                        if (isPlaying) {
                            startDiscAnimation()
                        }
                    }
                })
    }


    fun getDiscAnimator(disc: ImageView): ObjectAnimator {
        val objectAnimator = ObjectAnimator.ofFloat(disc, View.ROTATION, 0F, 360F)
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE)
        objectAnimator.setDuration((20 * 1000).toLong())
        objectAnimator.setInterpolator(LinearInterpolator())
        return objectAnimator
    }

    private fun mergeDrawable(backBitmap: Bitmap, coverBitmap: Bitmap): Drawable {
        var drawables = arrayOfNulls<Drawable>(2)

        var discDrawable = BitmapDrawable(resources, backBitmap)
        var coverDrawable = BitmapDrawable(resources, coverBitmap)

        drawables[0] = coverDrawable
        drawables[1] = discDrawable

        var layerDrawable = LayerDrawable(drawables)

        val margin = SizeUtils.dp2px(10F)
        layerDrawable.setLayerInset(0, margin, margin, margin, margin)

        return layerDrawable
    }

    /**
     * 暂停
     */
    fun pause() {
        pauseDiscAnimation()
        LogUtils.e("pauseDiscAnimation...")
    }

    /**
     * 播放
     */
    fun play() {
        startDiscAnimation()
        LogUtils.e("startDiscAnimation...")
    }

    /**
     * 播放结束
     */
    fun complete() {
        stopDiscAnimation()
    }


    /**
     * 开始碟片旋转动画
     */
    fun startDiscAnimation() {
        var paused = discAnimator?.isPaused
        if (paused == true) {
            discAnimator?.resume()
        } else {
            discAnimator?.start()
        }
    }

    /**
     * 暂停碟片旋转动画
     */
    fun pauseDiscAnimation() {
        discAnimator?.pause()
    }

    /**
     * 停止碟片旋转动画
     */
    fun stopDiscAnimation() {
        discAnimator?.cancel()
    }
}