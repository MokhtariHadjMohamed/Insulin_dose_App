package com.example.insulin_dose_app;

import android.util.Log
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class HorizontalAdapter(private val itemList: MutableList<List<String>>?) :
    RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {
    private var onItemClickListener: (() -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.horizontal_item_layout,
                parent,
                false
            )
            return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList?.get(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke()
        }
    }

    override fun getItemCount(): Int = itemList?.size!!
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val horizontalTextView: TextView = itemView.findViewById(R.id.horizontalTextView)
        fun bind(item: List<String>?) {
            // Check if the item is not null and not empty                        if (item != null && item.isNotEmpty()) {
            // Join the elements of the list and set it as the text                                horizontalTextView.text = item.joinToString("\n")
            Log.i("horizontal data", item.toString())
        }
    }


    // Setter للتفاعل مع النقر
    fun setOnItemClickListener(listener: () -> Unit) {
        this.onItemClickListener = listener
    }
}