package com.chenayi.supermusic.base

import android.content.pm.ActivityInfo
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.chenayi.supermusic.App
import com.chenayi.supermusic.R
import com.chenayi.supermusic.di.component.AppComponent
import com.yanzhenjie.sofia.Sofia
import me.yokeyword.fragmentation.SupportActivity
import javax.inject.Inject
import android.widget.RelativeLayout
import com.chenayi.supermusic.widget.FloatPlayerView
import org.greenrobot.eventbus.EventBus


/**
 * Created by Chenwy on 2018/4/10.
 */
abstract class BaseActivity<P : IPresenter, D : ViewDataBinding> : SupportActivity() {
    @Inject
    lateinit var mPresenter: P

    protected var binding: D? = null
    protected var app: App? = null
    protected var rootLayout: ViewGroup? = null
    protected var floatView: FloatPlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId());
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        //DatatBinding
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        //沉浸式状态栏
        initStatusBar()
        //根布局
        rootLayout = rootLayout()
        //eventbus
        if (isLoadEventBus()) {
            EventBus.getDefault().register(this)
        }
        app = application as App;
        setupComponent(app?.getAppComponent());
        initData();
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        addFloatPlayerView()
    }

    abstract fun getLayoutId(): Int;

    abstract fun setupComponent(appComponent: AppComponent?)

    abstract fun initData()

    abstract fun rootLayout(): ViewGroup?

    open fun isLoadEventBus(): Boolean {
        return false
    }

    /**
     * 状态栏沉浸式
     */
    open fun initStatusBar() {
        Sofia.with(this)
                .statusBarBackground(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
    }

    /**
     * 动态添加悬浮音乐播放器
     */
    fun addFloatPlayerView() {
        rootLayout?.let {
            if (it is RelativeLayout){
                var lp = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                floatView = FloatPlayerView(this)
                it.addView(floatView, lp)
            }
        }
    }

    override fun onDestroy() {
        binding?.unbind()
        if (::mPresenter.isInitialized) {
            mPresenter.onDestory();
        }
        floatView?.destory()
        if (isLoadEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }
}