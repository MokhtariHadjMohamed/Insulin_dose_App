package com.example.insulin_dose_app;
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insulin_dose_app.R.id.verticalRecyclerView

class CalendarFragment : Fragment() {
    private lateinit var verticalRecyclerView: RecyclerView
    private lateinit var horizontalRecyclerView: RecyclerView
    private val itemList = mutableListOf("Content 1", "Content 2", "Content 3", "Content 4", "Content 5")
    private val verticalLists = listOf(
        listOf("A", "B", "C", "D", "E"),
        listOf("F", "G", "H", "I", "J"),
        listOf("K", "L", "M", "N", "O"),
        listOf("P", "Q", "R", "S", "T"),
        listOf("U", "V", "W", "X", "Y")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calender, container, false)

        verticalRecyclerView = view.findViewById(R.id.verticalRecyclerView)
        verticalRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val verticalAdapter = ClanderAdapter(itemList) { position ->
            showHorizontalList(verticalLists[position])
        }
        verticalRecyclerView.adapter = verticalAdapter

        horizontalRecyclerView = view.findViewById(R.id.horizontalRecyclerView)
        horizontalRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        showHorizontalList(verticalLists[0])
        return view
    }

    private fun showHorizontalList(horizontalItemList: List<String>) {
        val horizontalAdapter = HorizontalAdapter(horizontalItemList)
        horizontalRecyclerView.adapter = horizontalAdapter
        horizontalRecyclerView.visibility = View.VISIBLE
       // verticalRecyclerView.visibility = View.GONE
    }

    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }

    // Other methods and logic for the fragment can be added here
}



// Other methods and logic for the fragment can be added here

