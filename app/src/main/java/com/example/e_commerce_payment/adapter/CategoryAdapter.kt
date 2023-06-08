package com.example.e_commerce_payment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.models.CategoriesItems

class CategoryAdapter (private val mList: List<CategoriesItems>, private var context: Context?) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_category_card, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.tvCategory.text  = category.categoryName

        holder.itemView.setOnClickListener {
            Toast.makeText(context, "onClick", Toast.LENGTH_SHORT).show()
        }
        holder.btnDelete.setOnClickListener {
            Toast.makeText(context, "OnDelete", Toast.LENGTH_SHORT).show()
        }
    }
}