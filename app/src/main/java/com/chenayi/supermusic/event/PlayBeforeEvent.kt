package com.chenayi.supermusic.event

import com.chenayi.supermusic.mvp.entity.Song

/**
 * Created by Chenwy on 2018/4/18.
 */
data class PlayBeforeEvent constructor(var song:Song) {
}