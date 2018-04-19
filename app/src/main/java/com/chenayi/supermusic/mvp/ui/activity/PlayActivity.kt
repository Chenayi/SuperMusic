package com.chenayi.supermusic.mvp.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.IBinder
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import butterknife.BindView
import butterknife.OnClick
import com.chenayi.supermusic.R
import com.chenayi.supermusic.base.BaseActivity
import com.chenayi.supermusic.base.IPresenter
import com.chenayi.supermusic.di.component.AppComponent
import com.chenayi.supermusic.event.*
import com.chenayi.supermusic.service.MusicService
import com.chenayi.supermusic.widget.DiscView
import com.yanzhenjie.sofia.Sofia
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by Chenwy on 2018/4/16.
 */
class PlayActivity : BaseActivity<IPresenter>() {
    @BindView(R.id.disc_view)
    lateinit var discView: DiscView
    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.musicSeekBar)
    lateinit var seekBar: SeekBar
    @BindView(R.id.iv_play)
    lateinit var ivPlay: ImageView

    private var musicService: MusicService? = null
    private var isSeekBarTouch = false
    private var isPlaying: Boolean? = null

    override fun setupComponent(appComponent: AppComponent?) {
    }

    override fun initData() {

        var intent = Intent(this, MusicService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

    override fun getLayoutId(): Int {
        return R.layout.activity_play
    }

    override fun initStatusBar() {
        Sofia.with(this)
                .statusBarBackground(Color.TRANSPARENT)
                .navigationBarBackground(Color.TRANSPARENT)
                .invasionStatusBar()
                .invasionNavigationBar()
                .fitsStatusBarView(R.id.ll_container)
                .fitsNavigationBarView(R.id.ll_container)
    }

    override fun showFloatPlayer(): Boolean {
        return false
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
        val progress = musicService?.curProgress()?.toInt()
        var total = musicService?.total()?.toInt()
        total?.let { seekBar.max = it }
        progress?.let { seekBar.progress = it }

        //是否正在播放
        isPlaying = musicService?.isPlaying()
        isPlaying?.let { discView.setPlaying(it) }
        if (isPlaying == true) {
            ivPlay.setImageResource(R.mipmap.ic_pause)
        } else {
            ivPlay.setImageResource(R.mipmap.ic_play)
        }

        //歌曲
        var song = musicService?.song()

        //封面
        var songCover = song?.cover
        songCover?.let { discView.setCover(it) }

        //标题
        toolbar.title = song?.songName
        toolbar.subtitle = song?.singer
    }


    @OnClick(R.id.iv_play)
    fun onClick(v: View) {
        when (v.id) {
            R.id.iv_play -> {
                isPlaying = musicService?.isPlaying()
                if (isPlaying == true) {
                    musicService?.pause()
                    ivPlay.setImageResource(R.mipmap.ic_play)
                    discView.pause()
                    EventBus.getDefault().post(PauseEvent())
                    isPlaying = false
                } else {
                    musicService?.rePlay()
                    ivPlay.setImageResource(R.mipmap.ic_pause)
                    discView.play()
                    EventBus.getDefault().post(RePlayEvent())
                    isPlaying = true
                }
            }
        }
    }


    /**
     * 开始播放
     */
    @Subscribe
    fun startPlay(playerStartEvent: PlayerStartEvent) {
    }

    /**
     * 进度
     */
    @Subscribe
    fun changeProgress(progressEvent: ProgressEvent) {
        if (isSeekBarTouch == false) {
            seekBar.max = progressEvent.total.toInt()
            seekBar.progress = progressEvent.progress.toInt()
        }
    }

    /**
     * 播放完成
     */
    @Subscribe
    fun completion(compleEvent: PlayCompleEvent) {
        seekBar.progress = 0
        ivPlay.setImageResource(R.mipmap.ic_play)
        discView.complete()
        isPlaying = false
    }


    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}