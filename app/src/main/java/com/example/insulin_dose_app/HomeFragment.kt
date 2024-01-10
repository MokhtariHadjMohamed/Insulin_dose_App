import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.insulin_dose_app.AboutActivity
import com.example.insulin_dose_app.AccountActivity
import com.example.insulin_dose_app.FoodsActivity
import com.example.insulin_dose_app.Mesurer_la_glycemieActivity
import com.example.insulin_dose_app.R
import com.example.insulin_dose_app.SettingsActivity
import com.example.insulin_dose_app.SportActivity
import com.example.insulin_dose_app.pharmaceuticalActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find the button by its ID
        val buttonNavigate = view.findViewById<CardView>(R.id.cardView9)

        // Set click listener for the button
        buttonNavigate.setOnClickListener {
            // Create an Intent to navigate to another activity
            val intent = Intent(activity, Mesurer_la_glycemieActivity::class.java)
            // Optionally, you can pass data to the next activity using intent.putExtra()
            // intent.putExtra("key", "value")
            // Start the new activity
            startActivity(intent)
        }

        val buttonFoods = view.findViewById<CardView>(R.id.cardView8)
        // Set click listener for the button
        buttonFoods.setOnClickListener {
            // Create an Intent to navigate to another activity
            val intent = Intent(activity, FoodsActivity::class.java)
            // Optionally, you can pass data to the next activity using intent.putExtra()
            // intent.putExtra("key", "value")
            // Start the new activity
            startActivity(intent)
        }

        // Bouton "navbar" et dialogue
        val navbar = view.findViewById<ImageView>(R.id.imageView15)
        navbar.setOnClickListener {
            showCustomDialog()
        }

        val buttonSport = view.findViewById<CardView>(R.id.cardView1)
        // Set click listener for the button
        buttonSport.setOnClickListener {
            // Create an Intent to navigate to another activity
            val intent = Intent(activity, SportActivity::class.java)
            // Optionally, you can pass data to the next activity using intent.putExtra()
            // intent.putExtra("key", "value")
            // Start the new activity
            startActivity(intent)
        }

        val buttonPharmaceutical = view.findViewById<CardView>(R.id.cardView0)
        // Set click listener for the button
        buttonPharmaceutical.setOnClickListener {
            // Create an Intent to navigate to another activity
            val intent = Intent(activity, pharmaceuticalActivity::class.java)
            // Optionally, you can pass data to the next activity using intent.putExtra()
            // intent.putExtra("key", "value")
            // Start the new activity
            startActivity(intent)
        }

        return view
    }

    companion object {
        fun newIntance(): HomeFragment {
            return HomeFragment()
        }
    }

    private fun showCustomDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.navbar, null)
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Find the TextViews inside the dialog layout
        val textViewEditAccount = dialogBinding.findViewById<TextView>(R.id.textView53)
        val textViewSettings = dialogBinding.findViewById<TextView>(R.id.textView55)
        val textViewAboutApp = dialogBinding.findViewById<TextView>(R.id.textView56)
        val textViewLogout = dialogBinding.findViewById<TextView>(R.id.textView54)

        // Set click listeners for each TextView
        textViewEditAccount.setOnClickListener {
            // Handle the click, navigate to the corresponding activity
            val intent = Intent(activity, AccountActivity::class.java)
            startActivity(intent)
            myDialog.dismiss() // Dismiss the dialog after navigating
        }

        textViewSettings.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
            myDialog.dismiss() // Dismiss the dialog after navigating
        }

        textViewAboutApp.setOnClickListener {
            val intent = Intent(activity, AboutActivity::class.java)
            startActivity(intent)
            myDialog.dismiss() // Dismiss the dialog after navigating
        }

        textViewLogout.setOnClickListener {
            val dialogBindingLogout = layoutInflater.inflate(R.layout.dialogue_logout, null)
            val myDialogLogout = Dialog(requireContext())
            myDialogLogout.setContentView(dialogBindingLogout)
            myDialogLogout.setCancelable(true)
            myDialogLogout.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialogLogout.show() // Show the logout dialog
            myDialog.dismiss() // Dismiss the main dialog after navigating to logout dialog
        }

        myDialog.show()
    }

    // Other methods and logic for the fragment can be added here

    // Add this function to handle button click
    fun onButtonClick(view: View) {
        // Handle button click here, if needed
    }
}
