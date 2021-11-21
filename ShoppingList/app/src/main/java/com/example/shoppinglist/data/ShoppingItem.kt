package com.example.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoppinglist.R
import java.io.Serializable

@Entity(tableName = "shopping_item")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) var shoppingItemId: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") var price: Float,
    @ColumnInfo(name = "categoryId") var categoryId: Int,
    @ColumnInfo(name = "found") var found: Boolean,
    @ColumnInfo(name = "description") var description: String
) : Serializable {

    companion object {
        const val FOOD: Int = 0
        const val ELECTRONIC: Int = 1
        const val BOOK: Int = 2
        const val OTHER: Int = 3
    }

    fun getImageResource(): Int {
        return when (categoryId) {
            FOOD -> {
                R.drawable.food
            }
            ELECTRONIC -> {
                R.drawable.electronic
            }
            BOOK -> {
                R.drawable.book
            }
            else -> {
                R.drawable.other
            }
        }
    }
}