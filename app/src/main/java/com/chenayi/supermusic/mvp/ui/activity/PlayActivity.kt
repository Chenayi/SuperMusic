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
    private var musicService: MusicService? = null
    private var seekBarTouching = false

    override fun setupComponent(appComponent: AppComponent?) {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_play
    }

    override fun rootLayout(): ViewGroup? {
        return null
    }

    override fun initData() {
        var intent = Intent(this, MusicService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        binding?.play = this
        setSupportActionBar(binding?.toolbar)
        binding?.toolbar?.setNavigationOnClickListener {
            finish()
        }
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
        NotNullUtils.ifNotNull(musicService?.curProgress()?.toInt(),
                musicService?.total()?.toInt(), { progress, max ->
            binding?.pbMax = max
            binding?.pbProgress = progress
        })

        //是否正在播放
        musicService?.isPlaying()?.let {
            if (it) {
                binding?.playIcon = R.mipmap.ic_pause
            } else {
                binding?.playIcon = R.mipmap.ic_play
            }
            binding?.isPlaying = it
        }

        //歌曲
        var song = musicService?.curSong()

        //封面
        binding?.cover = song?.cover

        //标题
        binding?.title = song?.songName
        binding?.subTitle = song?.singer
    }

    /**
     * 触控手势开始
     */
    fun onStartTrackingTouch(seekBar: SeekBar?) {
        seekBarTouching = true
    }

    /**
     * 触控手势结束
     */
    fun onStopTrackingTouch(seekBar: SeekBar?) {
        seekBarTouching = false
        var progress = seekBar?.progress
        progress?.let { musicService?.seekTo(it) }
    }

    /**
     * 点击事件
     */
    fun onClick(v: View) {
        when (v.id) {
            R.id.ivPlay -> {
                var isPlaying = musicService?.isPlaying()

                isPlaying?.let {
                    if (it) {
                        musicService?.pause()
                        binding?.playIcon = R.mipmap.ic_play
                        binding?.discView?.pause()
                        EventBus.getDefault().post(PauseEvent())
                    } else {
                        musicService?.rePlay()
                        binding?.playIcon = R.mipmap.ic_pause
                        binding?.discView?.play()
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
        if (!seekBarTouching) {
            binding?.pbMax = progressEvent.total.toInt()
            binding?.pbProgress = progressEvent.progress.toInt()
        }
    }

    /**
     * 播放完成
     */
    @Subscribe
    fun completion(compleEvent: PlayCompleEvent) {
        binding?.pbProgress = 0
        binding?.playIcon = R.mipmap.ic_play
        binding?.discView?.complete()
    }

    /**
     * 歌曲进入播放前播放器的准备
     */
    @Subscribe
    fun playBefore(playBeforeEvent: PlayBeforeEvent) {
        binding?.pbProgress = 0
        binding?.playIcon = R.mipmap.ic_pause
        var song = playBeforeEvent.song
        //标题
        binding?.title = song?.songName
        binding?.subTitle = song?.singer
        //封面
        binding?.cover = song.cover
        binding?.isPlaying = true
    }


    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}