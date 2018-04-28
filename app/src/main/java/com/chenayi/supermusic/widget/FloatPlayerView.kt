package com.chenayi.supermusic.widget

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.chenayi.supermusic.R
import com.chenayi.supermusic.databinding.FloatPlayerViewBinding
import com.chenayi.supermusic.event.*
import com.chenayi.supermusic.mvp.ui.activity.PlayActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by Chenwy on 2018/4/17.
 */
class FloatPlayerView : RelativeLayout {
    private var binding: FloatPlayerViewBinding? = null


    private var isPlaying: Boolean? = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.float_player_view, this, true)
        visibility = View.GONE
        init()
    }

    private fun init() {
        binding?.floatPlayer = this
        binding?.playIcon = R.mipmap.ic_play_bar_btn_play
        binding?.coverUrl = ""
        binding?.songName = ""
        binding?.singer = ""

        EventBus.getDefault().register(this)
        setOnClickListener {
            var intent = Intent()
            intent.setClass(context, PlayActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    /**
     * 歌曲进入播放前播放器的准备
     */
    @Subscribe
    fun playBefore(playBeforeEvent: PlayBeforeEvent) {
        visibility = View.VISIBLE
        binding?.pbProgress = 0
        binding?.playIcon = R.mipmap.ic_play_bar_btn_pause
        var playSong = playBeforeEvent.song
        binding?.songName = playSong?.songName
        binding?.singer = playSong?.singer
        binding?.coverUrl = playSong?.cover
    }

    /**
     * 歌曲开始播放
     */
    @Subscribe
    fun startPlay(playerStartEvent: PlayStartEvent) {
        binding?.pbMax = playerStartEvent.total.toInt()
        binding?.playIcon = R.mipmap.ic_play_bar_btn_pause
        isPlaying = true
    }

    /**
     * 歌曲实时进度
     */
    @Subscribe
    fun changeProgress(progressEvent: ProgressEvent) {
        binding?.pbProgress = progressEvent.progress.toInt()
    }


    /**
     * 暂停
     */
    @Subscribe
    fun pause(pauseEvent: PauseEvent) {
        binding?.playIcon = R.mipmap.ic_play_bar_btn_play
        isPlaying = false
    }

    /**
     * 继续播放
     */
    @Subscribe
    fun rePlay(rePlayEvent: RePlayEvent) {
        binding?.playIcon = R.mipmap.ic_play_bar_btn_pause
        isPlaying = true
    }

    /**
     * 播放完成
     */
    @Subscribe
    fun playComple(playCompleEvent: PlayCompleEvent) {
        binding?.pbProgress = 0
        binding?.playIcon = R.mipmap.ic_play_bar_btn_play
        isPlaying = false
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.iv_play -> {
                isPlaying?.let {
                    if (it){
                        EventBus.getDefault().post(PlayOrPauseEvent(false))
                        binding?.playIcon = R.mipmap.ic_play_bar_btn_play
                    }else{
                        EventBus.getDefault().post(PlayOrPauseEvent(true))
                        binding?.playIcon = R.mipmap.ic_play_bar_btn_pause
                    }
                    isPlaying = !it
                }
            }

            R.id.iv_next -> {
                EventBus.getDefault().post(PlayNextEvent())
            }
        }
    }

    fun destory() {
        EventBus.getDefault().unregister(this)
    }
}