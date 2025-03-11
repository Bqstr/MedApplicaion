 package io

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import io.oitech.med_application.MainActivity
import io.oitech.med_application.R
import org.w3c.dom.Text

 // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterGragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterGragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_register_gragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = view.findViewById<EditText>(R.id.emailRegister)
        val password = view.findViewById<EditText>(R.id.passwordRegister)

        val navController = findNavController()

        view.findViewById<Button>(R.id.sign_up_button)?.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()


            // Check if fields are not empty
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                // You might want to show a Toast or error message here
                return@setOnClickListener
            }

            MainActivity.register(emailText, passwordText, onSuccess = {
                MainActivity.showSuccessAlert(requireContext())
                navController.navigate(R.id.action_register_fragment_to_homeScreen)
                findNavController().clearBackStack(R.id.registerFragment)
                findNavController().clearBackStack(R.id.loginFragment)
            })
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
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}