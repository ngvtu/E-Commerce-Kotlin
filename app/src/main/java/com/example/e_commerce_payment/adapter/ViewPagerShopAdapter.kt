package com.example.e_commerce_payment.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerShopAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {


    private val tabFragmentList = ArrayList<Fragment>()
    private val tabTitleList = ArrayList<String>()
    override fun getCount(): Int {
        return tabFragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        args.putString("arg_title", tabTitleList.get(position))
        // init fragment
        val fragment = tabFragmentList.get(position)
        fragment.arguments = args
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitleList.get(position)
    }
    fun addFragment(fragment: Fragment, title: String) {
        tabFragmentList.add(fragment)
        tabTitleList.add(title)
    }

}