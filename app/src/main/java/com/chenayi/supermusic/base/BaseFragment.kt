package com.chenayi.supermusic.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.chenayi.supermusic.App
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.EventBus

/**
 * Created by Chenwy on 2018/4/12.
 */
abstract class BaseFragment : SupportFragment() {
    private var bind: Unbinder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(getLayoutId(), container, false)
        bind = ButterKnife.bind(this, view)
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

    abstract fun initDta()

    open fun isLoadEventBus(): Boolean {
        return false
    }

    override fun onDestroy() {
        bind?.unbind()
        if (isLoadEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

//    override fun startActivity(intent: Intent?) {
//        intent?.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//设置切换没有动画，用来实现活动之间的无缝切换
//        super.startActivity(intent)
//    }
//
//    override fun startActivity(intent: Intent?, options: Bundle?) {
//        intent?.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//设置切换没有动画，用来实现活动之间的无缝切换
//        super.startActivity(intent, options)
//    }
}