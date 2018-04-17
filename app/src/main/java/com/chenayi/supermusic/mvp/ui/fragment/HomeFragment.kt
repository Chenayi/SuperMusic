package com.chenayi.supermusic.mvp.ui.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.IBinder
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import butterknife.BindView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.blankj.utilcode.util.LogUtils
import com.chenayi.supermusic.R
import com.chenayi.supermusic.adapter.MusicAdapter
import com.chenayi.supermusic.base.BaseFragment
import com.chenayi.supermusic.event.PlayStatusEvent
import com.chenayi.supermusic.event.ProgressEvent
import com.chenayi.supermusic.linstener.OnMusicStatusChangeLinstener
import com.chenayi.supermusic.mvp.entity.Song
import com.chenayi.supermusic.mvp.ui.activity.PlayActivity
import com.chenayi.supermusic.service.MusicService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * Created by Chenwy on 2018/4/12.
 */
class HomeFragment : BaseFragment() {
    @BindView(R.id.rv_music)
    lateinit var rvMusic: RecyclerView;
    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar;

    private var musicAdapter: MusicAdapter? = null
    private var musicService: MusicService? = null

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_home
    }

    override fun initDta() {
        musicAdapter = MusicAdapter(ArrayList())
        musicAdapter?.setOnItemClickListener { adapter, view, position ->
            var song = musicAdapter?.getItem(position)
            song?.let { play(it) }
            EventBus.getDefault().post(song)
        }
        rvMusic.layoutManager = LinearLayoutManager(context)
        rvMusic.adapter = musicAdapter

        BmobQuery<Song>()
                .findObjects(object : FindListener<Song>() {
                    override fun done(p0: MutableList<Song>?, p1: BmobException?) {
                        progressBar.visibility = View.GONE
                        musicAdapter?.setNewData(p0)
                        bindMusicService()
                    }
                })
    }

    private fun play(song: Song) {
        musicService?.play(song, object : OnMusicStatusChangeLinstener {
            override fun onMusicProgress(progress: Long, total: Long) {
                EventBus.getDefault().post(ProgressEvent(progress, total))
            }


            override fun onMusicCompletion() {
            }
        })
    }

    private fun bindMusicService() {
        var intent = Intent(context, MusicService::class.java)
        context?.startService(intent)
        context?.bindService(intent, servideConnection, Context.BIND_AUTO_CREATE)
    }

    private var servideConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            var audioBinder = service as MusicService.AudioBinder?
            musicService = audioBinder?.getService()
        }
    }

    @Subscribe
    fun playStatus(playStatusEvent: PlayStatusEvent) {
        val play = playStatusEvent.isPlay
        if (play == true) {
            musicService?.rePlay()
        } else {
            musicService?.pause()
        }
    }

    override fun isLoadEventBus(): Boolean {
        return true
    }
}