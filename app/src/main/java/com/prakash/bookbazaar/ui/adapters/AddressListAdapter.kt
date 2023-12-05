package com.prakash.bookbazaar.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.prakash.bookbazaar.R
import com.prakash.bookbazaar.databinding.ItemAddressLayoutBinding
import com.prakash.bookbazaar.databinding.ItemCartLayoutBinding
import com.prakash.bookbazaar.models.Address
import com.prakash.bookbazaar.ui.activity.AddEditAddressActivity
import com.prakash.bookbazaar.ui.activity.CheckoutActivity
import com.prakash.bookbazaar.utils.Constants

open class AddressListAdapter(
    private val context: Context,
    private var list: ArrayList<Address>,
    private val selectAddress: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_address_layout, parent, false)
        return AddressListAdapter.MyViewHolder(ItemAddressLayoutBinding.bind(view))
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
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.binding.tvAddressFullName.text = model.name
            holder.binding.tvAddressType.text = model.type
            holder.binding.tvAddressDetails.text = "${model.address}, ${model.zipCode}"
            holder.binding.tvAddressMobileNumber.text = model.mobileNumber

            //Assign the click event to the address item when user is about to select the address.
            if (selectAddress) {
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, CheckoutActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, model)
                    context.startActivity(intent)
                }
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
     * A function to edit the address details and pass the existing details through intent.
     *
     * @param activity
     * @param position
     */
    fun notifyEditItem(activity: Activity, position: Int) {
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS, list[position])

        //Make it startActivityForResult instead of startActivity.
        activity.startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)


        notifyItemChanged(position) // Notify any registered observers that the item at position has changed.
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    private class MyViewHolder(val binding: ItemAddressLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}