package uz.akra.flagquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import uz.akra.flagquiz.databinding.ActivityMainBinding
import uz.akra.flagquiz.models.flag
import java.util.Random

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var binding: ActivityMainBinding
    lateinit var flagArrayList: ArrayList<flag>
    var count = 0
    var countryName = ""
    lateinit var buttonArrayList: ArrayList<Button>

    private lateinit var linearMatn: LinearLayout
    private lateinit var linearBtn1: LinearLayout
    private lateinit var linearBtn2: LinearLayout
    private lateinit var image: ImageView

    private lateinit var stopwatchTextView: TextView
    private var running = false
    private var seconds = 0
    private val handler = Handler()

    private val runnable = object : Runnable {
        override fun run() {
            if (running) {
                seconds++
                val hours = seconds / 3600
                val minutes = seconds % 3600 / 60
                val secs = seconds % 60

                val time = String.format("%02d:%02d:%02d", hours, minutes, secs)
                stopwatchTextView.text = time

                // Sekundomer davom etishi
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stopwatchTextView = binding.txtTime
        buttonArrayList = ArrayList()

        linearMatn = binding.l1Matn
        linearBtn1 = binding.l2Btn1
        linearBtn2 = binding.l3Btn2
        image = binding.image

        makingObject()
        btnMakeCount()
        startStopwatch()
    }

    private fun makingObject() {
        flagArrayList = ArrayList()
        flagArrayList.add(flag("indonesia", R.drawable.indo))
        flagArrayList.add(flag("belarus", R.drawable.belaruz))
        flagArrayList.add(flag("uzbekistan", R.drawable.uzbekistan))
        flagArrayList.add(flag("brazil", R.drawable.brasil))
        flagArrayList.add(flag("azerbaijan", R.drawable.azerbaijan))
        flagArrayList.add(flag("estonia", R.drawable.estonia))
        flagArrayList.add(flag("germany", R.drawable.germany))
        flagArrayList.add(flag("japan", R.drawable.japan))
        flagArrayList.add(flag("kazakhstan", R.drawable.kazakh))
        flagArrayList.add(flag("malaysia", R.drawable.malaysia))
        flagArrayList.add(flag("palestine", R.drawable.palestine))
        flagArrayList.add(flag("south korea", R.drawable.south_korea))
    }

    fun btnMakeCount() {
        image.setImageResource(flagArrayList[count].image!!)
        linearMatn.removeAllViews()
        linearBtn1.removeAllViews()
        linearBtn2.removeAllViews()
        countryName = ""
        btnMake(flagArrayList[count].name)
    }

    private fun btnMake(countryName: String?) {
        val btnArray: ArrayList<Button> = randombtn(countryName)
        for (i in 0..5) {
            linearBtn1.addView(btnArray[i])
        }
        for (i in 6..11) {
            linearBtn2.addView(btnArray[i])
        }
    }

    private fun randombtn(countryName: String?): ArrayList<Button> {
        val array = ArrayList<Button>()
        val arrayText = ArrayList<String>()

        for (c in countryName!!) {
            arrayText.add(c.toString())
        }
        if (arrayText.size != 12) {
            val str = "ABCDEFGHILJKLMNOPQRSTUVWXYZ"
            for (i in arrayText.size until 12) {
                val random = Random().nextInt(str.length)
                arrayText.add(str[random].toString())
            }
        }
        arrayText.shuffle()

        for (s in 0 until arrayText.size) {
            val button = Button(this)
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
            button.text = arrayText[s]
            button.setOnClickListener(this)
            array.add(button)
        }
        return array
    }

    override fun onClick(v: View?) {
        val button1 = v as Button
        if (buttonArrayList.contains(button1)) {
            linearMatn.removeView(button1)
            var hasC = false
            linearBtn1.children.forEach { button ->
                if ((button as Button).text.toString() == button1.text.toString()) {
                    button.visibility = View.VISIBLE
                    countryName = countryName.substring(0, countryName.length - 1)
                    hasC = true
                }
            }
            linearBtn2.children.forEach { button ->
                if ((button as Button).text.toString() == button1.text.toString()) {
                    button.visibility = View.VISIBLE
                    if (!hasC) {
                        countryName = countryName.substring(0, countryName.length - 1)
                    }
                }
            }
        } else {
            button1.visibility = View.INVISIBLE
            countryName += button1.text.toString().toUpperCase()
            val button2 = Button(this)
            button2.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
            button2.text = button1.text
            button2.setOnClickListener(this)
            buttonArrayList.add(button2)
            linearMatn.addView(button2)
            matnTogri()
        }
    }

    private fun matnTogri() {
        if (countryName == flagArrayList[count].name?.toUpperCase()) {
            Toast.makeText(this, "To'g'ri topildi", Toast.LENGTH_SHORT).show()
            if (count == flagArrayList.size - 1) {
                count = 0
            } else {
                count++
            }
            btnMakeCount()
        } else {
            if (countryName.length == flagArrayList[count].name?.length) {
                Toast.makeText(this, "Qaytadan urinib ko'ring", Toast.LENGTH_SHORT).show()
                linearMatn.removeAllViews()
                linearBtn1.removeAllViews()
                linearBtn2.removeAllViews()
                countryName = ""
                btnMake(flagArrayList[count].name)
                countryName = ""
            }
        }
    }

    fun startStopwatch() {
        running = true
        handler.post(runnable)
    }

    fun stopStopwatch() {
        running = false
        handler.post(runnable)
    }

}
