package com.chenayi.supermusic.helper

import android.media.AudioManager
import android.media.MediaPlayer
import com.blankj.utilcode.util.LogUtils
import com.chenayi.supermusic.linstener.OnMusicStatusChangeLinstener

/**
 * Created by Chenwy on 2018/4/16.
 */
class AudioPlayer private constructor() : MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {
    private var mediaPlayer: MediaPlayer? = null;
    private var onMusicStatusChangeLinstener: OnMusicStatusChangeLinstener? = null

    private val STATE_IDLE = 0
    private val STATE_PREPARING = 1
    private val STATE_PLAYING = 2
    private val STATE_PAUSE = 3
    private var state = STATE_IDLE

    companion object {
        val getInstance: AudioPlayer by lazy {
            Holder.INSTANCE
        }
    }

    private object Holder {
        val INSTANCE = AudioPlayer()
    }

    /**
     * 初始化
     */
    fun init() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer?.setOnBufferingUpdateListener(this);
        mediaPlayer?.setOnPreparedListener(this);
    }

    fun setOnMusicStatusChangeLinstener(onMusicStatusChangeLinstener: OnMusicStatusChangeLinstener) {
        this.onMusicStatusChangeLinstener = onMusicStatusChangeLinstener
    }

    fun play(musicUrl: String) {
        mediaPlayer?.reset();
        mediaPlayer?.setDataSource(musicUrl);
        mediaPlayer?.prepare();
    }

    /**
     * 添加到播放列表
     */
    fun addAndPlay() {

    }


    /**
     * 播放
     */
    fun play() {
        state = STATE_PLAYING;
    }

    /**
     * 暂停
     */
    fun pause() {
        state = STATE_PAUSE;
    }

    /**
     * 停止
     */
    fun stop() {
        state = STATE_IDLE;
    }

    fun isPreparing(): Boolean {
        return state === STATE_PREPARING
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        onMusicStatusChangeLinstener?.onMusicProgress(mp, percent)
    }

    override fun onPrepared(mp: MediaPlayer?) {
        LogUtils.e("onMusicPrepared ... ")
        mediaPlayer?.start()
    }
}