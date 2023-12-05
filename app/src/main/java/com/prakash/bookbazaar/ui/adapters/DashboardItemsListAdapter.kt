package com.prakash.bookbazaar.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prakash.bookbazaar.R
import com.prakash.bookbazaar.databinding.ItemDashboardLayoutBinding
import com.prakash.bookbazaar.models.Product
import com.prakash.bookbazaar.utils.GlideLoader

open class DashboardItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>
) : RecyclerView.Adapter<DashboardItemsListAdapter.MyViewHolder>() {

    // A global variable for OnClickListener interface.
    private var onClickListener: OnClickListener? = null


    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_dashboard_layout,parent,false)
        return MyViewHolder(ItemDashboardLayoutBinding.bind(view))
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
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(
                model.image,
                holder.binding.ivDashboardItemImage
            )
            holder.binding.tvDashboardItemTitle.text = model.title
            holder.binding.tvDashboardItemPrice.text = "Rs ${model.price}"
        }

        //  Assign the on click event for item view and pass the required params in the on click function.
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, model)
            }
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    //  Create A function for OnClickListener where the Interface is the expected parameter and assigned to the global variable.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    // Create an interface for OnClickListener.
    interface OnClickListener {

        //  Define a function to get the required params when user clicks on the item view in the interface.
        fun onClick(position: Int, product: Product)

    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(val binding : ItemDashboardLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}