package com.chenayi.supermusic.di.component

import android.content.Context
import com.chenayi.supermusic.App
import com.chenayi.supermusic.di.module.AppModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Chenwy on 2018/4/10.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun getContext(): Context?;
}