package com.mhkarazeybek.mywordbox

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    var backClick=0
    var ingArray =ArrayList<String>()
    var trArray=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,ingArray)
        detailsListView.adapter = arrayAdapter

        try {
            //ingArray.clear()
            //trArray.clear()
            val database = this.openOrCreateDatabase("Details", Context.MODE_PRIVATE,null)
            database.execSQL("CREATE TABLE IF NOT EXISTS details (ing VARCHAR, tr VARCHAR)")
            val cursor = database.rawQuery("SELECT * FROM details",null)
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

        detailsListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->

            val intent = Intent(applicationContext,Main2Activity::class.java)
            intent.putExtra("ing",ingArray[i])
            intent.putExtra("tr",trArray[i])
            intent.putExtra("info","old")
            startActivity(intent)
        }
        detailsListView.onItemLongClickListener= AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Delete Operation")
            alert.setMessage("Are you Sure? ("+ingArray[position]+")")
            alert.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                delete(ingArray[position])
                Toast.makeText(applicationContext,"Deleted", Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, DetailsActivity::class.java)
                startActivity(intent)
            }
            alert.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(applicationContext,"Canceled", Toast.LENGTH_LONG).show()

            }
            alert.show()
            true}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.delete_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.delete) {
            deleteAll()
            Toast.makeText(applicationContext,"Deleted",Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, DetailsActivity::class.java)
            startActivity(intent)


        }
        return super.onOptionsItemSelected(item)
    }

    fun delete(ing:String){

        try {

            val database = this.openOrCreateDatabase("Details", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS details (ing VARCHAR, tr VARCHAR)")
            database.execSQL("DELETE FROM details WHERE ing = '"+ing+ "'")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteAll(){
        try {

            val database = this.openOrCreateDatabase("Details", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS details (ing VARCHAR, tr VARCHAR)")
            for(i in ingArray){
                database.execSQL("DELETE FROM details WHERE ing = '"+i+ "'")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Override
    override fun onBackPressed() {

        if (backClick==0){
            val intent=Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            backClick=1
        }
        else {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Exit Operation")
            alert.setMessage("Do you want to exit?")
            alert.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                finish()
                System.exit(0)
            }
            alert.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
            }
            alert.show()
        }
    }
}
