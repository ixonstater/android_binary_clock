package io.codefordays.binaryclock

import java.util.Calendar

class Clock{
    private var currentTime = Time()

    fun run() : String{
        currentTime.update()
        return currentTime.makeBinTime()
    }

    class Time{
        private lateinit var timeDigits: List<Int>
        private lateinit var binTime: List<String>
        init {
            this.update()
        }

        fun update(){
            this.timeDigits = Calendar.getInstance().time.toString().split(" ")[3].filter {x -> x != ':'}.map {x -> x.toString().toInt()}
            this.binTime = timeDigits.map{x -> x.toString(2)}
        }

        fun makeBinTime(): String{
            var binDigits = ""
            val numBinDigitsList = arrayOf(2, 4, 3, 4, 3, 4)
            val convert: (Int, Int) -> String = {numBinDigits: Int, indx: Int -> "0".repeat(numBinDigits - binTime[indx].length) + binTime[indx]}
            for (i in 0..5){
                binDigits += convert(numBinDigitsList[i], i)
            }
            return binDigits
        }
    }
}


