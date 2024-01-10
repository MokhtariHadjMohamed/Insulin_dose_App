package com.example.insulin_dose_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class HomeActivity : AppCompatActivity() {

    private lateinit var navigation: MeowBottomNavigation // Declare the MeowBottomNavigation variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navigation = findViewById(R.id.navigation)
        setFragment(HomeFragment.newIntance())
        navigation.add(MeowBottomNavigation.Model(1, R.drawable.homeicon)) // Add items to the navigation bar
        navigation.add(MeowBottomNavigation.Model(2, R.drawable.calendaricon)) // Add items to the navigation bar
        navigation.add(MeowBottomNavigation.Model(3, R.drawable.reporticon)) // Add items to the navigation bar
        navigation.add(MeowBottomNavigation.Model(4, R.drawable.accounticon)) // Add items to the navigation bar
// Add more items if needed...

        navigation.setOnClickMenuListener {
            when(it.id){
                1->setFragment(HomeFragment.newIntance())
                2->setFragment(CalendarFragment.newInstance())
                3->setFragment(ReportFragment.newIntance())
                4->setFragment(profileFragment.newIntance())

                else->""
            }
        }
        navigation.show(1)
    }
    fun  setFragment(fragment:Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout,fragment,"HomeActivity")
            .commit()
    }
}
