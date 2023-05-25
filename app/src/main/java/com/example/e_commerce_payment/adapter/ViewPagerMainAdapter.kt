package com.example.e_commerce_payment.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.e_commerce_payment.fragment.BagFragment
import com.example.e_commerce_payment.fragment.FavoriteFragment
import com.example.e_commerce_payment.fragment.HomeFragment
import com.example.e_commerce_payment.fragment.ProfileFragment
import com.example.e_commerce_payment.fragment.ShopFragment

class  ViewPagerMainAdapter(fm: FragmentManager, behavior: Int) :
    FragmentStatePagerAdapter(fm, behavior) {  // fix error: FragmentStatePagerAdapter is deprecated

    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {
        when (position){
            0 -> return HomeFragment()
            1 -> return ShopFragment()
            2 -> return BagFragment()
            3 -> return FavoriteFragment()
            4 -> return ProfileFragment()
        }

        return TODO()
    }
}