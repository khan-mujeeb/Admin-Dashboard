package com.example.admindashboard.data

data class Pdf(
    val id: String,
    val subject: String,
    val downloadUrl: String?,
    var senderName: String = "",
    var date: String = "",
    var time: String = "",
    var publication: String = "",
    var fileName: String = ""
)
