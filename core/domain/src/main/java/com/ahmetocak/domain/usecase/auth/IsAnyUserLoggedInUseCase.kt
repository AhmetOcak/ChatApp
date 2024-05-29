package com.ahmetocak.domain.usecase.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.auth

object IsAnyUserLoggedInUseCase {

    operator fun invoke(): Boolean = Firebase.auth.currentUser != null
}