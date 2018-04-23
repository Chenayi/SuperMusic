package com.chenayi.supermusic.helper

import android.content.Context
import com.chenayi.supermusic.event.*
import com.chenayi.supermusic.mvp.entity.Song
import com.chenayi.supermusic.utils.NotNullUtils
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
    private var songs: MutableList<Song>? = null
    private var curSong: Song? = null
    private var curSongIndex: Int? = null

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

    /**
     * 歌曲列表
     */
    fun setSongs(songs: MutableList<Song>) {
        this.songs = songs
    }

    /**
     * 准备播放
     */
    fun preparPlay(songIndex: Int) {
        disposeProgress()

        curSong = this.songs?.get(songIndex)
        curSongIndex = songIndex

        var song = songs?.get(songIndex)

        song?.let {
            EventBus.getDefault().post(PlayBeforeEvent(it))
            mediaPlayer?.setDataSource(it.songLink);
            mediaPlayer?.prepareAsync();
            state = STATE_PREPARING;
        }
    }

    /**
     * 播放上一首
     */
    fun lastPlay() {
        var lastIndex = getLastIndex()
        lastIndex?.let {
            lastPlay(it)
        }
    }

    /**
     * 播放上一首
     */
    fun lastPlay(lastSongIndex: Int) {
        preparPlay(lastSongIndex)
    }

    /**
     * 播放下一首
     */
    fun nextPlay() {
        var nextIndex = getNextIndex()
        nextIndex?.let {
            nextPlay(it)
        }
    }

    /**
     * 播放下一首
     */
    fun nextPlay(nextSongIndex: Int) {
        preparPlay(nextSongIndex)
    }

    /**
     * 获取下一首歌的索引
     */
    fun getNextIndex(): Int? {
        var nextIndex: Int?
        //当前歌曲是列表的最后一首，播放第一首
        if (curSongIndex == songs?.size?.minus(1)) {
            nextIndex = 0
        }
        //否则直接下一首
        else {
            nextIndex = curSongIndex?.plus(1)
        }
        return nextIndex
    }

    /**
     * 获取上一首的索引
     */
    fun getLastIndex(): Int? {
        var lastIndex: Int?
        //当前歌曲为第一首，播放最后一首
        if (curSongIndex == 0) {
            lastIndex = songs?.size?.minus(1)
        }
        //否则直接上一首
        else {
            lastIndex = curSongIndex?.minus(1)
        }
        return lastIndex
    }

    /**
     * 当前歌曲的索引
     */
    fun getCurSongIndex(): Int? {
        return curSongIndex
    }

    /**
     * 进度
     */
    fun seekTo(progress: Int) {
        if (mediaPlayer?.isPlaying == true) {
            disposeProgress()
            mediaPlayer?.seekTo(progress.toLong())
            startProgressCallBack()
        }
    }

    /**
     * 恢复播放
     */
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

    fun curSong(): Song? {
        return curSong
    }

    /**
     * 当前进度
     */
    fun curPorgress(): Long? {
        return mediaPlayer?.currentPosition
    }

    /**
     * 总进度
     */
    fun total(): Long? {
        return mediaPlayer?.duration
    }

    /**
     * 缓冲
     */
    override fun onBufferingUpdate(p0: Int) {
    }

    /**
     * 准备完成
     */
    override fun onPrepared(p0: Int) {
        mediaPlayer?.start()
        var total = mediaPlayer?.duration
        total?.let { EventBus.getDefault().post(PlayStartEvent(it)) }
        startProgressCallBack()
        state = STATE_PLAYING
    }

    /**
     * 播放完成
     */
    override fun onCompletion() {
        state = STATE_IDLE

        EventBus.getDefault().post(PlayCompleEvent())
        disposeProgress()

        //播放下一首
        nextPlay()
    }

    /**
     * 切断进度刷新
     */
    fun disposeProgress() {
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
    }

    /**
     * 是否正在播放
     */
    fun isPlaying(): Boolean? {
        return mediaPlayer?.isPlaying
    }


    /**
     * 每隔1秒刷新一次进度条
     */
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
                        //总进度
                        var total = mediaPlayer?.duration
                        //当前进度
                        var progress = mediaPlayer?.currentPosition
                        //发送进度event
                        NotNullUtils.ifNotNull(progress, total, { progress, total ->
                            EventBus.getDefault().post(ProgressEvent(progress, total))
                        })
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }
}