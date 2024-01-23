package com.example.insulin_dose_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClanderAdapter(
    private val itemList: List<String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ClanderAdapter.ViewHolder>() {
    // Property to keep track of the currently pressed position
    private var selectedPosition: Int = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ver_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
        // Update the background drawable based on the pressed state
        holder.itemView.setBackgroundResource(R.drawable.ver_item_selector)
        holder.itemView.isPressed = (position == selectedPosition)
        // Update the layout parameters for the pressed item
        val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
        val resources = holder.itemView.context.resources
        // Set the height and width dimensions
        layoutParams.height = if (position == selectedPosition) {
            resources.getDimensionPixelSize(R.dimen.selected_height)
        } else {
            resources.getDimensionPixelSize(R.dimen.default_height)
        }
        layoutParams.width = resources.getDimensionPixelSize(R.dimen.default_width)
        // Set the right margin to create spacing between items
        layoutParams.rightMargin = resources.getDimensionPixelSize(R.dimen.item_margin)
        holder.itemView.layoutParams = layoutParams
        holder.itemView.setOnClickListener {
            // Update the selected position and notify data set changed
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onItemClick(selectedPosition)
        }
    }

    override fun getItemCount(): Int = itemList.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textViewNumber)
        fun bind(item: String) {
            textView.text = item
        }
    }
}