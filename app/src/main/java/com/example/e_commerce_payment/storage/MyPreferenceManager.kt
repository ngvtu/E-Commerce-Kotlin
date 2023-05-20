package com.example.e_commerce_payment.storage

import android.content.Context
import android.content.SharedPreferences


class MyPreferenceManager(context: Context) {
    var context: Context? = null
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    private val PREF_NAME = "e_commerce_payment"
    val KEY_ID = "id"
    val KEY_ACCESS_TOKEN = "access_token"
    val IS_LOGIN = "is_login"
    val KEY_EMAIL = "email"
    val KEY_PASSWORD = "password"


    init {
        this.context = context
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
    }

    fun saveToken(token: String?) {
        editor!!.putString(KEY_ACCESS_TOKEN, token)
        editor!!.apply()
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
        editor!!.remove(KEY_EMAIL)
        editor!!.remove(KEY_PASSWORD)
        editor!!.remove(IS_LOGIN)
        editor!!.remove(KEY_ID)
        editor!!.remove(KEY_ACCESS_TOKEN)
        editor!!.apply()
    }

    // create fun to get email, get password
    fun getEmail(): String? {
        return sharedPreferences!!.getString(KEY_EMAIL, null)
    }

    fun getPassword(): String? {
        return sharedPreferences!!.getString(KEY_PASSWORD, null)
    }

    fun saveLogin(context: Context, email: String, password: String, isLogin: Boolean) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_PASSWORD, password)
        editor.putBoolean(IS_LOGIN, isLogin)
        editor.apply()
    }

    fun getIsLogin(): Boolean {
        return sharedPreferences!!.getBoolean(IS_LOGIN, false)
    }

}