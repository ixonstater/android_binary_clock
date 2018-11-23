package io.codefordays.binaryclock

import android.graphics.Color
import java.io.Serializable

data class UserDefinedSettings(val i: Int = 0) : Serializable{
    var circleInnerColor = Color.parseColor("#dd735eff")
    var circleOuterColor = Color.parseColor("#000000")
    var backgroundColor = Color.parseColor("#F5F5F5")
}