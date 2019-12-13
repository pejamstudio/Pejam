package com.example.spk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.spk.Model.desaModel
import com.example.spk.Model.rtModel
import com.example.spk.Model.userModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class mainActivity : AppCompatActivity() {

    lateinit var login : ImageButton
    lateinit var username : EditText
    lateinit var password : EditText
    lateinit var SP:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login = findViewById(R.id.tbl_login)
        username = findViewById(R.id.username)
        password = findViewById(R.id.pass)
        SP = getSharedPreferences("Login",Context.MODE_PRIVATE)

        val status = SP.getString("STATUS","")

        if(status == "desa"){
            startActivity(Intent(this, desaActivity::class.java))
            finish()
        }else if(status == "rt"){
            startActivity(Intent(this, rtActivity::class.java))
            finish()
        }


        login.setOnClickListener {
            val usern = username.text.toString()
            val pass = password.text.toString()

            if(usern.isNullOrEmpty()){
                username.error = "Harap isi username"
                username.requestFocus()
            }else if(pass.isNullOrEmpty()){
                password.error = "Harap isi password"
                password.requestFocus()
            }else{
                loginUser(usern,pass)
            }

        }



    }

    override fun onBackPressed() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Keluar Aplikasi")
        alertDialog.setMessage("Apakah anda yakin ingin keluar aplikasi ?")

        alertDialog.setPositiveButton("Iya") { dialog, which ->
            finish()
        }
        alertDialog.setNegativeButton("Tidak") { dialog, which ->

        }
        alertDialog.create().show()
    }

    private fun loginUser(username: String,pass:String){
        val ref = FirebaseDatabase.getInstance().reference.child("User").orderByChild("username").equalTo(username)
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for (u in p0.children){
                        val user = u.getValue(userModel::class.java)
                        if(user!!.pass.equals(pass)){
                            val editor = SP.edit()
                            editor.putString("USERNAME",user.username)
                            editor.putString("STATUS", user.status)
                            editor.putString("ID", user.id)
                            editor.apply()
                            if(user.status =="desa"){
                                val intent = Intent(this@mainActivity,desaActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                val intent = Intent(this@mainActivity,rtActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                        }else{
                            Toast.makeText(applicationContext,"Password salah", Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Toast.makeText(applicationContext,"Akun tidak terdaftar", Toast.LENGTH_LONG).show()
                }
            }

        })
    }



}
