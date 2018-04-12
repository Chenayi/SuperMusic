package com.chenayi.supermusic.mvp.ui.activity

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import butterknife.BindView
import com.chenayi.supermusic.R
import com.chenayi.supermusic.adapter.CommonViewPagerAdapter
import com.chenayi.supermusic.base.BaseActivity
import com.chenayi.supermusic.di.component.AppComponent
import com.chenayi.supermusic.di.component.DaggerMainComponent
import com.chenayi.supermusic.di.module.MainModule
import com.chenayi.supermusic.mvp.contract.MainContract
import com.chenayi.supermusic.mvp.presenter.MainPresenter
import com.chenayi.supermusic.mvp.ui.fragment.DynamicFragment
import com.chenayi.supermusic.mvp.ui.fragment.FunnyFragment
import com.chenayi.supermusic.mvp.ui.fragment.HomeFragment
import com.chenayi.supermusic.widget.HomeTabBar

class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {
    @BindView(R.id.viewPager)
    lateinit var viewPager: ViewPager;
    @BindView(R.id.homeTabBar)
    lateinit var homeTabBar: HomeTabBar;

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

    override fun initData() {
        initViewPagerWithFragments()
        mPresenter.requestDatas()
    }

    private fun initViewPagerWithFragments() {
        fragments = ArrayList()
        fragments?.add(HomeFragment.newInstance())
        fragments?.add(FunnyFragment.newInstance())
        fragments?.add(DynamicFragment.newInstance())
        pageAdapter = fragments?.let { CommonViewPagerAdapter(supportFragmentManager, it) }
        viewPager.adapter = pageAdapter
        homeTabBar.setUpWithViewPager(viewPager)
    }

    override fun showResult(result: String) {
    }
}
