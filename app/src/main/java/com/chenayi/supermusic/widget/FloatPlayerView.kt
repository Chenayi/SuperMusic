package com.chenayi.supermusic.widget

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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


    private var pbPlayBar: ProgressBar? = null
    private var tvSongName: TextView? = null
    private var tvSinger: TextView? = null
    private var ivCover: ImageView? = null
    private var ivPlay: ImageView? = null

    private var isPlaying: Boolean? = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.float_player_view, this, true)
        visibility = View.GONE
        initWidgets()
        init()
    }

    private fun initWidgets() {
        pbPlayBar = binding?.pbPlayBar
        tvSongName = binding?.tvSongName
        tvSinger = binding?.tvSinger
        ivCover = binding?.ivCover
        ivPlay = binding?.ivPlay
        binding?.floatPlayer = this
    }

    private fun init() {
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
        pbPlayBar?.progress = 0
        ivPlay?.setImageResource(R.mipmap.ic_play_bar_btn_pause)

        var playSong = playBeforeEvent.song
        tvSongName?.setText(playSong?.songName)
        tvSinger?.setText(playSong?.singer)


        ivCover?.let {
            Glide.with(context)
                    .load(playSong?.cover)
                    .apply(RequestOptions()
                            .centerCrop())
                    .into(it)
        }
    }

    /**
     * 歌曲开始播放
     */
    @Subscribe
    fun startPlay(playerStartEvent: PlayStartEvent) {
        pbPlayBar?.max = playerStartEvent.total.toInt()
        ivPlay?.setImageResource(R.mipmap.ic_play_bar_btn_pause)
        isPlaying = true
    }

    /**
     * 歌曲实时进度
     */
    @Subscribe
    fun changeProgress(progressEvent: ProgressEvent) {
        pbPlayBar?.progress = progressEvent.progress.toInt()
    }


    /**
     * 暂停
     */
    @Subscribe
    fun pause(pauseEvent: PauseEvent) {
        ivPlay?.setImageResource(R.mipmap.ic_play_bar_btn_play)
        isPlaying = false
    }

    /**
     * 继续播放
     */
    @Subscribe
    fun rePlay(rePlayEvent: RePlayEvent) {
        ivPlay?.setImageResource(R.mipmap.ic_play_bar_btn_pause)
        isPlaying = true
    }

    /**
     * 播放完成
     */
    @Subscribe
    fun playComple(playCompleEvent: PlayCompleEvent) {
        pbPlayBar?.progress = 0
        ivPlay?.setImageResource(R.mipmap.ic_play_bar_btn_play)
        isPlaying = false
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.iv_play -> {
                if (isPlaying == true) {
                    EventBus.getDefault().post(PlayOrPauseEvent(false))
                    ivPlay?.setImageResource(R.mipmap.ic_play_bar_btn_play)
                    isPlaying = false
                } else {
                    EventBus.getDefault().post(PlayOrPauseEvent(true))
                    ivPlay?.setImageResource(R.mipmap.ic_play_bar_btn_pause)
                    isPlaying = true
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