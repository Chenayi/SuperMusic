package com.chenayi.supermusic.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chenayi.supermusic.R
import com.chenayi.supermusic.mvp.entity.Song

/**
 * Created by Chenwy on 2018/4/12.
 */
class MusicAdapter constructor(itemLayoutId: Int, var datas: MutableList<Song>)
    : BaseQuickAdapter<Song, BaseViewHolder>(itemLayoutId, datas) {

    constructor(datas: MutableList<Song>) : this(R.layout.item_music, datas)

    override fun convert(helper: BaseViewHolder?, item: Song?) {
        helper?.setText(R.id.tv_no, item?.songNo.toString())
                ?.setText(R.id.tv_song_name, item?.songName)
                ?.setText(R.id.tv_singer, item?.singer)

        item?.play?.let { helper?.setVisible(R.id.iv_acr,it) }
    }
}