package io.codefordays.binaryclock
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Settings : AppCompatActivity(){

    private var buttons = MutableList(0){Button(this)}
    private var colorSelectorFrag: ColorSelector? = null

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
    }

    override fun onBackPressed() {
        super.onBackPressed()
        for (button in buttons){
            button.isEnabled = true
        }
    }

    private fun launchColorSelector(button: Int){
        val fragTrans = supportFragmentManager.beginTransaction()
        val frag = ColorSelector()
        fragTrans.add(R.id.frag_root, frag, "color_selector_frag")
        fragTrans.addToBackStack(null)
        fragTrans.commit()
        disableButtons()
        colorSelectorFrag = frag
    }

    private fun disableButtons(){
        for (button in buttons){
            button.isEnabled = false
        }
    }

    private fun updateSettings(){

    }
}