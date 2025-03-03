package io.oitech.med_application

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import io.oitech.med_application.databinding.FragmentLoginGragmentBinding
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginGragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginGragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding:FragmentLoginGragmentBinding
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
        return inflater.inflate(R.layout.fragment_login_gragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)






        val navController = findNavController()

        view.findViewById<Button>(R.id.sign_in_button)?.setOnClickListener {
            val email =view.findViewById<EditText>(R.id.emailInLogin)?.text.toString()
            val password =view.findViewById<EditText>(R.id.passwordInLogin)?.text.toString()
            MainActivity.signInWithEmail(email,password, onSuccess = {
                navController.navigate(R.id.action_login_fragment_to_homeScreen)
            })
        }

        view.findViewById<TextView>(R.id.to_register_text)?.setOnClickListener {

            Log.d("asfdadsfsadfadsfadsfsfd","to register")
            navController.navigate(R.id.action_login_fragment_to_register)
            findNavController().clearBackStack(R.id.registerFragment)
            findNavController().clearBackStack(R.id.loginFragment)


        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginGragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginGragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}