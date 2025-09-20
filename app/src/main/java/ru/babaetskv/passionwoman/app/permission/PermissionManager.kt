package ru.babaetskv.passionwoman.app.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

class PermissionManager (
    private val activity: Activity
) {

    fun getStatus(permission: Permission): PermissionStatus =
        when {
            ContextCompat.checkSelfPermission(activity, permission.manifestName) == PackageManager.PERMISSION_GRANTED -> {
                PermissionStatus.GRANTED
            }
            shouldShowRequestPermissionRationale(activity, permission.manifestName) -> {
                PermissionStatus.SHOW_RATIONALE
            }
            else -> PermissionStatus.REQUEST_PERMISSION
        }
}
