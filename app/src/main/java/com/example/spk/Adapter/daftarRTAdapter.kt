package com.example.spk.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.spk.Model.rtModel
import com.example.spk.Model.wargaModel
import com.example.spk.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class daftarRTAdapter (options: FirebaseRecyclerOptions<rtModel>) :
    FirebaseRecyclerAdapter<rtModel, daftarRTAdapter.rtModelViewHolder>(options)
{
    lateinit var onUpdate: OnItemClickCallback
    lateinit var onDelete : OnItemClickCallback
    lateinit var onDetail : OnItemClickCallback
    lateinit var onChangePass : OnItemClickCallback

    fun setOnUpdateCallback(onItemClickCallback: OnItemClickCallback){
        this.onUpdate = onItemClickCallback
    }

    fun setOnDeleteCallback(onItemClickCallback: OnItemClickCallback){
        this.onDelete = onItemClickCallback
    }

    fun setOnDetailCallback(onItemClickCallback: OnItemClickCallback){
        this.onDetail = onItemClickCallback
    }
    fun setOnChangePassCallback(onItemClickCallback: OnItemClickCallback){
        this.onChangePass = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rtModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_rt,parent,false)
        return rtModelViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: rtModelViewHolder, posisi: Int, rt: rtModel) {
        viewHolder.textRT.text = "RT "+rt.rt
        viewHolder.textNama.text = rt.nama
        viewHolder.textGambar.text = inisial(rt.nama)

        viewHolder.tbl_update.setOnClickListener { onUpdate.onItemClicked(rt) }
        viewHolder.tbl_delete.setOnClickListener { onDelete.onItemClicked(rt) }
        viewHolder.info_detail.setOnClickListener { onDetail.onItemClicked(rt) }
        viewHolder.tbl_changePass.setOnClickListener { onChangePass.onItemClicked(rt) }
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

    inner class rtModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textRT = itemView.findViewById<TextView>(R.id.textRT)
        val textNama = itemView.findViewById<TextView>(R.id.textNama)
        val textGambar = itemView.findViewById<TextView>(R.id.gambar)

        val tbl_update = itemView.findViewById<Button>(R.id.btn_update)
        val tbl_delete = itemView.findViewById<Button>(R.id.btn_delete)
        val info_detail = itemView.findViewById<CardView>(R.id.info_rt)
        val tbl_changePass = itemView.findViewById<Button>(R.id.btn_changepass)
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: rtModel)
    }
}