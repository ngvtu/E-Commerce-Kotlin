package com.example.e_commerce_payment.activity.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.adapter.AddressShippingAdapter
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.databinding.ActivityShippingAddressBinding
import com.example.e_commerce_payment.models.AddressGetResponse
import com.example.e_commerce_payment.models.AddressItems
import com.example.e_commerce_payment.models.AddressResponse
import com.example.e_commerce_payment.models.MessagesResponse
import com.example.e_commerce_payment.storage.MyPreferenceManager
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShippingAddressActivity : AppCompatActivity(), AddressShippingAdapter.AddressEditListener {
    private lateinit var binding: ActivityShippingAddressBinding
    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(this@ShippingAddressActivity)
    }
    private var addressShippingAdapter: AddressShippingAdapter? = null
    private lateinit var listAddress: ArrayList<AddressItems>
    private lateinit var rcvListAddress: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShippingAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getListAddress()
        addEvents()
    }

    private fun getListAddress() {
        listAddress = ArrayList()
        rcvListAddress = binding.rcvListAddress
        rcvListAddress.setHasFixedSize(true)

        rcvListAddress.layoutManager = LinearLayoutManager(this@ShippingAddressActivity, LinearLayoutManager.VERTICAL, false)

        val retrofit: ApiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)
        val call = retrofit.getShippingAddress("Bearer " + myPreferenceManager.getToken(), myPreferenceManager.getIdUser())
        call.enqueue(object : Callback<AddressGetResponse> {
            override fun onResponse(
                call: Call<AddressGetResponse>,
                response: Response<AddressGetResponse>
            ) {
                if (response.isSuccessful) {
                    val addressResponse: AddressGetResponse? = response.body()
                    if (addressResponse != null) {

                        for (addressShipping in addressResponse.data) {
                            listAddress.add(addressShipping)
                        }
                        addressShippingAdapter = AddressShippingAdapter(listAddress, this@ShippingAddressActivity, this@ShippingAddressActivity)
                        rcvListAddress.adapter = addressShippingAdapter
                    }
                } else {
                    Log.d("ShippingAddressActivity", "onResponse: " + response.message())
                }
            }

            override fun onFailure(call: Call<AddressGetResponse>, t: Throwable) {
                Log.d("ShippingAddressActivity", "onFailure: " + t.message)
            }

        })
    }

    private fun addEvents() {

        binding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }
        binding.btnAddAddress.setOnClickListener {
            val dialog = Dialog(it.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet_add_address)

            val btnUpdateAddress = dialog.findViewById<TextView>(R.id.btnUpdateAddress)
            val btnSaveAddress = dialog.findViewById<TextView>(R.id.btnSaveAddress)

            val layout_name = dialog.findViewById<TextInputLayout>(R.id.layout_name)
            val layout_province = dialog.findViewById<TextInputLayout>(R.id.layout_province)
            val layout_ward = dialog.findViewById<TextInputLayout>(R.id.layout_ward)
            val layout_district = dialog.findViewById<TextInputLayout>(R.id.layout_district)
            val layout_address = dialog.findViewById<TextInputLayout>(R.id.layout_address)
            val layout_phone = dialog.findViewById<TextInputLayout>(R.id.layout_phone)

            val edtName = dialog.findViewById<TextInputEditText>(R.id.edtName)
            val edtProvince = dialog.findViewById<TextInputEditText>(R.id.edtProvince)
            val edtWard = dialog.findViewById<TextInputEditText>(R.id.edtWard)
            val edtDistrict = dialog.findViewById<TextInputEditText>(R.id.edtDistricts)
            val edtAddress = dialog.findViewById<TextInputEditText>(R.id.edtAddress)
            val edtPhone = dialog.findViewById<TextInputEditText>(R.id.edtPhone)

            btnSaveAddress.setOnClickListener {
                Log.d("ShippingAddressActivity", "addEvents: Clicked Save Address")

                val name: String = edtName.text.toString()
                val province: String = edtProvince.text.toString()
                val ward: String = edtWard.text.toString()
                val district: String = edtDistrict.text.toString()
                val phone: String = edtPhone.text.toString()
                val address: String = edtAddress.text.toString()

                if (name.isEmpty()) {
                    layout_name.error = "Please enter your name"
                    return@setOnClickListener
                }
                if (province.isEmpty()) {
                    layout_province.error = "Please enter your province"
                    return@setOnClickListener
                }
                if (ward.isEmpty()) {
                    layout_ward.error = "Please enter your ward"
                    return@setOnClickListener
                }
                if (district.isEmpty()) {
                    layout_district.error = "Please enter your district"
                    return@setOnClickListener
                }
                if (address.isEmpty()) {
                    layout_address.error = "Please enter your address"
                    return@setOnClickListener
                }
                if (phone.isEmpty()) {
                    layout_phone.error = "Please enter your phone"
                    return@setOnClickListener
                }

                bindProgressButton(btnSaveAddress)
                btnSaveAddress.attachTextChangeAnimator()
                btnSaveAddress.showProgress {
                    buttonTextRes = R.string.adding_address
                    progressColor = getColor(R.color.WHITE)
                }

                val idUser : String = myPreferenceManager.getIdUser().toString()
                Log.d("ShippingAddressActivity", "idUser: $idUser")
                val token: String = "Bearer " + myPreferenceManager.getToken()
                val retrofit: ApiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)
                val call = retrofit.addShippingAddress(token, idUser, name, province, phone, ward, district, address)

                call.enqueue(object : Callback<AddressResponse> {
                    override fun onResponse(
                        call: Call<AddressResponse>,
                        response: Response<AddressResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@ShippingAddressActivity,
                                "Add Shipping Address Done!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            btnSaveAddress.hideProgress("Add Shipping Address done!")
                            getListAddress()
                            dialog.dismiss()

                            Log.d("ShippingAddressActivity", "onResponse: ${response.body()}")
                        } else{
                            Toast.makeText(
                                this@ShippingAddressActivity,
                                "Add Shipping Address Failed!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            btnSaveAddress.hideProgress("Add Shipping Address failed!")
                            Log.d("ShippingAddressActivity", "onResponse: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                        Log.e("ShippingAddressActivity", "onFailure: ${t.message}")
                    }
                })

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


    }

    override fun onAddressDelete(addressShippingId: Int) {
        deleteAddressShipping(addressShippingId)

        updateListAddressShipping(addressShippingId)
    }

    private fun updateListAddressShipping(addressShippingId: Int) {
        val updateList = listAddress.filter { it.id != addressShippingId}
        addressShippingAdapter?.setData(updateList)
    }

    override fun onAddressEdit(addressShippingId: Int) {
        TODO("Not yet implemented")
    }

    private fun deleteAddressShipping(addressShippingId: Int){
        val retrofit: ApiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)
        val call = retrofit.deleteShippingAddress("Bearer " + myPreferenceManager.getToken(),addressShippingId )

        call.enqueue(object : Callback<MessagesResponse>{
            override fun onResponse(
                call: Call<MessagesResponse>,
                response: Response<MessagesResponse>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(this@ShippingAddressActivity, "Delete Address Done!", Toast.LENGTH_SHORT).show()
                    Log.d("ShippingAddressActivity", "onResponse: ${response.body()}")
                    getListAddress()

                } else{
                    Toast.makeText(this@ShippingAddressActivity, "Delete Address Failed!", Toast.LENGTH_SHORT).show()
                    Log.d("ShippingAddressActivity", "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<MessagesResponse>, t: Throwable) {
                Log.d("ShippingAddressActivity", "onFailure: ${t.message}")
            }

        })
    }
}