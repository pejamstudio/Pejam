package com.example.spk.Fragment


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spk.Adapter.daftarRTAdapter
import com.example.spk.Model.rtModel
import com.example.spk.Model.userModel
import com.example.spk.R
import com.example.spk.desaActivity
import com.example.spk.rtActivity
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class daftarRTFragment : Fragment() {
    lateinit var ref : DatabaseReference
    lateinit var query : Query
    lateinit var adapter : daftarRTAdapter
    lateinit var rview : RecyclerView
    lateinit var tanggal : String
    lateinit var SP: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        SP = this.activity!!.getSharedPreferences("Login", Context.MODE_PRIVATE)
        val view : View = inflater.inflate(R.layout.fragment_daftar_rt, container, false)
        ref = FirebaseDatabase.getInstance().getReference("RT")
        query = ref.orderByChild("rt")
        rview = view.findViewById(R.id.rView)
        rview.setHasFixedSize(true)
        rview.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        showRT()
        return view
    }

    private fun showRT(){
        query = ref.orderByChild("rt")
        val option = FirebaseRecyclerOptions.Builder<rtModel>()
            .setQuery(query, rtModel::class.java).build()


        adapter = daftarRTAdapter(option)
        adapter.setOnDeleteCallback(object: daftarRTAdapter.OnItemClickCallback{
            override fun onItemClicked(rt: rtModel) {
                showDeleteDialog(rt)

            }
        })
        adapter.setOnUpdateCallback(object: daftarRTAdapter.OnItemClickCallback{
            override fun onItemClicked(rt: rtModel) {
                showUpdateDialog(rt)

            }
        })
        adapter.setOnDetailCallback(object: daftarRTAdapter.OnItemClickCallback{
            override fun onItemClicked(rt: rtModel) {
                showDetailDialog(rt)

            }
        })
        adapter.setOnChangePassCallback(object: daftarRTAdapter.OnItemClickCallback{
            override fun onItemClicked(rt: rtModel) {
                showChangePassDialog(rt)

            }
        })
        rview.adapter = adapter
        adapter.startListening()
    }

    private fun showChangePassDialog(rt: rtModel){
        val builder = AlertDialog.Builder(context!!)

        builder.setTitle("Ubah Password")

        val inflater = LayoutInflater.from(context!!)

        val view = inflater.inflate(R.layout.change_pass_rt, null)
        val pass = view.findViewById<EditText>(R.id.Pass)
        val repass = view.findViewById<EditText>(R.id.RePass)
        builder.setView(view)
        builder.setPositiveButton("Ubah") { dialog, which ->
            val query = FirebaseDatabase.getInstance().getReference("User")
            val Pass = pass.text.toString()

            if(pass.text.toString().equals("")){
                Toast.makeText(context!!,"Gagal ubah password, Field password kosong", Toast.LENGTH_LONG).show()
            }else if(pass.text.toString().length < 8) {
                Toast.makeText(context!!,"Gagal ubah password, Password minimal 8 karakter", Toast.LENGTH_LONG).show()
            }else if(!pass.text.toString().equals(repass.text.toString())){
                Toast.makeText(context!!,"Gagal ubah password, Password tidak sama", Toast.LENGTH_LONG).show()
            }else {

                val user = userModel(rt.id,rt.username,Pass,rt.status)

                query.child(user.id).setValue(user).addOnCompleteListener {
                    Toast.makeText(context!!,"Berhasil", Toast.LENGTH_LONG).show()
                }
            }
        }

        builder.setNegativeButton("Batal") { dialog, which ->

        }
        val alert = builder.create()
        alert.show()

    }

    private fun showDeleteDialog(rt: rtModel) {
        val builder = AlertDialog.Builder(context!!)
        val query = FirebaseDatabase.getInstance().getReference("User")

        builder.setTitle("Hapus Akun RT")

        val inflater = LayoutInflater.from(context!!)

        val view = inflater.inflate(R.layout.delete_alert_warga, null)

        builder.setView(view)



        builder.setPositiveButton("Hapus") { dialog, which ->

            ref.child(rt.id).removeValue()
            query.child(rt.id).removeValue()
            Toast.makeText(context!!,"Deleted!!", Toast.LENGTH_LONG).show()


        }
        builder.setNegativeButton("Batal") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()


    }

    private fun showDetailDialog(rt: rtModel){
        val builder = AlertDialog.Builder(context!!)
        val inflater = LayoutInflater.from(context!!)
        val view = inflater.inflate(R.layout.detail_rt, null)

        val dNama = view.findViewById<TextView>(R.id.dNama)
        val dJK = view.findViewById<TextView>(R.id.dJK)
        val dAlamat = view.findViewById<TextView>(R.id.dAlamat)
        val dDesa = view.findViewById<TextView>(R.id.dDesa)
        val dRW = view.findViewById<TextView>(R.id.dRW)
        val dRT = view.findViewById<TextView>(R.id.dRT)

        dNama.text = rt.nama
        dJK.text = rt.jk
        dAlamat.text = rt.alamat
        dDesa.text = rt.desa
        dRW.text = rt.rw
        dRT.text = rt.rt


        builder.setView(view)

        val alert = builder.create()
        alert.show()
    }

    private fun showUpdateDialog(rt: rtModel) {
        val builder = AlertDialog.Builder(context!!)

        builder.setTitle("Edit Data RT")
        val inflater = LayoutInflater.from(context!!)
        val view = inflater.inflate(R.layout.update_data_rt, null)

        val etNama = view.findViewById<EditText>(R.id.editNama)
        val etJK = view.findViewById<Spinner>(R.id.editJK)
        val etAlamat = view.findViewById<EditText>(R.id.editAlamat)

        val jk : Int
        if (rt.jk =="Laki-Laki"){
            jk = 0
        }else{
            jk = 1
        }
        etNama.setText(rt.nama)
        etJK.setSelection(jk)
        etAlamat.setText(rt.alamat)



        builder.setView(view)

        builder.setPositiveButton("Edit") { dialog, which ->

            val nama = etNama.text.toString()
            val jk = etJK.selectedItem.toString()
            val alamat = etAlamat.text.toString()

            if(etNama.text.toString().equals("")){
                Toast.makeText(context!!,"Gagal edit data, Nama kosong", Toast.LENGTH_LONG).show()
            }else if(etAlamat.text.toString().equals("")){
                Toast.makeText(context!!,"Gagal edit data, Alamat kosong", Toast.LENGTH_LONG).show()
            }else {
                val user = rtModel(rt.id,rt.desa,rt.rw,rt.rt,nama,jk,alamat,rt.username,rt.status)

                ref.child(user.id).setValue(user).addOnCompleteListener {
                    Toast.makeText(context!!,"Berhasil", Toast.LENGTH_LONG).show()
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
