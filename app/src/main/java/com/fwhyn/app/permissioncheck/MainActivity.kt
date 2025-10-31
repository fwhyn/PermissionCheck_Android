package com.fwhyn.app.permissioncheck

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.fwhyn.app.permissioncheck.ui.theme.PermissionCheckTheme
import com.fwhyn.lib.core.perm.PerCheckActivity

class MainActivity : ComponentActivity() {

    // 1. Register for the activity result to request permissions
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            // This lambda is called with the results of the permission request
            val fineLocationGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            Log.d("MainActivity:PermissionResult", "Fine Location Granted: $fineLocationGranted")
            Log.d("MainActivity:PermissionResult", "Coarse Location Granted: $coarseLocationGranted")

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2. Check current permission status and decide whether to launch the request
        checkAndRequestLocationPermissions()

        enableEdgeToEdge()
        setContent {
            PermissionCheckTheme {
                PermissionCheckApp()
            }
        }
    }

    private fun checkAndRequestLocationPermissions() {
        val fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocationPermission = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val fineLocationStatus = ContextCompat.checkSelfPermission(this, fineLocationPermission)
        val coarseLocationStatus = ContextCompat.checkSelfPermission(this, coarseLocationPermission)
        val isFineLocationGranted = fineLocationStatus == PackageManager.PERMISSION_GRANTED
        val isCoarseLocationGranted = coarseLocationStatus == PackageManager.PERMISSION_GRANTED

        when {
            // 3. Check if permissions are already granted
            isFineLocationGranted && isCoarseLocationGranted -> {
                // Both permissions are already granted.
                // You can proceed with your app's logic.

                Log.d("MainActivity:CheckAndRequestLocationPermissions", "Permissions already granted")
            }

            // 4. (Optional but recommended) Explain why you need the permission
            shouldShowRequestPermissionRationale(fineLocationPermission) -> {
                // Explain to the user why this permission is needed.
                // This is good practice before showing the system permission dialog again.
                // For example, show a dialog explaining the feature.
                // After the user acknowledges, you can launch the request.

                Log.d("MainActivity:CheckAndRequestLocationPermissions", "Showing permission rationale dialog")
                requestPermissionLauncher.launch(arrayOf(fineLocationPermission, coarseLocationPermission))
            }

            // 5. Request the permissions
            else -> {
                // Request the permissions using the launcher

                Log.d("MainActivity:CheckAndRequestLocationPermissions", "Requesting permissions")
                requestPermissionLauncher.launch(arrayOf(fineLocationPermission, coarseLocationPermission))
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun PermissionCheckApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        val context = LocalContext.current
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column {
                Greeting(
                    name = "Android",
                    modifier = Modifier.padding(innerPadding)
                )

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        context.startActivity(Intent(context, PerCheckActivity::class.java))
                    }
                ) {
                    Text(text = "Other Permission")
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    FAVORITES("Favorites", Icons.Default.Favorite),
    PROFILE("Profile", Icons.Default.AccountBox),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PermissionCheckTheme {
        Greeting("Android")
    }
}