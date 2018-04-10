package com.chenayi.supermusic.di.module

import android.content.Context
import dagger.Module

/**
 * Created by Chenwy on 2018/4/10.
 */
@Module
class AppModule {
    private var mContext: Context? = null;


    constructor(context: Context?) {
        this.mContext = context
    }

    constructor()
}