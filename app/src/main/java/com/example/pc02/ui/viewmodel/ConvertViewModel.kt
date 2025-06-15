package com.example.pc02.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pc02.model.Conversion
import com.example.pc02.model.Rate
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConvertViewModel : ViewModel() {
    private val db   = Firebase.firestore
    private val auth = Firebase.auth

    private val _rates = MutableStateFlow<Map<String, Rate>>(emptyMap())
    val rates: StateFlow<Map<String, Rate>> = _rates

    private val _history = MutableStateFlow<List<Conversion>>(emptyList())
    val history: StateFlow<List<Conversion>> = _history

    init {
        db.collection("rates")
            .get()
            .addOnSuccessListener { snap ->
                val map = snap.documents.associate { d ->
                    d.id to Rate(d.getDouble("valueToUSD") ?: 1.0)
                }
                _rates.value = map
            }
    }

    fun convert(amount: Double, from: String, to: String, onResult: (Double)->Unit) {
        val user = auth.currentUser ?: return
        val rFrom = _rates.value[from]?.valueToUSD ?: 1.0
        val rTo   = _rates.value[to]?.valueToUSD   ?: 1.0
        val res   = amount / rFrom * rTo
        onResult(res)
        val conv = Conversion(
            uid = user.uid,
            timestamp = Timestamp.now(),
            amount = amount,
            from = from,
            to = to,
            result = res
        )
        db.collection("conversions").add(conv)
    }

    fun loadHistory() {
        val user = auth.currentUser ?: return
        db.collection("conversions")
            .whereEqualTo("uid", user.uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snap ->
                _history.value = snap.toObjects(Conversion::class.java)
            }
    }
}