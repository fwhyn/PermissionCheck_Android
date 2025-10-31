package com.fwhyn.lib.core.perm

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity

class PerCheckActivity : ComponentActivity() {

    private val locationPermissionLauncher = PermissionUtil.getLocationPermissionLauncher(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(this, "Test permission", Toast.LENGTH_SHORT).show()
        PermissionUtil.checkAndRequestLocationPermissions(this, locationPermissionLauncher)
    }
}