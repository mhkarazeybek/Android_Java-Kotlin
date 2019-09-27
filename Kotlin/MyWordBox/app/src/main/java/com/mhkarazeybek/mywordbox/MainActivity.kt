package com.mhkarazeybek.mywordbox

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    val imageArray = ArrayList<Bitmap>()
    var selectedImage: Bitmap? = null
    val ingArray = ArrayList<String>()
    val trArray = ArrayList<String>()
    var ksayısı = ingArray.size
    var c = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (user_ımage()){
            pctUser.setImageBitmap(imageArray[0])
        }else{
            pctUser.setImageResource(R.drawable.user)
        }
        pctUser.setOnLongClickListener{
            select()
        }

        testView.visibility = View.INVISIBLE
        addView2.visibility = View.INVISIBLE
        searchView.visibility = View.INVISIBLE

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ingArray)
        listView.adapter = arrayAdapter

        try {

            val database = this.openOrCreateDatabase("Words", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS words (ing VARCHAR, tr VARCHAR)")

            val cursor = database.rawQuery("SELECT * FROM words", null)

            val ingIx = cursor.getColumnIndex("ing")
            val trIx = cursor.getColumnIndex("tr")

            cursor.moveToFirst()

            while (cursor != null) {
                ingArray.add(cursor.getString(ingIx))
                trArray.add(cursor.getString(trIx))
                ksayısı = ingArray.size
                cursor.moveToNext()
                arrayAdapter.notifyDataSetChanged()

            }
            cursor?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        textView5.setText("Word Count: " + ksayısı)
        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->

            val intent = Intent(applicationContext, Main2Activity::class.java)
            intent.putExtra("ing", ingArray[i])
            intent.putExtra("tr", trArray[i])
            intent.putExtra("info", "old")
            startActivity(intent)

        }
        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Delete Operation")
            alert.setMessage("Are you sure? (" + ingArray[position] + ")")
            alert.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                delete(ingArray[position])
                Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
            alert.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_LONG).show()
            }
            alert.show()
            true
        }
    }

    fun delete(ing: String) {

        try {
            val database = this.openOrCreateDatabase("Words", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS words (ing VARCHAR, tr VARCHAR)")
            database.execSQL("DELETE FROM words WHERE ing = '" + ing + "'")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.kelime_ekle, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.ekle) {

            val intent = Intent(applicationContext, Main2Activity::class.java)
            intent.putExtra("info", "new")
            intent.putStringArrayListExtra("IngArray",ingArray)
            intent.putStringArrayListExtra("TrArray",trArray)
            startActivity(intent)

        }
        if (item?.itemId == R.id.test) {
            if (ingArray.size >= 10) {
                val intent = Intent(applicationContext, TestActivity::class.java)
                intent.putExtra("ing", ingArray)
                intent.putExtra("tr", trArray)
                val a = ingArray.size
                intent.putExtra("size", a)
                startActivity(intent)
            } else {
                val alert = AlertDialog.Builder(this)

                alert.setTitle("Insufficient word")
                alert.setMessage("start with at least 10 words")
                alert.setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
                alert.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun change(view: View) {
        if (c == 0) {
            addView.setImageResource(R.drawable.cancel)
            addView2.visibility = View.VISIBLE
            searchView.visibility = View.VISIBLE
            testView.visibility = View.VISIBLE
            c = 1
        } else {
            addView.setImageResource(R.drawable.ekle)
            addView2.visibility = View.INVISIBLE
            searchView.visibility = View.INVISIBLE
            testView.visibility = View.INVISIBLE
            c = 0
        }
    }

    fun add(view: View) {
        val intent = Intent(applicationContext, Main2Activity::class.java)
        intent.putExtra("info", "new")
        intent.putStringArrayListExtra("IngArray",ingArray)
        intent.putStringArrayListExtra("TrArray",trArray)
        startActivity(intent)
    }

    fun search(view: View) {
        val intent = Intent(applicationContext, SearchActivity::class.java)
        startActivity(intent)
    }

    fun test(view: View) {
        if (ingArray.size >= 10) {
            val intent = Intent(applicationContext, TestActivity::class.java)
            intent.putExtra("ing", ingArray)
            intent.putExtra("tr", trArray)
            val a = ingArray.size
            intent.putExtra("size", a)
            startActivity(intent)
        } else {
            val alert = AlertDialog.Builder(this)

            alert.setTitle("Kelime Yetersiz")
            alert.setMessage("En az 10 kelime ile başlamalısın")
            alert.setPositiveButton("Tamam") { dialogInterface: DialogInterface, i: Int ->
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
            alert.show()
        }
    }

    fun details(view: View) {

        val intent = Intent(applicationContext, DetailsActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("NewApi")
    fun select(): Boolean {

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2)
            return true
        }
        else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
            return false
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 2) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 1)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            val image = data.data

            try {

                selectedImage = MediaStore.Images.Media.getBitmap(this.contentResolver, image)
                pctUser.setImageBitmap(selectedImage)
                save()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun save(){
        imageArray.clear()
        val outputStream = ByteArrayOutputStream()
        selectedImage?.compress(Bitmap.CompressFormat.PNG,70,outputStream)
        val byteArray = outputStream.toByteArray()

        try {

            val database = this.openOrCreateDatabase("Images", Context.MODE_PRIVATE,null)
            database.execSQL("CREATE TABLE IF NOT EXISTS images (anahtar VARCHAR, image BLOB)")
            database.execSQL("DELETE FROM images WHERE anahtar = 'user'")
            val sqlString = "INSERT INTO images (anahtar, image) VALUES (?, ?)"
            val statement = database.compileStatement(sqlString)

            statement.bindString(1,"user")
            statement.bindBlob(2,byteArray)
            statement.execute()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun user_ımage(): Boolean {
        var a =""
        try {
            val database = this.openOrCreateDatabase("Images", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS images (anahtar VARCHAR, image BLOB)")
            val cursor = database.rawQuery("SELECT * FROM images WHERE anahtar LIKE 'user'", null)

            val anahtarIx = cursor.getColumnIndex("anahtar")
            val imageIx = cursor.getColumnIndex("image")
            pctUser.setImageBitmap(selectedImage)
            cursor.moveToFirst()
            while (cursor != null) {
                a = cursor?.getString(anahtarIx)
                val byteArray = cursor.getBlob(imageIx)
                val image = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                imageArray.add(image)
                pctUser.setImageBitmap(image)
                if (a.length > 0) {
                    return true
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
            return true
        } else {
            return false
        }
    }

    @Override
    override fun onBackPressed() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Exit Operation")
        alert.setMessage("Do you want to exit?")
        alert.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            moveTaskToBack(true)
        }
        alert.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.cancel()
        }
        alert.show()

    }
}