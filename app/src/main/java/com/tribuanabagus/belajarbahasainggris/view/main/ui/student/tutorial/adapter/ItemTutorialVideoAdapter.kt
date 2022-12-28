package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.tutorial.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tribuanabagus.belajarbahasainggris.databinding.ItemTutorialVideoBinding
import com.tribuanabagus.belajarbahasainggris.local_db.VideoPembelajaran

class ItemTutorialVideoAdapter :
    RecyclerView.Adapter<ItemTutorialVideoAdapter.ItemTutorialVideoViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val data = arrayListOf<VideoPembelajaran>()


    fun setData(listVA: List<VideoPembelajaran>?) {
        if (listVA == null) return
        data.clear()
        data.addAll(listVA)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemTutorialVideoViewHolder {
        val binding =
            ItemTutorialVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemTutorialVideoViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemTutorialVideoViewHolder, position: Int) {
        viewHolder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class ItemTutorialVideoViewHolder(private val binding: ItemTutorialVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: VideoPembelajaran) {
            with(binding) {
                tvTitleVideo.text = data.title
                tvDescVideo.text = data.description
            }

            itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(data) }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: VideoPembelajaran)
    }
}
