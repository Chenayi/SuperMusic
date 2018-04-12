package com.chenayi.supermusic.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by Chenwy on 2018/4/12.
 */
class CommonViewPagerAdapter constructor(fm: FragmentManager, var fragments: MutableList<Fragment>)
    : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }
}