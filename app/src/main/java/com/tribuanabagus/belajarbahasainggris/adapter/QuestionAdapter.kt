package com.tribuanabagus.belajarbahasainggris.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tribuanabagus.belajarbahasainggris.databinding.ItemListDmateryBinding
import com.tribuanabagus.belajarbahasainggris.model.questions.Question

class QuestionAdapter : RecyclerView.Adapter<QuestionAdapter.MateryViewHolder>() {
    private var listQuestion= ArrayList<Question>()

    private val TAG = QuestionAdapter::class.simpleName

    private var onItemBtnDeleteCallBack: OnItemBtnDeleteClickCallBack? = null
    private var onItemBtnEditCallBack: OnItemBtnEditClickCallBack? = null

    interface OnItemBtnDeleteClickCallBack {
        fun onDeleteClicked(position: Int,question: Question)
    }

    interface OnItemBtnEditClickCallBack {
        fun onEditClicked(question: Question)
    }

    fun setOnItemBtnDeleteCallBack(onItemBtnDeleteCallBack: OnItemBtnDeleteClickCallBack) {
        this.onItemBtnDeleteCallBack = onItemBtnDeleteCallBack
    }
    fun setOnItemBtnEditCallBack(onItemBtnEditCallBack: OnItemBtnEditClickCallBack) {
        this.onItemBtnEditCallBack = onItemBtnEditCallBack
    }

    fun setData(data: List<Question>?) {
        if (data == null) return
        listQuestion.clear()
        listQuestion.addAll(data)
        notifyDataSetChanged()

        Log.d(TAG, "setData: $listQuestion")
    }

    fun removeData(position: Int){
        listQuestion.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateryViewHolder {
        val binding =
            ItemListDmateryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MateryViewHolder(binding)    }

    override fun onBindViewHolder(holder: MateryViewHolder, position: Int) {
        holder.bind(listQuestion[position])
    }

    override fun getItemCount() = listQuestion.size

    fun setFilteredList(filteredList: ArrayList<Question>) {
        listQuestion = filteredList
        notifyDataSetChanged()

        Log.d(TAG, "filtered list: $filteredList")
    }

    inner class MateryViewHolder(private val binding: ItemListDmateryBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(question: Question) {
            with(binding) {
                tvNameMatery.text = question.teksJawaban
                btnDelete.setOnClickListener{onItemBtnDeleteCallBack?.onDeleteClicked(bindingAdapterPosition,question)}
                btnEdit.setOnClickListener{onItemBtnEditCallBack?.onEditClicked(question)}
            }
        }

    }

}