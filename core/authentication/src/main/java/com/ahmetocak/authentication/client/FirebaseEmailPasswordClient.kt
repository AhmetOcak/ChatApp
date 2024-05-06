package com.ahmetocak.authentication.client

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseEmailPasswordClient @Inject constructor() {

    private val auth = FirebaseAuth.getInstance()

    fun signUp(email: String, password: String): Task<AuthResult> =
        auth.createUserWithEmailAndPassword(email, password)

    fun signOut() = auth.signOut()

    fun login(email: String, password: String): Task<AuthResult> =
        auth.signInWithEmailAndPassword(email, password)

    fun sendResetPasswordEmail(email: String): Task<Void> =
        auth.sendPasswordResetEmail(email)

    fun reAuthenticate(email: String, password: String): Task<Void>? {
        return auth.currentUser?.reauthenticate(
            EmailAuthProvider.getCredential(email, password)
        )
    }

    fun updateUsername(name: String): Task<Void>? {
        val user = Firebase.auth.currentUser

        return user?.updateProfile(
            userProfileChangeRequest {
                displayName = name
            }
        )
    }

    fun updateUserProfileImage(imageUri: Uri): Task<Void>? {
        val user = Firebase.auth.currentUser

        return user?.updateProfile(
            userProfileChangeRequest {
                photoUri = imageUri
            }
        )
    }

    fun deleteAccount(): Task<Void>? = auth.currentUser?.delete()
}