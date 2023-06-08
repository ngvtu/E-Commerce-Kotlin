package com.example.e_commerce_payment.storage

import android.content.Context
import android.content.SharedPreferences


class MyPreferenceManager(context: Context) {
    var context: Context? = null
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    private val PREF_NAME = "e_commerce_payment"
    val KEY_ID = "id"
    val KEY_IMAGE = "image"
    val KEY_ACCESS_TOKEN = "access_token"
    val IS_LOGIN = "is_login"
    val KEY_EMAIL = "email"
    val KEY_PASSWORD = "password"
    val KEY_FULLNAME = "fullName"
    val KEY_GENDER = "gender"
    val KEY_DATE_OF_BIRTH = "dateOfBirth"
    val KEY_ADDRESS = "address"
    val KEY_PHONE = "phone"
    val KEY_ROLE = "role"


    init {
        this.context = context
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
    }

    fun saveIdUser(id: String?) {
        editor!!.putString(KEY_ID, id)
        editor!!.apply()
    }

    fun saveToken(token: String?) {
        editor!!.putString(KEY_ACCESS_TOKEN, token)
        editor!!.apply()
    }

    fun getToken(): String? {
        return sharedPreferences!!.getString(KEY_ACCESS_TOKEN, null)
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
        editor!!.putString(KEY_EMAIL, "")
        editor!!.putString(KEY_PASSWORD, "")
        editor!!.putString(KEY_ID, "")
        editor!!.putString(KEY_ACCESS_TOKEN, "")
        editor!!.putBoolean(IS_LOGIN, false)
        editor!!.putString(KEY_FULLNAME, "")
        editor!!.putString(KEY_DATE_OF_BIRTH, "")
        editor!!.putString(KEY_GENDER, "")
        editor!!.putString(KEY_ADDRESS, "")
        editor!!.putString(KEY_PHONE, "")
        editor!!.putString(KEY_IMAGE, "")
        editor!!.apply()
    }

    // create fun to get email, get password
    fun getEmail(): String? {
        return sharedPreferences!!.getString(KEY_EMAIL, null)
    }

    fun getFullName(): String? {
        return sharedPreferences!!.getString(KEY_FULLNAME, null)
    }

    fun getPassword(): String? {
        return sharedPreferences!!.getString(KEY_PASSWORD, null)
    }

    fun getIdUser(): String? {
        return sharedPreferences!!.getString(KEY_ID, null)
    }

    fun  getRole(): Int {
        return  sharedPreferences!!.getInt(KEY_ROLE, 2)
    }

    fun getImage(): String? {
        return sharedPreferences!!.getString(KEY_IMAGE, "https://firebasestorage.googleapis.com/v0/b/e-commerce-d49c3.appspot.com/o/anhthe.jpg?alt=media&token=897448da-af53-41f4-b719-92a192888640")
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

    fun saveProfile(context: Context, id: String, email: String, fullName: String, phone: String, address: String, dateOfBirth: String, gender: String, image: String, role: Int) {
        sharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
        editor?.putString(KEY_ID, id)
        editor?.putString(KEY_EMAIL, email)
        editor?.putString(KEY_FULLNAME, fullName)
        editor?.putString(KEY_PHONE, phone)
        editor?.putString(KEY_ADDRESS, address)
        editor?.putString(KEY_DATE_OF_BIRTH, dateOfBirth)
        editor?.putString(KEY_GENDER, gender)
        editor?.putString(KEY_IMAGE, "https://firebasestorage.googleapis.com/v0/b/e-commerce-d49c3.appspot.com/o/anhthe.jpg?alt=media&token=897448da-af53-41f4-b719-92a192888640")
        editor?.putInt(KEY_ROLE, role)
        editor?.apply()
    }
}