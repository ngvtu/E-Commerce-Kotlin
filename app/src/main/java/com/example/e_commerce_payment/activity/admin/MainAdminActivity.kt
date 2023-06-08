package com.example.e_commerce_payment.activity.admin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.databinding.ActivityMainAdminBinding

class MainAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEvents()

    }
    private fun addEvents() {
        val productsFragment = ProductsFragment()
        val oderFragment = OderFragment()
        val statisticalFragment = StatisticalFragment()
        val userManagerFragment = UserManagerFragment()
        val categoryFragment = CategoryFragment()


        replace(ProductsFragment())

        //get event bottom navigation
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.products -> replace(productsFragment)
                R.id.oder -> replace(oderFragment)
                R.id.statistical -> replace(statisticalFragment)
                R.id.user -> replace(userManagerFragment)
                R.id.category -> replace(categoryFragment)
            }
            true
        }

        binding.btnAddCategory.setOnClickListener {

        }
        binding.btnAddProduct.setOnClickListener {
            val intent = Intent(this@MainAdminActivity, CreateProductsActivity::class.java)
            startActivity(intent)
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

}