package com.example.insulin_dose_app


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ReportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false)
    }
    companion object{
        fun  newIntance(): ReportFragment{
            return ReportFragment()
        }
    }
    // Other methods and logic for the fragment can be added here
}
