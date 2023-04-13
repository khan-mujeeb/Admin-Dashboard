package com.example.admindashboard.data

data class PYQ(
    val id: String,
    val downloadUrl: String?,
    var senderName: String = "",
    var date: String = "",
    var time: String = "",
    var year: String = "",
    var exam: String = "",
    var subject: String
)
