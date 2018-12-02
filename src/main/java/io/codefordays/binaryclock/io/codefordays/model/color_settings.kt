package io.codefordays.binaryclock.io.codefordays.model

class ColorSettings{
    private var colorSettingsArr: Array<Int>

    init {
        colorSettingsArr = arrayOf(DEFAULT_BACKGROUND, DEFAULT_OUTER, DEFAULT_INNER)
    }

    fun setColorSettings(newSettings: Array<Int>){
        colorSettingsArr = newSettings
    }

    fun getColorSettings() : Array<Int>{
        return colorSettingsArr
    }
}