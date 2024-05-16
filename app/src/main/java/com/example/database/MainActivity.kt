package com.example.database

import android.content.ContentValues
import android.os.Bundle
import android.provider.ContactsContract.Contacts
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.database.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var contacts: ArrayList<Contact>
    lateinit var binding: ActivityMainBinding // --b

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        val db = openOrCreateDatabase("my_database", MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS contacts(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, phone TEXT NOT NULL)")


        val cursor = db.rawQuery("SELECT * FROM contacts", null)
        contacts = ArrayList<Contact>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val phone = cursor.getString(2)
            val contact = Contact(name, phone)
            contacts.add(contact)
        }
        val gson = Gson()
        val content = gson.toJson(contacts)
        Log.v("Prueva", content)
        db.close()
    }

    fun save(v: View) {
        val name = binding.edName.text.toString()
        val phone = binding.edPhoneNumber.text.toString()

        val db = openOrCreateDatabase("my_database", MODE_PRIVATE, null)
        val parameter = ContentValues()
        parameter.put("name", name)
        parameter.put("phone", phone)
        db.insert("contacts", null, parameter)

        Toast.makeText(this, "save correct", Toast.LENGTH_LONG).show()

        binding.edName.setText("")
        binding.edPhoneNumber.setText("")
        db.close()

    }

}