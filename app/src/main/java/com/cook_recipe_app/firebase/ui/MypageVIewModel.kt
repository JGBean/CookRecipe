package com.cook_recipe_app.firebase.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cook_recipe_app.firebase.MypageRepository

class MypageVIewModel: ViewModel() {

    private val _name = MutableLiveData<String>("username")
    val name: LiveData<String> get() = _name

    private val repository = MypageRepository()
        init{
            repository.observeName(_name)
        }

}