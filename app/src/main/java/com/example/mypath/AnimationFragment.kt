package com.example.mypath

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.Fragment


class AnimationFragment : Fragment() {
    private lateinit var mSceneView: View
    private lateinit var mSunView: View
    private lateinit var mSkyView: View
    private lateinit var mGround: View
    private lateinit var mPath: View
    private lateinit var mBike: View
    private lateinit var mTitle: View

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
            mGround = view.findViewById(R.id.ground)
            mPath = view.findViewById(R.id.night_path)
            mBike = view.findViewById(R.id.bike)
            mTitle = view.findViewById(R.id.title)
        }

        return view
    }

    @SuppressLint("Recycle")
    fun startAnimation(){
        val sunYStart = mSunView.top
        val sunYEnd = mSkyView.height

        val bikeYStart = mGround.bottom
        val bikeYEnd = mSkyView.top

        val mBlueSkyColor = resources.getColor(R.color.blue_sky)
        val mSunsetSkyColor = resources.getColor(R.color.sunset_sky)
        val mNightSkyColor = resources.getColor(R.color.night_sky)
        val mGroundColor = resources.getColor(R.color.ground)
        val mNightGroundColor = resources.getColor(R.color.night_ground)

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

        val nightGroundAnimator = ObjectAnimator
            .ofInt(mGround, "backgroundColor", mGroundColor, mNightGroundColor)
            .setDuration(1500)
        nightGroundAnimator.setEvaluator(ArgbEvaluator())

        val nightPathAnimator = ObjectAnimator
            .ofFloat(mPath, "alpha", 0f, 1f)
            .setDuration(1500)
        nightGroundAnimator.setEvaluator(ArgbEvaluator())

        val bikeAnimator = ObjectAnimator
            .ofFloat(mBike, "y", bikeYStart.toFloat(), bikeYEnd.toFloat())
            .setDuration(4000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val titleAnimator = ObjectAnimator
            .ofFloat(mTitle, "alpha", 0f, 1f)
            .setDuration(700)
        nightGroundAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator).with(sunsetSkyAnimator).with(bikeAnimator).before(nightSkyAnimator)
        animatorSet.play(nightSkyAnimator).with(nightGroundAnimator).with(nightPathAnimator)
        animatorSet.play(titleAnimator).after(nightSkyAnimator)

        animatorSet.start()
    }
}