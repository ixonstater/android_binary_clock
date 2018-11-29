package io.codefordays.binaryclock

import android.graphics.Color
const val DEFAULT_BACKGROUND = Color.WHITE
const val DEFAULT_INNER = Color.BLUE
const val DEFAULT_OUTER = Color.BLACK

class ColorSettings{
    private lateinit var colorSettingsArr: Array<Int>
    private val setColorSettingsCode = 1452
    private val getColorSettingsCode = 1724

    init {
        colorSettingsArr[0] = DEFAULT_BACKGROUND
        colorSettingsArr[1] = DEFAULT_OUTER
        colorSettingsArr[2] = DEFAULT_INNER
    }

    fun setColorSettings(newSetting: Int, Target: Int, code: Int){
        if(code != setColorSettingsCode){ throw error("Bad data mutate attempt. Expecting $setColorSettingsCode, got $code") }
        when(Target){
            0 -> this.colorSettingsArr[0] = newSetting
            1 -> this.colorSettingsArr[1] = newSetting
            2 -> this.colorSettingsArr[2] = newSetting
        }
    }

    fun getColorSettings(code: Int) : Array<Int>{
        if(code != getColorSettingsCode){ throw error("Bad data access attempt. Expecting $getColorSettingsCode, got $code") }
        return colorSettingsArr
    }
}