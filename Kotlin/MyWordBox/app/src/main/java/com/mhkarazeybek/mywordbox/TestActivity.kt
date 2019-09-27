package com.mhkarazeybek.mywordbox

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class TestActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    var ingArray = ArrayList<String>()
    var trArray = ArrayList<String>()
    var textArray = ArrayList<TextView>()
    var falseArray = HashSet<String>()

    var size = 10
    val random = Random()
    var index = random.nextInt(size - 0)
    var index2 = random.nextInt(size - 0)
    var index3 = random.nextInt(size - 0)
    var index4 = random.nextInt(3 - 0)
    var index5 = random.nextInt(3 - 0)
    var index6 = random.nextInt(3 - 0)
    var sınav = 0
    var score = 0
    var tıklama = 0
    var tıklama2 = 0
    var tıklama3 = 0
    var ssayısı = 1
    var dsayısı = 0
    var alertText=""
    var strText2=""
    lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech= TextToSpeech(applicationContext,this)

        val intent = intent
        ingArray = intent.getStringArrayListExtra("ing")
        trArray = intent.getStringArrayListExtra("tr")
        size = intent.getIntExtra("size", 0)

        val time=Calendar.getInstance()[12]
        val secound=Calendar.getInstance()[13]
        val resultTime=(59-time)
        val resultSecound=60-secound

        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val rnd=Random()
        val indexIntent=rnd.nextInt(size-0)
        val notificationIntent=Intent(this,NotificationClass::class.java)
        notificationIntent.putExtra("IngArray",ingArray)
        notificationIntent.putExtra("TrArray",trArray)
        notificationIntent.putExtra("index",indexIntent)

        val broadcast= PendingIntent.getBroadcast(this,100,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar=Calendar.getInstance()

        calendar.clear()
        calendar.add(Calendar.MINUTE,resultTime)
        calendar.add(Calendar.SECOND,resultSecound)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,broadcast)
        setContentView(R.layout.activity_test)
        textView.setText("Exercise")
        textView6.visibility = View.INVISIBLE
        textView8.visibility = View.INVISIBLE
        textView7.visibility = View.INVISIBLE
        scoreText.visibility = View.INVISIBLE

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ingArray)

        try {
            val database = this.openOrCreateDatabase("Words", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS words (ing VARCHAR, tr VARCHAR)")

            val cursor = database.rawQuery("SELECT * FROM words", null)

            val ingIx = cursor.getColumnIndex("ing")
            val trIx = cursor.getColumnIndex("tr")

            cursor.moveToFirst()

            while (cursor != null) {

                ingArray.add(cursor.getString(ingIx))
                trArray.add(cursor.getString(trIx))

                cursor.moveToNext()

                arrayAdapter.notifyDataSetChanged()
            }
            cursor?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onInit(status:Int){
        if (status==TextToSpeech.SUCCESS){
            val res:Int=textToSpeech.setLanguage(Locale.US)
            if (res==TextToSpeech.LANG_MISSING_DATA || res==TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(applicationContext,"This language is not supported...",Toast.LENGTH_LONG).show()
            }
            else{
                textToSpeechFunction()
            }
        }
        else{
            Toast.makeText(applicationContext,"Failed to İnitialize",Toast.LENGTH_LONG).show()
        }

    }

    private fun textToSpeechFunction(){

            val strText=textView.text.toString()
            textToSpeech.speak(strText,TextToSpeech.QUEUE_FLUSH,null)

    }

    @SuppressLint("ResourceAsColor")
    fun start(view: View) {
        tıklama = 0
        tıklama2 = 0
        tıklama3 = 0

        textView6.visibility = View.VISIBLE
        textView8.visibility = View.VISIBLE
        textView7.visibility = View.VISIBLE
        button3.visibility = View.INVISIBLE
        scoreText.visibility = View.VISIBLE
        scoreText.setText("" + ssayısı + ". Qestion")

        textArray = arrayListOf(textView6, textView7, textView8)

        index = random.nextInt(size - 0)
        index2 = random.nextInt(size - 0)
        index3 = random.nextInt(size - 0)

        index4 = random.nextInt(3 - 0)
        index5 = random.nextInt(3 - 0)
        index6 = random.nextInt(3 - 0)

        while (sınav <= 10) {
            if (index != index2) {
                if (index != index3) {
                    if (index2 != index3) {
                        if (index4 != index5) {
                            if (index4 != index6) {
                                if (index5 != index6) {
                                    textView.setText(ingArray[index])
                                    textArray[index4].setText(trArray[index])
                                    textArray[index5].setText(trArray[index2])
                                    textArray[index6].setText(trArray[index3])
                                    sınav++
                                    ssayısı++
                                    break
                                } else {
                                    index6 = random.nextInt(3 - 0)
                                }
                            } else {
                                index6 = random.nextInt(3 - 0)
                                continue
                            }
                        } else {
                            index5 = random.nextInt(3 - 0)
                            continue
                        }
                    } else {
                        index3 = random.nextInt(size - 0)
                        continue
                    }
                } else {
                    index3 = random.nextInt(size - 0)
                    continue
                }
            } else {
                index2 = random.nextInt(size - 0)
                continue
            }
        }
        if (sınav > 10) {
            scoreText.text = "Exercise finished!"
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Exercise finished")
            textView.text="Score:"+score.toString()
            textView6.visibility=View.INVISIBLE
            textView7.visibility=View.INVISIBLE
            textView8.visibility=View.INVISIBLE

            if (score > 40) {
                if (falseArray.size == 0) {
                    falseArray.add("All True")
                }
                alertText="Congratulations!"

                alert.setMessage(alertText +" ☺\n" + "Score: " + score + "  (" + dsayısı + "/10)\n" + falseArray)
            } else {
                alertText="You should try a little more! "
                alert.setMessage(alertText + "\nScore: " + score + "  (" + dsayısı + "/10)\n" + falseArray)
            }
             strText2=alertText+textView.text.toString()
            alert.setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
            //alert.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> Toast.makeText(applicationContext,"Not Saved", Toast.LENGTH_LONG).show() }

            alert.show()

            //16.07.2018


            ////////////////////
        }
        if(sınav<11){
            textToSpeechFunction()
        }
        else{

            textToSpeech.speak(strText2,TextToSpeech.QUEUE_FLUSH,null)
        }
    }

    fun kontrol6(view: View) {
        if (textView6.text == trArray[index] && tıklama == 0) {
            score += 5
            tıklama = 1
            tıklama2 = 1
            tıklama3 = 1
            dsayısı += 1
            Toast.makeText(applicationContext, "True", Toast.LENGTH_SHORT).show()
            start(view)
        } else {
            if (textView6.text != trArray[index] && tıklama == 0) {
                score -= 3
                tıklama = 1
                falseArray.add(ingArray[index])
                test_details(ingArray[index],trArray[index])
                Toast.makeText(applicationContext, "False", Toast.LENGTH_SHORT).show()
                start(view)
            }
        }
    }

    fun kontrol7(view: View) {
        if (textView7.text == trArray[index] && tıklama2 == 0) {
            score += 5
            tıklama = 1
            tıklama2 = 1
            tıklama3 = 1
            dsayısı += 1
            Toast.makeText(applicationContext, "True", Toast.LENGTH_SHORT).show()
            start(view)
        } else {
            if (textView7.text != trArray[index] && tıklama2 == 0) {
                score -= 3
                tıklama2 = 1
                falseArray.add(ingArray[index])
                test_details(ingArray[index],trArray[index])
                Toast.makeText(applicationContext, "False", Toast.LENGTH_SHORT).show()
                start(view)
            }
        }
    }

    fun kontrol8(view: View) {
        if (textView8.text == trArray[index] && tıklama3 == 0) {
            score += 5
            tıklama = 1
            tıklama2 = 1
            tıklama3 = 1
            dsayısı += 1
            Toast.makeText(applicationContext, "True", Toast.LENGTH_SHORT).show()
            start(view)
        } else {
            if (textView8.text != trArray[index] && tıklama3 == 0) {
                score -= 3
                tıklama3 = 1
                falseArray.add(ingArray[index])
                test_details(ingArray[index],trArray[index])
                Toast.makeText(applicationContext, "False", Toast.LENGTH_SHORT).show()

                start(view)

            }
        }
    }

    fun test_details(kelime1:String, kelime2:String) {
        var deger = control(kelime1)
        if (deger) {
            try {
                val database = this.openOrCreateDatabase("Details", Context.MODE_PRIVATE, null)
                database.execSQL("CREATE TABLE IF NOT EXISTS details (ing VARCHAR , tr VARCHAR)")
                val sqlString = "INSERT INTO details (ing, tr) VALUES (?, ?)"
                val statement = database.compileStatement(sqlString)

                statement.bindString(1, kelime1)
                statement.bindString(2, kelime2)
                statement.execute()
//
            } catch (e: Exception) {
//
               e.printStackTrace()
            }
        }else{
            //
        }

    }

    fun control(ing2: String): Boolean {
        var a = ""
        try {
            val database = this.openOrCreateDatabase("Details", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS details (ing VARCHAR, tr VARCHAR)")
            val cursor = database.rawQuery("SELECT * FROM details WHERE ing LIKE '" + ing2 + "'", null)

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
}