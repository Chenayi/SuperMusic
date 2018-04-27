package com.chenayi.supermusic.mvp.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.IBinder
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.chenayi.supermusic.R
import com.chenayi.supermusic.base.BaseActivity
import com.chenayi.supermusic.base.IPresenter
import com.chenayi.supermusic.databinding.ActivityPlayBinding
import com.chenayi.supermusic.di.component.AppComponent
import com.chenayi.supermusic.event.*
import com.chenayi.supermusic.service.MusicService
import com.chenayi.supermusic.utils.NotNullUtils
import com.chenayi.supermusic.widget.DiscView
import com.yanzhenjie.sofia.Sofia
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by Chenwy on 2018/4/16.
 */
class PlayActivity : BaseActivity<IPresenter, ActivityPlayBinding>() {
    private var discView: DiscView? = null
    private var toolbar: Toolbar? = null
    private var seekBar: SeekBar? = null

    private var musicService: MusicService? = null
    private var isSeekBarTouch = false


    override fun setupComponent(appComponent: AppComponent?) {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_play
    }

    override fun rootLayout(): ViewGroup? {
        return null
    }

    override fun initWidgets() {
        discView = binding?.discView
        toolbar = binding?.toolbar
        seekBar = binding?.musicSeekBar
        binding?.play = this
    }

    override fun initData() {

        var intent = Intent(this, MusicService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })


        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeekBarTouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeekBarTouch = false
                var progress = seekBar?.progress
                progress?.let { musicService?.seekTo(it) }
            }
        })
    }


    override fun initStatusBar() {
        Sofia.with(this)
                .statusBarBackground(Color.TRANSPARENT)
                .invasionStatusBar()
                .fitsStatusBarView(binding?.llContainer)
    }

    override fun isLoadEventBus(): Boolean {
        return true
    }

    private var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            var audioBinder: MusicService.AudioBinder = service as MusicService.AudioBinder
            musicService = audioBinder.getService()

            init()
        }
    }

    private fun init() {

        //进度
        var progress = musicService?.curProgress()?.toInt()
        var total = musicService?.total()?.toInt()
        total?.let { seekBar?.max = it }
        progress?.let { seekBar?.progress = it }

        //是否正在播放
        var isPlaying = musicService?.isPlaying()
        isPlaying?.let {
            if (it) {
                binding?.playIcon = R.mipmap.ic_pause
            } else {
                binding?.playIcon = R.mipmap.ic_play
            }
        }

        //歌曲
        var song = musicService?.curSong()

        //封面
        var songCover = song?.cover
        NotNullUtils.ifNotNull(isPlaying, songCover, { isPlaying, songCover ->
            discView?.setCover(songCover, isPlaying)
        })

        //标题
        toolbar?.title = song?.songName
        toolbar?.subtitle = song?.singer
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.ivPlay -> {
                var isPlaying = musicService?.isPlaying()

                isPlaying?.let {
                    if (it) {
                        musicService?.pause()
                        binding?.playIcon = R.mipmap.ic_play
                        discView?.pause()
                        EventBus.getDefault().post(PauseEvent())
                    } else {
                        musicService?.rePlay()
                        binding?.playIcon = R.mipmap.ic_pause
                        discView?.play()
                        EventBus.getDefault().post(RePlayEvent())
                    }
                }
            }
            R.id.ivLast -> {
                EventBus.getDefault().post(PlayLastEvent())
            }

            R.id.ivNext -> {
                EventBus.getDefault().post(PlayNextEvent())
            }
        }
    }


    /**
     * 开始播放
     */
    @Subscribe
    fun startPlay(playerStartEvent: PlayStartEvent) {
    }

    /**
     * 进度
     */
    @Subscribe
    fun changeProgress(progressEvent: ProgressEvent) {
        if (isSeekBarTouch == false) {
            seekBar?.max = progressEvent.total.toInt()
            seekBar?.progress = progressEvent.progress.toInt()
        }
    }

    /**
     * 播放完成
     */
    @Subscribe
    fun completion(compleEvent: PlayCompleEvent) {
        seekBar?.progress = 0
        binding?.playIcon = R.mipmap.ic_play
        discView?.complete()
    }

    /**
     * 歌曲进入播放前播放器的准备
     */
    @Subscribe
    fun playBefore(playBeforeEvent: PlayBeforeEvent) {
        seekBar?.progress = 0
        binding?.playIcon = R.mipmap.ic_pause
        var song = playBeforeEvent.song
        //标题
        toolbar?.title = song?.songName
        toolbar?.subtitle = song?.singer
        //封面
        song.cover?.let { discView?.setCover(it, true) }
    }


    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}