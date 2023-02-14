package com.tribuanabagus.belajarbahasainggris.view.main.ui.teacher.nilai.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tribuanabagus.belajarbahasainggris.databinding.ItemNilaiBinding
import com.tribuanabagus.belajarbahasainggris.model.nilai.belajar.ResultsNilaiBelajar
import com.tribuanabagus.belajarbahasainggris.model.nilai.permainan.ResultsNilaiPermainan

class ItemNilaiAdapter : RecyclerView.Adapter<ItemNilaiAdapter.ItemNilaiViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private var data = arrayListOf<Any>()

    fun setData(listData: List<Any>?) {
        if (listData == null) return
        data.clear()
        data.addAll(listData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemNilaiViewHolder {
        val binding =
            ItemNilaiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemNilaiViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemNilaiViewHolder, position: Int) {
        viewHolder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class ItemNilaiViewHolder(private val binding: ItemNilaiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Any?) {
            if (data != null) {
                with(binding) {
                    when (data) {
                        is ResultsNilaiBelajar -> {
                            tvTitle.text = data.soal?.title
                            tvSubtitle.text = data.soal?.subtitle
                            tvNilai.text = data.nilai.toString()

                        }
                        is ResultsNilaiPermainan -> {
                            // Result Nilai Permainan
                        }
                    }
                }

                itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(data) }
            }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: Any)
    }
}
