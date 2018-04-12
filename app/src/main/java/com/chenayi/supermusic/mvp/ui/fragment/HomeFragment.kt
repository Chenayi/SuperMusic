package com.chenayi.supermusic.mvp.ui.fragment

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
import com.chenayi.supermusic.mvp.entity.Song


/**
 * Created by Chenwy on 2018/4/12.
 */
class HomeFragment : BaseFragment() {
    @BindView(R.id.rv_music)
    lateinit var rvMusic: RecyclerView;
    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar;

    private var musicAdapter: MusicAdapter? = null

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
        rvMusic.layoutManager = LinearLayoutManager(context)
        rvMusic.adapter = musicAdapter

        BmobQuery<Song>()
                .findObjects(object : FindListener<Song>() {
                    override fun done(p0: MutableList<Song>?, p1: BmobException?) {
                        progressBar.visibility = View.GONE
                        musicAdapter?.setNewData(p0)
                    }
                })
    }
}