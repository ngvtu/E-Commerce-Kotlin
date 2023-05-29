package com.example.e_commerce_payment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.models.AddressItems

class AddressShippingAdapter (private val mList: List<AddressItems>, private var context: Context?):
    RecyclerView.Adapter<AddressShippingAdapter.ViewHolder>() {
    private var listAddressShipping: List<AddressItems>? = null
    fun setData(newList: List<AddressItems>) {
        listAddressShipping = newList
        notifyDataSetChanged()
    }
    constructor() : this(
        emptyList(),
        null
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.example.e_commerce_payment.R.layout.line_shipping_address, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address = mList[position]
        
        holder.tvNameUser.text = address.fullName
        holder.tvPhoneUser.text = address.phone
        holder.tvAddress.text = address.address
        holder.tvWard.text = address.ward
        holder.tvDistrict.text = address.district
        holder.tvProvince.text = address.province
        holder.btnDelete.setOnClickListener {
            mList.drop(position)
            notifyDataSetChanged()
        }
        holder.btnEdit.setOnClickListener {
            Toast.makeText(context, "Edit address", Toast.LENGTH_SHORT).show()
        }
        
        holder.cbxUse.setOnClickListener {
            if (holder.cbxUse.isChecked) {
                Toast.makeText(context, "Use address", Toast.LENGTH_SHORT).show()
            }
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNameUser: TextView = itemView.findViewById(R.id.tvNameUser)
        val tvPhoneUser: TextView = itemView.findViewById(R.id.tvPhoneNumber)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvWard: TextView = itemView.findViewById(R.id.tvWard)
        val tvDistrict: TextView = itemView.findViewById(R.id.tvDistrict)
        val tvProvince: TextView = itemView.findViewById(R.id.tvProvince)
        val btnDelete: TextView = itemView.findViewById(R.id.btnDelete)
        val btnEdit: TextView = itemView.findViewById(R.id.btnEdit)
        val cbxUse: CheckBox = itemView.findViewById(R.id.cbxUse)
        
    }
}
