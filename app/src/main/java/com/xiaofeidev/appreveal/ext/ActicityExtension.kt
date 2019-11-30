package com.xiaofeidev.appreveal.ext

import android.app.Activity
import android.graphics.Color
import android.view.View

//状态栏沉浸
fun Activity.statusBarSteep(){
    val version = android.os.Build.VERSION.SDK_INT
    val decorView = window.decorView
    @SuppressWarnings // 忽略警告
    if (version >= 21) {
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.statusBarColor = Color.TRANSPARENT
    }
    ////////////////////////
    /*// 设置状态栏透明
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    val rootView = (findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
    rootView.fitsSystemWindows = true
    rootView.clipToPadding = true*/
}

//Activity 的扩展属性，状态栏高度
val Activity.statusBarHeight:Int
    get() {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }