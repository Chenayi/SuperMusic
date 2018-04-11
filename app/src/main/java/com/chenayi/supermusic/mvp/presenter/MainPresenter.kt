package com.chenayi.supermusic.mvp.presenter

import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import com.chenayi.supermusic.R
import com.chenayi.supermusic.base.BasePresenter
import com.chenayi.supermusic.di.scope.ActivityScope
import com.chenayi.supermusic.mvp.contract.MainContract
import javax.inject.Inject

/**
 * Created by Chenwy on 2018/4/10.
 */
@ActivityScope
class MainPresenter @Inject constructor(var context: Context?, var view: MainContract.View?) : BasePresenter<MainContract.View>(context, view) {

    private var handler:Handler? = object :Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            mContext?.getString(R.string.result)?.let { mView?.showResult(it) };
        }
    }

    fun requestDatas() {
        handler?.sendEmptyMessageDelayed(0,1000);
    }
}