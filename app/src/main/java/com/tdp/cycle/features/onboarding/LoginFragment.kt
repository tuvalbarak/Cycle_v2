package com.tdp.cycle.features.onboarding

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tdp.cycle.R
import com.tdp.cycle.bases.CycleBaseFragment
import com.tdp.cycle.common.safeNavigate
import com.tdp.cycle.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : CycleBaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val REQ_ONE_TAP = 33333333
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate with Firebase.
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(requireActivity()) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithCredential:success")
                                        val user = auth.currentUser
                                        Log.d(TAG, "user = ${user.toString()}")
                                        //Navigating to home screen
                                        loginViewModel.auth(user)
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.d(TAG, "signInWithCredential:failure", task.exception)
                                        Log.d(TAG, "user is null")
//                                        updateUI(null)
                                    }
                                }
                            Log.d(TAG, "Got ID token.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    Log.e(TAG, e.toString())
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        initUi()
        initObservers()
    }

    private fun initUi() {
        binding?.apply {
            //Sign in with google
            loginFragmentSignInGoogle.setOnClickListener {
                presentOneTapSignInGoogle()
            }

            //Sign in with Email
            loginFragmentCTA.setOnClickListener {

            }
        }
    }

    private fun initObservers() {
        loginViewModel.navigationEvent.observe(viewLifecycleOwner) {event ->
            event?.getContentIfNotHandled()?.let { navigation ->
                when(navigation) {
                    LoginViewModel.NavigationEvent.GO_TO_HOME -> {
                        findNavController().safeNavigate(LoginFragmentDirections.actionLoginFragmentToRoutesFragment())
                    }
                }
            }
        }

        loginViewModel.progressData.observe(viewLifecycleOwner) { isLoading ->
            handleProgress(isLoading)
        }
    }

    private fun presentOneTapSignInGoogle() {

        oneTapClient = Identity.getSignInClient(requireActivity())

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("638030478727-ogfmt4reno2hlus7h3oeqv7kthkrl775.apps.googleusercontent.com")
                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(requireActivity()) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, e.localizedMessage)
            }
    }

    companion object {
        private const val TAG = "LoginFragmentTAG"
    }

}