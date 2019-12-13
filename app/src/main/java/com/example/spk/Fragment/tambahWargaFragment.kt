package com.example.spk.Fragment


import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.example.spk.Fuzzy
import com.example.spk.Model.desaModel
import com.example.spk.Model.hasilModel
import com.example.spk.Model.wargaModel

import com.example.spk.R
import com.example.spk.desaActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_tambah_warga.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class tambahWargaFragment : Fragment() {
    lateinit var Fuzzy : Fuzzy
    lateinit var ref : DatabaseReference
    lateinit var tanggal: String
    lateinit var SP: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tanggal = ""
        SP = this.activity!!.getSharedPreferences("Login", Context.MODE_PRIVATE)
        Fuzzy = Fuzzy()
        val view : View = inflater.inflate(R.layout.fragment_tambah_warga, container, false)
        val tbl_tanggal = view.findViewById<Button>(R.id.getTgllahir)

        ref = FirebaseDatabase.getInstance().getReference("Warga")
        val spJK = view.findViewById<Spinner>(R.id.getJK)


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)-30
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        tbl_tanggal.setOnClickListener {
            getTgllahir.error = null
            val dpd = DatePickerDialog(context!!,DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                tbl_tanggal.text = ""+mDay+"/"+(mMonth.toInt()+1).toString()+"/"+mYear
                tanggal = ""+mDay+"/"+(mMonth.toInt()+1).toString()+"/"+mYear
            },year,month,day)
            dpd.show()
        }

        val tombol = view.findViewById<Button>(R.id.tombolSimpan)
        tombol.setOnClickListener {

            if(getNIK.text.toString().equals("")){
                getNIK.error = "Masukkan NIK"
                getNIK.requestFocus()
            }else if(getNIK.text.toString().length != 16){
                getNIK.error = "NIK harus 16 digit"
                getNIK.requestFocus()
            }else if(getNama.text.toString().equals("")){
                getNama.error = "Masukkan Nama"
                getNama.requestFocus()
            }else if(getTlahir.text.toString().equals((""))){
                getTlahir.error = "Masukkan tempat lahir"
                getTlahir.requestFocus()
            }else if(tanggal == ""){
                getTgllahir.error = "Pilih Tanggal Lahir"
                labelTgllahir.requestFocus()
            }else if(getAlamat.text.toString().equals("")){
                getAlamat.error = "Masukkan alamat"
                getAlamat.requestFocus()
            }else if(getPekerjaan.text.toString().equals("")){
                getPekerjaan.error = "Masukkan pekerjaan"
                getPekerjaan.requestFocus()
            }else if(getGaji.text.toString().equals("")){
                getGaji.error = "Masukkan jumlah gaji"
                getGaji.requestFocus()
            }else if(getTanggungan.text.toString().equals("")){
                getTanggungan.error = "Masukkan tanggungan keluarga"
                getTanggungan.requestFocus()
            }else if(getLuas.text.toString().equals("")){
                getLuas.error = "Masukkan luas rumah"
                getLuas.requestFocus()
            }else{
                saveData(tanggal,spJK.selectedItem.toString())
            }
        }

        return view
    }

    private fun saveData(tanggal1: String, jenkel : String){
        val query = FirebaseDatabase.getInstance().getReference("Hasil")
        val nik = getNIK.text.toString()
        val nama = getNama.text.toString()
        val tl = getTlahir.text.toString()
        val tgl = tanggal1
        val jk = jenkel
        val alamat = getAlamat.text.toString()
        val pekerjaan = getPekerjaan.text.toString()
        val gaji = getGaji.text.toString().toInt()
        val tanggungan = getTanggungan.text.toString().toInt()
        val luastanah = getLuas.text.toString().toInt()
        val rt = SP.getString("RT","").toString()
        val userId = ref.push().key.toString()
        var rekomendasi = 0
        Fuzzy.fuzzy(gaji,tanggungan,luastanah)
        if(Fuzzy.getRekomendasi()){
           rekomendasi = 1
        }else{
           rekomendasi = 2
        }

        val user = wargaModel(userId,nama,nik,tgl,tl,jk,alamat,pekerjaan,gaji,tanggungan,luastanah,rt,rekomendasi)
        val hasil = hasilModel(userId,rekomendasi,Fuzzy.getDecissionIndex())
        ref.child(userId).setValue(user).addOnCompleteListener {
            Toast.makeText(context!!, "Successs", Toast.LENGTH_SHORT).show()
            getNIK.setText("")
            getNama.setText("")
            getTlahir.setText("")
            getTgllahir.text = "Pilih Tanggal Lahir"
            tanggal = ""
            getAlamat.setText("")
            getPekerjaan.setText("")
            getGaji.setText("")
            getTanggungan.setText("")
            getLuas.setText("")
        }
        query.child(userId).setValue(hasil).addOnCompleteListener {

        }

    }



}
