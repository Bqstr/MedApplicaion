package io.oitech.med_application.fragments.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import io.oitech.med_application.MainActivity
import io.oitech.med_application.R

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
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
        val email = view.findViewById<EditText>(R.id.emailInLogin)

        email.addTextChangedListener {
            when {
                Patterns.EMAIL_ADDRESS.matcher(it).matches() -> {
                    Log.d("asdfasfdasfdafsd","email")
                    viewModel.isEmailAuthorization.postValue(true)
                }
                Patterns.PHONE.matcher(it).matches() ->{
                    Log.d("asdfasfdasfdafsd","phone")

                    viewModel.isEmailAuthorization.postValue(false)

                }
                else -> {
                    Log.d("asdfasfdasfdafsd","else")
                    viewModel.isEmailAuthorization.postValue(true)

                }
            }
        }


        viewModel.isEmailAuthorization.observe(viewLifecycleOwner){

            if(it) {
                Log.d("asdfasfdasfdafsd","set phone")

                view.findViewById<EditText>(R.id.emailInLogin)
                    .setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.group, 0, 0, 0)
            }else{
                Log.d("asdfasfdasfdafsd","set email")

                view.findViewById<EditText>(R.id.emailInLogin)
                    .setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.phone, 0, 0, 0)
            }
        }


        val navController = findNavController()

        view.findViewById<Button>(R.id.auth_button)!!.setOnClickListener {
            Log.d("jdsfhkjsdhfkjsdf", "ksadfjhadsl")
            val email = view.findViewById<EditText>(R.id.emailInLogin)?.text.toString()
            val password = view.findViewById<EditText>(R.id.passwordInLogin)?.text.toString()
            viewModel.loginByEmailAndPassword(email, password, onSuccess = {
                if (context != null) {
                    MainActivity.showSuccessAlert(requireContext())
                }
                MainActivity.showSuccessAlert(requireContext())
                navController.navigate(R.id.action_login_fragment_to_homeScreen)
            })

        }

        view.findViewById<TextView>(R.id.to_register_text)?.setOnClickListener {
            Log.d("asfdadsfsadfadsfadsfsfd", "to register")
            navController.navigate(R.id.action_login_fragment_to_register, null)
        }



        view.findViewById<ConstraintLayout>(R.id.google_sign_in).setOnClickListener {
            mainActivity.signIn {
                navController.navigate(
                    R.id.action_login_fragment_to_register, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true) // Clears all previous fragments
                        .build()
                )
            }
        }


    }


}