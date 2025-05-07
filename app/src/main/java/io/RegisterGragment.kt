package io

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import io.oitech.med_application.MainActivity
import io.oitech.med_application.R
import io.oitech.med_application.fragments.MainViewModel
import io.oitech.med_application.fragments.login.NumberVerificationFragment
import io.oitech.med_application.fragments.login.PhoneVerificationData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterGragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class RegisterGragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_register_gragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = view.findViewById<EditText>(R.id.emailRegister)
        val password = view.findViewById<EditText>(R.id.passwordRegister)
        val name = view.findViewById<EditText>(R.id.user_name_register)

        email.addTextChangedListener {
            when {
                Patterns.EMAIL_ADDRESS.matcher(it).matches() -> {
                    Log.d("asdfasfdasfdafsd","email")
                    viewModel.isEmailAuthorizationForRegister.postValue(true)
                }
                Patterns.PHONE.matcher(it).matches() ->{
                    Log.d("asdfasfdasfdafsd","phone")

                    viewModel.isEmailAuthorizationForRegister.postValue(false)

                }
                else -> {
                    Log.d("asdfasfdasfdafsd","else")
                    viewModel.isEmailAuthorizationForRegister.postValue(true)

                }
            }
        }


        viewModel.isEmailAuthorizationForRegister.observe(viewLifecycleOwner){

            if(it) {
                Log.d("asdfasfdasfdafsd","set phone")

                view.findViewById<EditText>(R.id.emailRegister)
                    .setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.group, 0, 0, 0)
            }else{
                Log.d("asdfasfdasfdafsd","set email")

                view.findViewById<EditText>(R.id.emailRegister)
                    .setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.phone, 0, 0, 0)
            }
        }







        val navController = findNavController()

        view.findViewById<Button>(R.id.sign_up_button)?.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()
            val nameText = name.text.toString().trim()


            // Check if fields are not empty
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                // You might want to show a Toast or error message here
                return@setOnClickListener
            }

            if(viewModel.isEmailAuthorizationForRegister.value == true) {
                viewModel.register(emailText, passwordText, onSuccess = {
                    MainActivity.showSuccessAlert(requireContext())
                    navController.navigate(R.id.action_register_fragment_to_homeScreen)
                    findNavController().clearBackStack(R.id.registerFragment)
                    findNavController().clearBackStack(R.id.loginFragment)
                }, nameText, auth = Firebase.auth)
            }else{
                viewModel.sendVerificationCode(emailText,requireActivity())
                val bundle =Bundle()
                val userData = PhoneVerificationData(name = nameText, password =passwordText , phone = emailText)
                bundle.putParcelable("user_number",userData)
                findNavController().navigate(R.id.action_registerFragment_to_numberVerificationFragment,bundle)


            }
        }

        view.findViewById<TextView>(R.id.to_login_text)?.setOnClickListener {
            navController.navigate(R.id.action_register_fragment_to_loginScreen)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterGragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterGragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}