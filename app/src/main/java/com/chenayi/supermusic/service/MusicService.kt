package com.chenayi.supermusic.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.blankj.utilcode.util.LogUtils
import com.chenayi.supermusic.helper.AudioPlayer
import com.chenayi.supermusic.linstener.OnMusicStatusChangeLinstener
import com.chenayi.supermusic.mvp.entity.Song

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
        audioPlayer?.init(applicationContext)
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

    /**
     * 设置歌曲列表
     */
    fun setSongs(songs: MutableList<Song>) {
        audioPlayer?.setSongs(songs)
    }

    /**
     * 准备播放
     */
    fun preparPlay(songIndex: Int) {
        audioPlayer?.preparPlay(songIndex)
    }

    /**
     * 暂停播放
     */
    fun pause() {
        audioPlayer?.pause()
    }

    /**
     * 恢复播放
     */
    fun rePlay() {
        audioPlayer?.rePlay()
    }

    /**
     * 上一首
     */
    fun last() {
        audioPlayer?.lastPlay()
    }

    /**
     * 下一首
     */
    fun next() {
        audioPlayer?.nextPlay()
    }

    /**
     * 当前歌曲的索引
     */
    fun getCurSongIndex(): Int? {
        return audioPlayer?.getCurSongIndex()
    }

    /**
     * 进度
     */
    fun seekTo(progress: Int) {
        audioPlayer?.seekTo(progress)
    }

    /**
     * 是否正在播放
     */
    fun isPlaying(): Boolean? {
        return audioPlayer?.isPlaying()
    }

    /**
     * 当前进度
     */
    fun curProgress(): Long? {
        return audioPlayer?.curPorgress()
    }

    /**
     * 总进度
     */
    fun total(): Long? {
        return audioPlayer?.total()
    }

    fun curSong(): Song? {
        return audioPlayer?.curSong()
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