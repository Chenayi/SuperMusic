package com.chenayi.supermusic.helper

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.chenayi.supermusic.linstener.OnMusicStatusChangeLinstener
import com.chenayi.supermusic.mvp.entity.Song
import com.chenayi.supermusic.utils.RxScheduler
import com.pili.pldroid.player.PLMediaPlayer
import com.pili.pldroid.player.PLOnBufferingUpdateListener
import com.pili.pldroid.player.PLOnPreparedListener
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by Chenwy on 2018/4/16.
 */
class AudioPlayer private constructor() : PLOnBufferingUpdateListener, PLOnPreparedListener {
    private var mediaPlayer: PLMediaPlayer? = null;
    private var onMusicStatusChangeLinstener: OnMusicStatusChangeLinstener? = null

    private val STATE_IDLE = 0
    private val STATE_PREPARING = 1
    private val STATE_PLAYING = 2
    private val STATE_PAUSE = 3
    private var state = STATE_IDLE

    private var disposable: Disposable? = null

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
    fun init(context: Context) {
        mediaPlayer = PLMediaPlayer(context)
        mediaPlayer?.setOnBufferingUpdateListener(this);
        mediaPlayer?.setOnPreparedListener(this);
    }

    fun setOnMusicStatusChangeLinstener(onMusicStatusChangeLinstener: OnMusicStatusChangeLinstener) {
        this.onMusicStatusChangeLinstener = onMusicStatusChangeLinstener
    }

    fun play(song: Song) {
        mediaPlayer?.setDataSource(song?.songLink);
        mediaPlayer?.prepareAsync();
        state = STATE_PREPARING;
    }

    fun rePlay() {
        mediaPlayer?.start()
        setProgressCallBack()
    }

    /**
     * 暂停
     */
    fun pause() {
        mediaPlayer?.pause()
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
        state = STATE_PAUSE;
    }

    /**
     * 停止
     */
    fun stop() {
        mediaPlayer?.stop()
        state = STATE_IDLE;
    }

    override fun onBufferingUpdate(p0: Int) {
    }

    override fun onPrepared(p0: Int) {
        mediaPlayer?.start()
        setProgressCallBack()
        state = STATE_PLAYING
    }


    fun setProgressCallBack() {
        Observable.interval(1, 1, TimeUnit.SECONDS)
                .compose(RxScheduler.compose())
                .subscribe(object : Observer<Long> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: Long) {
                        val duration = mediaPlayer?.duration
                        val currentPosition = mediaPlayer?.currentPosition
                        LogUtils.e("duration : " + duration + "\ncurrentPosition : " + currentPosition)
                        onMusicStatusChangeLinstener?.onMusicProgress(currentPosition!!, duration!!)
                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }
}