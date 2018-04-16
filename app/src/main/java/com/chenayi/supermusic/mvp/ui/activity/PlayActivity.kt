package com.chenayi.supermusic.mvp.ui.activity

import android.os.Bundle
import com.chenayi.supermusic.R
import com.chenayi.supermusic.base.BaseActivity
import com.chenayi.supermusic.base.IPresenter
import com.chenayi.supermusic.di.component.AppComponent
import com.chenayi.supermusic.mvp.presenter.MainPresenter
import me.yokeyword.fragmentation.SupportActivity

/**
 * Created by Chenwy on 2018/4/16.
 */
class PlayActivity : BaseActivity<IPresenter>() {
    override fun setupComponent(appComponent: AppComponent?) {
    }

    override fun initData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_play
    }

}