package com.chenayi.supermusic.base

import android.content.Context

/**
 * Created by Chenwy on 2018/4/10.
 */
open class BasePresenter<V : IView>(context: Context?, view: V?) : IPresenter {
    var mContext: Context? = context;
    var mView: V? = view;

    init {
        this.onStart();
    }
}