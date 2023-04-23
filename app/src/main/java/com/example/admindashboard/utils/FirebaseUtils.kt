package com.example.admindashboard.utils

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object FirebaseUtils {

    const val bookNodeName = "books"

    val firebaseDatabase = FirebaseDatabase.getInstance()
    
    var bookRef = firebaseDatabase.getReference(bookNodeName)
    var pyqRef = firebaseDatabase.getReference("pyq")

    val storageRef = FirebaseStorage.getInstance().reference
    val notificationRef = firebaseDatabase.getReference("notification")
}