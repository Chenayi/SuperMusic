package com.chenayi.supermusic.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import butterknife.BindView
import butterknife.ButterKnife
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
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.SizeUtils


/**
 * Created by Chenwy on 2018/4/18.
 */
class DiscView : RelativeLayout {
    @BindView(R.id.ivDiscBlackgound)
    lateinit var ivDiscBlackgound: ImageView
    @BindView(R.id.iv_disc)
    lateinit var ivDisc: ImageView

    private var discAnimator: ObjectAnimator? = null
    private var isPlaying: Boolean? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val view = LayoutInflater.from(context).inflate(R.layout.disc_view, this, true)
        ButterKnife.bind(view)
        init()
    }

    private fun init() {

    }

    fun setPlaying(isPlaying: Boolean) {
        this.isPlaying = isPlaying
    }

    fun setCover(cover: String) {
        Glide.with(context)
                .asBitmap()
                .load(cover)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        var backBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_disc)
                        var mergeDrawable = mergeDrawable(backBitmap, resource)
                        ivDisc.setImageDrawable(mergeDrawable)
                        discAnimator = getDiscAnimator(ivDisc)
                        if (isPlaying == true) {
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
        isPlaying = false
    }

    /**
     * 播放
     */
    fun play() {
        startDiscAnimation()
        isPlaying = true
    }

    /**
     * 播放结束
     */
    fun complete() {
        stopDiscAnimation()
        isPlaying = false
    }


    /**
     * 开始碟片旋转动画
     */
    fun startDiscAnimation() {
        var paused = discAnimator?.isPaused
        if (paused == true){
            discAnimator?.resume()
        }else{
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
        ivDisc.rotation = 0f
    }
}