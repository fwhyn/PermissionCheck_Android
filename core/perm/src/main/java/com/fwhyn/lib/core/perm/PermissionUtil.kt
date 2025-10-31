package com.fwhyn.lib.core.perm

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

object PermissionUtil {

    // 1. Register for the activity result to request permissions
    fun getLocationPermissionLauncher(activity: ComponentActivity): ActivityResultLauncher<Array<String>> =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            // This lambda is called with the results of the permission request
            val fineLocationGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            Log.d("PermissionUtil:PermissionResult", "Fine Location Granted: $fineLocationGranted")
            Log.d("PermissionUtil:PermissionResult", "Coarse Location Granted: $coarseLocationGranted")

            if (fineLocationGranted) {
                // Precise location access granted.
                // You can now proceed with location-dependent functionality.
            } else if (coarseLocationGranted) {
                // Only approximate location access granted.
                // You can request a more precise location if needed.
            } else {
                // No location access granted.
                // Gracefully degrade the app's functionality or explain
                // to the user why the permission is needed.
            }
        }

    // 2. Check current permission status and decide whether to launch the request
    fun checkAndRequestLocationPermissions(
        activity: Activity,
        locationPermissionLauncher: ActivityResultLauncher<Array<String>>,
    ) {
        val fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocationPermission = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val fineLocationStatus = ContextCompat.checkSelfPermission(activity, fineLocationPermission)
        val coarseLocationStatus = ContextCompat.checkSelfPermission(activity, coarseLocationPermission)
        val isFineLocationGranted = fineLocationStatus == PackageManager.PERMISSION_GRANTED
        val isCoarseLocationGranted = coarseLocationStatus == PackageManager.PERMISSION_GRANTED

        when {
            // 3. Check if permissions are already granted
            isFineLocationGranted && isCoarseLocationGranted -> {
                // Both permissions are already granted.
                // You can proceed with your app's logic.

                Log.d("PermissionUtil:CheckAndRequestLocationPermissions", "Permissions already granted")
            }

            // 4. (Optional but recommended) Explain why you need the permission
            activity.shouldShowRequestPermissionRationale(fineLocationPermission) -> {
                // Explain to the user why this permission is needed.
                // This is good practice before showing the system permission dialog again.
                // For example, show a dialog explaining the feature.
                // After the user acknowledges, you can launch the request.

                Log.d("PermissionUtil:CheckAndRequestLocationPermissions", "Showing permission rationale dialog")
                locationPermissionLauncher.launch(arrayOf(fineLocationPermission, coarseLocationPermission))
            }

            // 5. Request the permissions
            else -> {
                // Request the permissions using the launcher

                Log.d("PermissionUtil:CheckAndRequestLocationPermissions", "Requesting permissions")
                locationPermissionLauncher.launch(arrayOf(fineLocationPermission, coarseLocationPermission))
            }
        }
    }
}