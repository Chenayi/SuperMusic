package com.chenayi.supermusic.mvp.ui.fragment

import com.chenayi.supermusic.R
import com.chenayi.supermusic.base.BaseFragment

/**
 * Created by Chenwy on 2018/4/12.
 */
class FunnyFragment : BaseFragment() {

    companion object {
        fun newInstance(): FunnyFragment {
            return FunnyFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_funny
    }

    override fun initDta() {
    }
}