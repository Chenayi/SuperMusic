package com.chenayi.supermusic.mvp.ui.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import butterknife.BindView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.chenayi.supermusic.R
import com.chenayi.supermusic.adapter.MusicAdapter
import com.chenayi.supermusic.base.BaseFragment
import com.chenayi.supermusic.event.*
import com.chenayi.supermusic.mvp.entity.Song
import com.chenayi.supermusic.service.MusicService
import com.chenayi.supermusic.utils.NotNullUtils
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
        //歌曲点击事件
        musicAdapter?.setOnItemClickListener { adapter, view, position ->
            var songs = musicAdapter?.data
            var clickSong = songs?.get(position)
            var songsSize = songs?.size

            NotNullUtils.ifNotNull(songsSize, clickSong, { songsSize, clickSong ->
                notifySongItems(songsSize, clickSong)
            })

            songs?.let { preparPlay(it, position) }
        }
        rvMusic.layoutManager = LinearLayoutManager(context)
        rvMusic.adapter = musicAdapter

        requestSongs()
    }

    /**
     * 请求后端云获取歌曲列表
     */
    private fun requestSongs() {
        BmobQuery<Song>()
                .findObjects(object : FindListener<Song>() {
                    override fun done(songs: MutableList<Song>?, p1: BmobException?) {
                        progressBar.visibility = View.GONE
                        musicAdapter?.setNewData(songs)
                        startAndBindMusicService()
                    }
                })
    }

    /**
     * 刷新歌曲正在播放标识
     */
    private fun notifySongItems(songsSize: Int, clickSong: Song) {
        for (i in 0 until songsSize) {
            musicAdapter?.getItem(i)?.play = false
        }
        clickSong?.play = true
        musicAdapter?.notifyDataSetChanged()
    }

    /**
     * 准备播放
     */
    private fun preparPlay(songs: MutableList<Song>, songIndex: Int) {
        musicService?.setSongs(songs)
        musicService?.preparPlay(songIndex)
    }

    /**
     * 开启并绑定Service
     */
    private fun startAndBindMusicService() {
        var intent = Intent(context, MusicService::class.java)
        context?.startService(intent)
        context?.bindService(intent, servideConnection, Context.BIND_AUTO_CREATE)
    }

    private var servideConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            var audioBinder: MusicService.AudioBinder = service as MusicService.AudioBinder
            musicService = audioBinder.getService()
        }
    }

    /**
     * 播放或暂停
     */
    @Subscribe
    fun playOrPause(playStatusEvent: PlayOrPauseEvent) {
        val play = playStatusEvent.isPlay
        play?.let {
            if (it) {
                musicService?.rePlay()
            } else {
                musicService?.pause()
            }
        }
    }

    /**
     * 播放下一首
     */
    @Subscribe
    fun playNext(playNextEvent: PlayNextEvent) {
        val curSongIndex = musicService?.getCurSongIndex()
        curSongIndex?.let {
            val datas = musicAdapter?.data
            val size = datas?.size

            datas?.get(curSongIndex)?.play = false
            //如果当前是最后一首，播放第一首
            if (curSongIndex == size?.minus(1)) {
                datas?.get(0)?.play = true
            }
            //否则直接播放下一首
            else {
                val nextSongIndex = curSongIndex.plus(1)
                datas?.get(nextSongIndex)?.play = true
            }

            musicAdapter?.notifyDataSetChanged()
        }
        musicService?.next()
    }

    /**
     * 播放上一首
     */
    @Subscribe
    fun playLast(playLastEvent: PlayLastEvent) {
        val curSongIndex = musicService?.getCurSongIndex()

        curSongIndex?.let {
            val datas = musicAdapter?.data
            val size = datas?.size

            datas?.get(curSongIndex)?.play = false

            //如果当前是第一首，播放最后一首
            if (curSongIndex == 0) {
                size?.let {
                    datas?.get(it.minus(1))?.play = true
                }
            }
            //否则直接播放上一首
            else {
                val lastSongIndex = curSongIndex.minus(1)
                datas?.get(lastSongIndex)?.play = true
            }

            musicAdapter?.notifyDataSetChanged()
        }

        musicService?.last()
    }

    override fun isLoadEventBus(): Boolean {
        return true
    }
}