package com.example.shopinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "shop_list_item")
data class ShopListItem(
    @PrimaryKey (autoGenerate = true)
    val id: Int?,
    @ColumnInfo (name = "name")
    val name: String,
    @ColumnInfo (name = "itemInfo")
    val itemInfo: String = "",
    @ColumnInfo (name = "itemChecked")
    val itemChecked: Boolean = false,
    @ColumnInfo (name = "listId")
    val listId: Int,
    @ColumnInfo (name = "itemType")
    val itemType: Int = 0
)