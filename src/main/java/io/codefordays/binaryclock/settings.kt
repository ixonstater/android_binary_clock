package io.codefordays.binaryclock
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Settings : AppCompatActivity(){

    private lateinit var settingsData: SettingsData
    private var buttons = MutableList(0){Button(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        buttons.add(findViewById(R.id.button1))
        buttons.add(findViewById(R.id.button2))
        buttons.add(findViewById(R.id.button3))
        buttons[0].setOnClickListener { launchColorSelector(1) }
        buttons[1].setOnClickListener { launchColorSelector(2) }
        buttons[2].setOnClickListener { launchColorSelector(3) }

        if(supportFragmentManager.findFragmentByTag("color_selector_frag") != null){
            this.disableButtons()
        }
        settingsData = ViewModelProviders.of(this).get(SettingsData::class.java)
        settingsData.pullData()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.findFragmentByTag("color_selector_frag") == null) {
            enableButtons()
        }
    }

    override fun onPause() {
        super.onPause()
        settingsData.pushData()
    }

    private fun launchColorSelector(button: Int){
        val fragTrans = supportFragmentManager.beginTransaction()
        val frag = ColorSelector()
        val args = Bundle()
        args.putInt("buttonNumber", button)
        frag.arguments = args
        fragTrans.add(R.id.frag_root, frag, "color_selector_frag")
        fragTrans.addToBackStack(null)
        fragTrans.commit()
        disableButtons()
    }

    private fun disableButtons(){
        for (button in buttons){
            button.isEnabled = false
        }
    }

    private fun enableButtons(){
        for (button in buttons){
            button.isEnabled = true
        }
    }
}

class SettingsData : ViewModel(){
    private val colorSettingsPushCode = 1452
    private val colorSettingsPullCode = 1724
    private lateinit var colorSettings: Array<Int>
    fun pushData(){
        DataModel.colorSettings.setColorSettings(colorSettings, colorSettingsPushCode)
    }
    fun pullData(){
        this.colorSettings = DataModel.colorSettings.getColorSettings(colorSettingsPullCode)
    }

    fun getColorSettings(): Array<Int>{
        return this.colorSettings
    }
    fun setColorSettings(newSetting: Int, target: Int){
        this.colorSettings[target] = newSetting
    }
}