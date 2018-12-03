package io.codefordays.binaryclock
import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import io.codefordays.binaryclock.io.codefordays.model.DataModel
import java.lang.NumberFormatException

class ColorSelector : DialogFragment() {

    private var buttonNum = 0
    lateinit var settingsData: SettingsData
    private lateinit var currentColorDisplay: ImageView
    private lateinit var seekBarR: SeekBar
    private lateinit var seekBarG: SeekBar
    private lateinit var seekBarB: SeekBar
    private lateinit var editText: EditText

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

        this.seekBarR = activity!!.findViewById(R.id.seekBarR)
        this.seekBarR.setOnSeekBarChangeListener(SeekbarListener(this))
        this.seekBarG = activity!!.findViewById(R.id.seekBarG)
        this.seekBarG.setOnSeekBarChangeListener(SeekbarListener(this))
        this.seekBarB = activity!!.findViewById(R.id.seekBarB)
        this.seekBarB.setOnSeekBarChangeListener(SeekbarListener(this))

        editText = activity!!.findViewById(R.id.color_selector_text)
        editText.setOnEditorActionListener(EnterPressedListener(this))

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
        val red = seekBarR.progress * 255 / 100
        val green = seekBarG.progress * 255 / 100
        val blue = seekBarB.progress * 255 / 100
        settingsData.setColorSettings(convertColor(red, green, blue), buttonNum)
        settingsData.pushData()
        parentAct.closeColorSelector()
    }

    private fun convertColor(red: Int, green: Int, blue: Int) : Int{
        var redHex =  red.toString(16)
        if(redHex.length == 1){redHex = "0$redHex"}

        var greenHex = green.toString(16)
        if(greenHex.length == 1){greenHex = "0$greenHex"}

        var blueHex = blue.toString(16)
        if(blueHex.length == 1){blueHex = "0$blueHex"}

        val hexColor = "#$redHex$greenHex$blueHex"
        return Color.parseColor(hexColor)
    }

    fun updateColorDisplay(){
        currentColorDisplay.setBackgroundColor(convertColor(settingsData.currentColorR, settingsData.currentColorG, settingsData.currentColorB))
    }

    private fun updateProgressBars(){
        this.seekBarR.progress = (settingsData.currentColorR / 255.0 * 100).toInt()
        this.seekBarG.progress = (settingsData.currentColorG / 255.0 * 100).toInt()
        this.seekBarB.progress = (settingsData.currentColorB / 255.0 * 100).toInt()
    }

    private fun updateForTextSelectedColor(colorString: String){
        if(colorString.length != 7) {throw IllegalArgumentException("Bad text input.")}
        if(colorString[0] != '#') {throw IllegalArgumentException("Bad text input.")}
        settingsData.currentColorR = colorString.slice(1..2).toInt(16)
        settingsData.currentColorG = colorString.slice(3..4).toInt(16)
        settingsData.currentColorB = colorString.slice(5..6).toInt(16)
        updateColorDisplay()
        updateProgressBars()
    }

    fun onEnter(colorString: String){
        val imm =  activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity!!.currentFocus
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        try{
            updateForTextSelectedColor(colorString)
        }catch (e: IllegalArgumentException){
            return
        }catch (e:NumberFormatException){
            return
        }
    }
}

private class SeekbarListener(val frag: ColorSelector) : SeekBar.OnSeekBarChangeListener{

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if(!fromUser) {return}
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


class EnterPressedListener(private val frag: ColorSelector) : TextView.OnEditorActionListener{
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        frag.onEnter(v!!.text.toString())
        return true
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