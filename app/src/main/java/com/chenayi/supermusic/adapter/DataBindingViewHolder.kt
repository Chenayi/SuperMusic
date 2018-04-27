package com.chenayi.supermusic.adapter

import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import android.databinding.ViewDataBinding
import com.chenayi.supermusic.R


/**
 * Created by Chenwy on 2018/4/27.
 */
class DataBindingViewHolder constructor(view: View) : BaseViewHolder(view) {
    fun getBinding(): ViewDataBinding {
        return itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding
    }
}