package com.chenayi.supermusic.base

import javax.inject.Inject

/**
 * Created by Chenwy on 2018/4/10.
 */
open class BasePresenter<V : IView>(view: V?) : IPresenter {
    var mView : V? = null;

    init {
        this.mView = view;
    }

//    constructor(view: V?) {
//        this.mView = view
//        this.onStart();
//    }
}