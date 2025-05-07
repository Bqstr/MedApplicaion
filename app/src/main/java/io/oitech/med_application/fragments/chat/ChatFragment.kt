package io.oitech.med_application.fragments.chat

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
private const val DOCTOR_ID_PARAM = "DOCTOR_ID_PARAM"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
 class ChatFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()

    // TODO: Rename and change types of parameters

    var doctorId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            doctorId = it.getInt(DOCTOR_ID_PARAM, -1)
        }

        //viewModel.getMessageRooms()
        viewModel.getMessageRoomByDoctorId(doctorId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (doctorId >= 0) {
            viewModel.getMessagesOfOneRoom(doctorId)
        }


        var thisChatRoom: ChatRoomModel? = null







        viewModel.currentChatRoom.observe(viewLifecycleOwner){
            Log.d("sdfsdfsadfsdfasdfsdf", it.toString())

            if(it!=null) {
                view.findViewById<ComposeView>(R.id.chat_compose).setContent {
                    ChatScreen(
                        onBackPress = { findNavController().popBackStack() },
                        chatRoomModel = it,
                        viewModel = viewModel
                    )
                }
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
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
    }
}