package com.chenayi.supermusic.di.component

import com.chenayi.supermusic.mvp.view.activity.MainActivity
import com.chenayi.supermusic.di.module.MainModule
import com.chenayi.supermusic.di.scope.ActivityScope
import dagger.Component

/**
 * Created by Chenwy on 2018/4/10.
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(MainModule::class))
interface MainComponent {
    fun inject(mainActivity: MainActivity);
}