package com.example.spk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
//import com.example.spk.Fragment.daftarRTFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.FragmentTransaction
import com.example.spk.Fragment.*
import com.example.spk.Model.desaModel
import com.example.spk.Model.userModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class desaActivity : AppCompatActivity() {
    lateinit var SP : SharedPreferences
    lateinit var daftarRTFragment: daftarRTFragment
    lateinit var penerimaFragment: penerimaFragment
    lateinit var profileFragment: profileFragment
    lateinit var tambahRTFragment: tambahRTFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desa)
        SP = getSharedPreferences("Login", Context.MODE_PRIVATE)
        getBiodata(SP.getString("ID","").toString())



        val bottomNavigation : BottomNavigationView = findViewById(R.id.btm_nav_ds)

        tambahRTFragment = tambahRTFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content,tambahRTFragment)
            .setTransition(FragmentTransaction.TRANSIT_NONE)
            .commit()

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){

                R.id.d_RT -> {
                    daftarRTFragment = daftarRTFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content,daftarRTFragment)
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .commit()

                }
                R.id.t_RT -> {
                    tambahRTFragment = tambahRTFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content,tambahRTFragment)
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .commit()

                }

                R.id.d_warga -> {
                    penerimaFragment = penerimaFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content,penerimaFragment)
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .commit()

                }

                R.id.profile_ds -> {
                    profileFragment = profileFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content,profileFragment)
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .commit()

                }




            }
            true
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
    private fun getBiodata(id:String){
        val ref = FirebaseDatabase.getInstance().reference.child("Desa").orderByChild("id").equalTo(id)
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for (u in p0.children){
                        val user = u.getValue(desaModel::class.java)
                        if(user!!.id.equals(id)){
                            val editor = SP.edit()
                            editor.putString("DESA",user.desa)
                            editor.putString("NAMA", user.nama)
                            editor.apply()
                        }
                    }
                }
            }

        })
    }
}
