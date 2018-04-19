package com.chenayi.supermusic.mvp.entity

import java.io.Serializable

/**
 * Created by Chenwy on 2018/4/12.
 */
class Song :Serializable{

    var songNo: Int? = null;
    var songName: String? = null;
    var songLink: String? = null;
    var singer: String? = null;
    var cover: String? = null
    var play: Boolean? = null
}