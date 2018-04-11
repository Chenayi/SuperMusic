package com.chenayi.supermusic.di.component

import com.chenayi.supermusic.di.module.MainModule
import com.chenayi.supermusic.di.scope.ActivityScope
import com.chenayi.supermusic.mvp.ui.activity.MainActivity
import dagger.Component

/**
 * Created by Chenwy on 2018/4/10.
 */
@ActivityScope
@Component(modules = arrayOf(MainModule::class),dependencies = arrayOf(AppComponent::class))
interface MainComponent {
    fun inject(mainActivity: MainActivity);
}