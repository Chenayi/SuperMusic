package com.chenayi.supermusic

import android.app.Application
import cn.bmob.v3.Bmob
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

    override fun onCreate() {
        super.onCreate()
        Bmob.initialize(this, "0df3e408bb02f5592b97817ed82c16c4");
    }

    fun getAppComponent(): AppComponent? = component;
}