package com.example.fragmenty

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.Fragment


class AnimationFragment : Fragment() {
    private lateinit var mSceneView: View
    private lateinit var mSunView: View
    private lateinit var mSkyView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_animation, container, false)

        if (view != null) {
            mSceneView = view
            mSunView = view.findViewById(R.id.sun)
            mSkyView = view.findViewById(R.id.sky)
        }

        return view
    }

    @SuppressLint("Recycle")
    fun startAnimation(){
        val sunYStart = mSunView.top
        val sunYEnd = mSkyView.height

        val mBlueSkyColor = resources.getColor(R.color.blue_sky)
        val mSunsetSkyColor = resources.getColor(R.color.sunset_sky)
        val mNightSkyColor = resources.getColor(R.color.night_sky)

        val heightAnimator = ObjectAnimator
            .ofFloat(mSunView, "y", sunYStart.toFloat(), sunYEnd.toFloat())
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator: ObjectAnimator = ObjectAnimator
            .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet
            .play(heightAnimator)
            .with(sunsetSkyAnimator)
            .before(nightSkyAnimator)
        animatorSet.start()
    }
}