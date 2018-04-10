package com.chenayi.supermusic.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chenayi.supermusic.App
import com.chenayi.supermusic.di.component.AppComponent
import javax.inject.Inject

/**
 * Created by Chenwy on 2018/4/10.
 */
abstract class BaseActivity<P : IPresenter> : AppCompatActivity() {
    @Inject
    lateinit var  mPresenter: P;

    var app: App? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId());
        app = application as App?;
        setupComponent(app?.getAppComponent());
        initData();
    }

    abstract fun getLayoutId(): Int;

    abstract fun setupComponent(appComponent: AppComponent?);

    abstract fun initData();

    override fun onDestroy() {
        mPresenter?.onDestory();
        super.onDestroy()
    }
}