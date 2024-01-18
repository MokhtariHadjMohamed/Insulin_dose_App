package com.example.insulin_dose_app.RecViews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.insulin_dose_app.PillsAndInsulin
import com.example.insulin_dose_app.R

class RecycleViewAdapterTreatment(private val mList: List<PillsAndInsulin>) :
    RecyclerView.Adapter<HolderTreatment>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderTreatment {
        // Inflate the layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicines_pills, parent, false)

        // Adding space/margin between items
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        layoutParams.bottomMargin = 16  // Adjust the value based on your spacing preference
        view.layoutParams = layoutParams

        return HolderTreatment(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: HolderTreatment, position: Int) {
        val pillsAndInsulin: PillsAndInsulin = mList[position]
        val amount = pillsAndInsulin.cereal
        val date = pillsAndInsulin.date
        val time = pillsAndInsulin.time

        // Adding spaces between different pieces of information
        holder.amount.text = "$amount حبة"
        holder.date.text = date
        holder.time.text = time

        when (pillsAndInsulin.type) {
            "Insulin" -> {
                holder.image.setImageResource(R.drawable.ibraa)
            }
            "Pill" -> {
                holder.image.setImageResource(R.drawable.adwiya)
            }
            else ->{
                holder.image.setImageResource(R.drawable.adwiya)
            }
        }
    }
}
