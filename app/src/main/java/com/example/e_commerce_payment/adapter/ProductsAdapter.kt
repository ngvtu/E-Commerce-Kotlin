package com.example.e_commerce_payment.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce_payment.R
import com.example.e_commerce_payment.activity.DetailActivity
import com.example.e_commerce_payment.activity.admin.UpdateProductsActivity
import com.example.e_commerce_payment.api.ApiConfig
import com.example.e_commerce_payment.api.ApiService
import com.example.e_commerce_payment.models.FavoriteResponse
import com.example.e_commerce_payment.models.ProductItems
import com.example.e_commerce_payment.storage.MyPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsAdapter(
    private val mList: List<ProductItems>,
    private var context: Context?,
    private val deleteListener: OnProductDeleteListener
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private var listItems: List<ProductItems>? = null
    private val myPreferenceManager: MyPreferenceManager by lazy {
        MyPreferenceManager(context!!)
    }


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
        context?.let { Glide.with(it).load(product.productImg).into(holder.imgItem) }
        // set the data in items
        holder.tvNameItem.text = product.productName
        holder.tvPrice.text = product.sellingPrice.toString()

        if (myPreferenceManager.getRole() == 1) {
            if (product.discountPrice > 0) {
                val newPrice: Float = (product.sellingPrice - product.discountPrice).toFloat()
                val discountValue: Float =
                    ((product.sellingPrice - newPrice) / product.sellingPrice) * 100.toFloat()

                val solution = Math.round(discountValue * 10.0) / 10.0

                holder.tvIsSale.visibility = View.VISIBLE
                holder.tvIsSale.text = "-" + solution.toString() + "%"
                holder.layout_new_price.visibility = View.VISIBLE
                holder.tvNameItem.text = product.productName
                holder.tvIsNew.visibility = View.GONE
                holder.tvNewPrice.text = newPrice.toString()
                holder.btnAddFavorite.visibility = View.GONE
                holder.tvPrice.setTextColor(Color.parseColor("#9B9B9B"))
                holder.textView2.setTextColor(Color.parseColor("#9B9B9B"))
                holder.imageView8.visibility = View.VISIBLE
                holder.btnDelete.visibility = View.VISIBLE

                holder.itemView.setOnClickListener {
                    // open another activity on item click
                    val productItems = mList[position]
                    val intent = Intent(context, UpdateProductsActivity::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable("productItems", productItems)
                    intent.putExtras(bundle)
                    context!!.startActivity(intent)
                }

                holder.btnDelete.setOnClickListener {
                    val deletedProductId = mList[position].id
                    mList.toMutableList().removeAt(position)

                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, mList.size)
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                    deleteListener.onProductDelete(deletedProductId)
                }
            }

        } else {
            if (product.discountPrice > 0) {
                val newPrice: Float = (product.sellingPrice - product.discountPrice).toFloat()
                val discountValue: Float =
                    ((product.sellingPrice - newPrice) / product.sellingPrice) * 100.toFloat()

                val solution = Math.round(discountValue * 10.0) / 10.0

                holder.tvIsSale.visibility = View.VISIBLE
                holder.tvIsSale.text = "-" + solution.toString() + "%"
                holder.layout_new_price.visibility = View.VISIBLE
                holder.tvNameItem.visibility = View.VISIBLE
                holder.tvIsNew.visibility = View.GONE
                holder.tvNewPrice.text = newPrice.toString()
                holder.tvPrice.setTextColor(Color.parseColor("#9B9B9B"))
                holder.textView2.setTextColor(Color.parseColor("#9B9B9B"))
                holder.imageView8.visibility = View.VISIBLE
                holder.btnDelete.visibility = View.GONE
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

            holder.btnAddFavorite.setOnClickListener {
                val token = "Bearer " + myPreferenceManager.getToken()
                Log.d("ProductAdapter", "onBindViewHolder: $token")
                val apiService: ApiService =
                    ApiConfig.setUpRetrofit().create(ApiService::class.java)
                Log.d("ProductAdapter", "onBindViewHolder: ${product.id}")
                val call = apiService.addToFavoriteItem(token, product.id)
                call.enqueue(object : Callback<FavoriteResponse> {
                    override fun onResponse(
                        call: Call<FavoriteResponse>,
                        response: Response<FavoriteResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.d(
                                "ProductAdapter",
                                "onResponse: ${response.body()?.data?.id.toString()}"
                            )
                            Toast.makeText(context, "Add to favorite success", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(context, "Add to favorite failed", Toast.LENGTH_SHORT)
                                .show()
                            Log.d(
                                "ProductAdapter",
                                "onResponse: ${response.body()}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                        Log.e("ProductAdapter", "onFailure: ${t.message}")
                    }

                })
            }

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
        val btnAddFavorite: ImageButton = itemView.findViewById(R.id.btnAddFavorite)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

    }


    interface OnProductDeleteListener {
        fun onProductDelete(productId: Int)
    }
}