package com.chenayi.supermusic.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.blankj.utilcode.util.LogUtils
import com.chenayi.supermusic.helper.AudioPlayer
import com.chenayi.supermusic.linstener.OnMusicStatusChangeLinstener

/**
 * Created by Chenwy on 2018/4/16.
 */
class MusicService : Service() {
    private var audioBinder: IBinder? = AudioBinder(this)
    private var audioPlayer: AudioPlayer? = null

    override fun onCreate() {
        super.onCreate()
        LogUtils.e("MusicService onCreate ... ")
        audioPlayer = AudioPlayer.getInstance;
        audioPlayer?.init()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtils.e("MusicService onStartCommand ... ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        LogUtils.e("MusicService onBind ... ")
        return audioBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        LogUtils.e("MusicService onUnbind ... ")
        return super.onUnbind(intent)
    }

    fun play(musicUrl: String, onMusicStatusChangeLinstener: OnMusicStatusChangeLinstener) {
        audioPlayer?.play(musicUrl)
        audioPlayer?.setOnMusicStatusChangeLinstener(onMusicStatusChangeLinstener)
    }

    class AudioBinder constructor(private var musicService: MusicService) : Binder() {
        fun getService(): MusicService {
            return musicService
        }
    }

    override fun onDestroy() {
        LogUtils.e("MusicService onDestroy ... ")
        super.onDestroy()
    }
}