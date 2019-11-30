package com.xiaofeidev.appreveal

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import com.totoro.basemodule.utils.StatusBarUtilKt
import com.xiaofeidev.appreveal.base.BaseActivity
import com.xiaofeidev.appreveal.ext.statusBarSteep
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        statusBarSteep()
        StatusBarUtilKt.setStatusBarColor(this, android.R.color.holo_blue_bright)
        btnReveal.setOnClickListener { view ->
            //系统提供的揭露动画需 5.0 及以上的 sdk 版本
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return@setOnClickListener
            }
            val startRadius:Float
            val endRadius:Float
            if (viewBg.visibility == View.VISIBLE){
                startRadius = viewBg.height.toFloat()
                endRadius = 0f
            }else{
                startRadius = 0f
                endRadius = viewBg.height.toFloat()
            }
            val location = IntArray(2)
            view.getLocationInWindow(location)
            val animReveal = ViewAnimationUtils.createCircularReveal(viewBg,
                    location[0] + view.width/2,
                    location[1] + view.height/2,
                    startRadius,
                    endRadius

            )
            animReveal.duration = 400
            animReveal.interpolator = LinearInterpolator()
            animReveal.addListener(onStart = {
                viewBg.visibility = View.VISIBLE
            },onEnd = {
                if (startRadius != 0f){
                    viewBg.visibility = View.INVISIBLE
                    btnReveal.setText(R.string.app_reveal)
                }else{
                    viewBg.visibility = View.VISIBLE
                    btnReveal.setText(R.string.app_reveal_r)
                }
            })
            animReveal.start()
        }
    }
}
