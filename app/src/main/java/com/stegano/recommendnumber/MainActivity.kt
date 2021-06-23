package com.stegano.recommendnumber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.stegano.recommendnumber.db.LottoDB
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private var lottoDB: LottoDB? = null
    val lottoNumberRange = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        lottoDB = LottoDB.getInstance(this)

        val r = Runnable {
            Log.i(TAG, "onCreate: DB Data : ${lottoDB?.lottoEntityDAO()?.getAll()}")
        }

        Log.e(TAG, "onCreate: lottoNumberRange : $lottoNumberRange")

        button_all_click.setOnClickListener {
            if (targetToggleButton1.isChecked && targetToggleButton2.isChecked &&
                targetToggleButton3.isChecked && targetToggleButton4.isChecked &&
                targetToggleButton5.isChecked && targetToggleButton6.isChecked &&
                targetToggleButtonB.isChecked
            ) {
                targetToggleButton1.isChecked = false
                targetToggleButton2.isChecked = false
                targetToggleButton3.isChecked = false
                targetToggleButton4.isChecked = false
                targetToggleButton5.isChecked = false
                targetToggleButton6.isChecked = false
                targetToggleButtonB.isChecked = false
            } else {
                targetToggleButton1.isChecked = true
                targetToggleButton2.isChecked = true
                targetToggleButton3.isChecked = true
                targetToggleButton4.isChecked = true
                targetToggleButton5.isChecked = true
                targetToggleButton6.isChecked = true
                targetToggleButtonB.isChecked = true
            }
        }

        applyButton.setOnClickListener {
            textView3.text = "123456789"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LottoDB.destroyInstance()
    }
}