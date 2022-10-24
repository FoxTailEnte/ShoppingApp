package com.example.shopinglist.database

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopinglist.R
import com.example.shopinglist.databinding.ListNameItemBinding
import com.example.shopinglist.entities.ShopListNameItem

class ShopListNameAdapter(private var listener: Listener) :
    ListAdapter<ShopListNameItem, ShopListNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListNameItemBinding.bind(view)

        fun setData(shopListNameItem: ShopListNameItem, listener: Listener) = with(binding) {
            tvListName.text = shopListNameItem.name
            tvTime.text = shopListNameItem.time
            pBar.max = shopListNameItem.allItemCount
            pBar.progress = shopListNameItem.checkedItemsCounter
            val colorState = ColorStateList.valueOf(getProgressColorState(shopListNameItem,binding.root.context))
            pBar.progressTintList = colorState
            counterCard.backgroundTintList = colorState
            val counterText = "${shopListNameItem.checkedItemsCounter}/${shopListNameItem.allItemCount}"
            tvCountre.text = counterText
            itemView.setOnClickListener{
                listener.onClickItem(shopListNameItem)
            }
            imDeleteList.setOnClickListener {
                listener.deleteItem(shopListNameItem.id!!)
            }
            imRebuildList.setOnClickListener {
                listener.editItem(shopListNameItem)
            }
        }

        private fun getProgressColorState(item:ShopListNameItem, context: Context): Int{
            return if(item.checkedItemsCounter==item.allItemCount){
                ContextCompat.getColor(context,R.color.green_main)
            }else{
                ContextCompat.getColor(context,R.color.red_pBar)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_name_item, parent, false)
                )
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<ShopListNameItem>() {
        override fun areItemsTheSame(oldItem: ShopListNameItem, newItem: ShopListNameItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopListNameItem, newItem: ShopListNameItem): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun deleteItem(id: Int)
        fun editItem(shopListName: ShopListNameItem)
        fun onClickItem(shopListName: ShopListNameItem)
    }


}