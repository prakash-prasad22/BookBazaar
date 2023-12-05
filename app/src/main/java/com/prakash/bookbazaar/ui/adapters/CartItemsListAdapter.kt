package com.prakash.bookbazaar.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.prakash.bookbazaar.R
import com.prakash.bookbazaar.databinding.ItemCartLayoutBinding
import com.prakash.bookbazaar.firestore.FirestoreClass
import com.prakash.bookbazaar.models.Cart
import com.prakash.bookbazaar.ui.activity.CartListActivity
import com.prakash.bookbazaar.utils.Constants
import com.prakash.bookbazaar.utils.GlideLoader

open class CartItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Cart>,
    private val updateCartItems: Boolean
) : RecyclerView.Adapter<CartItemsListAdapter.MyViewHolder>()  {
    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_cart_layout, parent, false)
        return MyViewHolder(ItemCartLayoutBinding.bind(view))
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
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(model.image, holder.binding.ivCartItemImage)

            holder.binding.tvCartItemTitle.text = model.title
            holder.binding.tvCartItemPrice.text = "Rs ${model.price}"
            holder.binding.tvCartQuantity.text = model.cart_quantity

            if (model.cart_quantity == "0") {
                holder.binding.ibRemoveCartItem.visibility = View.GONE
                holder.binding.ibAddCartItem.visibility = View.GONE

                if (updateCartItems) {
                    holder.binding.ibDeleteCartItem.visibility = View.VISIBLE
                } else {
                    holder.binding.ibDeleteCartItem.visibility = View.GONE
                }

                holder.binding.tvCartQuantity.text =
                    context.resources.getString(R.string.lbl_out_of_stock)

                holder.binding.tvCartQuantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSnackBarError
                    )
                )
            } else {
                if (updateCartItems) {
                    holder.binding.ibRemoveCartItem.visibility = View.VISIBLE
                    holder.binding.ibAddCartItem.visibility = View.VISIBLE
                    holder.binding.ibDeleteCartItem.visibility = View.VISIBLE
                } else {

                    holder.binding.ibRemoveCartItem.visibility = View.GONE
                    holder.binding.ibAddCartItem.visibility = View.GONE
                    holder.binding.ibDeleteCartItem.visibility = View.GONE
                }

                holder.binding.tvCartQuantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSecondaryText
                    )
                )
            }

            // Assign the click event to the ib_remove_cart_item.
            holder.binding.ibRemoveCartItem.setOnClickListener {

                // Call the update or remove function of firestore class based on the cart quantity.
                if (model.cart_quantity == "1") {
                    FirestoreClass().removeItemFromCart(context, model.id)
                } else {

                    val cartQuantity: Int = model.cart_quantity.toInt()

                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                    // Show the progress dialog.

                    if (context is CartListActivity) {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }

                    FirestoreClass().updateMyCart(context, model.id, itemHashMap)
                }

            }


            // Assign the click event to the ib_add_cart_item.
            holder.binding.ibAddCartItem.setOnClickListener {

                //  Call the update function of firestore class based on the cart quantity.
                val cartQuantity: Int = model.cart_quantity.toInt()

                if (cartQuantity < model.stock_quantity.toInt()) {

                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                    // Show the progress dialog.
                    if (context is CartListActivity) {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }

                    FirestoreClass().updateMyCart(context, model.id, itemHashMap)
                } else {
                    if (context is CartListActivity) {
                        context.showErrorSnackBar(
                            context.resources.getString(
                                R.string.msg_for_available_stock,
                                model.stock_quantity
                            ),
                            true
                        )
                    }
                }

            }
            

            holder.binding.ibDeleteCartItem.setOnClickListener {

                when (context) {
                    is CartListActivity -> {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                }

                FirestoreClass().removeItemFromCart(context, model.id)
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
    class MyViewHolder(val binding: ItemCartLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}