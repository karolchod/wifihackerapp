package com.example.wifihacker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.io.InputStream
import kotlin.properties.Delegates
import kotlin.system.exitProcess

class ActivityFourth : AppCompatActivity() {

    lateinit var ssid: String
    lateinit var correctPass: String
    var status by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)

        val i: Intent = getIntent()                        //odebranie ssid z  poprzedniego activity
        ssid = i.extras?.getString("ssid").toString()
        correctPass = i.extras?.getString("correctPass").toString()
        status = i.getBooleanExtra("status",false)
        println("received "+ssid+" "+correctPass+" "+status)

        if (status){
            val haslo_text4: TextView = findViewById(R.id.Haslo_text)
            haslo_text4.text=correctPass
            haslo_text4.visibility=View.VISIBLE
            val udane_text4: TextView = findViewById(R.id.HasloZlamane_text)
            udane_text4.visibility= View.VISIBLE
            val nieudane_text4: TextView = findViewById(R.id.Nieudane_text)
            nieudane_text4.visibility=View.GONE
        }else{
            val haslo_text4: TextView = findViewById(R.id.Haslo_text)
            haslo_text4.visibility=View.GONE
            val udane_text4: TextView = findViewById(R.id.HasloZlamane_text)
            udane_text4.visibility= View.GONE
            val nieudane_text4: TextView = findViewById(R.id.Nieudane_text)
            nieudane_text4.visibility=View.VISIBLE
        }




        val WyjscieButton: Button = findViewById(R.id.Wyjscie_button)
        WyjscieButton.setOnClickListener {
//            val wifiManager = this.applicationContext //reset wifi zeby znikly bledne sieci z listy sieci, chyba ze to tylko w moim tel tak jest ze widac wiele sieci tej samej nazwy
//                .getSystemService(Context.WIFI_SERVICE) as WifiManager
//            wifiManager.isWifiEnabled = false
//            wifiManager.isWifiEnabled = true
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            }
    }
}