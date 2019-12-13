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
import com.example.spk.R
import com.example.spk.mainActivity

/**
 * A simple [Fragment] subclass.
 */
class profileFragment : Fragment() {

    lateinit var SP : SharedPreferences
    lateinit var namaDS : TextView
    lateinit var nmDs : TextView
    lateinit var Usern : TextView
    lateinit var logout : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)
        SP = this.activity!!.getSharedPreferences("Login", Context.MODE_PRIVATE)
        namaDS = view.findViewById(R.id.namaDs)
        nmDs = view.findViewById(R.id.Ds)
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
        namaDS.text = SP.getString("NAMA","")
        nmDs.text = SP.getString("DESA","")
        Usern.text = SP.getString("USERNAME","")
    }

}
