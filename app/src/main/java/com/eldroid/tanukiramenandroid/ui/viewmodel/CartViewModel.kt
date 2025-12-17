package com.eldroid.tanukiramenandroid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eldroid.tanukiramenandroid.backend.model.CartItem
import com.eldroid.tanukiramenandroid.backend.model.MenuItem

class CartViewModel : ViewModel() {
    private val _items = MutableLiveData<List<CartItem>>(emptyList())
    val items: LiveData<List<CartItem>> = _items

    fun add(menuItem: MenuItem) {
        val currentItems = _items.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentItems.find { it.menuItem.name == menuItem.name }

        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            val itemIndex = currentItems.indexOf(existingItem)
            currentItems[itemIndex] = updatedItem
        } else {
            currentItems.add(CartItem(menuItem = menuItem, quantity = 1))
        }
        _items.value = currentItems
    }

    fun increment(cartItem: CartItem) {
        val currentItems = _items.value?.toMutableList() ?: return
        val existingItem = currentItems.find { it.menuItem.name == cartItem.menuItem.name } ?: return

        val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
        val itemIndex = currentItems.indexOf(existingItem)
        currentItems[itemIndex] = updatedItem
        _items.value = currentItems
    }

    fun decrement(cartItem: CartItem) {
        val currentItems = _items.value?.toMutableList() ?: return
        val existingItem = currentItems.find { it.menuItem.name == cartItem.menuItem.name } ?: return

        if (existingItem.quantity > 1) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity - 1)
            val itemIndex = currentItems.indexOf(existingItem)
            currentItems[itemIndex] = updatedItem
        } else {
            currentItems.remove(existingItem)
        }
        _items.value = currentItems
    }

    fun clear() {
        _items.value = emptyList()
    }
}