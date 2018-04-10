package com.chenayi.supermusic.di.module

import com.chenayi.supermusic.di.scope.ActivityScope
import com.chenayi.supermusic.mvp.contract.MainContract
import dagger.Module
import dagger.Provides

/**
 * Created by Chenwy on 2018/4/10.
 */
@Module
class MainModule {
    private var mMainView: MainContract.View? = null;

    constructor(mainView: MainContract.View?) {
        this.mMainView = mainView
    }

    constructor()

    @ActivityScope
    @Provides
    fun provideMainView(): MainContract.View? {
        return mMainView;
    }
}