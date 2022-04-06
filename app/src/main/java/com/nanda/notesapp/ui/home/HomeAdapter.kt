package com.nanda.notesapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nanda.notesapp.R
import com.nanda.notesapp.data.entity.Notes
import com.nanda.notesapp.data.entity.Priority
import com.nanda.notesapp.databinding.RowItemNotesBinding

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    val listNotes = ArrayList<Notes>()


    inner class MyViewHolder(val binding: RowItemNotesBinding) :
    RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        RowItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = listNotes[position]
        holder.binding.apply {
            mNotes = data
            executePendingBindings()
        }
        /*holder.binding.apply {
            tvTitle.text = data.title
            tvDescription.text = data.description
            tvDate.text = data.date

            val pink = ContextCompat.getColor(priorityIndicator.context, R.color.pink)
            val yellow = ContextCompat.getColor(priorityIndicator.context, R.color.yellow)
            val green = ContextCompat.getColor(priorityIndicator.context, R.color.green)

            when(data.priority){
                Priority.HIGH -> priorityIndicator.setCardBackgroundColor(pink)
                Priority.MEDIUM -> priorityIndicator.setCardBackgroundColor(yellow)
                Priority.LOW -> priorityIndicator.setCardBackgroundColor(green)
            }
        }*/
    }

    override fun getItemCount()= listNotes.size

    fun setData(data: List<Notes>?){
        if (data == null)return
            val diffCallback = DiffCallback(listNotes, data)
            val diffCallbackResult = DiffUtil.calculateDiff(diffCallback)
            listNotes.clear()
            listNotes.addAll(data)
            diffCallbackResult.dispatchUpdatesTo(this)

    }


}