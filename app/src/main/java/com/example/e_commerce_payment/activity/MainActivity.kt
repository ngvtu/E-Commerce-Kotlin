package com.example.e_commerce_payment.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.databinding.ActivityMainBinding
import com.example.e_commerce_payment.fragment.BagFragment
import com.example.e_commerce_payment.fragment.FavoriteFragment
import com.example.e_commerce_payment.fragment.HomeFragment
import com.example.e_commerce_payment.fragment.ProfileFragment
import com.example.e_commerce_payment.fragment.ShopFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEvents()

    }

    private fun addEvents() {
        val homeFragment = HomeFragment()
        val shopFragment = ShopFragment()
        val bagFragment = BagFragment()
        val favoriteFragment = FavoriteFragment()
        val profileFragment = ProfileFragment()

        replace(HomeFragment())

        //get event bottom navigation
        binding.bottomBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.home -> replace(homeFragment)
                R.id.shop -> replace(shopFragment)
                R.id.bag -> replace(bagFragment)
                R.id.favorite -> replace(favoriteFragment)
                R.id.profile -> replace(profileFragment)
            }
            true
        }
    }

    // create function to replace fragment
    private fun replace(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }


}