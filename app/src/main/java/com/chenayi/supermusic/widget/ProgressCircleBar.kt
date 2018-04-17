package com.chenayi.supermusic.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chenayi.supermusic.R
import android.view.ViewGroup


/**
 * Created by Chenwy on 2018/4/16.
 */
class ProgressCircleBar : View {
    private var mXCenter: Float = 0.0f
    private var mYCenter: Float = 0.0f

    /**
     * 内圆画笔
     */
    private var circlePaint: Paint? = null
    /**
     * 内圆半径
     */
    private var mRadius: Float = 0.0f

    // 画圆环的画笔
    private var ringPaint: Paint? = null
    // 画圆环的画笔背景色
    private var ringPaintBg: Paint? = null

    /**
     * 圆环半径
     */
    private var ringRadius: Float = 0.0f

    /**
     * 圆环宽度
     */
    private var strokeWidth: Float = 0.0f

    /**
     * 播放图标
     */
    private var playBitmap: Bitmap? = null

    /**
     * 暂停图标
     */
    private var pauseBitmap: Bitmap? = null

    private var isPlaying: Boolean? = false


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        var t: TypedArray? = context?.obtainStyledAttributes(attrs, R.styleable.ProgressCircleBar)
        mRadius = t?.getDimension(R.styleable.ProgressCircleBar_radius, resources.getDimension(R.dimen.dp_20))!!
        strokeWidth = t?.getDimension(R.styleable.ProgressCircleBar_strokeWidth, resources.getDimension(R.dimen.dp_2))!!
        t?.recycle()

        init()
    }

    private fun init() {
        ringRadius = mRadius + strokeWidth / 2.0f

        circlePaint = Paint()
        circlePaint?.setAntiAlias(true);
        circlePaint?.setColor(Color.WHITE);
        circlePaint?.setStyle(Paint.Style.FILL);

        //外圆弧背景
        ringPaintBg = Paint()
        ringPaintBg?.setAntiAlias(true)
        ringPaintBg?.setColor(ContextCompat.getColor(context, R.color.c_333))
        ringPaintBg?.setStyle(Paint.Style.STROKE)
        ringPaintBg?.setStrokeWidth(strokeWidth)

        //外圆弧
        ringPaint = Paint()
        ringPaint?.setAntiAlias(true)
        ringPaint?.setColor(ContextCompat.getColor(context, R.color.colorPrimary))
        ringPaint?.setStyle(Paint.Style.STROKE)
        ringPaint?.setStrokeWidth(strokeWidth)

        playBitmap = BitmapFactory.decodeResource(resources, R.mipmap.play)
        pauseBitmap = BitmapFactory.decodeResource(resources, R.mipmap.pause)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获取宽
        var widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        // 获取高
        var heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        //默认宽高
        var width: Int = (mRadius * 2 + strokeWidth * 2).toInt()
        var height: Int = width

        if (widthSize < width) {
            widthSize = width
        }

        if (heightSize < height) {
            heightSize = height
        }

        // 当布局参数设置为wrap_content时，设置默认值
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT
                && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(width, height);
        }
        //保持宽高一致
        else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(heightSize, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, widthSize);
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mXCenter = getWidth() / 2.0f;
        mYCenter = getHeight() / 2.0f;

        //内圆
        canvas?.drawCircle(mXCenter, mYCenter, mRadius, circlePaint)

        //外圆弧背景
        canvas?.drawCircle(mXCenter, mYCenter, ringRadius, ringPaintBg)


        //外圆弧
        var oval1 = RectF(
                mXCenter - ringRadius,
                mYCenter - ringRadius,
                mXCenter + ringRadius,
                mYCenter + ringRadius)
        canvas?.drawArc(oval1, -90.0f, 180.0f, false, ringPaint)

        //播放按钮
        var playWidth = playBitmap?.width
        var playHeight = playBitmap?.height
        var srcRect = Rect(0, 0, playWidth!!, playHeight!!)
        var left = (mXCenter / 2.0f).toInt()
        var top = (mYCenter / 2.0f).toInt()
        var destRect = Rect(left, top, left + playWidth!!, top + playHeight!!)
        if (isPlaying == false) {
            canvas?.drawBitmap(playBitmap, srcRect, destRect, Paint())
        } else {
            canvas?.drawBitmap(pauseBitmap, srcRect, destRect, Paint())
        }
    }
}