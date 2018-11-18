package io.codefordays.binaryclock
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Settings : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<Button>(R.id.button1).setOnClickListener { launchColorSelector(1) }
    }

    private fun launchColorSelector(button: Int){
        val fragTrans = supportFragmentManager.beginTransaction()
        val frag = ColorSelector()
        fragTrans.add(R.id.frag_root, frag)
        fragTrans.show(frag)
        fragTrans.commit()
        findViewById<Button>(R.id.button1).isEnabled = false
        findViewById<Button>(R.id.button2).isEnabled = false
        findViewById<Button>(R.id.button3).isEnabled = false
    }

    private fun updateSettings(){

    }
}
