package com.chenayi.supermusic.linstener

import android.media.MediaPlayer
import com.chenayi.supermusic.mvp.entity.Song

/**
 * Created by Chenwy on 2018/4/16.
 */
interface OnMusicStatusChangeLinstener {
    fun onMusicStartPlay(total : Long)

    fun onMusicProgress(progress: Long, total: Long)

    fun onMusicCompletion()
}