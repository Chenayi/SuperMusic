package com.chenayi.supermusic.di.module

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by Chenwy on 2018/4/10.
 */
@Module
class AppModule(var context: Context?) {
    @Provides
    fun provideContext() : Context? = context;
}