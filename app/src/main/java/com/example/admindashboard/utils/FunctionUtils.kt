package com.example.admindashboard.utils

object FunctionUtils {
    fun semesterNameToNumber(semesterName: String): Int {
        return when (semesterName.toLowerCase()) {
            "first" -> 1
            "second" -> 2
            "third" -> 3
            "fourth" -> 4
            "fifth" -> 5
            "six" -> 6
            "seventh" -> 7
            "eight" -> 8
            else -> -1 // invalid semester name
        }
    }

}