package com.xiaofeidev.appreveal

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.core.view.doOnAttach
import com.xiaofeidev.appreveal.base.BaseActivity
import com.xiaofeidev.appreveal.ext.statusBarSteep
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        statusBarSteep()
        btnReveal.doOnAttach {

        }
        //中间按钮的点击事件
        btnReveal.setOnClickListener { view ->
            //系统提供的揭露动画需 5.0 及以上的 sdk 版本
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return@setOnClickListener
            }
            //动画开始半径和结束半径，两者相对关系可用于控制是揭露还是反揭露，也即是从无到有还是从有到无
            val startRadius:Float
            val endRadius:Float
            if (viewBg.visibility == View.VISIBLE){
                //从有到无，即反揭露
                startRadius = viewBg.height.toFloat()
                endRadius = 0f
            }else{
                //从无到有，即揭露效果
                startRadius = 0f
                endRadius = viewBg.height.toFloat()
            }
            val location = IntArray(2)
            view.getLocationInWindow(location)
            //关键代码，构建一个揭露动画对象，注意圆形揭露动画的圆心以及开始半径和结束半径是如何计算出来的，应该很好理解，这里不做过多解释
            val animReveal = ViewAnimationUtils.createCircularReveal(viewBg,
                    location[0] + view.width/2,
                    location[1] + view.height/2,
                    startRadius,
                    endRadius

            )
            //构建好了揭露动画对象，开始设置动画的一些属性和相关监听
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
