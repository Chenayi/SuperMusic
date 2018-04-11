package com.chenayi.supermusic.mvp.ui.activity

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import com.chenayi.supermusic.R
import com.chenayi.supermusic.base.BaseActivity
import com.chenayi.supermusic.di.component.AppComponent
import com.chenayi.supermusic.di.component.DaggerMainComponent
import com.chenayi.supermusic.di.module.MainModule
import com.chenayi.supermusic.mvp.contract.MainContract
import com.chenayi.supermusic.mvp.presenter.MainPresenter

class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {
    @BindView(R.id.tvContent)
    lateinit var tvContent: TextView;
    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar;

    override fun setupComponent(appComponent: AppComponent?) {
        DaggerMainComponent.builder()
                .appComponent(appComponent)
                .mainModule(MainModule(this))
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
        progressBar.visibility = View.GONE;
        tvContent.visibility = View.VISIBLE;
        tvContent.setText(result)
    }
}
