package com.example.insulin_dose_app

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class SettingsActivity : ComponentActivity() {
    var timer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        val delAcc: TextView = findViewById(R.id.textView4)
        val change: ImageView = findViewById(R.id.imageView43)

        change.setOnClickListener {
            val intent = Intent(this@SettingsActivity, GraphActivity::class.java)
            startActivity(intent)
        }

        delAcc.setOnClickListener {
            popup()
        }
        findViewById<View>(R.id.imageView9).setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this@SettingsActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun popup() {
        val pop1 = Dialog(this)
        pop1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        pop1.setCancelable(false)
        pop1.setContentView(R.layout.popup1_1)
        pop1.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val cancel = pop1.findViewById<ImageView>(R.id.cancel)
        val yes = pop1.findViewById<TextView>(R.id.yes)

        val no: TextView = pop1.findViewById<TextView>(R.id.no)
        yes.setOnClickListener {
            Toast.makeText(this, "Yes clicked", Toast.LENGTH_LONG).show()
            Thread.sleep(1000)
            pop1.dismiss()
            popup2()

        }
        no.setOnClickListener {
            pop1.dismiss()
        }
        cancel.setOnClickListener {
            pop1.dismiss()
        }
        pop1.show()
    }

    private fun popup2() {
        val pop2 = Dialog(this)
        pop2.requestWindowFeature(Window.FEATURE_NO_TITLE)
        pop2.setCancelable(false)
        pop2.setContentView(R.layout.popup1_2)
        pop2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val cancel = pop2.findViewById<ImageView>(R.id.cancel)
        val yes = pop2.findViewById<TextView>(R.id.yes)
        timer = object : CountDownTimer(10000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                yes.text = "نعم( ${millisUntilFinished / 1000} )"
            }

            override fun onFinish() {
                yes.text = "نعم"
                yes.isEnabled = true
            }
        }

        yes.isEnabled = false
        timer?.start()

        val no: TextView = pop2.findViewById<TextView>(R.id.no)
        yes.setOnClickListener {
            Toast.makeText(this, "Yes clicked", Toast.LENGTH_LONG).show()
            Thread.sleep(1000)
            pop2.dismiss()

        }
        no.setOnClickListener {
            pop2.dismiss()
        }
        cancel.setOnClickListener {
            pop2.dismiss()
        }
        pop2.show()
    }


}

