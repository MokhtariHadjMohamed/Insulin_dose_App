package com.example.insulin_dose_app

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom
import androidx.core.view.setMargins


class InfoActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var next: CardView
    lateinit var before: CardView
    lateinit var info01: View
    lateinit var point01: CardView
    lateinit var info02: View
    lateinit var point02: CardView
    lateinit var info03: View
    lateinit var point03: CardView
    lateinit var info04: View
    lateinit var point04: CardView
    lateinit var startBtn: Button
    var key: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        next = findViewById(R.id.nextBtnMainActivity)
        before = findViewById(R.id.beforBtnMainActivity)

        info01 = findViewById(R.id.info01)
        info02 = findViewById(R.id.info02)
        info03 = findViewById(R.id.info03)
        info04 = findViewById(R.id.info04)

        point01 = findViewById(R.id.point01)
        point02 = findViewById(R.id.point02)
        point03 = findViewById(R.id.point03)
        point04 = findViewById(R.id.point04)

        startBtn = findViewById(R.id.startBtnInfo)

        startBtn.setOnClickListener(this)
        next.setOnClickListener(this)
        before.setOnClickListener(this)
    }

    private fun right_to_left_hide(view:View, id:Int){
        val anim: Animation = AnimationUtils.loadAnimation(this, id)
        anim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationRepeat(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.GONE
            }
        })
        view.startAnimation(anim)
    }
    private fun right_to_left_show(view:View, id:Int){
        val anim: Animation = AnimationUtils.loadAnimation(this, id)
        anim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {

            }
        })
        view.startAnimation(anim)
    }

    private fun pointsRemove(view:CardView){
        view.cardElevation = 0f
        view.setCardBackgroundColor(Color.WHITE)
        val p = view.layoutParams as ViewGroup.MarginLayoutParams
        p.bottomMargin = 0
        view.layoutParams = p
    }
    private fun pointsAdd(view: CardView){
        view.cardElevation = 5.0f
        view.setCardBackgroundColor(getResources().getColor(R.color.text01))
        val p = view.layoutParams as ViewGroup.MarginLayoutParams
        p.bottomMargin = 5
        view.layoutParams = p
    }
    override fun onClick(v: View?) {
        if (v == next) {
            key++
            when (key) {
                1 -> {
                    before.visibility = View.VISIBLE
                    right_to_left_hide(info01, R.anim.slide_out)
                    right_to_left_show(info02, R.anim.slide_in)
                    key = 1
                }

                2 -> {
                    before.visibility = View.VISIBLE
                    right_to_left_hide(info01, R.anim.slide_out)
                    right_to_left_show(info02, R.anim.slide_in)

                    pointsRemove(point01)
                    pointsAdd(point02)

                    key = 2
                }

                3 -> {
                    right_to_left_hide(info02, R.anim.slide_out)
                    right_to_left_show(info03, R.anim.slide_in)

                    pointsRemove(point02)
                    pointsAdd(point03)

                    key = 3
                }

                4 -> {
                    next.visibility = View.INVISIBLE
                    right_to_left_hide(info03, R.anim.slide_out)
                    right_to_left_show(info04, R.anim.slide_in)

                    pointsRemove(point03)
                    pointsAdd(point04)

                    key = 4
                }
            }
        } else if (v == before) {
            key--
            when (key) {
                1 -> {
                    before.visibility = View.INVISIBLE
                    right_to_left_show(info01, R.anim.slide_in_rev)
                    right_to_left_hide(info02, R.anim.slide_out_rev)

                    pointsAdd(point01)
                    pointsRemove(point02)

                    key = 1
                }
                2 -> {
                    right_to_left_show(info02, R.anim.slide_in_rev)
                    right_to_left_hide(info03, R.anim.slide_out_rev)

                    pointsAdd(point02)
                    pointsRemove(point03)

                    key = 2
                }
                3 -> {
                    next.visibility = View.VISIBLE
                    right_to_left_show(info03, R.anim.slide_in_rev)
                    right_to_left_hide(info04, R.anim.slide_out_rev)

                    pointsAdd(point03)
                    pointsRemove(point04)

                    key = 3
                }
                4 -> {
                    key = 4
                }
            }
        } else if (v == startBtn){
            startActivity(Intent(this, StartActivity::class.java))
        }
    }

}