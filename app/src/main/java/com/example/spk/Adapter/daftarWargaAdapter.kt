package com.example.spk.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.spk.Model.wargaModel
import com.example.spk.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class daftarWargaAdapter (options: FirebaseRecyclerOptions<wargaModel>) :
    FirebaseRecyclerAdapter<wargaModel, daftarWargaAdapter.wargaModelViewHolder>(options)
    {
        lateinit var onUpdate: OnItemClickCallback
        lateinit var onDelete : OnItemClickCallback
        lateinit var onDetail : OnItemClickCallback

        fun setOnUpdateCallback(onItemClickCallback: OnItemClickCallback){
            this.onUpdate = onItemClickCallback
        }

        fun setOnDeleteCallback(onItemClickCallback: OnItemClickCallback){
            this.onDelete = onItemClickCallback
        }

        fun setOnDetailCallback(onItemClickCallback: OnItemClickCallback){
            this.onDetail = onItemClickCallback
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): wargaModelViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.listwarga,parent,false)
            return wargaModelViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: wargaModelViewHolder, posisi: Int, warga: wargaModel) {
            viewHolder.textNIK.text = warga.nik
            //viewHolder.infoWarga.setCardBackgroundColor(Color.RED)
            viewHolder.textNama.text = warga.nama
            viewHolder.textGambar.text = inisial(warga.nama)

            viewHolder.tbl_update.setOnClickListener { onUpdate.onItemClicked(warga) }
            viewHolder.tbl_delete.setOnClickListener { onDelete.onItemClicked(warga) }
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
            val textRekomendasi = itemView.findViewById<TextView>(R.id.idrekomendasi)
            val backRekomendasi = itemView.findViewById<CardView>(R.id.backrekomendasi)

            val tbl_update = itemView.findViewById<Button>(R.id.btn_update)
            val tbl_delete = itemView.findViewById<Button>(R.id.btn_delete)
            val info_detail = itemView.findViewById<CardView>(R.id.info_warga)
        }

        interface OnItemClickCallback{
            fun onItemClicked(data: wargaModel)
        }
}