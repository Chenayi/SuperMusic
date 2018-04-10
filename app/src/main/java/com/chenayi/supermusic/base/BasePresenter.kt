package com.chenayi.supermusic.base

import javax.inject.Inject

/**
 * Created by Chenwy on 2018/4/10.
 */
open class BasePresenter<V : IView> : IPresenter {
    var mView : V? = null;
    constructor()
    constructor(view: V?) {
        this.mView = view
        this.onStart();
    }
}