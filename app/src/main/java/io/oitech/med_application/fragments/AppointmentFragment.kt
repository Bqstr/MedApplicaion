package io.oitech.med_application.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.oitech.med_application.AppointmentScreen
import io.oitech.med_application.R
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppointmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AppointmentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_appointment, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appointment = arguments?.getParcelable<HomeDoctorUiItem?>("doctorAppointment")

        val composeView = view.findViewById<ComposeView>(R.id.appointment_compose_view)

//        val navigateToAppointment:(HomeDoctorUiItem) ->Unit ={doctor ->
//            val bundle = Bundle()
//            bundle.putParcelable("doctorAppointment", doctor)
//            findNavController().navigate(R.id.action_homeFragment_to_doctorDetailFragment, bundle)
//
//        }
        composeView.setContent {
            val date  =appointment?.listOfTimes?.firstOrNull()?.listOfDates?.firstOrNull()?.dateTime
            if(appointment!=null && date!=null) {
                AppointmentScreen(doctor = appointment, navigateToChatWithDoctor = {

                }, navigateBacktoHome = {
                    findNavController().navigate(
                        R.id.homeFragment, // Replace with your destination fragment ID
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.nav_host_fragment, inclusive = true) // Clears the entire back stack
                            .setLaunchSingleTop(true)
                            .build()
                    )
                }, selectedTimeAndDate = date , navigateBack = {
                    findNavController().popBackStack()
                }, viewModel = viewModel)
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AppointmentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppointmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}