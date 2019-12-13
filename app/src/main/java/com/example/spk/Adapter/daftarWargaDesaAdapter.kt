package com.example.spk.Adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.spk.Model.hasilModel
import com.example.spk.Model.userModel
import com.example.spk.Model.wargaModel
import com.example.spk.R
import com.example.spk.desaActivity
import com.example.spk.rtActivity
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class daftarWargaDesaAdapter(options: FirebaseRecyclerOptions<wargaModel>) :
    FirebaseRecyclerAdapter<wargaModel, daftarWargaDesaAdapter.wargaModelViewHolder>(options)
{
    lateinit var onDetail : OnItemClickCallback



    fun setOnDetailCallback(onItemClickCallback: OnItemClickCallback){
        this.onDetail = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): wargaModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listwargadesa,parent,false)
        return wargaModelViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: wargaModelViewHolder, posisi: Int, warga: wargaModel) {

        viewHolder.textNIK.text = warga.nik
        //viewHolder.infoWarga.setCardBackgroundColor(Color.RED)
        viewHolder.textNama.text = warga.nama
        viewHolder.textGambar.text = inisial(warga.nama)

        if(warga.rekomendasi==1){
            viewHolder.textRekomendasi.text = "Direkomendasi"
            viewHolder.backRekomendasi.setCardBackgroundColor(Color.rgb(57, 219, 81))
        }else if(warga.rekomendasi==2){
            viewHolder.textRekomendasi.text = "Tidak Direkomendasi"
            viewHolder.backRekomendasi.setCardBackgroundColor(Color.rgb(219, 57, 57))

        }

        viewHolder.info_detail.setOnClickListener { onDetail.onItemClicked(warga) }
    }

    private fun inisial(nama:String): String {
        var ininama = nama.split(' ').mapNotNull { it.firstOrNull()?.toString() }.reduce { acc, s -> acc+s  }
        var panjang :Int = ininama.length
        if (panjang >= 2) {
            ininama = ininama.substring(0,2).toUpperCase()
        }else{
            ininama = ininama.substring(0,1).toUpperCase()
        }
        return ininama
    }

    inner class wargaModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textNIK = itemView.findViewById<TextView>(R.id.textNIK)
        val textNama = itemView.findViewById<TextView>(R.id.textNama)
        val textGambar = itemView.findViewById<TextView>(R.id.gambar)
        //val infoWarga = itemView.findViewById<CardView>(R.id.info_warga)
        val textRekomendasi = itemView.findViewById<TextView>(R.id.idrekomendasi)
        val backRekomendasi = itemView.findViewById<CardView>(R.id.backrekomendasi)
        val info_detail = itemView.findViewById<CardView>(R.id.info_warga)
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: wargaModel)
    }
}