package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDao {

    @Query("SELECT * FROM shopping_item")
    fun getAllShopping() : LiveData<List<ShoppingItem>>

    @Query("SELECT COUNT(found) FROM shopping_item WHERE found = 0")
    fun getNumberNotFound() : LiveData<Integer>

    @Insert
    fun addShopping(shoppingItem: ShoppingItem) : Long

    @Delete
    fun deleteShopping(shoppingItem: ShoppingItem)

    @Query("DELETE FROM shopping_item")
    fun deleteAllShopping()

    @Update
    fun updateShopping(shoppingItem: ShoppingItem)

}