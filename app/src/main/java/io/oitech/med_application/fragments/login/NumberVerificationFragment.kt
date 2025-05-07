package io.oitech.med_application.fragments.login

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.oitech.med_application.MainActivity
import io.oitech.med_application.R
import io.oitech.med_application.fragments.MainViewModel
import io.oitech.med_application.fragments.numberVerification.OtpEditText
import kotlinx.parcelize.Parcelize


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NumberVerificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NumberVerificationFragment : Fragment() {
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
    val viewModel :MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        return inflater.inflate(io.oitech.med_application.R.layout.fragment_number_verification, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args =arguments?.getParcelable<PhoneVerificationData>("user_number")
        Log.d("dsfaasdfdfsasfdsdf",args.toString())
        view.findViewById<TextView>(R.id.verification_description).text ="${requireContext().getString(R.string.verification_description_without_number)} ${args?:""}"

        val otpEditText: OtpEditText =
            view.findViewById(io.oitech.med_application.R.id.otp_verification_text) // or binding if using ViewBinding
        val enteredOtp = otpEditText.text.toString()

        otpEditText.addTextChangedListener(){text ->
            if(text.toString().length==6){
                viewModel.verifyCode(code =  text.toString(), name = args?.name?:"", password = args?.password?:"", phoneNumber = args?.phone?:"", onSuccess = {
                    if (context != null) {
                        MainActivity.showSuccessAlert(requireContext())
                    }
                    MainActivity.showSuccessAlert(requireContext())
                    findNavController().navigate(R.id.action_numberVerificationFragment_to_homeFragment)
                })
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
         * @return A new instance of fragment NumberVerificationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NumberVerificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
@Parcelize
data class PhoneVerificationData(val phone:String,val password:String,val name:String):Parcelable
