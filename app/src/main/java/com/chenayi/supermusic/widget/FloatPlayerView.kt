package com.chenayi.supermusic.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chenayi.supermusic.R
import com.chenayi.supermusic.event.PlayStatusEvent
import com.chenayi.supermusic.event.ProgressEvent
import com.chenayi.supermusic.mvp.entity.Song
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by Chenwy on 2018/4/17.
 */
class FloatPlayerView : RelativeLayout {
    @BindView(R.id.pb_play_bar)
    lateinit var pbPlayBar: ProgressBar
    @BindView(R.id.tv_song_name)
    lateinit var tvSongName: TextView
    @BindView(R.id.tv_singer)
    lateinit var tvSinger: TextView
    @BindView(R.id.iv_cover)
    lateinit var ivCover: ImageView
    @BindView(R.id.iv_play)
    lateinit var ivPlay: ImageView

    private var isPlaying: Boolean? = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val rootView = LayoutInflater.from(context).inflate(R.layout.float_player_view, this, true)
        ButterKnife.bind(rootView)
        init()
    }

    private fun init() {
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun changeProgress(progressEvent: ProgressEvent) {
        pbPlayBar.max = progressEvent.total.toInt()
        pbPlayBar.progress = progressEvent.progress.toInt()
    }

    @Subscribe
    fun startPlay(song: Song) {
        tvSongName.setText(song?.songName)
        tvSinger.setText(song?.singer)
        Glide.with(context)
                .load(song.cover)
                .apply(RequestOptions()
                        .centerCrop())
                .into(ivCover)
        ivPlay.setImageResource(R.mipmap.ic_play_bar_btn_pause)
        isPlaying = true
    }

    @OnClick(R.id.iv_play)
    fun onClick(v: View) {
        when (v.id) {
            R.id.iv_play -> {
                if (isPlaying == true){
                    EventBus.getDefault().post(PlayStatusEvent(false))
                    ivPlay.setImageResource(R.mipmap.ic_play_bar_btn_play)
                    isPlaying = false
                }else{
                    EventBus.getDefault().post(PlayStatusEvent(true))
                    ivPlay.setImageResource(R.mipmap.ic_play_bar_btn_pause)
                    isPlaying = true
                }
            }
        }
    }

    fun destory() {
        EventBus.getDefault().unregister(this)
    }
}