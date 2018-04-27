package com.chenayi.supermusic.mvp.ui.fragment

import com.chenayi.supermusic.R
import com.chenayi.supermusic.base.BaseFragment
import com.chenayi.supermusic.databinding.FrgFunnyBinding

/**
 * Created by Chenwy on 2018/4/12.
 */
class FunnyFragment : BaseFragment<FrgFunnyBinding>() {

    companion object {
        fun newInstance(): FunnyFragment {
            return FunnyFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_funny
    }

    override fun initWidgets() {
    }

    override fun initDta() {
    }
}