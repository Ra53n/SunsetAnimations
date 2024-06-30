package com.example.sunsetanimations

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var sceneView: View
    private lateinit var sunView: View
    private lateinit var skyView: View

    private var isNight = false

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }

    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }

    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById(R.id.scene)
        sunView = findViewById(R.id.sun)
        skyView = findViewById(R.id.sky)

        sceneView.setOnClickListener {
            startAnimation()
            isNight = isNight.not()
        }
    }

    private fun startAnimation() {
        val sunYStart = sunView.top.toFloat()
        val sunYEnd = skyView.height.toFloat()
        val accelerateInterpolator = AccelerateInterpolator()
        val argbEvaluator = ArgbEvaluator()
        val backgroundColorPropertyName = "backgroundColor"

        val sunsetHeightAnimator = ObjectAnimator.ofFloat(sunView, View.Y, sunYStart, sunYEnd)
            .apply {
                duration = SUNSET_DURATION
                interpolator = accelerateInterpolator
            }

        val sunriseHeightAnimator = ObjectAnimator.ofFloat(sunView, View.Y, sunYEnd, sunYStart)
            .apply {
                duration = SUNSET_DURATION
                interpolator = accelerateInterpolator
            }

        val sunsetSkyAnimator =
            ObjectAnimator.ofInt(skyView, backgroundColorPropertyName, blueSkyColor, sunsetSkyColor)
                .apply {
                    duration = SUNSET_DURATION
                    setEvaluator(argbEvaluator)
                }


        val sunriseSkyAnimator =
            ObjectAnimator.ofInt(skyView, backgroundColorPropertyName, sunsetSkyColor, blueSkyColor)
                .apply {
                    duration = SUNSET_DURATION
                    setEvaluator(argbEvaluator)
                }


        val nightSkyAnimator =
            ObjectAnimator.ofInt(skyView, backgroundColorPropertyName, sunsetSkyColor, nightSkyColor)
                .apply {
                    duration = NIGHT_DURATION
                    setEvaluator(argbEvaluator)
                }

        val animatorSet = AnimatorSet()
        if (isNight) {
            animatorSet.playTogether(sunriseHeightAnimator, sunriseSkyAnimator)
        } else {
            animatorSet.play(sunsetHeightAnimator).with(sunsetSkyAnimator).before(nightSkyAnimator)
        }

        animatorSet.start()
    }

    companion object {
        private const val SUNSET_DURATION = 3000L
        private const val NIGHT_DURATION = 1000L
    }
}