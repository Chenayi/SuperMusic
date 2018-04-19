package com.chenayi.supermusic.helper

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.chenayi.supermusic.event.PlayCompleEvent
import com.chenayi.supermusic.event.PlayerStartEvent
import com.chenayi.supermusic.event.ProgressEvent
import com.chenayi.supermusic.linstener.OnMusicStatusChangeLinstener
import com.chenayi.supermusic.mvp.entity.Song
import com.chenayi.supermusic.utils.RxScheduler
import com.pili.pldroid.player.PLMediaPlayer
import com.pili.pldroid.player.PLOnBufferingUpdateListener
import com.pili.pldroid.player.PLOnCompletionListener
import com.pili.pldroid.player.PLOnPreparedListener
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

/**
 * Created by Chenwy on 2018/4/16.
 */
class AudioPlayer private constructor() : PLOnBufferingUpdateListener, PLOnPreparedListener, PLOnCompletionListener {
    private var mediaPlayer: PLMediaPlayer? = null;

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
        mediaPlayer?.setOnCompletionListener(this)
    }

    fun play(song: Song) {
        disposeProgress()
        mediaPlayer?.setDataSource(song?.songLink);
        mediaPlayer?.prepareAsync();
        state = STATE_PREPARING;
    }

    fun seekTo(progress: Int) {
        if (mediaPlayer?.isPlaying == true) {
            disposeProgress()
            mediaPlayer?.seekTo(progress.toLong())
            startProgressCallBack()
        }
    }

    fun rePlay() {
        mediaPlayer?.start()
        startProgressCallBack()
    }

    /**
     * 暂停
     */
    fun pause() {
        mediaPlayer?.pause()
        disposeProgress()
        state = STATE_PAUSE;
    }

    fun curPorgress(): Long? {
        return mediaPlayer?.currentPosition
    }

    fun total(): Long? {
        return mediaPlayer?.duration
    }

    /**
     * 停止
     */
    fun stop() {
        mediaPlayer?.stop()
        disposeProgress()
        state = STATE_IDLE;
    }

    override fun onBufferingUpdate(p0: Int) {
    }

    override fun onPrepared(p0: Int) {
        mediaPlayer?.start()
        var total = mediaPlayer?.duration
        total?.let { EventBus.getDefault().post(PlayerStartEvent(it)) }
        startProgressCallBack()
        state = STATE_PLAYING
    }

    override fun onCompletion() {
        EventBus.getDefault().post(PlayCompleEvent())
        disposeProgress()
    }

    fun disposeProgress() {
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }


    fun startProgressCallBack() {
        Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
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
//                        LogUtils.e("duration : " + duration + "\ncurrentPosition : " + currentPosition)
                        EventBus.getDefault().post(ProgressEvent(currentPosition!!, duration!!))
                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }
}