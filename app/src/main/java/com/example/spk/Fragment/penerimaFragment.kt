package com.example.spk.Fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spk.Adapter.daftarWargaDesaAdapter
import com.example.spk.Model.wargaModel
import com.example.spk.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class penerimaFragment : Fragment() {

    lateinit var ref : DatabaseReference
    lateinit var query : Query
    lateinit var adapter : daftarWargaDesaAdapter
    lateinit var rview : RecyclerView
    lateinit var SP: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        SP = this.activity!!.getSharedPreferences("Login", Context.MODE_PRIVATE)
        val view : View = inflater.inflate(R.layout.fragment_penerima, container, false)
        ref = FirebaseDatabase.getInstance().getReference("Warga")
        query = ref.orderByChild("rekomendasi")
        val search = view.findViewById<Button>(R.id.btn_cari)
        val evsearch = view.findViewById<EditText>(R.id.textSearch)
        rview = view.findViewById(R.id.rView)
        rview.setHasFixedSize(true)
        rview.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        showWarga("")
        search.setOnClickListener {
            showWarga(evsearch.text.toString())
        }
        return view
    }

    private fun showWarga(cari: String) {
        if(cari.isNullOrEmpty()){
            query = ref.orderByChild("rekomendasi")
        }else{
            query = ref.orderByChild("nama").startAt(cari).endAt(cari+"\uf8ff")
        }
        val option = FirebaseRecyclerOptions.Builder<wargaModel>()
            .setQuery(query, wargaModel::class.java).build()


        adapter = daftarWargaDesaAdapter(option)
        adapter.setOnDetailCallback(object: daftarWargaDesaAdapter.OnItemClickCallback{
            override fun onItemClicked(warga: wargaModel) {
                showDetailDialog(warga)

            }
        })
        rview.adapter = adapter
        adapter.startListening()
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



    override fun onStart() {
        super.onStart()
        if(adapter != null)
            adapter.startListening()
    }

}
