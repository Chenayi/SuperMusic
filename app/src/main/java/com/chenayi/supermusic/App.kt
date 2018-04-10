package com.chenayi.supermusic

import android.app.Application
import com.chenayi.supermusic.di.component.AppComponent
import com.chenayi.supermusic.di.component.DaggerAppComponent
import com.chenayi.supermusic.di.module.AppModule

/**
 * Created by Chenwy on 2018/4/10.
 */
class App : Application() {
    var component: AppComponent? = null;

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build();
    }

    fun getAppComponent(): AppComponent? {
        return component;
    }
}