package io.oitech.med_application.fragments.doctor_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.oitech.med_application.R
import io.oitech.med_application.fragments.MainViewModel
import io.oitech.med_application.fragments.chat.ChatFragment
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [DoctorDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DoctorDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters

    val viewModel: MainViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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


        Log.d("asdfasdfasdfasfd", "abobus")
        val doctor = arguments?.getParcelable<HomeDoctorUiItem?>("doctorDetails")
        Log.d("asdfasdfasdfasfd", doctor.toString())

        val composeView = view.findViewById<ComposeView>(R.id.doctor_detail_compose_view)

        val setFavoriteDoctor :(Boolean) ->Unit={
            if(doctor!=null) {
                viewModel.setFavoriteDoctor(doctorId = doctor.id, setFavorite =it)
            }
        }


        val navigateToAppointment: (HomeDoctorUiItem) -> Unit = { doctor ->
            val bundle = Bundle()
            bundle.putParcelable("doctorAppointment", doctor)
            findNavController().navigate(
                R.id.action_doctorDetailFragment_to_appointmentFragment,
                bundle
            )

        }
        val navigateBack: () -> Unit = {
            findNavController().popBackStack()

        }
        val navigateToChatToDoctor: (Int) -> Unit = {
            val bundle = Bundle().apply {
                putInt("DOCTOR_ID_PARAM", it)
            }
            findNavController().navigate(R.id.action_doctorDetailFragment_to_chatFragment2, bundle)

        }

        

        composeView.setContent {
            if (doctor != null) {
                DoctorDetailsScreen(doctor = doctor,navigateToAppointment,navigateBack =navigateBack, navigateToChat = {
                    navigateToChatToDoctor.invoke(it)
                }, setFavoriteDoctor)
//
            }
        }
    }

    override fun onResume() {
        super.onResume()
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