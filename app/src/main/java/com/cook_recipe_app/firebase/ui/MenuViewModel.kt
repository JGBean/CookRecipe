package com.cook_recipe_app.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel() {
    private val _selectedMenu = MutableLiveData<String>()
    val selectedMenu: LiveData<String> get() = _selectedMenu

    fun setSelectedMenu(menuName: String) {
        _selectedMenu.value = menuName
    }
}
