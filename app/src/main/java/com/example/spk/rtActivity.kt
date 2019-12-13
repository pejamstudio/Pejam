package com.example.spk

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.spk.Fragment.*
import com.example.spk.Model.desaModel
import com.example.spk.Model.rtModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class rtActivity : AppCompatActivity() {
    lateinit var wargaFragment: wargaFragment
    lateinit var profileRTFragment: profileRTFragment
    lateinit var tambahWargaFragment: tambahWargaFragment
    lateinit var SP : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rt)
        val bottomNavigation : BottomNavigationView = findViewById(R.id.btm_nav_rt)
        SP = getSharedPreferences("Login", Context.MODE_PRIVATE)
        getBiodataRT(SP.getString("ID","").toString())
        tambahWargaFragment = tambahWargaFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content,tambahWargaFragment)
            .setTransition(FragmentTransaction.TRANSIT_NONE)
            .commit()

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){

                R.id.warga -> {
                    wargaFragment = wargaFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content,wargaFragment)
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .commit()

                }

                R.id.tambah_warga_rt ->{
                    tambahWargaFragment = tambahWargaFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content,tambahWargaFragment)
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .commit()
                }
                R.id.profile_rt -> {
                    profileRTFragment = profileRTFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content,profileRTFragment)
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

    private fun getBiodataRT(id: String){
        val ref = FirebaseDatabase.getInstance().reference.child("RT").orderByChild("id").equalTo(id)
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for (u in p0.children){
                        val user = u.getValue(rtModel::class.java)
                        if(user!!.id.equals(id)){
                            val editor = SP.edit()
                            editor.putString("DESA",user.desa)
                            editor.putString("NAMA", user.nama)
                            editor.putString("RW", user.rw)
                            editor.putString("RT", user.rt)
                            editor.apply()
                            println(SP.getString("RT","").toString())
                        }
                    }
                }
            }

        })
    }

}
