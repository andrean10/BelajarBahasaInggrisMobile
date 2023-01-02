package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.hapalan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.ItemHafalanBinding
import com.tribuanabagus.belajarbahasainggris.local_db.Hafalan

class ItemHafalanAdapter : RecyclerView.Adapter<ItemHafalanAdapter.ItemHafalanViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val data = arrayListOf<Hafalan>()

    fun setData(listVA: List<Hafalan>?) {
        if (listVA == null) return
        data.clear()
        data.addAll(listVA)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemHafalanViewHolder {
        val binding =
            ItemHafalanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHafalanViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemHafalanViewHolder, position: Int) {
        viewHolder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class ItemHafalanViewHolder(private val binding: ItemHafalanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Hafalan) {
            with(binding) {
                Glide.with(itemView)
                    .load(data.img)
                    .placeholder(R.drawable.img_placeholder_hafalan)
                    .error(if (data.img != null) R.drawable.img_error else R.drawable.img_placeholder_hafalan)
                    .into(imgTutorialHafalan)
                tvTitleHafalan.text = data.title
                tvDescHafalan.text = data.description
            }

            itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(data) }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: Hafalan)
    }
}
