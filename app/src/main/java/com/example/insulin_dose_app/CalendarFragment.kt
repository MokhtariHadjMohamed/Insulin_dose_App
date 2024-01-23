package com.example.insulin_dose_app;

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insulin_dose_app.R.id.verticalRecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class CalendarFragment : Fragment() {
    private lateinit var HOR_RECYCLER: RecyclerView
    private lateinit var VERT_RECYCLER: RecyclerView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val dateMap = mutableMapOf<String, MutableList<List<String>>>()
    private lateinit var horizontalAdapter: HorizontalAdapter
    private lateinit var verticalAdapter: ClanderAdapter
    private var selectedDate: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calender, container, false)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        VERT_RECYCLER = view.findViewById(R.id.horizontalRecyclerView)
        VERT_RECYCLER.layoutManager = LinearLayoutManager(requireContext())
        HOR_RECYCLER = view.findViewById(R.id.verticalRecyclerView)
        HOR_RECYCLER.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val userId = getUserID()
// Use the retrieved userID to fetch sports data
        fetchSportsData(userId.toString())
        return view
    }

    companion object {
        fun newIntance(): CalendarFragment {
            return CalendarFragment()
        }
    }

    private fun fetchSportsData(userId: String) {
        val measurementsCollection =
            firestore.collection("Measurements").document(userId).collection("userMeasurements")
        val mealsCollection = firestore.collection("Meals")
        val sportsCollection = firestore.collection("Sport")
        fetchData(measurementsCollection, listOf("sugarLevelValue"), "SugarLevel")
        fetchData(mealsCollection, listOf("food", "mealType"), "Meal")
        fetchData(sportsCollection, listOf("sport"), "Sport")
    }

    private fun fetchData(
        collection: CollectionReference,
        field: List<String>,
        entryType: String
    ) {
        collection.whereEqualTo("uid", auth.currentUser?.uid)
            .get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val date = document.getString("date") ?: ""
                    val time = document.getString("time") ?: ""
                    val entries = document.data // Use document.data to get all fields
                    val mealType = entries["mealType"] as? String ?: ""
                    val food = entries["food"] as? String ?: ""
                    val sugarLevel = entries["sugarLevelValue"] as? String ?: ""
                    val sport = entries["sport"] as? String ?: ""
                    // Check if any of the required fields is empty or null
                    val entryStringBuilder = StringBuilder("$time\n")
                    if (mealType.isNotEmpty()) {
                        entryStringBuilder.append("نوع الأكل : $mealType\n")
                    }
                    if (food.isNotEmpty()) {
                        entryStringBuilder.append("السعرات الحرارية في الاكلة : $food\n")
                    }
                    if (sugarLevel.isNotEmpty()) {
                        entryStringBuilder.append("قيمة السكر في الدم : $sugarLevel\n")
                    }
                    if (sport.isNotEmpty()) {
                        entryStringBuilder.append("مدة ممارسة الرياضة : $sport\n")
                    }
                    val entryString = entryStringBuilder.toString().trim()
                    if (dateMap.containsKey(date)) {
                        val list =
                            dateMap[date]?.toMutableList() ?: mutableListOf()
                        list.add(
                            listOf(
                                entryString
                            )
                        )
                        dateMap[date] = list
                    } else {
                        dateMap[date] = listOf(mutableListOf(entryString)).toMutableList()
                    }
                }
                setupRecyclerView()
            }


            .addOnFailureListener { exception ->
                Log.e(
                    "CalendarFragment",
                    "Error fetching $field documents: $exception"
                )                // Handle errors
            }
    }

    private fun setupRecyclerView() {
        val dates = dateMap.keys.toList()
        verticalAdapter = ClanderAdapter(dates) { position ->
            Log.d("CalendarFragment", "Clicked on position $position")
            selectedDate = dates[position]
            showHorizontalList(selectedDate)
        }
        HOR_RECYCLER.adapter = verticalAdapter
        verticalAdapter.notifyDataSetChanged()
    }

    private fun showHorizontalList(selectedDate: String?) {
        if (selectedDate != null) {
            val horizontalItemList = dateMap[selectedDate]
            if (horizontalItemList != null) {
                // Now, horizontalItemList is a list of lists containing values
                // Use it as needed
                // Example: horizontalItemList[0][0] gives the first value of the first entry
                // If you need to print the values, you can do something like this:
                for (item in horizontalItemList) {
                    Log.d("CalendarFragment", "Values: $item")
                }
                // You can still use HorizontalAdapter with horizontalItemList if needed
                horizontalAdapter = HorizontalAdapter(horizontalItemList)
                VERT_RECYCLER.adapter = horizontalAdapter
                horizontalAdapter.notifyDataSetChanged()
                VERT_RECYCLER.visibility = View.VISIBLE
            }
        }
    }

    fun getUserID(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid
    }

    data class CalendarEntry(
        val date: String,
        val time: String,
        val mealType: String,
        val food: String,
        val sugarLevel: String,
        val sport: String
    )
}

