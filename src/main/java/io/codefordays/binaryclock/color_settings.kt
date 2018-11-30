package io.codefordays.binaryclock

class ColorSettings{
    private lateinit var colorSettingsArr: Array<Int>
    private val setColorSettingsCode = 1452
    private val getColorSettingsCode = 1724

    init {
        colorSettingsArr[0] = DEFAULT_BACKGROUND
        colorSettingsArr[1] = DEFAULT_OUTER
        colorSettingsArr[2] = DEFAULT_INNER
    }

    fun setColorSettings(newSettings: Array<Int>, code: Int){
        if(code != setColorSettingsCode){ throw error("Bad data mutate attempt. Expecting $setColorSettingsCode, got $code") }
        colorSettingsArr = newSettings
    }

    fun getColorSettings(code: Int) : Array<Int>{
        if(code != getColorSettingsCode){ throw error("Bad data access attempt. Expecting $getColorSettingsCode, got $code") }
        return colorSettingsArr
    }
}