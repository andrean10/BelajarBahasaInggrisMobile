package com.tribuanabagus.belajarbahasainggris.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.ItemListSiswaBinding
import com.tribuanabagus.belajarbahasainggris.model.users.ResultsUsers
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.SiswaViewHolder>() {

    private var listSiswa = ArrayList<ResultsUsers>()
    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setData(listData: List<ResultsUsers>?) {
        if (listData == null) return
        listSiswa.clear()
        listSiswa.addAll(listData)
        notifyDataSetChanged()
    }

    fun setFilteredList(filteredList: ArrayList<ResultsUsers>) {
        listSiswa = filteredList
        notifyDataSetChanged()
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiswaViewHolder {
        val binding =
            ItemListSiswaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SiswaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SiswaViewHolder, position: Int) {
        holder.bind(listSiswa[position])
    }

    override fun getItemCount() = listSiswa.size

    inner class SiswaViewHolder(private val binding: ItemListSiswaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(studentsResult: ResultsUsers) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(ApiConfig.URL_IMAGES + studentsResult.gambar)
                    .error(R.drawable.no_profile_images)
                    .into(imgUser)
                tvName.text = studentsResult.nama

                itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(studentsResult) }
            }
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: ResultsUsers)
    }
}