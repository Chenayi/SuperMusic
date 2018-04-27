package com.chenayi.supermusic.mvp.ui.fragment

import com.chenayi.supermusic.R
import com.chenayi.supermusic.base.BaseFragment
import com.chenayi.supermusic.databinding.FrgDynamicBinding

/**
 * Created by Chenwy on 2018/4/12.
 */
class DynamicFragment : BaseFragment<FrgDynamicBinding>() {
    companion object {
        fun newInstance(): DynamicFragment {
            return DynamicFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_dynamic;
    }

    override fun initWidgets() {
    }

    override fun initDta() {
    }
}