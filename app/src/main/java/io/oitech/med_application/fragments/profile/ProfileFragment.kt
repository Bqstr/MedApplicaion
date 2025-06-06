package io.oitech.med_application.fragments.profile

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.oitech.med_application.R
import io.oitech.med_application.fragments.MainViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.getMyProfile()
//        viewModel.profileName.observe(viewLifecycleOwner){
//            val textView = view.findViewById<TextView>(R.id.profile_name)
//            textView.text = it
//        }


        val textView = view.findViewById<TextView>(R.id.profile_name)
            textView.text = "Bekzhan"





        view.findViewById<LinearLayout>(R.id.profile_logout_button).setOnClickListener{
            showLogoutDialogAlert(requireContext())
        }








    }
    fun showLogoutDialogAlert(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.logout_dialog, null)

        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        val alertDialog = builder.create()

// Find the button inside the inflated dialog layout
        val button = view.findViewById<FrameLayout>(R.id.logout_alert_button)
        button.setOnClickListener {
            findNavController().navigate(
                R.id.loginFragment, // Replace with your destination fragment ID
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.nav_host_fragment, inclusive = true) // Clears the entire back stack
                    .setLaunchSingleTop(true)
                    .build()
            )
            alertDialog.dismiss()
            viewModel.logoutUser()

        }

        val cancelButton =view.findViewById<TextView>(R.id.cancel_logout_alert)
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }


        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }


    companion object {




        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}