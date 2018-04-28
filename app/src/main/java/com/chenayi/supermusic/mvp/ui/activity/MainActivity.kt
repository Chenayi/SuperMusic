package com.chenayi.supermusic.mvp.ui.activity

import android.support.v4.app.Fragment
import android.view.ViewGroup
import com.chenayi.supermusic.R
import com.chenayi.supermusic.adapter.CommonViewPagerAdapter
import com.chenayi.supermusic.base.BaseActivity
import com.chenayi.supermusic.databinding.ActivityMainBinding
import com.chenayi.supermusic.di.component.AppComponent
import com.chenayi.supermusic.di.component.DaggerMainComponent
import com.chenayi.supermusic.di.module.MainModule
import com.chenayi.supermusic.mvp.contract.MainContract
import com.chenayi.supermusic.mvp.presenter.MainPresenter
import com.chenayi.supermusic.mvp.ui.fragment.DynamicFragment
import com.chenayi.supermusic.mvp.ui.fragment.FunnyFragment
import com.chenayi.supermusic.mvp.ui.fragment.HomeFragment

class MainActivity : BaseActivity<MainPresenter, ActivityMainBinding>(), MainContract.View {
    private var fragments: MutableList<Fragment>? = null;
    private var pageAdapter: CommonViewPagerAdapter? = null;

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

    override fun rootLayout(): ViewGroup? {
        return binding?.rootLayout
    }

    override fun initData() {
        initViewPagerWithFragments()
        mPresenter.requestDatas()
    }

    private fun initViewPagerWithFragments() {
        fragments = ArrayList()
        fragments?.add(HomeFragment.newInstance())
        fragments?.add(FunnyFragment.newInstance())
        fragments?.add(DynamicFragment.newInstance())
        pageAdapter = CommonViewPagerAdapter(supportFragmentManager, fragments!!)
        binding?.viewPager?.offscreenPageLimit = fragments?.size!!
        binding?.viewPager?.adapter = pageAdapter
        binding?.viewPager?.let { binding?.homeTabBar?.setUpWithViewPager(it) }
    }

    override fun showResult(result: String) {
    }
}
