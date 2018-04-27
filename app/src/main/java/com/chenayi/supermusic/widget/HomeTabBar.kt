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
import com.chenayi.supermusic.R
import com.chenayi.supermusic.databinding.HomeToolBarBinding

/**
 * Created by Chenwy on 2018/4/11.
 */
class HomeTabBar : RelativeLayout {
    private var binding: HomeToolBarBinding? = null

    private var ivMuscis: ImageView? = null
    private var ivDiscover: ImageView? = null
    private var ivFriends: ImageView? = null

    private var mViewPager: ViewPager? = null;
    private var smoothScroll: Boolean = true;

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_tool_bar, this, true)
        initWidgets()
        init();
    }

    private fun initWidgets() {
        ivMuscis = binding?.ivMusic
        ivDiscover = binding?.ivDiscover
        ivFriends = binding?.ivFriends
        binding?.homeTabBar = this
    }

    private fun init() {
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
     * 重置tab
     */
    fun resetTab() {
        ivMuscis?.setImageResource(R.mipmap.actionbar_music_prs)
        ivDiscover?.setImageResource(R.mipmap.actionbar_discover_prs)
        ivFriends?.setImageResource(R.mipmap.actionbar_friends_prs)
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
                        resetTab()
                        ivMuscis?.setImageResource(R.mipmap.actionbar_music_selected)
                    }

                    1 -> {
                        resetTab()
                        ivDiscover?.setImageResource(R.mipmap.actionbar_discover_selected)
                    }
                    2 -> {
                        resetTab()
                        ivFriends?.setImageResource(R.mipmap.actionbar_friends_selected)
                    }
                }
            }

        })

        mViewPager = viewPager;
    }

    fun setSmoothScroll(smoothScroll: Boolean) {
        this.smoothScroll = smoothScroll
    }
}