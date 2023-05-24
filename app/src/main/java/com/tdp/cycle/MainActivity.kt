package com.tdp.cycle

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import br.ufrn.imd.obd.commands.ObdCommandGroup
import br.ufrn.imd.obd.commands.engine.RPMCommand
import com.tdp.cycle.bases.CycleBaseActivity
import com.tdp.cycle.common.IProgressView
import com.tdp.cycle.common.gone
import com.tdp.cycle.common.logd
import com.tdp.cycle.common.show
import com.tdp.cycle.databinding.ActivityMainBinding
import com.tdp.cycle.features.routes.RoutesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import java.util.UUID


@AndroidEntryPoint
class MainActivity : CycleBaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate), IProgressView {

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainActivityNavigation) as? NavHostFragment)?.navController
    }

    val bluetoothAdapter: BluetoothAdapter? by lazy { ContextCompat.getSystemService(this, BluetoothManager::class.java)?.adapter }

    val obd2Devices = mutableSetOf<BluetoothDevice>()

    var obdSocket: BluetoothSocket? = null

    private var isLocationPermissionGranted = false

    inner class BluetoothDiscoveryReceiver : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.S)
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("onreceive", "onreceive")
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
                when (intent.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        // A new Bluetooth device has been discovered
                        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        // Filter the device to only show OBD-II devices
                        if (device?.name?.contains("OBD", true) == true
                                || device?.name?.contains("ELM", true) == true
                        ) {
                            obd2Devices.add(device)
                        }
                    }

                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        // Discovery has finished, show the list of OBD-II devices in a dialog
                        showObdList()
                    }
                }
            }
        }
    }

    private val enableBluetoothForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // User enabled Bluetooth, proceed to scan for devices
//            startDeviceDiscovery()
            bluetoothAdapter?.startDiscovery()
        } else {
            // User declined to enable Bluetooth
            AlertDialog.Builder(this).apply {
                setTitle("Bluetooth is off")
                setMessage("You need to enable Bluetooth to enjoy OBD benefits")
                show()
            }.show()
        }
    }

    // Show dialog with near obd devices
    private fun showObdList() {
        bluetoothAdapter?.bondedDevices?.filter { device ->
            device?.name?.contains("OBD", true) == true || device?.name?.contains("ELM", true) == true
        }?.let { obd2Devices.addAll(it) }

        val deviceNames = obd2Devices.map { device -> device.name }.toTypedArray()

        if (deviceNames.isNotEmpty()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose an OBD-II device")
            builder.setItems(deviceNames) { _, which ->
                // User clicked on an OBD-II device
                val device = obd2Devices.elementAt(which)
                // Connect to the device
                lifecycleScope.launch(Dispatchers.IO) {
                        obdSocket = createObdSocket(device)
                }
            }
            builder.show()
        } else {
            //no obd device found
            val builder = AlertDialog.Builder(this)
            builder.setTitle("No obd device found")
            builder.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    suspend fun createObdSocket(device: BluetoothDevice): BluetoothSocket? = withContext(Dispatchers.IO) {

        var uuid: UUID? = null
        device?.apply {
            if (uuids != null && uuids.isNotEmpty()) {
                val uuidss = uuids
                uuid = uuids[0].uuid
            }
        }

        bluetoothAdapter?.cancelDiscovery()

        val socket: BluetoothSocket? = try {
//            device.fetchUuidsWithSdp()
            device?.createRfcommSocketToServiceRecord(uuid)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to create socket", e)
            null
        }

        socket

//        socket?.let { sock ->
//            try {
//                sock.connect()
//            } catch (e: IOException) {
//                Log.e(TAG, "Failed to connect to device", e)
//                try {
//                    sock.close()
//                } catch (e: IOException) {
//                    Log.e(TAG, "Failed to close socket", e)
//                }
//                null
//            }
//            Log.d(TAG, sock.toString())
//
//            bluetoothSocket = sock
//            obdCommunication(sock)
//            sock
//        }
    }

    suspend fun connectObdSocket(bluetoothSocket: BluetoothSocket?): BluetoothSocket? = withContext(Dispatchers.IO) {
        obdSocket?.let { sock ->
            try {
                sock.connect()
            } catch (e: IOException) {
                Log.e(TAG, "Failed to connect to device", e)
                try {
                    sock.close()
                } catch (e: IOException) {
                    Log.e(TAG, "Failed to close socket", e)
                }
                null
            }
            Log.d(TAG, sock.toString())

//            obdSocket = sock
//            obdCommunication(sock)
            sock
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    suspend fun obdCommunication(bluetoothSocket: BluetoothSocket?) {
        withContext(Dispatchers.IO) {
            bluetoothSocket?.let {
                try {
                    val inputStream = it.inputStream
                    val outputStream = it.outputStream
                    val buffer = ByteArray(1024)
                    // Group many obd commands into a single command ()
                    val obdCommands = ObdCommandGroup()
                    obdCommands.add(RPMCommand())

                    // Run all commands at once
                    val response = obdCommands.run(inputStream, outputStream)
                    val response2 = obdCommands.commandPID
                    val response3 = obdCommands.result
                    val response4 = obdCommands.name

                    // Receive the response into buffer from the OBD-II device.
//                    val responseLength = inputStream.read(buffer)
//
//                    // Parse the response to extract the RPM value.
//                    val responseString = String(buffer, 0, responseLength)

                    Log.d("ressssssssponse", response.toString())
                    Log.d("ressssssssponse", response2.toString())
                    Log.d("ressssssssponse", response3.toString())
                    Log.d("ressssssssponse", response4.toString())
//                    Log.d("ressssssssponse", responseString)


                } catch (e: Exception) {
                    Log.e(RoutesFragment.TAG, "Could not connect to obd", e)
                    null
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handlePermissions()

//        if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
//            lifecycleScope.launch {
//                delay(1000)
//                checkBluetoothOn()
//            }
//        }

        initUi()

    }

    private fun checkBluetoothOn() {
        bluetoothAdapter?.let { btAdapter ->
            if(!btAdapter.isEnabled)  {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enableBluetoothForResult.launch(enableBtIntent)
            } else   {
                activateBluetoothReceiver()
                bluetoothAdapter?.startDiscovery()
            }

        } ?: run {
            // Bluetooth is already enabled, proceed to scan for devices
            activateBluetoothReceiver()
            bluetoothAdapter?.startDiscovery()
//            startDeviceDiscovery()
        }
    }

    private fun activateBluetoothReceiver(){
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(BluetoothDiscoveryReceiver(), filter)
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

    @RequiresApi(Build.VERSION_CODES.S)
    fun handlePermissions() {
        requestMultiplePermissions.launch(
            listOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ).toTypedArray()
        )
    }

    override fun handleProgress(isLoading: Boolean) {
        logd("isLoading = $isLoading")
        binding.mainActivityPB.handleProgress(isLoading)
    }

//    private val requestPermissionLauncher =
    private val requestMultiplePermissions =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            permissions.forEach { permisson ->
                val checkSelfPermission = (ContextCompat.checkSelfPermission(this, permisson.key) == PackageManager.PERMISSION_GRANTED)
                when {
                    checkSelfPermission -> {
                        // You can use the API that requires the permission.
                    }
                    shouldShowRequestPermissionRationale(permisson.key) -> {
                        // In an educational UI, explain to the user why your app requires this
                        // permission for a specific feature to behave as expected, and what
                        // features are disabled if it's declined. In this UI, include a
                        // "cancel" or "no thanks" button that lets the user continue
                        // using your app without granting the permission.
//                showInContextUI(...)
                    }
                    else -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                    }
                }
            }


            val bluetoothConnectGranted = permissions[Manifest.permission.BLUETOOTH_CONNECT] ?: false
            val bluetoothScanGranted = permissions[Manifest.permission.BLUETOOTH_SCAN] ?: false
            val accessFineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val accessCoarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (bluetoothConnectGranted && bluetoothScanGranted) {
                // Both BLUETOOTH_CONNECT and BLUETOOTH_SCAN permissions are granted
//                bluetoothAdapter?.let { adapter ->
//                    if (adapter.isDiscovering()) {
//                        adapter.cancelDiscovery()
//                    }
//                    adapter.startDiscovery()
//                }
                checkBluetoothOn()
            } else {
                // Bluetooth permission is denied by the user
                AlertDialog.Builder(this).apply {
                    setTitle("Bluetooth permission is required")
                    setMessage("You need to enable Bluetooth to enjoy OBD benefits")
                    show()
                }.show()
            }

            if(accessFineLocationGranted && accessCoarseLocationGranted && !isLocationPermissionGranted) {
                isLocationPermissionGranted = true
//                binding.mainActivityNavigation.getFragment<RoutesFragment>()
//                navController?.safeNavigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Location approval needed")
                    setCancelable(false)
                    setPositiveButton("Approve Location") { dialog, _ ->
                        handlePermissions()
                        dialog.dismiss()
                    }.show()
//                ActivityCompat.requestPermissions(this, permissions.keys.toList().toTypedArray(), 1)
                }
            }
        }


    companion object {
        private const val TAG = "MainActivityTAG"
    }

}