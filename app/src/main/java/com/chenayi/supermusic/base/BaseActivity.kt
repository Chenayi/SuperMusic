package com.chenayi.supermusic.base

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import com.chenayi.supermusic.App
import com.chenayi.supermusic.R
import com.chenayi.supermusic.di.component.AppComponent
import com.yanzhenjie.sofia.Bar
import com.yanzhenjie.sofia.Sofia
import me.yokeyword.fragmentation.SupportActivity
import javax.inject.Inject

/**
 * Created by Chenwy on 2018/4/10.
 */
abstract class BaseActivity<P : IPresenter> : SupportActivity() {
    @Inject
    lateinit var mPresenter: P;

    protected var app: App? = null;
    private var bind: Unbinder? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId());
        initStatusBar()
        bind = ButterKnife.bind(this);
        app = application as App;
        setupComponent(app?.getAppComponent());
        initData();
    }

    abstract fun getLayoutId(): Int;

    abstract fun setupComponent(appComponent: AppComponent?);

    abstract fun initData();

    fun initStatusBar() {
        Sofia.with(this)
                .statusBarBackground(ContextCompat.getColor(applicationContext,R.color.colorPrimary))
                .navigationBarBackground(ContextCompat.getColor(applicationContext,R.color.colorPrimary))
    }

    override fun onDestroy() {
        bind?.unbind();
        mPresenter?.onDestory();
        super.onDestroy()
    }
}