package io.codefordays.binaryclock
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.MenuItem
import android.widget.ImageView
import kotlin.concurrent.timer
import android.util.TypedValue

class MainActivity : AppCompatActivity() {

    private val myClock = Clock()
    private val digitCoords = arrayOf( Pair(0,0), Pair(0,1),
                                    Pair(1,0), Pair(1,1), Pair(1,2), Pair(1,3),
                                    Pair(2,0), Pair(2,1), Pair(2,2),
                                    Pair(3,0), Pair(3,1), Pair(3,2), Pair(3,3),
                                    Pair(4,0), Pair(4,1), Pair(4,2),
                                    Pair(5,0), Pair(5,1), Pair(5,2), Pair(5,3))
    private var verticalStep = 0.0
    private var horizontalStep = 0.0
    private var verticalOffset = 20
    private var horizontalOffset = 20
    private var clockDigits = MutableList(0){ImageView(this)}
    private lateinit var drawer: DrawerLayout
    private lateinit var mainAcitvityData: MainActivityData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSettings()
        getRuntimeXMLVals()
        createDisplay()
        setupEventHandlers()

        mainAcitvityData = ViewModelProviders.of(this).get(MainActivityData::class.java)
        mainAcitvityData.pullData()
    }

    override fun onStart() {
        super.onStart()
        timer(period = 1000){runOnUiThread{updateDisplay()}}
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getSettings(){

    }

    private fun createDisplay(){
        val myInflater = this.layoutInflater
        for (i in 0..19){
            val digit = myInflater.inflate(R.layout.circle_view, null) as ImageView
            if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                digit.setPadding((digitCoords[i].second * horizontalStep + horizontalOffset).toInt(), (digitCoords[i].first * verticalStep + verticalOffset).toInt(), 0, 0)
            } else {
                digit.setPadding((digitCoords[i].first * horizontalStep + horizontalOffset).toInt(), (digitCoords[i].second * verticalStep + verticalOffset).toInt(), 0, 0)
            }
            findViewById<ConstraintLayout>(R.id.root_elem).addView(digit)
            clockDigits.add(digit)
        }
        setSupportActionBar(findViewById(R.id.toolbar))
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.dots_menu)
        }
        drawer = findViewById(R.id.drawer_layout)
        drawer.bringToFront()
    }

    private fun setupEventHandlers(){
        val myNavView: NavigationView = findViewById(R.id.nav_view)
        myNavView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.nav_settings -> startActivityForResult(Intent(this, Settings::class.java), SETTINGS_REQUEST_CODE)
                R.id.nav_about -> startActivity(Intent(this, About::class.java))
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun getRuntimeXMLVals(){
        val myMetric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(myMetric)
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            verticalStep = (myMetric.heightPixels - 60) / 8.0
            horizontalStep = (myMetric.widthPixels) / 5.0
        } else {
            verticalStep = (myMetric.heightPixels - 50) / 5.0
            horizontalStep = (myMetric.widthPixels) / 8.0
        }
        val tv = TypedValue()
        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            verticalOffset += TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        } else {
            throw RuntimeException("Action bar size not resolvable in MainActivity.kt private fun createDisplay()")
        }
    }

    private fun updateDisplay() {
        val binDigits = myClock.run()
        for (i in 0..19) {
            if (binDigits[i] == '0') {
                clockDigits[i].setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
            } else {
                clockDigits[i].setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.MULTIPLY)
            }
        }
    }

    private fun dpToPx(dp: Int) : Int{
        return (dp * this.resources.displayMetrics.density).toInt()
    }
}

class MainActivityData : ViewModel(){
    private val colorSettingsPullCode = 1724
    private lateinit var colorSettings: Array<Int>
    fun pullData(){
        this.colorSettings = DataModel.colorSettings.getColorSettings(colorSettingsPullCode)
    }
    fun getColorSettings(): Array<Int>{
        return colorSettings
    }
}