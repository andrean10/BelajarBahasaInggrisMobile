package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import com.tribuanabagus.belajarbahasainggris.R
import com.tribuanabagus.belajarbahasainggris.databinding.ItemSliderBinding


class HomeSliderAdapter : SliderViewAdapter<HomeSliderAdapter.HomeSliderViewHolder>() {
    private val sliderItems = arrayListOf<Int>()

    fun setData(banner: List<Int>) {
        if (banner == null) return
        sliderItems.clear()
        sliderItems.addAll(banner)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?): HomeSliderViewHolder {
        val binding =
            ItemSliderBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        return HomeSliderViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: HomeSliderViewHolder, position: Int) {
        viewHolder.bind(sliderItems[position])
    }

    override fun getCount() = sliderItems.size

    inner class HomeSliderViewHolder(private val binding: ItemSliderBinding) :
        SliderViewAdapter.ViewHolder(binding.root) {
        fun bind(img: Int) {
            Glide.with(itemView.context)
                .asBitmap()
                .load(img)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .fallback(R.drawable.img_error)
                .into(binding.imgSlider)
        }
    }
}