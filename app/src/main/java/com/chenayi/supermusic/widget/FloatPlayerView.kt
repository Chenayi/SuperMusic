package com.chenayi.supermusic.widget

import android.content.Context
import android.content.Intent
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
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chenayi.supermusic.R
import com.chenayi.supermusic.event.*
import com.chenayi.supermusic.mvp.entity.Song
import com.chenayi.supermusic.mvp.ui.activity.PlayActivity
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
    private var playSong: Song? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val rootView = LayoutInflater.from(context).inflate(R.layout.float_player_view, this, true)
        ButterKnife.bind(rootView)
        visibility = View.GONE
        init()
    }

    private fun init() {
        EventBus.getDefault().register(this)
        setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                var intent = Intent()
                intent.setClass(context, PlayActivity::class.java)
                intent.putExtra("song", playSong)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        })
    }

    /**
     * 刷新播放器
     */
    @Subscribe
    fun refreshPlayer(playerRefreshEvent: PlayerRefreshEvent) {
        visibility = View.VISIBLE
        playSong = playerRefreshEvent.song
        tvSongName.setText(playSong?.songName)
        tvSinger.setText(playSong?.singer)
        Glide.with(context)
                .load(playSong?.cover)
                .apply(RequestOptions()
                        .centerCrop())
                .into(ivCover)
        pbPlayBar.progress = 0
        ivPlay.setImageResource(R.mipmap.ic_play_bar_btn_play)
    }

    /**
     * 进度
     */
    @Subscribe
    fun changeProgress(progressEvent: ProgressEvent) {
        pbPlayBar.progress = progressEvent.progress.toInt()
    }

    /**
     * 播放
     */
    @Subscribe
    fun startPlay(playerStartEvent: PlayerStartEvent) {
        pbPlayBar.max = playerStartEvent.total.toInt()

        ivPlay.setImageResource(R.mipmap.ic_play_bar_btn_pause)
        isPlaying = true
    }

    /**
     * 暂停
     */
    @Subscribe
    fun pause(pauseEvent: PauseEvent) {
        ivPlay.setImageResource(R.mipmap.ic_play_bar_btn_play)
        isPlaying = false
    }

    /**
     * 继续播放
     */
    @Subscribe
    fun rePlay(rePlayEvent: RePlayEvent) {
        ivPlay.setImageResource(R.mipmap.ic_play_bar_btn_pause)
        isPlaying = true
    }

    /**
     * 播放完成
     */
    @Subscribe
    fun playComple(playCompleEvent: PlayCompleEvent) {
        pbPlayBar.progress = 0
        ivPlay.setImageResource(R.mipmap.ic_play_bar_btn_play)
        isPlaying = false
    }

    @OnClick(R.id.iv_play)
    fun onClick(v: View) {
        when (v.id) {
            R.id.iv_play -> {
                if (isPlaying == true) {
                    EventBus.getDefault().post(PlayStatusEvent(false))
                    ivPlay.setImageResource(R.mipmap.ic_play_bar_btn_play)
                    isPlaying = false
                } else {
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