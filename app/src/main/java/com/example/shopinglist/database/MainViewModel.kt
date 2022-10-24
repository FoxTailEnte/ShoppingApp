package com.example.shopinglist.database

import androidx.lifecycle.*
import com.example.shopinglist.entities.LibraryItem
import com.example.shopinglist.entities.NoteItem
import com.example.shopinglist.entities.ShopListItem
import com.example.shopinglist.entities.ShopListNameItem
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(database: MainDataBase) : ViewModel() {
    val dao = database.getDao()
    val libraryItems = MutableLiveData<List<LibraryItem>>()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShopListName: LiveData<List<ShopListNameItem>> = dao.getAllShopListNames().asLiveData()
    fun getAllItemsFromList(listId: Int):LiveData<List<ShopListItem>>{
        return dao.getAllShopListItems(listId).asLiveData()
    }
    fun getAllLibraryItems(name: String) = viewModelScope.launch{
        libraryItems.postValue(dao.getAllLibraryItems(name))
    }
    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertShopListName(listNameItem: ShopListNameItem) = viewModelScope.launch {
        dao.insertShopListName(listNameItem)
    }
    fun insertShopItem(shopListItem: ShopListItem) = viewModelScope.launch {
        dao.insertItem(shopListItem)
        if(!isLibraryItemExist(shopListItem.name)) dao.insertLibraryItem(LibraryItem(null,shopListItem.name))
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }
    fun updateListItem(item: ShopListItem) = viewModelScope.launch {
        dao.updateListItem(item)
    }
    fun updateListName(shopListName: ShopListNameItem) = viewModelScope.launch {
        dao.updateListName(shopListName)
    }
    fun updateLibraryItem(item: LibraryItem) = viewModelScope.launch {
        dao.updateLibraryItem(item)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNotes(id)
    }
    fun deleteLibraryItem(id: Int) = viewModelScope.launch {
        dao.deleteLibraryItem(id)
    }
    fun deleteShopList(id: Int,deleteList: Boolean) = viewModelScope.launch {
        if(deleteList) dao.deleteShopListName(id)
        dao.deleteShopItemsByListId(id)
    }
    private suspend fun isLibraryItemExist(name: String): Boolean{
        return dao.getAllLibraryItems(name).isNotEmpty()
    }

    class MainViewModelFactory(var database: MainDataBase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}


