package io.oitech.med_application

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import io.oitech.med_application.MainActivity.Companion.showSuccessAlert
import io.oitech.med_application.databinding.FragmentLoginGragmentBinding

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
    private lateinit var viewModel:LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity =context as MainActivity
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


        viewModel  = ViewModelProvider(this)[LoginViewModel::class.java]

        val navController = findNavController()

        view.findViewById<Button>(R.id.auth_button)!!.setOnClickListener {
            Log.d("jdsfhkjsdhfkjsdf","ksadfjhadsl")
            val email =view.findViewById<EditText>(R.id.emailInLogin)?.text.toString()
            val password =view.findViewById<EditText>(R.id.passwordInLogin)?.text.toString()
            viewModel.loginByEmailAndPassword(email,password, onSuccess = {
                if(context!=null) {
                    showSuccessAlert(requireContext())
                }
                showSuccessAlert(requireContext())
                navController.navigate(R.id.action_login_fragment_to_homeScreen)
            })

        }

        view.findViewById<TextView>(R.id.to_register_text)?.setOnClickListener {
            Log.d("asfdadsfsadfadsfadsfsfd","to register")
            navController.navigate(R.id.action_login_fragment_to_register,null)
        }



        view.findViewById<ConstraintLayout>(R.id.google_sign_in).setOnClickListener{
            mainActivity.signIn {
                navController.navigate(R.id.action_login_fragment_to_register,null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true) // Clears all previous fragments
                        .build())
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