package com.xiaofeidev.appreveal.base

import android.animation.Animator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener

//将 Activity 的揭露效果写在 Base 类中，需要展示效果时继承
abstract class BaseActivity : AppCompatActivity(){
    companion object {
        //手动往 intent 里传入上个界面的点击位置坐标
        val CLICK_X = "CLICK_X"
        val CLICK_Y = "CLICK_Y"
    }
    private var onGlobalLayout : ViewTreeObserver.OnGlobalLayoutListener? = null
    //揭露(进入)动画
    var mAnimReveal : Animator? = null
    //反揭露(退出)动画
    var mAnimRevealR : Animator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        circularReveal(intent)
    }

    //Activity 揭露(进入)动画，进入时使用
    private fun circularReveal(intent: Intent?){
        //系统提供的揭露动画需 5.0 及以上的 sdk 版本，当我们获取不到上个界面的点击区域时就不展示揭露动画，因为此时没有合适的锚点
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || (intent?.sourceBounds == null && intent?.hasExtra(CLICK_X)?.not()?:true)) return
        val rect = intent?.sourceBounds
        val v = window.decorView
        v.visibility = View.INVISIBLE

        @SuppressWarnings
        onGlobalLayout = object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {//此时既是开始揭露动画的最佳时机
                mAnimReveal?.removeAllListeners()
                mAnimReveal?.cancel()
                mAnimReveal = ViewAnimationUtils.createCircularReveal(v,
                        rect?.centerX()?:intent?.getIntExtra(CLICK_X, 0)?:0,
                        rect?.centerY()?:intent?.getIntExtra(CLICK_Y, 0)?:0,
                        0f,
                        v.height.toFloat()
                )
                mAnimReveal?.duration = 400
                mAnimReveal?.interpolator = LinearInterpolator()
                mAnimReveal?.addListener(onEnd = {
                    onGlobalLayout?.let {
                        //我们需要在揭露动画进行完后及时移除回调
                        v?.viewTreeObserver?.removeOnGlobalLayoutListener(it)
                    }
                })
                mAnimReveal?.start()
            }
        }
        v.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayout)
    }

    //Activtiy 反揭露(退出)动画，即退出时的过渡动画，这么起名可能不恰当，其实还是同样的动画，只不过揭露的起始和终结半径跟上面相比反过来了
    private fun circularRevealReverse(intent: Intent?){
        //系统提供的揭露动画需 5.0 及以上的 sdk 版本，当我们获取不到上个界面的点击区域时就不展示揭露动画，因为此时没有合适的锚点
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || (intent?.sourceBounds == null && intent?.hasExtra(CLICK_X)?.not()?:true)) {
            super.onBackPressed()
            return
        }
        val rect = intent?.sourceBounds
        val v = window.decorView
        mAnimRevealR?.removeAllListeners()
        mAnimRevealR?.cancel()
        mAnimRevealR = ViewAnimationUtils.createCircularReveal(v,
                rect?.centerX()?:intent?.getIntExtra(CLICK_X, 0)?:0,
                rect?.centerY()?:intent?.getIntExtra(CLICK_Y, 0)?:0,
                v.height.toFloat(),
                0f

        )
        mAnimRevealR?.duration = 400
        mAnimRevealR?.interpolator = LinearInterpolator()
        mAnimRevealR?.addListener(onEnd = {
            v.visibility = View.GONE
            super.onBackPressed()
        })
        mAnimRevealR?.start()
    }

    //打开可有退出的揭露动画，不过只对栈底的 Activity 有效
    override fun onBackPressed() {
        circularRevealReverse(intent)
    }

    override fun onDestroy() {
        //及时释放资源以保证代码健壮性
        mAnimReveal?.removeAllListeners() 
        mAnimReveal?.cancel()
        mAnimRevealR?.removeAllListeners()
        mAnimRevealR?.cancel()
        //及时释放资源以保证代码健壮性
        onGlobalLayout?.let {

            window.decorView.viewTreeObserver?.removeOnGlobalLayoutListener(it)
        }
        super.onDestroy()
    }

    //这个方法很重要，如果我们应用的启动图标在桌面上的位置有变化，可在此收到新的位置信息，然而实践所知作用十分有限
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        circularReveal(intent)
        //更新intent
        this.intent = intent
    }

}

