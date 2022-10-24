package com.example.shopinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shoping_list_names")
data class ShopListNameItem(

    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "allItemCount")
    val allItemCount: Int,

    @ColumnInfo(name = "checkedItemsCounter")
    val checkedItemsCounter: Int,

    @ColumnInfo(name = "itemsIds")
    val itemsIds: String
): Serializable
