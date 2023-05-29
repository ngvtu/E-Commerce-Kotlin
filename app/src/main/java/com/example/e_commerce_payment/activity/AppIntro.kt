package com.example.e_commerce_payment.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.storage.MyPreferenceManager
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment

class AppIntro : AppIntro2() {
    private lateinit var myPreferenceManager: MyPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(
            AppIntroFragment.createInstance(
                title = "Online shop",
                description = "You can buy something in App",
                descriptionColorRes = R.color.black,
                titleColorRes = R.color.red_app,
                backgroundDrawable = R.color.white,
                imageDrawable = R.drawable.img_intro_buy_online
            )
        )

        // create slide 2 of app intro
        addSlide(
            AppIntroFragment.createInstance(
                title = "Payment",
                description = "You can pay by card or cash",
                descriptionColorRes = R.color.black,
                titleColorRes = R.color.red_app,
                backgroundDrawable = R.color.white,
                imageDrawable = R.drawable.img_intro_payment
            )
        )

        // create slide 3 of app intro sale off
        addSlide(
            AppIntroFragment.createInstance(
                title = "Sale off",
                description = "You can get sale off in App",
                descriptionColorRes = R.color.black,
                titleColorRes = R.color.red_app,
                backgroundDrawable = R.color.white,
                imageDrawable = R.drawable.img_intro_sale
            )
        )
        //create slide 4 of app intro quick buy
        addSlide(
            AppIntroFragment.createInstance(
                title = "Quick buy",
                description = "You can buy something in App",
                descriptionColorRes = R.color.black,
                titleColorRes = R.color.red_app,
                backgroundDrawable = R.color.white,
                imageDrawable = R.drawable.img_intro_quick_buy
            )
        )

        //create skip button
        isSkipButtonEnabled = true
        //show status bar
        showStatusBar(true)


    }

    //create onskippressed
    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        myPreferenceManager = MyPreferenceManager(this@AppIntro)

        if (myPreferenceManager.getToken() != null) {
            intent = intent.setClass(this@AppIntro, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            intent = intent.setClass(this@AppIntro, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //create ondonepressed to main activity
    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        myPreferenceManager = MyPreferenceManager(this@AppIntro)

        if (myPreferenceManager.getToken() != null) {
            intent = intent.setClass(this@AppIntro, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            intent = intent.setClass(this@AppIntro, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}