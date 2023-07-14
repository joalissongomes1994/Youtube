package joalissongomes.dev.youtube

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlin.math.abs

class TouchMotionLayout(context: Context, attributeSet: AttributeSet) :
    MotionLayout(context, attributeSet) {

    private val iconArrowDown: ImageView by lazy {
        findViewById<ImageView>(R.id.hide_player)
    }

    private val imgBase: ImageView by lazy {
        findViewById<ImageView>(R.id.video_player)
    }

    private val playButton: ImageView by lazy {
        findViewById<ImageView>(R.id.play_button)
    }

    private val seekBar: SeekBar by lazy {
        findViewById<SeekBar>(R.id.seek_bar)
    }

    private var startX: Float? = null
    private var startY: Float? = null
    private var isPaused = false

    private lateinit var animFadeIn: AnimatorSet
    private lateinit var animFadeOut: AnimatorSet

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        val isInTarget: Boolean = touchEventInsideTargetView(imgBase, event!!)
        val isInProgress: Boolean = (progress > 0.0f && progress < 1.0f)

        return if (isInTarget || isInProgress) {
            super.onInterceptTouchEvent(event)
        } else {
            false
        }
    }

    private fun touchEventInsideTargetView(view: View, event: MotionEvent): Boolean {
        if (event.x > view.left && event.x < view.right) {
            if (event.y > view.top && event.y < view.bottom) {
                return true
            }
        }
        return false
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }

            MotionEvent.ACTION_UP -> {
                val endX = event.x
                val endY = event.y

                if (isAClick(startX!!, endX, startY!!, endY)) {
                    if (touchEventInsideTargetView(imgBase, event)) {
                        if (doClick(imgBase)) {
                            return true
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun isAClick(startX: Float, endX: Float, startY: Float, endY: Float): Boolean {
        val differenceX = abs(startX - endX)
        val differenceY = abs(startY - endY)

        return !(differenceX > 200 || differenceY > 200)
    }

    private fun doClick(view: View): Boolean {
        var isClickHandled = false

        if (progress < 0.05f) {
            isClickHandled = true

            when (view) {
                imgBase -> {
                    if (isPaused) {

                    } else {
                        animateFade {
                            animFadeOut.startDelay = 1000
                            animFadeOut.start()
                        }

                    }
                }
            }
        }

        return isClickHandled
    }

    private fun animateFade(onAnimationEndOn: () -> Unit) {
        animFadeIn = AnimatorSet()
        animFadeOut = AnimatorSet()

        //animate fadeIn
        fade(
            animFadeIn, arrayOf(
                findViewById<ImageView>(R.id.play_button),
                findViewById<ImageView>(R.id.hide_player),
                findViewById<ImageView>(R.id.next_button),
                findViewById<ImageView>(R.id.previous_button),
                findViewById<ImageView>(R.id.playlist_player),
                findViewById<ImageView>(R.id.full_player),
                findViewById<ImageView>(R.id.share_player),
                findViewById<ImageView>(R.id.more_player),
                findViewById<ImageView>(R.id.current_time),
                findViewById<ImageView>(R.id.duration_time),
            ),
            true
        )
        animFadeIn.play(
            ObjectAnimator.ofFloat(findViewById<ImageView>(R.id.view_frame), View.ALPHA, 0f, .5f)
        )
        val valueFadeIn = ValueAnimator.ofInt(0, 255)
            .apply {
                addUpdateListener {
                    findViewById<SeekBar>(R.id.seek_bar).thumb.mutate().alpha =
                        it.animatedValue as Int
                }
            }

        animFadeIn.play(valueFadeIn)

        //animate fadeOut
        fade(
            animFadeOut, arrayOf(
                findViewById<ImageView>(R.id.play_button),
                findViewById<ImageView>(R.id.hide_player),
                findViewById<ImageView>(R.id.next_button),
                findViewById<ImageView>(R.id.previous_button),
                findViewById<ImageView>(R.id.playlist_player),
                findViewById<ImageView>(R.id.full_player),
                findViewById<ImageView>(R.id.share_player),
                findViewById<ImageView>(R.id.more_player),
                findViewById<ImageView>(R.id.current_time),
                findViewById<ImageView>(R.id.duration_time),
            ),
            false
        )
        animFadeOut.play(
            ObjectAnimator.ofFloat(findViewById<ImageView>(R.id.view_frame), View.ALPHA, .5f, 0f)
        )
        val valueFadeOut = ValueAnimator.ofInt(255, 0)
            .apply {
                addUpdateListener {
                    findViewById<SeekBar>(R.id.seek_bar).thumb.mutate().alpha =
                        it.animatedValue as Int
                }
            }

        animFadeOut.play(valueFadeOut)

        animFadeIn.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                onAnimationEndOn.invoke()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}
        })

        animFadeIn.start()
    }

    private fun fade(animatorSet: AnimatorSet, view: Array<View>, toZero: Boolean) {
        view.forEach {
            animatorSet.play(
                ObjectAnimator.ofFloat(
                    it, View.ALPHA,
                    if (toZero) 0f else 1f,
                    if (toZero) 1f else 0f
                )
            )
        }
    }
}