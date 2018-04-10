package com.chenayi.supermusic.mvp.presenter

import com.chenayi.supermusic.base.BasePresenter
import com.chenayi.supermusic.di.scope.ActivityScope
import com.chenayi.supermusic.mvp.contract.MainContract
import javax.inject.Inject

/**
 * Created by Chenwy on 2018/4/10.
 */
@ActivityScope
class MainPresenter @Inject constructor(var view : MainContract.View?) : BasePresenter<MainContract.View>(view) {

    fun requestDatas() {
        mView?.showResult("result");
    }
}