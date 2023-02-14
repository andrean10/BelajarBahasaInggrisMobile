package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.hapalan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.ItemHafalanBinding
import com.tribuanabagus.belajarbahasainggris.model.hafalan.ResultsHafalan

class ItemHafalanAdapter : RecyclerView.Adapter<ItemHafalanAdapter.ItemHafalanViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val data = arrayListOf<ResultsHafalan>()

    fun setData(listData: List<ResultsHafalan>?) {
        if (listData == null) return
        data.clear()
        data.addAll(listData)
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
        fun bind(data: ResultsHafalan) {
            with(binding) {
                Glide.with(itemView)
                    .load(R.drawable.img_placeholder_hafalan)
                    .into(imgTutorialHafalan)
                tvTitleHafalan.text = data.title
                tvDescHafalan.text = data.subtitle
            }

            itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(data) }
            itemView.setOnLongClickListener {
                onItemClickCallBack?.onItemLongClicked(data)
                true
            }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: ResultsHafalan)
        fun onItemLongClicked(data: ResultsHafalan)
    }
}
