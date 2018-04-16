package com.chenayi.supermusic.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

/**
 * Created by Chenwy on 2018/4/16.
 */
class ProgressCircleBar : View {
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init()
    }

    private fun init(){

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }
}