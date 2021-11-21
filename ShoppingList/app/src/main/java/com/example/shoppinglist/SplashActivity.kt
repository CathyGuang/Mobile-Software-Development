package com.example.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageView = findViewById<ImageView>(R.id.imageAnimation)
        val animation = AnimationUtils.loadAnimation(this,
            R.anim.anim)

        animation.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                val intentDetails = Intent()
                intentDetails.setClass(this@SplashActivity,
                    ScrollingActivity::class.java
                )

                startActivity(intentDetails)

                finish()
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }
        })

        imageView.startAnimation(animation)
    }
}