package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.percakapan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.ItemPercakapanBinding
import com.tribuanabagus.belajarbahasainggris.model.percakapan.ResultsPercakapan

class ItemPercakapanAdapter :
    RecyclerView.Adapter<ItemPercakapanAdapter.ItemPercakapanViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val data = arrayListOf<ResultsPercakapan>()

    fun setData(listData: List<ResultsPercakapan>?) {
        if (listData == null) return
        data.clear()
        data.addAll(listData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemPercakapanViewHolder {
        val binding =
            ItemPercakapanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemPercakapanViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemPercakapanViewHolder, position: Int) {
        viewHolder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class ItemPercakapanViewHolder(private val binding: ItemPercakapanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResultsPercakapan) {
            with(binding) {
                Glide.with(itemView)
                    .load(R.drawable.img_placeholder_hafalan)
                    .into(imgTutorialPercakapan)
                tvTitlePercakapan.text = data.title
                tvDescPercakapan.text = data.subtitle
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
        fun onItemClicked(data: ResultsPercakapan)
        fun onItemLongClicked(data: ResultsPercakapan)
    }
}
