package com.furkan.itenproje.model

data class Article(
    var title: String,
    var info: String,
    var image: Int,
    var profile: Int,
    var date: String,
    var reason: String,
    var isFavorite: Boolean,
    val link: String,
    val farm:Int
)
