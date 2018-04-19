package com.chenayi.supermusic.base

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import butterknife.ButterKnife
import butterknife.Unbinder
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
abstract class BaseActivity<P : IPresenter> : SupportActivity() {
    @Inject
    lateinit var mPresenter: P;

    protected var app: App? = null;
    private var bind: Unbinder? = null;
    private var rootView: RelativeLayout? = null
    protected var floatView: FloatPlayerView? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId());
        if (showFloatPlayer()){
            initFloatPlayerView()
        }
        initStatusBar()
        bind = ButterKnife.bind(this);
        if (isLoadEventBus()) {
            EventBus.getDefault().register(this)
        }
        app = application as App;
        setupComponent(app?.getAppComponent());
        initData();
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (showFloatPlayer()){
            addPlayerView()
        }
    }

    abstract fun getLayoutId(): Int;

    abstract fun setupComponent(appComponent: AppComponent?);

    abstract fun initData();

    open fun isLoadEventBus(): Boolean {
        return false
    }

    open fun showFloatPlayer():Boolean{
        return true
    }

    fun initFloatPlayerView() {
        var decorView = window.decorView;
        var contentView = decorView.findViewById<FrameLayout>(android.R.id.content)
        rootView = contentView.getChildAt(0) as RelativeLayout
        floatView = FloatPlayerView(this)
    }

    fun addPlayerView() {
        var lp = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        rootView?.addView(floatView, lp)
    }

    open fun initStatusBar() {
        Sofia.with(this)
                .statusBarBackground(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                .navigationBarBackground(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
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
//
//    override fun finish() {
//        super.finish()
//        overridePendingTransition(0, 0);//设置返回没有动画
//    }

    override fun onDestroy() {
        bind?.unbind();
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