package com.cook_recipe_app.firebase

data class Post(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String = "" // Add this line to include the user ID
)
