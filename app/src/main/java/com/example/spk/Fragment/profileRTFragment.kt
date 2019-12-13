package com.example.spk.Fragment


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.spk.Model.desaModel
import com.example.spk.Model.rtModel

import com.example.spk.R
import com.example.spk.desaActivity
import com.example.spk.mainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile_rt.*

/**
 * A simple [Fragment] subclass.
 */
class profileRTFragment : Fragment() {

    lateinit var SP : SharedPreferences
    lateinit var namaRT : TextView
    lateinit var nmDs : TextView
    lateinit var Rw :TextView
    lateinit var Rt :TextView
    lateinit var Usern :TextView
    lateinit var logout : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_profile_rt, container, false)
        SP = this.activity!!.getSharedPreferences("Login",Context.MODE_PRIVATE)
        namaRT = view.findViewById(R.id.namaRt)
        nmDs = view.findViewById(R.id.Ds)
        Rw = view.findViewById(R.id.Rw)
        Rt = view.findViewById(R.id.Rt)
        Usern = view.findViewById(R.id.username)
        logout = view.findViewById(R.id.btn_logout)


        getProfilRT()

        logout.setOnClickListener {
            val editor = SP.edit()
            editor.putString("USERNAME","")
            editor.putString("STATUS","")
            editor.putString("RT","")
            editor.putString("RW","")
            editor.putString("NAMA","")
            editor.putString("DESA","")
            editor.apply()
            val intent = Intent(this.activity, mainActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }



        return view
    }

    private fun getProfilRT(){
        Usern.text = SP.getString("USERNAME","")
        namaRT.text = SP.getString("NAMA","")
        nmDs.text = SP.getString("DESA","")
        Rw.text = SP.getString("RW","")
        Rt.text = SP.getString("RT","")
    }


}
