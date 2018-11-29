package io.codefordays.binaryclock
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

class ColorSelector : DialogFragment() {

    private var buttonNum = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        buttonNum = arguments!!.getInt("buttonNumber")
        return inflater.inflate(R.layout.fragment_color_selector, container, false)
    }

    private fun percentToHex(percent: Int) : String{
        val hexNum = percent * 255 / 100
        return hexNum.toString(16)
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