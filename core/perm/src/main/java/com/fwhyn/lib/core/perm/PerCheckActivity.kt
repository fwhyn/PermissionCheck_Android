package com.fwhyn.lib.core.perm

import android.app.Activity
import android.os.Bundle
import android.widget.Toast

class PerCheckActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(this, "Test permission", Toast.LENGTH_SHORT).show()

    }
}