package com.example.e_commerce_payment.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.ActivityMainBinding
import com.example.e_commerce_payment.fragment.BagFragment
import com.example.e_commerce_payment.fragment.FavoriteFragment
import com.example.e_commerce_payment.fragment.HomeFragment
import com.example.e_commerce_payment.fragment.ProfileFragment
import com.example.e_commerce_payment.fragment.ShopFragment
import com.example.e_commerce_payment.models.User
import com.example.e_commerce_payment.storage.MyPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getProfile()
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

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    private fun getProfile() {
        val token: String = "Bearer " + myPreferenceManager.getToken()

        val apiService = ApiConfig.setUpRetrofit().create(ApiService::class.java).getProfile(token)
        apiService.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Log.d("ProfileFragment", "get profile done!@ ${response.body()}")
                    val user = response.body()
                    myPreferenceManager.getImage()
                    myPreferenceManager.saveProfile(
                        this@MainActivity,
                        user?.id.toString(),
                        user?.email.toString(),
                        user?.fullName.toString(),
                        user?.phone.toString(),
                        user?.address.toString(),
                        user?.dateOfBirth.toString(),
                        user?.gender.toString(),
                        user?.image.toString(),
                        user?.role!!.toInt()
                    )
                }

            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("ProfileFragment", "get profile failed!@ ${t.message}")

            }
        })
    }

}