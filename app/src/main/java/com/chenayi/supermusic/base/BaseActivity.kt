package com.chenayi.supermusic.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.widget.FrameLayout
import butterknife.ButterKnife
import butterknife.Unbinder
import com.chenayi.supermusic.App
import com.chenayi.supermusic.R
import com.chenayi.supermusic.di.component.AppComponent
import com.yanzhenjie.sofia.Sofia
import me.yokeyword.fragmentation.SupportActivity
import javax.inject.Inject
import android.view.View
import android.widget.RelativeLayout


/**
 * Created by Chenwy on 2018/4/10.
 */
abstract class BaseActivity<P : IPresenter> : SupportActivity() {
    @Inject
    lateinit var mPresenter: P;

    protected var app: App? = null;
    private var bind: Unbinder? = null;
    private var rootView: RelativeLayout? = null
    private var floatView: View? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId());
        initFloatPlayerView()
        initStatusBar()
        bind = ButterKnife.bind(this);
        app = application as App;
        setupComponent(app?.getAppComponent());
        initData();
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        addPlayerView()
    }

    abstract fun getLayoutId(): Int;

    abstract fun setupComponent(appComponent: AppComponent?);

    abstract fun initData();

    fun initFloatPlayerView() {
        var decorView = window.decorView;
        var contentView = decorView.findViewById<FrameLayout>(android.R.id.content)
        rootView = contentView.getChildAt(0) as RelativeLayout
        floatView = LayoutInflater.from(applicationContext).inflate(R.layout.float_player_view, null);
    }

    fun addPlayerView() {
        var lp = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        rootView?.addView(floatView, lp)
    }

    fun initStatusBar() {
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
        super.onDestroy()
    }
}