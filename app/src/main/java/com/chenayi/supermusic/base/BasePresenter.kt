package com.chenayi.supermusic.base

import android.content.Context
import javax.inject.Inject

/**
 * Created by Chenwy on 2018/4/10.
 */
open class BasePresenter<V : IView>(context: Context?, view: V?) : IPresenter {
    var mContext: Context? = null;
    var mView: V? = null;

    init {
        this.mContext = context;
        this.mView = view;
        this.onStart();
    }
}