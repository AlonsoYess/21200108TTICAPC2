package com.example.pc02.model

import com.google.firebase.Timestamp

data class Conversion(
    val uid: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val amount: Double = 0.0,
    val from: String = "",
    val to: String = "",
    val result: Double = 0.0
)