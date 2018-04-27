package com.chenayi.supermusic.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.EventBus

/**
 * Created by Chenwy on 2018/4/12.
 */
abstract class BaseFragment<D : ViewDataBinding> : SupportFragment() {
    protected var binding: D? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(getLayoutId(), container, false)
        binding = DataBindingUtil.bind(view)
        initWidgets()
        return view
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if (isLoadEventBus()) {
            EventBus.getDefault().register(this)
        }
        initDta()
    }

    abstract fun getLayoutId(): Int

    abstract fun initWidgets()

    abstract fun initDta()

    open fun isLoadEventBus(): Boolean {
        return false
    }

    override fun onDestroy() {
        binding?.unbind()
        if (isLoadEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }
}