package io.oitech.med_application.fragments.chatList

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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 * Use the [MessagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MessagesFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private val mainViewModel: MainViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainViewModel.getMessageRooms()


        val navigateToChatToDoctor: (Int) -> Unit = {
            val bundle = Bundle().apply {
                putInt("DOCTOR_ID_PARAM", it)
            }
            findNavController().navigate(R.id.action_messagesFragment_to_chatFragment, bundle)
        }


        Log.d("adsfasdffsadadsfsdfff","aaaaasss")

        view.findViewById<ComposeView>(R.id.messages_list_compose)?.setContent {
            Log.d("adsfasdffsadadsfsdfff","aaaaa")
            MessagesScreen(navigateBack = {
                findNavController().popBackStack()
            }, viewModel = mainViewModel, navigateToChatRoom = navigateToChatToDoctor)
        }







    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MessagesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MessagesFragment().apply {
                arguments = Bundle().apply {
                }
            }

    }
}