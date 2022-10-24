package com.example.shopinglist.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shopinglist.entities.LibraryItem
import com.example.shopinglist.entities.NoteItem
import com.example.shopinglist.entities.ShopListNameItem
import com.example.shopinglist.entities.ShopListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query ("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>
    @Query ("SELECT * FROM shoping_list_names")
    fun getAllShopListNames(): Flow<List<ShopListNameItem>>

    @Query ("SELECT * FROM shop_list_item WHERE listId LIKE :listId")
    fun getAllShopListItems(listId: Int): Flow<List<ShopListItem>>

    @Query ("SELECT * FROM library WHERE name LIKE :name")
    suspend fun getAllLibraryItems(name: String): List<LibraryItem>

    @Query ("DELETE FROM note_list WHERE id IS :id ")
    suspend fun deleteNotes(id: Int)

    @Query ("DELETE FROM library WHERE id IS :id ")
    suspend fun deleteLibraryItem(id: Int)

    @Query ("DELETE FROM shoping_list_names WHERE id IS :id ")
    suspend fun deleteShopListName(id: Int)

    @Query ("DELETE FROM shop_list_item WHERE listId LIKE :listId")
    suspend fun deleteShopItemsByListId(listId: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)
    @Insert
    suspend fun insertLibraryItem(libraryItem: LibraryItem)
    @Insert
    suspend fun insertItem(shopListItem: ShopListItem)
    @Insert
    suspend fun insertShopListName(name: ShopListNameItem)
    @Update
    suspend fun updateNote(note: NoteItem)
    @Update
    suspend fun updateListItem(item: ShopListItem)
    @Update
    suspend fun updateLibraryItem(item: LibraryItem)
    @Update
    suspend fun updateListName(shopListName: ShopListNameItem)
}