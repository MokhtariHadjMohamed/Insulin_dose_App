package com.example.insulin_dose_app

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.github.guilhe.views.CircularProgressView

class GraphActivity : ComponentActivity() {
    private var timer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.  graph)
        val export: TextView = findViewById(R.id.textView30)

        export.setOnClickListener {
            popup1()
        }
        val change: ImageView = findViewById(R.id.imageView41)

        change.setOnClickListener {
            val intent = Intent(this@GraphActivity, AboutActivity::class.java)
            startActivity(intent)
        }
        val cpv: CircularProgressView = findViewById(R.id.circularProgressView)
        //You can change the contents of the "progresslist" and "colorlist" below
        val progresslist: List<Float> = listOf(50f,20f,20f)
        val colorlist: List<Int> = listOf(R.color.color5,R.color.color1,R.color.color4)
        cpv.setProgress(progresslist,colorlist)

    }

    private fun popup1() {
        val pop1 = Dialog(this)
        pop1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        pop1.setCancelable(false)
        pop1.setContentView(R.layout.popup2_1)
        pop1.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        timer = object : CountDownTimer(3500, 500) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                pop1.dismiss()
                popup2()
            }

        }
        timer?.start()
        pop1.show()

    }

    private fun popup2() {
        val pop2 = Dialog(this)
        pop2.requestWindowFeature(Window.FEATURE_NO_TITLE)
        pop2.setCancelable(false)
        pop2.setContentView(R.layout.popup2_2)
        pop2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val ok: ImageView = pop2.findViewById(R.id.imageView22)
        ok.setOnClickListener {
            pop2.dismiss()
        }
        pop2.show()
    }
}