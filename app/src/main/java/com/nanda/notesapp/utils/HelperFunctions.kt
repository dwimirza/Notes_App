package com.nanda.notesapp.utils

import android.content.Context
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.nanda.notesapp.R
import com.nanda.notesapp.data.entity.Notes
import com.nanda.notesapp.data.entity.Priority

object HelperFunctions {
    fun  spinnerListener(context : Context?, priorityIndicator : CardView):AdapterView.OnItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            context?.let {
                when(position){
                    0 -> {
                        //for chosing color
                        val pink = ContextCompat.getColor(it, R.color.pink)
                        //set background color
                        priorityIndicator.setCardBackgroundColor(pink)
                    }
                    1 -> {
                        val yellow = ContextCompat.getColor(it, R.color.yellow)
                        priorityIndicator.setCardBackgroundColor(yellow)
                    }
                    2 -> {
                        val green = ContextCompat.getColor(it, R.color.green)
                        priorityIndicator.setCardBackgroundColor(green)
                    }
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }

    fun parseToPriority(priority: String, context: Context?): Priority {
        val expectedPriority = context?.resources?.getStringArray(R.array.priorities)
        return when (priority) {
            expectedPriority?.get(0) -> Priority.HIGH
            expectedPriority?.get(1) -> Priority.MEDIUM
            expectedPriority?.get(2) -> Priority.LOW
            else -> Priority.HIGH
        }
    }

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(true)
    fun checkIsDataEmpty(data: List<Notes>) {
       emptyDatabase.value = data.isEmpty()
    }
}