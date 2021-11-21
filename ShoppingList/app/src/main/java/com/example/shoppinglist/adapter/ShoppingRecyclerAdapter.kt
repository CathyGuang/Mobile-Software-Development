package com.example.shoppinglist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.ScrollingActivity
import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.ShoppingItem
import com.example.shoppinglist.databinding.ShoppingRowBinding
import com.example.shoppinglist.touch.ShoppingTouchHelperCallback
import kotlin.concurrent.thread

class ShoppingRecyclerAdapter : ListAdapter<ShoppingItem, ShoppingRecyclerAdapter.ViewHolder>, ShoppingTouchHelperCallback {

    val context: Context

    constructor(context: Context) : super(ShoppingDiffCallback()) {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val shoppingRowBinding = ShoppingRowBinding.inflate(LayoutInflater.from(context),
            parent, false)
        return ViewHolder(shoppingRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentShopping = getItem(holder.adapterPosition)
        holder.bind(currentShopping)

        holder.shoppingRowBinding.btnDelete.setOnClickListener {
            deleteShopping(holder.adapterPosition)
        }

        holder.shoppingRowBinding.btnDetail.setOnClickListener {
            // Edit...
            (context as ScrollingActivity).showEditDialog(currentShopping)
        }

        holder.shoppingRowBinding.cbDone.setOnClickListener{
            currentShopping.found = holder.shoppingRowBinding.cbDone.isChecked
            thread {
                AppDatabase.getInstance(context).ItemDao().updateShopping(currentShopping)
            }
        }
    }

    fun deleteShopping(index: Int) {
        thread {
            AppDatabase.getInstance(context).ItemDao().deleteShopping(getItem(index))
        }
    }

    override fun onDismissed(position: Int) {
        deleteShopping(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(val shoppingRowBinding: ShoppingRowBinding) : RecyclerView.ViewHolder(shoppingRowBinding.root) {
        fun bind(shoppingItem: ShoppingItem) {
            shoppingRowBinding.tvName.text = shoppingItem.name
            shoppingRowBinding.tvPrice.text = shoppingItem.price.toString()
            shoppingRowBinding.myIcon.setImageResource(shoppingItem.getImageResource())
            shoppingRowBinding.cbDone.isChecked = shoppingItem.found
            shoppingRowBinding.tvDescription.text = shoppingItem.description
        }
    }
}

class ShoppingDiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
    override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem.shoppingItemId == newItem.shoppingItemId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem == newItem
    }
}