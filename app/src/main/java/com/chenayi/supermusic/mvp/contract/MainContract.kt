package com.chenayi.supermusic.mvp.contract

import com.chenayi.supermusic.base.IView

/**
 * Created by Chenwy on 2018/4/10.
 */
interface MainContract {
    interface View : IView {
        fun showResult(result: String);
    }
}