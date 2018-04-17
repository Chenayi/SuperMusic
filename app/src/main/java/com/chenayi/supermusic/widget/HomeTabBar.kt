package com.chenayi.supermusic.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.chenayi.supermusic.R

/**
 * Created by Chenwy on 2018/4/11.
 */
class HomeTabBar : RelativeLayout {
    @BindView(R.id.iv_music)
    lateinit var ivMuscis: ImageView
    @BindView(R.id.iv_discover)
    lateinit var ivDiscover: ImageView
    @BindView(R.id.iv_friends)
    lateinit var ivFriends: ImageView

    private var mViewPager: ViewPager? = null;
    private var smoothScroll:Boolean = true;

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        var view = LayoutInflater.from(context).inflate(R.layout.home_tool_bar, this, true);
        ButterKnife.bind(view);
        init();
    }

    private fun init() {
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
    }

    @OnClick(R.id.iv_music, R.id.iv_discover, R.id.iv_friends)
    fun onClick(v: View) {
        when (v.id) {
            R.id.iv_music -> mViewPager?.setCurrentItem(0,smoothScroll)
            R.id.iv_discover -> mViewPager?.setCurrentItem(1,smoothScroll)
            R.id.iv_friends -> mViewPager?.setCurrentItem(2,smoothScroll)
        }
    }

    /**
     * 重置tab
     */
    fun resetTab() {
        ivMuscis.setImageResource(R.mipmap.actionbar_music_prs)
        ivDiscover.setImageResource(R.mipmap.actionbar_discover_prs)
        ivFriends.setImageResource(R.mipmap.actionbar_friends_prs)
    }

    /**
     * 关联viewpager
     */
    fun setUpWithViewPager(viewPager: ViewPager) {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
               when(position){
                   0->{
                       resetTab()
                       ivMuscis.setImageResource(R.mipmap.actionbar_music_selected)
                   }

                   1->{
                       resetTab()
                       ivDiscover.setImageResource(R.mipmap.actionbar_discover_selected)
                   }
                   2->{
                       resetTab()
                       ivFriends.setImageResource(R.mipmap.actionbar_friends_selected)
                   }
               }
            }

        })

        mViewPager = viewPager;
    }

    fun setSmoothScroll(smoothScroll:Boolean){
        this.smoothScroll = smoothScroll
    }
}