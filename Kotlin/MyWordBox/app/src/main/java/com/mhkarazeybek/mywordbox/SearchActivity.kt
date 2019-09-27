package com.mhkarazeybek.mywordbox

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    var array1 = ArrayList<String>()
    var array2 = ArrayList<String>()
    var c=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    fun search2(view: View) {
        array1.clear()
        array2.clear()
        if (textView.text=="Main Word"){
            search3()
        }else {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, array1)
            listView2.adapter = arrayAdapter
            var x = searchText2.text.toString()
            try {

                val database = this.openOrCreateDatabase("Words", Context.MODE_PRIVATE, null)
                database.execSQL("CREATE TABLE IF NOT EXISTS words (ing VARCHAR, tr VARCHAR)")
                val cursor = database.rawQuery("SELECT * FROM words WHERE ing LIKE '%" + x.removeSuffix(" ") + "%'", null)
                val ingIndex = cursor.getColumnIndex("ing")
                val trIndex = cursor.getColumnIndex("tr")
                cursor.moveToFirst()
                while (cursor != null) {
                    array1.add(cursor.getString(ingIndex))
                    array2.add(cursor.getString(trIndex))
                    cursor.moveToNext()
                    arrayAdapter.notifyDataSetChanged()
                }
                cursor?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            listView2.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->

                val intent = Intent(applicationContext, Main2Activity::class.java)
                intent.putExtra("ing", array1[i])
                intent.putExtra("tr", array2[i])
                intent.putExtra("info", "old")
                startActivity(intent)
            }
            listView2.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Silme işlemi")
                alert.setMessage("Bu kelimeyi silmek istiyormusunuz? (" + array1[position] + ")")
                alert.setPositiveButton("Evet") { dialogInterface: DialogInterface, i: Int ->
                    delete(array1[position])
                    Toast.makeText(applicationContext, "Silme işlemi gerçekleştirildi", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
                alert.setNegativeButton("Hayır") { dialogInterface: DialogInterface, i: Int ->
                    Toast.makeText(applicationContext, "Silme işlemi iptal edildi", Toast.LENGTH_LONG).show()

                }
                alert.show()
                true
            }
        }
    }

    fun delete(ing:String){

        try {
            val database = this.openOrCreateDatabase("Words", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS words (ing VARCHAR, tr VARCHAR)")
            database.execSQL("DELETE FROM words WHERE ing = '"+ing+ "'")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun back(view: View){
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)

    }

    fun search3(){
        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,array2)
        listView2.adapter = arrayAdapter
        var x=searchText2.text.toString()
        try {

            val database = this.openOrCreateDatabase("Words", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS words (ing VARCHAR, tr VARCHAR)")
            val cursor = database.rawQuery("SELECT * FROM words WHERE tr LIKE '%"+x.removeSuffix(" ")+"%'", null)
            val ingIndex = cursor.getColumnIndex("ing")
            val trIndex=cursor.getColumnIndex("tr")
            cursor.moveToFirst()
            while (cursor != null) {
                array1.add(cursor.getString(ingIndex))
                array2.add(cursor.getString(trIndex))
                cursor.moveToNext()
                arrayAdapter.notifyDataSetChanged()
            }
            cursor?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        listView2.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->

            val intent = Intent(applicationContext,Main2Activity::class.java)
            intent.putExtra("ing",array1[i])
            intent.putExtra("tr",array2[i])
            intent.putExtra("info","old")
            startActivity(intent)
        }
        listView2.onItemLongClickListener=AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Delete")
            alert.setMessage("Are you sure? ("+array1[position]+")")
            alert.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                delete(array1[position])
                Toast.makeText(applicationContext,"Deleted", Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
            alert.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(applicationContext,"Cancelled", Toast.LENGTH_LONG).show()

            }
            alert.show()
            true}
    }

    fun search_change(view: View){
        if(c==0){
            imageView4.setImageResource(R.drawable.search_change2)
            var x=textView.text
            textView.text=textView2.text
            textView2.text=x
            c=1
        }else{
            imageView4.setImageResource(R.drawable.search_change)
            var x=textView.text
            textView.text=textView2.text
            textView2.text=x
            c=0
        }
    }

    @Override
    override fun onBackPressed() {
      val intent=Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
    }
    }
