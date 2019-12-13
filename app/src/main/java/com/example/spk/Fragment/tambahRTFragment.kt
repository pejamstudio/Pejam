package com.example.spk.Fragment


import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.spk.Model.rtModel
import com.example.spk.Model.userModel
import com.example.spk.Model.wargaModel

import com.example.spk.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_tambah_warga.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class tambahRTFragment : Fragment() {

    lateinit var ref : DatabaseReference
    lateinit var SP: SharedPreferences
    lateinit var gNama : EditText
    lateinit var gAlamat : EditText
    lateinit var gRw : EditText
    lateinit var gRt : EditText
    lateinit var gUsername : EditText
    lateinit var gPass : EditText
    lateinit var gRePass : EditText
    lateinit var spJK : Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        SP = this.activity!!.getSharedPreferences("Login", Context.MODE_PRIVATE)
        val view : View = inflater.inflate(R.layout.fragment_tambah_rt, container, false)
        ref = FirebaseDatabase.getInstance().getReference("RT")
        spJK = view.findViewById(R.id.getJK)
        gNama = view.findViewById(R.id.getNama)
        gAlamat = view.findViewById(R.id.getAlamat)
        gRw = view.findViewById(R.id.getRW)
        gRt = view.findViewById(R.id.getRT)
        gUsername = view.findViewById(R.id.getUsername)
        gPass = view.findViewById(R.id.getPass)
        gRePass = view.findViewById(R.id.getRePass)


        val tombol = view.findViewById<Button>(R.id.tombolSimpan)
        tombol.setOnClickListener {

            if(gNama.text.toString().equals("")){
                gNama.error = "Masukkan nama"
                gNama.requestFocus()
            }else if(gAlamat.text.toString().equals("")){
                gAlamat.error = "Masukkan alamat"
                gAlamat.requestFocus()
            }else if(gRw.text.toString().equals("")){
                gRw.error = "Masukkan RW"
                gRw.requestFocus()
            }else if(gRt.text.toString().equals("")){
                gRt.error = "Masukkan RT"
                gRt.requestFocus()
            }else if(gUsername.text.toString().equals("")){
                gUsername.error = "Masukkan username"
                gUsername.requestFocus()
            }else if(gPass.text.toString().equals("")){
                gPass.error = "Masukkan password"
                gPass.requestFocus()
            }else if(gPass.text.toString().length < 8) {
                gPass.error = "Password minimal 8 karakter"
                gPass.requestFocus()
            }else if(!gPass.text.toString().equals(gRePass.text.toString())){
                gRePass.error = "Password tidak sama"
                gRePass.requestFocus()
            }else{
                saveData()
            }
        }

        return view
    }

    private fun saveData(){
        val query = FirebaseDatabase.getInstance().getReference("User")
        val nama = gNama.text.toString()
        val jk = spJK.selectedItem.toString()
        val alamat = gAlamat.text.toString()
        val rw = gRw.text.toString()
        val rt = gRt.text.toString()
        val username = gUsername.text.toString()
        val pass = gPass.text.toString()
        val userId = ref.push().key.toString()

        val RT = rtModel(userId,SP.getString("DESA","").toString(),rw,rt,nama,jk,alamat,username,"rt")
        val user = userModel(userId,username,pass,"rt")

        ref.child(userId).setValue(RT).addOnCompleteListener {
            Toast.makeText(context!!, "Berhasil", Toast.LENGTH_SHORT).show()
            gNama.setText("")
            gAlamat.setText("")
            gRw.setText("")
            gRt.setText("")
            gUsername.setText("")
            gPass.setText("")
            gRePass.setText("")
        }
        query.child(userId).setValue(user).addOnCompleteListener {

        }

    }


}
