package com.example.insulin_dose_app.RecViews

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.insulin_dose_app.R

class HolderTreatment(itemView : View): RecyclerView.ViewHolder(itemView) {
    val time:TextView = itemView.findViewById(R.id.houersItemMedicines)
    val date:TextView = itemView.findViewById(R.id.dateItemMedicines)
    val image:ImageView = itemView.findViewById(R.id.imageViewItemMedicines)
    val amount:TextView = itemView.findViewById(R.id.amountItemMedicines)
}