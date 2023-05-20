package com.example.e_commerce_payment.storage

import android.content.Context
import android.content.SharedPreferences


class MyPreferenceManager {
    var context: Context? = null

    var sharedPreferences: SharedPreferences? = null

    var editor: SharedPreferences.Editor? = null

    private val PREF_NAME = "e_commerce_payment"

    val KEY_ID = "id"
    val KEY_ACCESS_TOKEN = "access_token"

    val KEY_EMAIL = "email"
    val KEY_PASSWORD = "password"


    fun MyPreferenceManager(context: Context) {
        this.context = context
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor?.apply()
    }

    fun putString(key: String?, value: String?) {
        editor!!.putString(key, value)
        editor!!.apply()
    }

    fun getString(key: String?): String? {
        return sharedPreferences!!.getString(key, null)
    }

    //Method to clear the login data of the application.
    fun clearLoginData() {
        editor!!.remove(KEY_ID)
        editor!!.remove(KEY_ACCESS_TOKEN)
        editor!!.apply()
    }

    // create fun to save email and password
    fun saveEmailAndPassword(email: String, password: String) {
        editor!!.putString(KEY_EMAIL, email)
        editor!!.putString(KEY_PASSWORD, password)
        editor!!.apply()
    }

    // create fun to get email, get password
    fun getEmail(): String? {
        return sharedPreferences!!.getString(KEY_EMAIL, null)
    }

    fun getPassword(): String? {
        return sharedPreferences!!.getString(KEY_PASSWORD, null)
    }

}