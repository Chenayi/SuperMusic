package com.chenayi.supermusic.mvp.presenter

import com.chenayi.supermusic.base.BasePresenter
import com.chenayi.supermusic.mvp.contract.MainContract
import javax.inject.Inject

/**
 * Created by Chenwy on 2018/4/10.
 */
class MainPresenter : BasePresenter<MainContract.View>() {
    fun requestDatas() {
        mView?.showResult("reqult");
    }
}