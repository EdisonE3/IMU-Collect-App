package com.wireless.spyapp.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager {
    var permissions = arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    var mPermissionList: MutableList<String> = ArrayList()
    private val PERMISSION_REQUEST = 1

    fun checkPermission(context: Context, activity: Activity) {
        mPermissionList.clear()

        //判断哪些权限未授予
        for (i in permissions.indices) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i])
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) { //未授予的权限为空，表示都授予了
        } else { //请求权限方法
            val permissions = mPermissionList.toTypedArray() //将List转为数组
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST)
        }
    }
}