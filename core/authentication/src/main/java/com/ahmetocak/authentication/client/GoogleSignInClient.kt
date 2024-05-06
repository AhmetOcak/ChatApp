package com.ahmetocak.authentication.client

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

private const val WEB_CLIENT_ID =
    "77871938852-l88vnrd9nat6j73voetko585146kcojb.apps.googleusercontent.com"

class GoogleSignInClient @Inject constructor(
    private val context: Context,
    private val oneTapClient: SignInClient
) {

    fun startSignInIntent(): Task<BeginSignInResult> = oneTapClient.beginSignIn(signInRequest())

    fun signInWithIntent(intent: Intent): Task<AuthResult> {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val idToken = credential.googleIdToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(idToken, null)

        return Firebase.auth.signInWithCredential(googleAuthCredential)
    }

    fun reAuthenticate(): Task<GoogleSignInAccount> {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .build()

        val googleClient = GoogleSignIn.getClient(context, googleSignInOptions)

        return googleClient.silentSignIn()
    }

    private fun signInRequest() = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()
}