package com.emreeldemir.artbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.emreeldemir.artbook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        try {
            val database = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null)

            val cursor = database.rawQuery("SELECT * FROM arts", null)
            val artNameIx = cursor.getColumnIndex("artname")
            val idIx = cursor.getColumnIndex("id")

            while (cursor.moveToNext()){
                println("Art Name: ${cursor.getString(artNameIx)}")
                println("ID: ${cursor.getInt(idIx)}")
            }



        } catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.art_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.add_art_item){
            val intent = Intent(this@MainActivity, ArtActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

}