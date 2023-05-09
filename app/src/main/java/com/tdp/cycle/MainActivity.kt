package com.tdp.cycle

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tdp.cycle.bases.CycleBaseActivity
import com.tdp.cycle.common.IProgressView
import com.tdp.cycle.common.gone
import com.tdp.cycle.common.logd
import com.tdp.cycle.common.show
import com.tdp.cycle.databinding.ActivityMainBinding
import com.tdp.cycle.features.routes.RoutesFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : CycleBaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate), IProgressView{

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainActivityNavigation) as? NavHostFragment)?.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handlePermissions()
        initUi()
    }

    private fun initUi() {
        initNavController()
        binding.mainActivityBottomNavigationView.itemIconTintList = null

    }

    private fun initNavController() {
        binding.apply {
            navController?.let {
                mainActivityBottomNavigationView.setupWithNavController(it)
                mainActivityBottomNavigationView.setOnItemReselectedListener{
                    Log.d(TAG, "$it was reselected")
                }
            }

            navController?.enableOnBackPressed(false)

            navController?.addOnDestinationChangedListener { navController, destination, bundle ->
                when(destination.id) {
                    R.id.profileFragment,
                    R.id.routesFragment,
                    R.id.chatFragment -> mainActivityBottomNavigationView.show()
                    else -> mainActivityBottomNavigationView.gone()
                }
            }
        }
    }

    private fun handlePermissions() {
        checkPermissions(
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun checkPermissions(permissions: List<String>): Boolean {
        var areAllPermissionsGranted = true
        permissions.forEach { permisson ->
            val checkSelfPermission = (ContextCompat.checkSelfPermission(this, permisson) == PackageManager.PERMISSION_GRANTED)
            when {
                checkSelfPermission -> {
                    // You can use the API that requires the permission.
                }
                shouldShowRequestPermissionRationale(permisson) -> {
                    // In an educational UI, explain to the user why your app requires this
                    // permission for a specific feature to behave as expected, and what
                    // features are disabled if it's declined. In this UI, include a
                    // "cancel" or "no thanks" button that lets the user continue
                    // using your app without granting the permission.
//                showInContextUI(...)
                    areAllPermissionsGranted = false
                }
                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    areAllPermissionsGranted = false
                    requestPermissionLauncher.launch(permisson)
                }
            }
        }

        return areAllPermissionsGranted
    }

    override fun handleProgress(isLoading: Boolean) {
        logd("isLoading = $isLoading")
        binding.mainActivityPB.handleProgress(isLoading)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

    companion object {
        private const val TAG = "MainActivityTAG"
    }

}