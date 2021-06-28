package com.example.wifihacker

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream


class ActivitySecond : AppCompatActivity() {

    var Sciezka = "Brak wybranego pliku"
    var RozszerzeniePliku = ".xxx"
    var ssid = "aaa"
    lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val i: Intent = getIntent()                        //odebranie ssid z poprzedniego activity
        ssid = i.getExtras()?.getString("ssid").toString()


        val ssid2text: TextView = findViewById(R.id.ssid2_text)
        ssid2text.text="SSID: "+ssid

        val StartButton: Button = findViewById(R.id.Start_button)
        StartButton.setOnClickListener {
            if (RozszerzeniePliku == ".txt") {
                val intent = Intent(this, ActivityThird::class.java)

                intent.putExtra("ssid",ssid)
                intent.putExtra("uri", uri)
                println("passed "+ssid+" "+uri)
                startActivity(intent)
            }
            else {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Błąd")
                alertDialogBuilder.setMessage("Wybierz plik z rozszerzeniem .txt")
                alertDialogBuilder.show()
            }

        }

        val WybierzPlikButton: Button = findViewById(R.id.WybierzPlik_button)
        WybierzPlikButton.setOnClickListener {
            val intent2 = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent2, "Wybierz plik"), 111)

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            val WybranyPlik = data?.data
            val WybranyPlik_text: TextView = findViewById(R.id.WybranyPlik_text)
            Sciezka = WybranyPlik?.path.toString()
            WybranyPlik_text.text = Sciezka

            RozszerzeniePliku = Sciezka?.takeLast(4)
            println(Sciezka);

            uri = data?.getData()!!

            //println("---"+uri)

//            val inputStream: InputStream? = contentResolver.openInputStream(uri!!)
//            val lineList = mutableListOf<String>()
//            if (inputStream != null) {
//                inputStream.bufferedReader().forEachLine { lineList.add(it) }
//            }
//            lineList.forEach{println(">  " + it)}

        }
    }
}