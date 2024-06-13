package com.ahmetocak.domain.usecase.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class GetSignInProviderUseCase @Inject constructor() {

    operator fun invoke(): SignInProvider {
        return when (FirebaseAuth.getInstance().getAccessToken(false).result.signInProvider) {
            GoogleAuthProvider.PROVIDER_ID -> {
               SignInProvider.GOOGLE
            }

            else -> SignInProvider.EMAIL_PASSWORD
        }
    }
}

enum class SignInProvider {
    EMAIL_PASSWORD,
    GOOGLE
}