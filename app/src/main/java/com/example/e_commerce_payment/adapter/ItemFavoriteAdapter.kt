package com.example.e_commerce_payment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.models.ProductsInCartItems
import com.example.e_commerce_payment.models.ProductsInFavoriteItems
import com.example.e_commerce_payment.storage.MyPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemFavoriteAdapter(
    private val mList: List<ProductsInFavoriteItems>, private var context: Context?,
    private val deleteListener: OnProductDeleteListener
) : RecyclerView.Adapter<ItemFavoriteAdapter.ViewHolder>() {

    private var listItems: List<ProductsInFavoriteItems>? = null
    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(context!!)
    }

    fun setData(newList: List<ProductsInFavoriteItems>) {
        listItems = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageItem: ImageView = itemView.findViewById(R.id.imgItem)
        val btnRemove: ImageView = itemView.findViewById(R.id.btnRemove)
        val btnAddToBag: ImageButton = itemView.findViewById(R.id.btnAddToBag)
        val tvNameItem: TextView = itemView.findViewById(R.id.tvNameItem)

        //        val tvNameCategory: TextView = itemView.findViewById(R.id.tvNameBranch)
        val tvPriceItem: TextView = itemView.findViewById(R.id.tvPrice)
        val tvSize: TextView = itemView.findViewById(R.id.tvSize)
        val tvColor: TextView = itemView.findViewById(R.id.tvColor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.line_item_list_in_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productsInFavoriteItems: ProductsInFavoriteItems = mList[position]
        context?.let {
            Glide.with(it).load(productsInFavoriteItems.productImg).into(holder.imageItem)
        }

        holder.tvNameItem.text = productsInFavoriteItems.productName
//        holder.tvNameCategory.text = productsInFavoriteItems.categoryId.toString()
        holder.tvPriceItem.text =
            (productsInFavoriteItems.sellingPrice - productsInFavoriteItems.discountPrice).toString()
        holder.tvSize.text = "Size"
        holder.tvColor.text = "Color"

        holder.btnRemove.setOnClickListener {
            val deleteProductsId = mList[position].id
            mList.toMutableList().removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, mList.size)

            Toast.makeText(context, "Item deleted", Toast.LENGTH_LONG).show()
            deleteListener.onProductDelete(deleteProductsId)

        }

        holder.btnAddToBag.setOnClickListener {
            // call api add to cart

            val apiService: ApiService = ApiConfig.setUpRetrofit().create(ApiService::class.java)
            val token = "Bearer " + myPreferenceManager.getToken()

            val call = apiService.addProductToCart(token, productsInFavoriteItems.id, 1)
            call.enqueue(object : Callback<ProductsInCartItems> {
                override fun onResponse(
                    call: Call<ProductsInCartItems>,
                    response: Response<ProductsInCartItems>
                ) {
                    if (response.isSuccessful) {
                        val deleteProductsId = mList[position].id
                        mList.toMutableList().removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, mList.size)

                        Toast.makeText(context, "Item added", Toast.LENGTH_LONG).show()
                        deleteListener.onProductDelete(deleteProductsId)


                        Log.e("ItemFavoriteAdapter", "Add done!")
                        Toast.makeText(context, "Add to cart successfully", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Log.e("ItemFavoriteAdapter", "Add failed!")
                        Toast.makeText(context, "Add to cart failed", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<ProductsInCartItems>, t: Throwable) {
                    Toast.makeText(context, "Add to cart failed", Toast.LENGTH_LONG).show()
                    Log.e("ItemFavoriteAdapter", "onFailure: ${t.message}")
                }

            })


        }
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Click test", Toast.LENGTH_SHORT).show()
        }


    }

    interface OnProductDeleteListener {
        fun onProductDelete(productId: Int)
    }
}