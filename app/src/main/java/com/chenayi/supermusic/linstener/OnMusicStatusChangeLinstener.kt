package com.chenayi.supermusic.linstener

import android.media.MediaPlayer

/**
 * Created by Chenwy on 2018/4/16.
 */
interface OnMusicStatusChangeLinstener {
    fun onMusicProgress(progress: Long, total: Long)

    fun onMusicCompletion()
}