package com.example.admindashboard

import com.google.firebase.database.FirebaseDatabase

object FirebaseUtils {
    var firebaseDatabase = FirebaseDatabase.getInstance()


    var bookRef = firebaseDatabase.getReference("books")
    var pyqRef = firebaseDatabase.getReference("pyq")
}