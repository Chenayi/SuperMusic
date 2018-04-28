package com.chenayi.supermusic.widget

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.blankj.utilcode.util.LogUtils
import com.chenayi.supermusic.BR
import com.chenayi.supermusic.R
import com.chenayi.supermusic.databinding.HomeToolBarBinding

/**
 * Created by Chenwy on 2018/4/11.
 */
class HomeTabBar : RelativeLayout {
    private var binding: HomeToolBarBinding? = null
    private var mViewPager: ViewPager? = null;
    private var smoothScroll: Boolean = true;

    private var icons = intArrayOf(
            R.mipmap.actionbar_music_selected,
            R.mipmap.actionbar_discover_prs,
            R.mipmap.actionbar_friends_prs)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_tool_bar, this, true)
        init();
    }

    private fun init() {
        binding?.homeTabBar = this
        binding?.icons = icons
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.iv_music -> mViewPager?.setCurrentItem(0, smoothScroll)
            R.id.iv_discover -> mViewPager?.setCurrentItem(1, smoothScroll)
            R.id.iv_friends -> mViewPager?.setCurrentItem(2, smoothScroll)
        }
    }

    /**
     * 关联viewpager
     */
    fun setUpWithViewPager(viewPager: ViewPager) {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        icons[0] = R.mipmap.actionbar_music_selected
                        icons[1] = R.mipmap.actionbar_discover_prs
                        icons[2] = R.mipmap.actionbar_friends_prs
                    }
                    1 -> {
                        icons[0] = R.mipmap.actionbar_music_prs
                        icons[1] = R.mipmap.actionbar_discover_selected
                        icons[2] = R.mipmap.actionbar_friends_prs
                    }
                    2 -> {
                        icons[0] = R.mipmap.actionbar_music_prs
                        icons[1] = R.mipmap.actionbar_discover_prs
                        icons[2] = R.mipmap.actionbar_friends_selected
                    }
                }
                binding?.icons = icons
            }
        })

        mViewPager = viewPager;
    }

    fun setSmoothScroll(smoothScroll: Boolean) {
        this.smoothScroll = smoothScroll
    }
}