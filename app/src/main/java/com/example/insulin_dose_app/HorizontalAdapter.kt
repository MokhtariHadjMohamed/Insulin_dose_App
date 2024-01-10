package com.example.insulin_dose_app;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HorizontalAdapter(private val itemList: List<String>) :
        RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {

        private var onItemClickListener: (() -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.horizontal_item_layout, parent, false)
                return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val item = itemList[position]
                holder.bind(item)
                holder.itemView.setOnClickListener {
                        onItemClickListener?.invoke()
                }
        }

        override fun getItemCount(): Int = itemList.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                private val horizontalTextView: TextView = itemView.findViewById(R.id.horizontalTextView)

                fun bind(item: String) {
                        horizontalTextView.text = item
                }
        }

        // Setter للتفاعل مع النقر
        fun setOnItemClickListener(listener: () -> Unit) {
                this.onItemClickListener = listener
        }
}


