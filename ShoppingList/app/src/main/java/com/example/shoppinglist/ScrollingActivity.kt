package com.example.shoppinglist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.shoppinglist.adapter.ShoppingRecyclerAdapter
import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.ShoppingItem
import com.example.shoppinglist.databinding.ActivityScrollingBinding
import com.example.shoppinglist.dialog.ShoppingDialog
import com.example.shoppinglist.touch.ShoppingRecyclerTouchCallback
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import kotlin.concurrent.thread

class ScrollingActivity : AppCompatActivity(), ShoppingDialog.ShoppingHandler {

    companion object {
        const val KEY_SHOP_EDIT = "KEY_SHOP_EDIT"
        var totalPrice: Float = 0F
    }

    private lateinit var binding: ActivityScrollingBinding
    private lateinit var adapter: ShoppingRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            ShoppingDialog().show(supportFragmentManager, "Shopping_DIALOG")
        }
        binding.fableft.setOnClickListener {
            shoppingDeleted()
        }
        initShoppingRecyclerView()

        if(!wasAlreadyStarted()){
            MaterialTapTargetPrompt.Builder(this@ScrollingActivity)
                .setTarget(binding.fab)
                .setPrimaryText(getString(R.string.new_shop_item))
                .setSecondaryText(getString(R.string.create_new_shop_description))
                .show()

            saveThatAppWasStarted()
        }
    }

    public fun showEditDialog(shoppingToEdit: ShoppingItem) {
        val editDialog = ShoppingDialog()

        val bundle = Bundle()  //key,value storage
        bundle.putSerializable(KEY_SHOP_EDIT, shoppingToEdit)
        editDialog.arguments = bundle

        editDialog.show(supportFragmentManager, "TAG_ITEM_EDIT")
    }

    fun updateTotalPrice(price:Float){
        binding.totalPrice.text = getString(R.string.total_price, price.toString())
    }

    override fun shoppingCreated(newShopping: ShoppingItem) {
        thread {
            AppDatabase.getInstance(this).ItemDao().addShopping(newShopping)
        }

    }

    private fun shoppingDeleted(){
        thread {
            AppDatabase.getInstance(this).ItemDao().deleteAllShopping()
        }
        updateTotalPrice(0F)
    }

    override fun shoppingUpdated(editedShopping: ShoppingItem) {
        // update in the Database
        thread {
            AppDatabase.getInstance(this).ItemDao().updateShopping(editedShopping)
        }
    }

    private fun saveThatAppWasStarted() {
        val sharedPref = getSharedPreferences("PREF_DEFAULT", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("KEY_STARTED", true)
        editor.commit()
    }

    private fun wasAlreadyStarted() : Boolean {
        val sharedPref = getSharedPreferences("PREF_DEFAULT", MODE_PRIVATE)
        val wasStarted = sharedPref.getBoolean("KEY_STARTED", false)
        return wasStarted
    }

    private fun initShoppingRecyclerView() {
        adapter = ShoppingRecyclerAdapter(this)
        binding.recyclerShopping.adapter = adapter

        var liveDataShoppings = AppDatabase.getInstance(this).ItemDao().getAllShopping()
        liveDataShoppings.observe(this, Observer { items ->
            adapter.submitList(items)
        })

        val touchCallbackList = ShoppingRecyclerTouchCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(touchCallbackList)
        itemTouchHelper.attachToRecyclerView(binding.recyclerShopping)
    }
}