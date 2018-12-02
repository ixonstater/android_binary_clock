package io.codefordays.binaryclock
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import io.codefordays.binaryclock.io.codefordays.model.DataModel

class ColorSelector : DialogFragment() {

    private var buttonNum = 0
    private lateinit var settingsData: SettingsData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        buttonNum = arguments!!.getInt("buttonNumber")
        return inflater.inflate(R.layout.fragment_color_selector, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.findViewById<Button>(R.id.color_selector_button).setOnClickListener { applySettings() }
        settingsData = ViewModelProviders.of(this).get(SettingsData::class.java)
        settingsData.pullData()
    }

    override fun onPause() {
        super.onPause()
        settingsData.pushData()
    }

    private fun applySettings(){
        val parentAct = activity as Settings
        settingsData.setColorSettings(convertColor(), buttonNum)
        settingsData.pushData()
        parentAct.closeColorSelector()
    }

    private fun convertColor() : Int{
        var red =  (activity!!.findViewById<SeekBar>(R.id.seekBarR).progress * 255 / 100).toString(16)
        if(red.length == 1){red = "0$red"}

        var green = (activity!!.findViewById<SeekBar>(R.id.seekBarG).progress * 255 / 100).toString(16)
        if(green.length == 1){green = "0$green"}

        var blue = (activity!!.findViewById<SeekBar>(R.id.seekBarB).progress * 255 / 100).toString(16)
        if(blue.length == 1){blue = "0$blue"}

        val hexColor = "#$red$green$blue"
        return Color.parseColor(hexColor)
    }
}

private class SeekbarListener() : SeekBar.OnSeekBarChangeListener{
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when(seekBar?.id){

        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
}

class SettingsData : ViewModel(){
    private lateinit var colorSettings: Array<Int>
    fun pushData(){
        DataModel.colorSettings.setColorSettings(colorSettings)
    }
    fun pullData(){
        this.colorSettings = DataModel.colorSettings.getColorSettings()
    }
    fun setColorSettings(newSetting: Int, target: Int){
        this.colorSettings[target] = newSetting
    }
}