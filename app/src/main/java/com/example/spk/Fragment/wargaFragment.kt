package com.example.spk.Fragment


import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spk.Adapter.daftarWargaAdapter
import com.example.spk.Model.wargaModel

import com.example.spk.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_warga.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class wargaFragment : Fragment() {
    lateinit var ref : DatabaseReference
    lateinit var query : Query
    lateinit var adapter : daftarWargaAdapter
    lateinit var rview : RecyclerView
    lateinit var tanggal : String
    lateinit var SP: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        SP = this.activity!!.getSharedPreferences("Login", Context.MODE_PRIVATE)
        val view : View = inflater.inflate(R.layout.fragment_warga, container, false)
        ref = FirebaseDatabase.getInstance().getReference("Warga")
        rview = view.findViewById(R.id.rView)
        rview.setHasFixedSize(true)
        rview.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        showWarga()
        return view
    }

    private fun showWarga(){

        query = ref.orderByChild("rt").equalTo(SP.getString("RT","").toString())
        val option = FirebaseRecyclerOptions.Builder<wargaModel>()
            .setQuery(query, wargaModel::class.java).build()


        adapter = daftarWargaAdapter(option)
        adapter.setOnDeleteCallback(object: daftarWargaAdapter.OnItemClickCallback{
            override fun onItemClicked(warga: wargaModel) {
                showDeleteDialog(warga)

            }
        })
        adapter.setOnUpdateCallback(object: daftarWargaAdapter.OnItemClickCallback{
            override fun onItemClicked(warga: wargaModel) {
                showUpdateDialog(warga)

            }
        })
        adapter.setOnDetailCallback(object: daftarWargaAdapter.OnItemClickCallback{
            override fun onItemClicked(warga: wargaModel) {
                showDetailDialog(warga)

            }
        })
        rview.adapter = adapter
        adapter.startListening()
    }

    private fun showDeleteDialog(user: wargaModel) {
        val builder = AlertDialog.Builder(context!!)
        val query = FirebaseDatabase.getInstance().getReference("Hasil")

        builder.setTitle("Hapus Data Warga")

        val inflater = LayoutInflater.from(context!!)

        val view = inflater.inflate(R.layout.delete_alert_warga, null)

        builder.setView(view)



        builder.setPositiveButton("Hapus") { dialog, which ->

            ref.child(user.id).removeValue()
            query.child(user.id).removeValue()
            Toast.makeText(context!!,"Deleted!!", Toast.LENGTH_LONG).show()


        }
        builder.setNegativeButton("Batal") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()


    }

        private fun showDetailDialog(user: wargaModel){
            val builder = AlertDialog.Builder(context!!)
            val inflater = LayoutInflater.from(context!!)
            val view = inflater.inflate(R.layout.detail_warga, null)

            val dNIK = view.findViewById<TextView>(R.id.dNIK)
            val dNama = view.findViewById<TextView>(R.id.dNama)
            val dTempat = view.findViewById<TextView>(R.id.dTlahir)
            val dTanggal = view.findViewById<TextView>(R.id.dTgllahir)
            val dJK = view.findViewById<TextView>(R.id.dJK)
            val dAlamat = view.findViewById<TextView>(R.id.dAlamat)
            val dPekerjaan = view.findViewById<TextView>(R.id.dPekerjaan)
            val dGaji = view.findViewById<TextView>(R.id.dGaji)
            val dTanggungan = view.findViewById<TextView>(R.id.dTanggungan)
            val dLuas = view.findViewById<TextView>(R.id.dLuas)

            dNIK.text = user.nik
            dNama.text = user.nama
            dTempat.text = user.tl
            dTanggal.text = user.tgl
            dJK.text = user.jk
            dAlamat.text = user.alamat
            dPekerjaan.text = user.pekerjaan
            dGaji.text = user.gaji.toString()
            dTanggungan.text = user.tkeluarga.toString()
            dLuas.text = user.lrumah.toString()


            builder.setView(view)

            val alert = builder.create()
            alert.show()
        }

        private fun showUpdateDialog(user: wargaModel) {
            val builder = AlertDialog.Builder(context!!)

            builder.setTitle("Edit Data Warga")
            val inflater = LayoutInflater.from(context!!)
            val view = inflater.inflate(R.layout.update_data_warga, null)

            val etNIK = view.findViewById<EditText>(R.id.editNIK)
            val etNama = view.findViewById<EditText>(R.id.editNama)
            val etTempat = view.findViewById<EditText>(R.id.editTlahir)
            val etTanggal = view.findViewById<Button>(R.id.editTgllahir)
            val etJK = view.findViewById<Spinner>(R.id.editJK)
            val etAlamat = view.findViewById<EditText>(R.id.editAlamat)

            val jk : Int
            if (user.jk =="Laki-Laki"){
                jk = 0
            }else{
                jk = 1
            }
            etNIK.setText(user.nik)
            etNama.setText(user.nama)
            etTempat.setText(user.tl)
            etTanggal.text = user.tgl
            tanggal = user.tgl
            etJK.setSelection(jk)
            etAlamat.setText(user.alamat)

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)-30
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            etTanggal.setOnClickListener {
                val dpd = DatePickerDialog(builder.context,
                    DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                        etTanggal.text = ""+mDay+"/"+(mMonth.toInt()+1).toString()+"/"+mYear
                        tanggal = ""+mDay+"/"+(mMonth.toInt()+1).toString()+"/"+mYear
                    },year,month,day)
                dpd.show()
            }

            builder.setView(view)

            builder.setPositiveButton("Edit") { dialog, which ->

                val nik = etNIK.text.toString()
                val nama = etNama.text.toString()
                val tempat = etTempat.text.toString()
                val jk = etJK.selectedItem.toString()
                val alamat = etAlamat.text.toString()

                if(etNIK.text.toString().equals("")){
                    Toast.makeText(context!!,"Gagal edit data, NIK kosong", Toast.LENGTH_LONG).show()
                }else if(etNIK.text.toString().length != 16){
                    Toast.makeText(context!!,"Gagal edit data, NIK harus 16 digit", Toast.LENGTH_LONG).show()
                }else if(etNama.text.toString().equals("")){
                    Toast.makeText(context!!,"Gagal edit data, Nama kosong", Toast.LENGTH_LONG).show()
                }else if(etTempat.text.toString().equals((""))){
                    Toast.makeText(context!!,"Gagal edit data, Tempat lahir kosong", Toast.LENGTH_LONG).show()
                }else if(etAlamat.text.toString().equals("")){
                    Toast.makeText(context!!,"Gagal edit data, Alamat kosong", Toast.LENGTH_LONG).show()
                }else {
                    val user = wargaModel(user.id,nama,nik,tanggal,tempat,jk,alamat,user.pekerjaan,user.gaji,user.tkeluarga,user.lrumah,user.rt,user.rekomendasi)

                    ref.child(user.id).setValue(user).addOnCompleteListener {
                        Toast.makeText(context!!,"Updated", Toast.LENGTH_LONG).show()
                    }
                }



            }

            builder.setNegativeButton("Batal") { dialog, which ->

            }
            val alert = builder.create()
            alert.show()

    }

    override fun onStart() {
        super.onStart()
        if(adapter != null)
            adapter.startListening()
    }
}
