package com.chenayi.supermusic.mvp.ui.fragment

import com.chenayi.supermusic.R
import com.chenayi.supermusic.base.BaseFragment

/**
 * Created by Chenwy on 2018/4/12.
 */
class DynamicFragment : BaseFragment() {
    companion object {
        fun newInstance():DynamicFragment{
            return DynamicFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_dynamic;
    }

    override fun initDta() {
    }
}