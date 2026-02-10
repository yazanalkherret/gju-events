package com.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseAuthViewModel : ViewModel() {
    protected val mAuth = FirebaseAuth.getInstance()
    protected val db = FirebaseFirestore.getInstance()
    protected val messageState = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = messageState

    fun clearMessage() {
        messageState.value = null
    }
}