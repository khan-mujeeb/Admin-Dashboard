package com.example.admindashboard.data

data class Pdf(
    val id: String = "",
    val subject: String = "",
    val downloadUrl: String? = null,
    var senderName: String = "",
    var date: String = "",
    var time: String = "",
    var publication: String = "",
    var fileName: String = ""
) {
    constructor() : this("", "", null, "", "", "", "", "")
}

