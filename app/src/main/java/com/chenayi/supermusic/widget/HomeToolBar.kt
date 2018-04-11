package com.chenayi.supermusic.widget

import android.content.Context
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.chenayi.supermusic.R

/**
 * Created by Chenwy on 2018/4/11.
 */
class HomeToolBar : Toolbar {
    @BindView(R.id.iv_music)
    lateinit var ivMuscis: ImageView
    @BindView(R.id.iv_discover)
    lateinit var ivDiscover: ImageView
    @BindView(R.id.iv_friends)
    lateinit var ivFriends: ImageView

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        var view = LayoutInflater.from(context).inflate(R.layout.home_tool_bar, this, true);
        ButterKnife.bind(view);
    }

    @OnClick(R.id.iv_music, R.id.iv_discover, R.id.iv_friends)
    fun onClick(v: View) {
        when (v.id) {
            R.id.iv_music -> {
                resetTab()
                ivMuscis.setImageResource(R.mipmap.actionbar_music_selected)
            }
            R.id.iv_discover -> {
                resetTab()
                ivDiscover.setImageResource(R.mipmap.actionbar_discover_selected)
            }
            R.id.iv_friends -> {
                resetTab()
                ivFriends.setImageResource(R.mipmap.actionbar_friends_selected)
            };
        }
    }

    fun resetTab() {
        ivMuscis.setImageResource(R.mipmap.actionbar_music_prs)
        ivDiscover.setImageResource(R.mipmap.actionbar_discover_prs)
        ivFriends.setImageResource(R.mipmap.actionbar_friends_prs)
    }
}