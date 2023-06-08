package com.example.e_commerce_payment.activity.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.activity.LoginActivity
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.ActivitySettingsBinding
import com.example.e_commerce_payment.models.UpdateInFoResponse
import com.example.e_commerce_payment.storage.MyPreferenceManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

//    private val myPreferenceManager: MyPreferenceManager by lazy {
//        MyPreferenceManager(this@SettingsActivity)
//    }
    private lateinit var myPreferenceManager: MyPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreferenceManager = MyPreferenceManager(this@SettingsActivity)

        addEvents()
    }

    private fun addEvents() {


        binding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.btnChangeInfo.setOnClickListener {
            val dialog = Dialog(it.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet_change_info)

            val layout_change_name = dialog.findViewById<TextInputLayout>(R.id.layout_change_name)
            val layout_change_phone = dialog.findViewById<TextInputLayout>(R.id.layout_change_phone)
            val layout_change_address =
                dialog.findViewById<TextInputLayout>(R.id.layout_change_address)
            val layout_date_of_birth =
                dialog.findViewById<TextInputLayout>(R.id.layout_date_of_birth)
            val edtName = dialog.findViewById<TextInputEditText>(R.id.edtName)
            val edtPhone = dialog.findViewById<TextInputEditText>(R.id.edtPhone)
            val edtAddress = dialog.findViewById<TextInputEditText>(R.id.edtAddress)
            val edtDateOfBirth = dialog.findViewById<TextInputEditText>(R.id.edtDateOfBirth)
            val radioMale = dialog.findViewById<RadioButton>(R.id.radioMale)
            val radioFeMale = dialog.findViewById<RadioButton>(R.id.radioFeMale)
            val radioGroup = dialog.findViewById<RadioGroup>(R.id.radioGroup)
            val btnUpdate = dialog.findViewById<TextView>(R.id.btnUpdate)

            btnUpdate.setOnClickListener {

                Log.d("SettingsActivity", "addEvents: click")
                val name = sanitizeInput(edtName.text.toString())
                val phone = sanitizeInput(edtPhone.text.toString())
                val address = sanitizeInput(edtAddress.text.toString())
                val dateOfBirth = edtDateOfBirth.text.toString()
                val gender = if (radioMale.isChecked) "1" else "2"


                if (name.nonEmpty() && phone.nonEmpty() && address.nonEmpty() && dateOfBirth.nonEmpty()) {
                    layout_change_name.error = null
                    layout_change_phone.error = null
                    layout_change_address.error = null
                    layout_date_of_birth.error = null

                    val token = "Bearer "  +myPreferenceManager.getToken()
                    val id = myPreferenceManager.getIdUser()

                    val apiService: ApiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)
                    val call = apiService.updateInfoUser(token, id, name, address, gender, dateOfBirth)
                    Log.d("SettingsActivity", "addEvents: $token $id $name $address $gender $dateOfBirth")
                    call.enqueue(object: Callback<UpdateInFoResponse>{
                        override fun onResponse(
                            call: Call<UpdateInFoResponse>,
                            response: Response<UpdateInFoResponse>
                        ) {

                            if (response.isSuccessful){
                                Log.d("SettingsActivity", "Call done: ${response.body()?.message}")
                                dialog.dismiss()
                            } else {
                                try {
                                    val errorBody = response.errorBody()?.string()
                                    val jsonObject = errorBody?.let { JSONObject(it) }
                                    val errorMessage = jsonObject?.getString("message")

                                    Log.e("SettingsActivity", "response fail: $errorMessage")

                                    // Xử lý lỗi dựa trên thông báo lỗi nhận được
                                    // ...
                                    Log.e("SettingsActivity", "call sai: $errorMessage")


                                } catch (e: JSONException) {
                                    Log.e("SettingsActivity", "Error parsing error response: ${e.message}")

                                    // Xử lý lỗi khi không thể phân tích thông báo lỗi
                                    // ...
                                }

                            }
                        }
                        override fun onFailure(call: Call<UpdateInFoResponse>, t: Throwable) {
                            Log.d("SettingsActivity", "onFailure: ${t.message}")
                        }

                    })

                } else {
                    if (!name.nonEmpty()) {
                        layout_change_name.error = "Name is required"
                    } else {
                        layout_change_name.error = null
                    }
                    if (!phone.nonEmpty()) {
                        layout_change_phone.error = "Phone is required"
                    } else {
                        layout_change_phone.error = null
                    }
                    if (!address.nonEmpty()) {
                        layout_change_address.error = "Address is required"
                    } else {
                        layout_change_address.error = null
                    }
                    if (!dateOfBirth.nonEmpty()) {
                        layout_date_of_birth.error = "Date of birth is required"
                    } else {
                        layout_date_of_birth.error = null
                    }
                }
            }

            dialog.show()
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }


        binding.btnSignOut.setOnClickListener {
            myPreferenceManager.clearLoginData()
            intent = intent.setClass(this@SettingsActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnChangePass.setOnClickListener {
            val dialog = Dialog(it.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet_change_password)




            dialog.show()
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }

    }

    fun sanitizeInput(input: String): String {
        // Loại bỏ các ký tự có thể tấn công XSS
        val sanitizedInput = input.replace(Regex("[<>&'\"]"), "")

        // Loại bỏ các ký tự có thể tấn công SQL injection
        val pattern = Pattern.compile("[\"';\\\\]")
        val matcher = pattern.matcher(sanitizedInput)
        val safeInput = matcher.replaceAll("")

        return safeInput
    }
}