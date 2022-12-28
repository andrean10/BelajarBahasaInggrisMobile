package com.tribuanabagus.belajarbahasainggris.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.ItemListSiswaBinding
import com.tribuanabagus.belajarbahasainggris.model.student.StudentsResult
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.SiswaViewHolder>() {

    private var listSiswa = ArrayList<StudentsResult>()
    private var onItemClickCallBack: OnItemClickCallBack? = null

    private val TAG = StudentAdapter::class.simpleName

    fun setData(StudentsResult: List<StudentsResult>?) {
        if (StudentsResult == null) return
        listSiswa.clear()
        listSiswa.addAll(StudentsResult)
        notifyDataSetChanged()

        Log.d(TAG, "setData: $listSiswa")
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

    fun setFilteredList(filteredList: ArrayList<StudentsResult>) {
        listSiswa = filteredList
        notifyDataSetChanged()

        Log.d(TAG, "filtered list: $filteredList")
    }

    inner class SiswaViewHolder(private val binding: ItemListSiswaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(studentsResult: StudentsResult) {
            with(binding) {
                tvName.text = studentsResult.nama

                Glide.with(itemView.context)
                    .load(ApiConfig.URL_IMAGE+studentsResult.gambar)
                    .error(R.drawable.no_profile_images)
                    .into(imgUser)

                itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(studentsResult) }
            }
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(StudentsResult: StudentsResult)
    }
}