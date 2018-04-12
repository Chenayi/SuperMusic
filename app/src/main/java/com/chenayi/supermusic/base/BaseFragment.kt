package com.chenayi.supermusic.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.chenayi.supermusic.App
import me.yokeyword.fragmentation.SupportFragment

/**
 * Created by Chenwy on 2018/4/12.
 */
abstract class BaseFragment : SupportFragment() {
    private var bind: Unbinder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(getLayoutId(), container, false)
        bind = ButterKnife.bind(view)
        return view
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initDta()
    }

    abstract fun getLayoutId(): Int

    abstract fun initDta()

    override fun onDestroy() {
        bind?.unbind()
        super.onDestroy()
    }
}