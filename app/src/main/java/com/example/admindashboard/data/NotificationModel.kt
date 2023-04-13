package com.example.admindashboard.data

data class NotificationModel(
    var id: String = "",
    var notification: String = "",
    var senderName: String = "",
    var date: String = "",
    var time: String = "",
    val title: String = "",
    val img: String
)
