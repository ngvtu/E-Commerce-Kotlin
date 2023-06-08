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

class ProductsInCartAdapter(
    private val mList: List<ProductsInCartItems>,
    private var context: Context?,
    private val deleteListener: OnProductDeleteListener
) : RecyclerView.Adapter<ProductsInCartAdapter.ViewHolder>() {

    private var listItems: List<ProductsInCartItems>? = null
    private var totalAmount: Int = 0  // Biến tổng số tiền
    private var onQuantityChangeListener: OnQuantityChangeListener? = null


    fun setData(newList: List<ProductsInCartItems>) {
        listItems = newList

        totalAmount = 0  // Đặt giá trị ban đầu của totalAmount
        for (item in newList) {
            totalAmount += (item.products.sellingPrice - item.products.discountPrice) * item.quantity
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            .inflate(R.layout.line_item_list_in_cart, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productsInCartItems: ProductsInCartItems = mList[position]

        context?.let {
            Glide.with(it).load(productsInCartItems.products.productImg).into(holder.image)
        }

        holder.tvNameItem.text = productsInCartItems.products.productName
        holder.tvPriceItem.text = (productsInCartItems.products.sellingPrice - productsInCartItems.products.discountPrice).toString()
        holder.tvSize.text = productsInCartItems.productSize
        holder.tvColor.text = productsInCartItems.productColor
        holder.tvQuantity.text = productsInCartItems.quantity.toString()

        holder.btnSub.setOnClickListener {
            if (productsInCartItems.quantity > 1) {
                productsInCartItems.quantity -= 1
                val priceTotalItems =
                    (productsInCartItems.products.sellingPrice - productsInCartItems.products.discountPrice) * productsInCartItems.quantity

//                priceTotalItems = priceTotalItems - productsInCartItems.products.sellingPrice
                totalAmount -= (productsInCartItems.products.sellingPrice - productsInCartItems.products.discountPrice)
                onQuantityChangeListener?.onQuantityChanged()
                holder.tvPriceItem.text = priceTotalItems.toString()
                holder.tvQuantity.text = productsInCartItems.quantity.toString()
            }
        }

        holder.btnPlus.setOnClickListener {
            productsInCartItems.quantity += 1
            val priceTotalItems =
                (productsInCartItems.products.sellingPrice - productsInCartItems.products.discountPrice)* productsInCartItems.quantity

            totalAmount -= (productsInCartItems.products.sellingPrice - productsInCartItems.products.discountPrice)
            onQuantityChangeListener?.onQuantityChanged()
            holder.tvPriceItem.text = priceTotalItems.toString()
            holder.tvQuantity.text = productsInCartItems.quantity.toString()
        }

        holder.btnMenu.setOnClickListener { v ->
            val popupMenu = PopupMenu(v.context, v)
            popupMenu.inflate(R.menu.menu_items_in_cart)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_delete -> {
                        val deletedProductId = mList[position].id
                        mList.toMutableList().removeAt(position)

                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, mList.size)
                        Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                        deleteListener.onProductDelete(deletedProductId)
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

    interface OnProductDeleteListener {
        fun onProductDelete(productId: Int)
    }


    fun setOnQuantityChangeListener(listener: OnQuantityChangeListener) {
        onQuantityChangeListener = listener
    }
    interface OnQuantityChangeListener {
        fun onQuantityChanged()
    }

}