package com.example.wifihacker

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    var resultList = ArrayList<ScanResult>()
    var ssidList = ArrayList<String>()
    lateinit var wifiManager: WifiManager

    lateinit var listView: ListView

    var ad: ArrayAdapter<String>? = null

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            resultList = wifiManager.scanResults as ArrayList<ScanResult>
            Log.d("TESTING", "onReceive Called")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//       val WybierzSiecButton: Button = findViewById(R.id.PrzykladowaSiec_button)
//        WybierzSiecButton.setOnClickListener{
//            val chosen_ssid = "siecwifi1";
//            val intent = Intent(this, ActivitySecond::class.java)
//            intent.putExtra("ssid", chosen_ssid)
//            startActivity(intent)
//
//
//
//        }

        listView = findViewById(R.id.sieciListView)

        ad = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,ssidList)
        listView.adapter=ad

        val scanpbar: ProgressBar = findViewById(R.id.scan_pBar)
        scanpbar.visibility=View.GONE

        val odswiezButton: Button = findViewById(R.id.odswiezButton)
        odswiezButton.setOnClickListener{
            startScanning()
            ssidList.clear()
            ad!!.notifyDataSetChanged()

        }

        listView.setOnItemClickListener { parent, view, position, id ->
            val chosen_ssid = ad!!.getItem(position)// The item that was clicked
//            val chosen_ssid = "siecwifi1";
            val intent = Intent(this, ActivitySecond::class.java)
            intent.putExtra("ssid", chosen_ssid)
            startActivity(intent)

        }

        wifiManager = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = false
        wifiManager.isWifiEnabled = true

        if (ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }

    }

    fun startScanning() {
        runOnUiThread {
            val odswiezButton: Button = findViewById(R.id.odswiezButton)
            odswiezButton.visibility= View.GONE
            val scanpbar: ProgressBar = findViewById(R.id.scan_pBar)
            scanpbar.visibility=View.VISIBLE
        }
        registerReceiver(broadcastReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        wifiManager.startScan()
        Handler().postDelayed({
            unregisterReceiver(broadcastReceiver)
            ssidList.clear()
            for (result in resultList) {
                ssidList.add(result.SSID)
            }
            println(ssidList)
            runOnUiThread {
                val odswiezButton: Button = findViewById(R.id.odswiezButton)
                odswiezButton.visibility= View.VISIBLE
                val scanpbar: ProgressBar = findViewById(R.id.scan_pBar)
                scanpbar.visibility=View.GONE
                ad!!.notifyDataSetChanged()
            }
        }, 5000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Udzielono dostępu do lokalizacji", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Brak dostępu do lokalizacji. Aplikacja nie będzie działać poprawnie. Proszę przyznać uprawnienia w Ustawienia>Aplikacje", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

}