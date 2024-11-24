package com.cook_recipe_app.firebase

data class MenuItem(
    val type: Int,
    val name: String,
    val id: String? = null  // id를 추가
) {
    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }
}

