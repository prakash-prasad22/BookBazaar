package com.prakash.bookbazaar.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prakash.bookbazaar.R
import com.prakash.bookbazaar.databinding.ItemListLayoutBinding
import com.prakash.bookbazaar.models.Product
import com.prakash.bookbazaar.ui.activity.ProductDetailsActivity
import com.prakash.bookbazaar.ui.fragments.ProductsFragment
import com.prakash.bookbazaar.utils.Constants
import com.prakash.bookbazaar.utils.GlideLoader

open class MyProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: ProductsFragment
): RecyclerView.Adapter<MyProductsListAdapter.MyViewHolder>() {
    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false)
        return MyViewHolder(ItemListLayoutBinding.bind(view))

    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(model.image, holder.binding.ivItemImage)

            holder.binding.tvItemName.text = model.title
            holder.binding.tvItemPrice.text = "Rs ${model.price}"

            holder.binding.ibDeleteProduct.setOnClickListener {

                fragment.deleteProduct(model.product_id)
            }

            holder.itemView.setOnClickListener {
                // Launch Product details screen.
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)
                context.startActivity(intent)
            }
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(val binding : ItemListLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}