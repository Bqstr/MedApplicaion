package io.oitech.med_application.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.oitech.med_application.DoctorDetailsScreen
import io.oitech.med_application.R
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DoctorDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DoctorDetailFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val doctor = arguments?.getParcelable<HomeDoctorUiItem?>("doctorDetails")
        val composeView = view.findViewById<ComposeView>(R.id.doctor_detail_compose_view)

        val navigateToAppointment:(HomeDoctorUiItem) ->Unit ={doctor ->
            val bundle = Bundle()
            bundle.putParcelable("doctorAppointment", doctor)
            findNavController().navigate(R.id.action_doctorDetailFragment_to_appointmentFragment, bundle)

        }
        val navigateBack ={
            findNavController().popBackStack()

        }
        val navigateToChatToDoctor :(Int) ->Unit ={
            val bundle = Bundle().apply {
                putInt("DOCTOR_ID_PARAM", it)
            }
            findNavController().navigate(R.id.action_doctorDetailFragment_to_chatFragment2, bundle)
        }



        composeView.setContent {
            if(doctor!=null) {
                DoctorDetailsScreen(doctor = doctor,navigateToAppointment,navigateBack =navigateBack, navigateToChat = {
                    navigateToChatToDoctor.invoke(it)
                })
            }
        }
    }

    companion object {
        fun newInstance(doctorId: Int): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle()
            args.putInt("DOCTOR_ID_PARAM", doctorId)
            fragment.arguments = args
            return fragment
        }
    }
}