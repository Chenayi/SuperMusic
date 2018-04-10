package com.chenayi.supermusic

import android.app.Application
import com.chenayi.supermusic.di.component.AppComponent
import com.chenayi.supermusic.di.component.DaggerAppComponent
import com.chenayi.supermusic.di.module.AppModule

/**
 * Created by Chenwy on 2018/4/10.
 */
class App : Application() {
    val component : AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build();
    }

    fun getAppComponent(): AppComponent? = component;
}