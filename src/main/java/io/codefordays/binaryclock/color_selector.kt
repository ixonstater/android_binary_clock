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
import android.widget.ImageView
import android.widget.SeekBar
import io.codefordays.binaryclock.io.codefordays.model.DataModel

class ColorSelector : DialogFragment() {

    private var buttonNum = 0
    lateinit var settingsData: SettingsData
    private lateinit var currentColorDisplay: ImageView

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
        activity!!.findViewById<SeekBar>(R.id.seekBarR).setOnSeekBarChangeListener(SeekbarListener(this))
        activity!!.findViewById<SeekBar>(R.id.seekBarG).setOnSeekBarChangeListener(SeekbarListener(this))
        activity!!.findViewById<SeekBar>(R.id.seekBarB).setOnSeekBarChangeListener(SeekbarListener(this))
        currentColorDisplay = activity!!.findViewById(R.id.imageView)
        settingsData = ViewModelProviders.of(this).get(SettingsData::class.java)
        settingsData.pullData()
    }

    override fun onPause() {
        super.onPause()
        settingsData.pushData()
    }

    private fun applySettings(){
        val parentAct = activity as Settings
        val red = activity!!.findViewById<SeekBar>(R.id.seekBarR).progress * 255 / 100
        val green = activity!!.findViewById<SeekBar>(R.id.seekBarG).progress * 255 / 100
        val blue = activity!!.findViewById<SeekBar>(R.id.seekBarB).progress * 255 / 100
        settingsData.setColorSettings(convertColor(red, green, blue), buttonNum)
        settingsData.pushData()
        parentAct.closeColorSelector()
    }

    private fun convertColor(red: Int, green: Int, blue: Int) : Int{
        var redHex =  red.toString(16)
        if(redHex.length == 1){ redHex = "0$redHex" }

        var greenHex = green.toString(16)
        if(greenHex.length == 1){greenHex = "0$greenHex"}

        var blueHex = blue.toString(16)
        if(blueHex.length == 1){blueHex = "0$blueHex"}

        val hexColor = "#$redHex$greenHex$blueHex"
        var retVal = 0
        try{
            retVal = Color.parseColor(hexColor)
        } catch (e: IllegalArgumentException){
            println(retVal)
        }
        return Color.parseColor(hexColor)
    }

    fun updateColorDisplay(){
        currentColorDisplay.setBackgroundColor(convertColor(settingsData.currentColorR, settingsData.currentColorG, settingsData.currentColorB))
    }
}

private class SeekbarListener(val frag: ColorSelector) : SeekBar.OnSeekBarChangeListener{

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when(seekBar?.id){
            R.id.seekBarR -> redChanged(progress)
            R.id.seekBarG -> greenChanged(progress)
            R.id.seekBarB -> blueChanged(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    fun redChanged(progress: Int){
        frag.settingsData.currentColorR = progress * 255 / 100
        frag.updateColorDisplay()
    }

    fun greenChanged(progress: Int){
        frag.settingsData.currentColorG = progress * 255 / 100
        frag.updateColorDisplay()
    }

    fun blueChanged(progress: Int){
        frag.settingsData.currentColorB = progress * 255 / 100
        frag.updateColorDisplay()
    }
}

class SettingsData : ViewModel(){
    private lateinit var colorSettings: Array<Int>
    var currentColorR = 255
    var currentColorG = 255
    var currentColorB = 255
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