package com.example.e_commerce_payment.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.activity.DetailActivity
import com.example.e_commerce_payment.models.ProductItems

class ProductsAdapter(private val mList: List<ProductItems>, private var context: Context?) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    constructor() : this(
        emptyList(),
        null
    )
    private var listItems: List<ProductItems>? = null


    fun setData(newList: List<ProductItems>) {
        listItems = newList
        notifyDataSetChanged()
    }


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.line_item_new_list_in_home, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val product = mList[position]

        // Glide is use to load image
        // from url in your imageview.
        context?.let { Glide.with(it).load(product.productImg).into(holder.imgItem) }
        // set the data in items
        holder.tvNameItem.text = product.productName
        holder.tvPrice.text = product.sellingPrice.toString()

        if (product.discountPrice > 0){
            val newPrice: Float = (product.sellingPrice - product.discountPrice ).toFloat()
            val discountValue: Float = ((product.sellingPrice - newPrice)/product.sellingPrice)*100.toFloat()

            val solution = Math.round(discountValue * 10.0) / 10.0

            holder.tvIsSale.setVisibility(View.VISIBLE)
            holder.tvIsSale.setText("-" + solution.toString() + "%")
            holder.layout_new_price.setVisibility(View.VISIBLE)
//            holder.tvPrice.setText(newPrice.toString())
            holder.tvNameItem.setText(product.productName)
            holder.tvIsNew.visibility = View.GONE
            holder.tvNewPrice.setText(newPrice.toString())
            holder.tvPrice.setTextColor(Color.parseColor("#9B9B9B"))
            holder.textView2.setTextColor(Color.parseColor("#9B9B9B"))
            holder.imageView8.setVisibility(View.VISIBLE)
        }

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener {
            // open another activity on item click
            val productItems = mList[position]
            val intent = Intent(context, DetailActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("productItems", productItems)
            intent.putExtras(bundle)
            context!!.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItem: ImageView = itemView.findViewById(R.id.imgItem)
        val tvNameItem: TextView = itemView.findViewById(R.id.tvNameItem)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvIsSale: TextView = itemView.findViewById(R.id.tvIsSale)
        val tvIsNew: TextView = itemView.findViewById(R.id.tvIsNew)
        val layout_new_price: LinearLayout = itemView.findViewById(R.id.layout_new_price)
        val tvNewPrice: TextView = itemView.findViewById(R.id.tvNewPrice)
        val textView2: TextView = itemView.findViewById(R.id.textView2)
        val imageView8: ImageView = itemView.findViewById(R.id.imageView8)

    }
}