package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"
    }
    private var currentVisibleView: String = METRIC_UNITS_VIEW  // A variable hold a value to make a selected view visible

    private var bindind: ActivityBmiBinding ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(bindind?.root)

        setSupportActionBar(bindind?.toolbarBmiActivity)
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        bindind?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitView()

        bindind?.rgUnits?.setOnCheckedChangeListener{_, checkedId: Int ->
            if(checkedId== R.id.rbMetricUnit){
                makeVisibleMetricUnitView()
            }else{
                makeVisibleUSUnitView()
            }
        }

        bindind?.btnCalculateUnits?.setOnClickListener {
           calculateUnit()
        }
    }

    private fun makeVisibleMetricUnitView(){
        currentVisibleView = METRIC_UNITS_VIEW
        bindind?.tillMetricUnit?.visibility = View.VISIBLE
        bindind?.tillMetricUnitHeight?.visibility = View.VISIBLE
        bindind?.tillMetricUSUnitHeight?.visibility = View.GONE
        bindind?.tillMetricUSUnitHeightInch?.visibility = View.GONE
        bindind?.tillUsMetricUnitWeight?.visibility = View.GONE

        bindind?.etMetricUnitHeight?.text!!.clear()
        bindind?.etMetricUnitWeight?.text!!.clear()
        bindind?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUSUnitView(){
        currentVisibleView = US_UNITS_VIEW
        bindind?.tillMetricUnit?.visibility = View.INVISIBLE
        bindind?.tillMetricUnitHeight?.visibility = View.INVISIBLE
        bindind?.tillMetricUSUnitHeight?.visibility = View.VISIBLE
        bindind?.tillMetricUSUnitHeightInch?.visibility = View.VISIBLE
        bindind?.tillUsMetricUnitWeight?.visibility = View.VISIBLE

        bindind?.etMetricUnitHeight?.text!!.clear()
        bindind?.etMetricUnitWeight?.text!!.clear()
        bindind?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription: String
        if(bmi.compareTo(15f)<=0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more"
        }else if(bmi.compareTo(15f)>0 && bmi.compareTo(16f)<=0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more"
        }else if(bmi.compareTo(16f)>0 && bmi.compareTo(18.5f)<=0){
            bmiLabel = "Underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more"
        }else if(bmi.compareTo(18.5f)>0 && bmi.compareTo(25f)<=0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        }else if(bmi.compareTo(25f)>0 && bmi.compareTo(30f)<=0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of yourself! WorkOut"
        }else if(bmi.compareTo(30f)>0 && bmi.compareTo(35f)<=0){
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of yourself! Workout some more"
        }else if(bmi.compareTo(35f)>0 && bmi.compareTo(40f)<=0){
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }else{
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        bindind?.llDisplayBMIResult?.visibility = View.VISIBLE
        bindind?.tvBMIValue?.text = bmiValue
        bindind?.tvBMIType?.text = bmiLabel
        bindind?.tvBMIDescription?.text = bmiDescription
    }


    private fun validateMetricUnit():Boolean{
        var isValid = true
        if(bindind?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if(bindind?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun calculateUnit(){
        if(currentVisibleView == METRIC_UNITS_VIEW){
            if(validateMetricUnit()){
                val weightValue: Float = bindind?.etMetricUnitWeight?.text.toString().toFloat()
                val heightValue: Float = bindind?.etMetricUnitHeight?.text.toString().toFloat() / 100

                val bmi = weightValue / (heightValue*heightValue)

                displayBMIResult(bmi)
            }else{
                Toast.makeText(this, "please enter valid value", Toast.LENGTH_SHORT).show()
            }
        }else{
            if(validateUsUnit()){
                val usUnitHeightValueFeet: String =
                    bindind?.etUsMetricUnitHeightFeet?.text.toString()
                val usUnitHeightValueInch: String =
                    bindind?.etUsMetricUnitHeightInch?.text.toString()
                val usUnitWeightValue: Float = bindind?.etUsMetricUnitWeight?.text.toString().toFloat()
                val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12
                val bmi = 703*(usUnitWeightValue / (heightValue * heightValue))
                displayBMIResult(bmi)
            }else{
                Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateUsUnit():Boolean{
        var isValid = true
        when {
            bindind?.etUsMetricUnitWeight?.text.toString().isEmpty() -> {
                isValid = false
            }
            bindind?.etUsMetricUnitHeightFeet?.text.toString().isEmpty() -> {
                isValid = false
            }
            bindind?.etUsMetricUnitHeightInch?.text.toString().isEmpty() -> {
                isValid = false
            }
        }
        return isValid
    }

}