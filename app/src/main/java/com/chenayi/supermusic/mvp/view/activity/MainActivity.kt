package com.chenayi.supermusic.mvp.view.activity

import android.widget.Toast
import com.chenayi.supermusic.R
import com.chenayi.supermusic.base.BaseActivity
import com.chenayi.supermusic.di.component.AppComponent
import com.chenayi.supermusic.di.component.DaggerMainComponent
import com.chenayi.supermusic.mvp.contract.MainContract
import com.chenayi.supermusic.mvp.presenter.MainPresenter

class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {
    override fun setupComponent(appComponent: AppComponent?) {
        DaggerMainComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main;
    }

    override fun initData() {
        mPresenter?.requestDatas();
    }

    override fun showResult(result: String) {
        Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show();
    }
}
