package com.example.shoppinglist.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.shoppinglist.R
import com.example.shoppinglist.ScrollingActivity
import com.example.shoppinglist.ScrollingActivity.Companion.totalPrice
import com.example.shoppinglist.data.ShoppingItem
import com.example.shoppinglist.databinding.ShoppingDialogBinding

class ShoppingDialog : DialogFragment() {

        interface ShoppingHandler {
            fun shoppingCreated(newShopping: ShoppingItem)
            fun shoppingUpdated(editedShopping: ShoppingItem)
        }

        lateinit var shoppingHandler: ShoppingHandler
        lateinit var shoppingDialogBinding: ShoppingDialogBinding


        override fun onAttach(context: Context) {
            super.onAttach(context)

            if (context is ShoppingHandler){
                shoppingHandler = context
            } else {
                throw RuntimeException(
                    getString(R.string.runtimeError))
            }
        }

        var isEditMode = false


        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val dialogBuilder = AlertDialog.Builder(requireContext())
            shoppingDialogBinding = ShoppingDialogBinding.inflate(layoutInflater)
            dialogBuilder.setView(shoppingDialogBinding.root)

            val categoryAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.category_names_array,
                android.R.layout.simple_spinner_item
            )
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            shoppingDialogBinding.spinnerCategory.adapter = categoryAdapter

            if (arguments != null && requireArguments().containsKey(
                    ScrollingActivity.KEY_SHOP_EDIT)) {
                isEditMode = true
                dialogBuilder.setTitle(getString(R.string.edit_shopping))
            } else {
                isEditMode = false
                dialogBuilder.setTitle(getString(R.string.new_shopping))
            }


            if (isEditMode) {
                val shoppingToEdit =
                    requireArguments().getSerializable(
                        ScrollingActivity.KEY_SHOP_EDIT) as ShoppingItem

                shoppingDialogBinding.etShopName.setText(shoppingToEdit.name)
                shoppingDialogBinding.etShopPrice.setText(shoppingToEdit.price.toString())
                shoppingDialogBinding.cbShopDone.isChecked = shoppingToEdit.found
                shoppingDialogBinding.etDescription.setText(shoppingToEdit.description)
                shoppingDialogBinding.spinnerCategory.setSelection(shoppingToEdit.categoryId)
            }

            dialogBuilder.setPositiveButton(getString(R.string.create)) {
                    dialog, which ->

                val newShopping = ShoppingItem(null, shoppingDialogBinding.etShopName.text.toString(),
                    shoppingDialogBinding.etShopPrice.text.toString().toFloat(),
                    shoppingDialogBinding.spinnerCategory.selectedItemPosition,
                    shoppingDialogBinding.cbShopDone.isChecked,
                    shoppingDialogBinding.etDescription.text.toString())
                shoppingHandler.shoppingCreated(newShopping)
            }

            dialogBuilder.setNegativeButton(getString(R.string.cancelbtn)) {
                    dialog, which ->
            }

            return dialogBuilder.create()
        }

        override fun onResume() {
            super.onResume()

            val dialog = dialog as AlertDialog
            val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)

            positiveButton.setOnClickListener {
                if (shoppingDialogBinding.etShopName.text.isNotEmpty()) {
                    if (shoppingDialogBinding.etShopPrice.text.isNotEmpty()) {
                        if (isEditMode) {
                            handleShoppingEdit()
                        } else {
                            handleShoppingCreate()
                            totalPrice += shoppingDialogBinding.etShopPrice.text.toString().toFloat()
                            (context as ScrollingActivity).updateTotalPrice(totalPrice)
                        }

                        dialog.dismiss()
                    } else {
                        shoppingDialogBinding.etShopPrice.error = getString(R.string.empty_err)
                    }
                } else {
                    shoppingDialogBinding.etShopName.error = getString(R.string.empty_err)
                }
            }
        }

        private fun handleShoppingCreate() {
            shoppingHandler.shoppingCreated(ShoppingItem(
                        null, shoppingDialogBinding.etShopName.text.toString(),
                shoppingDialogBinding.etShopPrice.text.toString().toFloat(),
                shoppingDialogBinding.spinnerCategory.selectedItemPosition,
                false,
                shoppingDialogBinding.etDescription.text.toString()))
        }

        private fun handleShoppingEdit() {
            val shoppingToEdit = (arguments?.getSerializable(
                ScrollingActivity.KEY_SHOP_EDIT) as ShoppingItem).copy(
                name =  shoppingDialogBinding.etShopName.text.toString(),
                price =  shoppingDialogBinding.etShopPrice.text.toString().toFloat(),
                categoryId = shoppingDialogBinding.spinnerCategory.selectedItemPosition,
                found = shoppingDialogBinding.cbShopDone.isChecked,
                description = shoppingDialogBinding.etDescription.text.toString()
            )
            shoppingHandler.shoppingUpdated(shoppingToEdit)
        }


    }