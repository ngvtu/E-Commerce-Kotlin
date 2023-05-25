package com.example.e_commerce_payment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.models.ProductsInCartItems

class ProductsInCartAdapter(private val mList: List<ProductsInCartItems>, private var context: Context?)
    :RecyclerView.Adapter<ProductsInCartAdapter.ViewHolder>(){
    constructor() : this(
        emptyList(),
        null
    )
    private var listItems: List<ProductsInCartItems>? = null

    fun setData(newList: List<ProductsInCartItems>) {
        listItems = newList
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imgItem)
        val tvNameItem: TextView = itemView.findViewById(R.id.tvNameItem)
        val tvPriceItem: TextView = itemView.findViewById(R.id.tvPrice)
        val tvSize: TextView = itemView.findViewById(R.id.tvSize)
        val tvColor: TextView = itemView.findViewById(R.id.tvColor)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnSub: ImageView = itemView.findViewById(R.id.btnSub)
        val btnPlus: ImageView = itemView.findViewById(R.id.btnPlus)
        val btnMenu: ImageView = itemView.findViewById(R.id.btnMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.line_item_list_in_cart, parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productsInCartItems: ProductsInCartItems = mList[position]

        context?.let { Glide.with(it).load(productsInCartItems.products.productImg).into(holder.image) }
        holder.tvNameItem.text = productsInCartItems.products.productName
        holder.tvPriceItem.text = productsInCartItems.products.sellingPrice.toString()
        holder.tvSize.text = productsInCartItems.productSize
        holder.tvColor.text = productsInCartItems.productColor
        holder.tvQuantity.text = productsInCartItems.quantity.toString()
        holder.btnSub.setOnClickListener {
            if (productsInCartItems.quantity > 1){
                productsInCartItems.quantity -= 1
                holder.tvQuantity.text = productsInCartItems.quantity.toString()
            }
        }
        holder.btnPlus.setOnClickListener {
            productsInCartItems.quantity += 1
            holder.tvQuantity.text = productsInCartItems.quantity.toString()
        }
        holder.btnMenu.setOnClickListener { v ->
            val popupMenu = PopupMenu(v.context, v)
            popupMenu.inflate(R.menu.menu_items_in_cart)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_delete -> {

                        Toast.makeText(context, "Delete items", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.menu_favorite -> {
                        Toast.makeText(context, "Add to Favorite Coming soon", Toast.LENGTH_SHORT)
                            .show()
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }

        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Item will cumback", Toast.LENGTH_SHORT).show()
        }
    }

}