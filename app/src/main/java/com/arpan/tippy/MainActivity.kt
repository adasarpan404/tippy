package com.arpan.tippy

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.lang.Double.parseDouble
import java.text.DecimalFormat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {

    private val df = DecimalFormat("0.00")
    private lateinit var tvBillAmount: EditText
    private lateinit var seekBar: SeekBar
    private lateinit var tipPercent: TextView
    private lateinit var totalTip: TextView
    private lateinit var totalAmount: TextView
    private lateinit var tvTipDescription : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvBillAmount = findViewById(R.id.tvBillAmount);
        seekBar = findViewById(R.id.tvTipPercent);
        tipPercent = findViewById(R.id.tvTipPercentLabel);
        totalTip = findViewById(R.id.tvTipAmount);
        totalAmount = findViewById(R.id.tvTotalAmount);
        tvTipDescription = findViewById(R.id.tvTipDescription)
        seekBar.progress = INITIAL_TIP_PERCENT
        tipPercent.text = "$INITIAL_TIP_PERCENT%"


        updateTipDescription(INITIAL_TIP_PERCENT)

        //seekbar value changes it will make the desired changes
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChange $progress")
                tipPercent.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        } )


        // after changing the bill amount
        tvBillAmount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                computeTipAndTotal()
            }

        })
    }

    private fun updateTipDescription(progress: Int) {
        val tipDescription = when (progress) {
            in 0..9 -> "poor"
            in 10..14 -> "acceptable"
            in 15..19 -> "good"
            in 20..24 -> "great"
            else -> "amazing"
        }
        tvTipDescription.text = tipDescription

        //update tip color description

        val color = ArgbEvaluator().evaluate(
            progress.toFloat()/seekBar.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)

        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if(tvBillAmount.text.toString().length == 0){
            totalAmount.text="";
            totalTip.text = ""
            return
        }

        val baseAmount = parseDouble(tvBillAmount.text.toString());

        val tipPercent = seekBar.progress;
        val tipAmount = baseAmount * tipPercent /100
        val totalBill = baseAmount + tipAmount

        totalTip.text = df.format(tipAmount).toString()
        totalAmount.text = df.format(totalBill).toString()

    }
}