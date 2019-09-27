package com.mhkarazeybek.mywordbox

import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*
import kotlin.collections.ArrayList

class Main2Activity : AppCompatActivity(), TextToSpeech.OnInitListener {

    var ingArray=ArrayList<String>()
    var trArray=ArrayList<String>()
    var utr = ""
    var uing = ""
    var speak=0
    lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        textToSpeech= TextToSpeech(applicationContext,this)

        val intent = intent

        val info = intent.getStringExtra("info")

        if (info.equals("new")) {
            ingArray=intent.getStringArrayListExtra("IngArray")
            trArray=intent.getStringArrayListExtra("TrArray")
            button2.visibility = View.INVISIBLE
            button.visibility = View.VISIBLE
            button5.visibility = View.INVISIBLE
            imgSpeakerIng.visibility=View.INVISIBLE
            editText.setText("")

        }
        else {
            val ing = intent.getStringExtra("ing")
            val tr = intent.getStringExtra("tr")
            uing = ing
            utr = tr
            editText.setText(ing)
            editText2.setText(tr)
            imgSpeakerIng.visibility=View.VISIBLE
            button.visibility = View.INVISIBLE
            button2.visibility = View.VISIBLE
            button5.visibility=View.VISIBLE
        }

    }

    override fun onInit(status:Int){
        if (status==TextToSpeech.SUCCESS){
            val res:Int=textToSpeech.setLanguage(Locale.US)
            if (res==TextToSpeech.LANG_MISSING_DATA || res==TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(applicationContext,"This language is not supported...",Toast.LENGTH_LONG).show()
            }
            else{
                imgSpeakerIng.isEnabled=true
                textToSpeechFunction()
            }
        }
        else{
            Toast.makeText(applicationContext,"Failed to İnitialize",Toast.LENGTH_LONG).show()
        }

    }

    private fun textToSpeechFunction(){
        if (speak!=0){
            val strText=editText.text.toString()
            textToSpeech.speak(strText,TextToSpeech.QUEUE_FLUSH,null)
            speak=0
        }


    }

    fun konus(view: View){
        speak=1
        textToSpeechFunction()
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun save(view: View) {

        val ing = editText.text.toString()
        val tr = editText2.text.toString()
        var deger = control(ing)
        if (deger) {
            try {

                val database = this.openOrCreateDatabase("Words", Context.MODE_PRIVATE, null)
                database.execSQL("CREATE TABLE IF NOT EXISTS words (ing VARCHAR, tr VARCHAR)")
                val sqlString = "INSERT INTO words (ing, tr) VALUES (?, ?)"
                val statement = database.compileStatement(sqlString)
                if (ing != "" && tr != "") {
                    statement.bindString(1, ing.removeSuffix(" "))
                    statement.bindString(2, tr.removeSuffix(" "))
                    statement.execute()



                    val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)


                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }



        } else {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Registered")
            alert.setMessage("The word is already there!")
            alert.setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->

            }
            alert.show()
        }
    }

    fun update(view: View) {

            val ing = editText.text.toString()
            val tr = editText2.text.toString()

            try {

                val database = this.openOrCreateDatabase("Words", Context.MODE_PRIVATE, null)
                database.execSQL("CREATE TABLE IF NOT EXISTS words (ing VARCHAR, tr VARCHAR)")
                database.execSQL("DELETE FROM words WHERE ing = '" + uing + "'")
                val sqlString = "INSERT INTO words (ing, tr) VALUES (?, ?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1, ing.removeSuffix(" "))
                statement.bindString(2, tr.removeSuffix(" "))
                statement.execute()


            } catch (e: Exception) {
                e.printStackTrace()
            }

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)

        }

    fun control(ing2: String): Boolean {
            var a = ""
            try {


                val database = this.openOrCreateDatabase("Words", Context.MODE_PRIVATE, null)
                database.execSQL("CREATE TABLE IF NOT EXISTS words (ing VARCHAR, tr VARCHAR)")
                val cursor = database.rawQuery("SELECT * FROM words WHERE ing LIKE '" + ing2 + "'", null)

                val ingIndex = cursor.getColumnIndex("ing")
                cursor.moveToFirst()
                while (cursor != null) {
                    a = cursor?.getString(ingIndex)
                    if (a.length > 0) {
                        return false

                    }
                    cursor.moveToNext()
                }

                if (cursor != null) {
                    cursor!!.close()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (a.length > 0) {
                return false
            } else {
                return true
            }
        }

    fun geri(view: View){
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)

    }
}