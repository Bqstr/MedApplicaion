package io.oitech.med_application

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.oitech.med_application.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(this, "You are logged in already", Toast.LENGTH_LONG).show()
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Get from google-services.json
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.root.post {

            val navController = findNavController(R.id.nav_host_fragment)
            myNavController =navController
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.home_bottomBar)

            bottomNavigationView.setupWithNavController(navController)

            if (auth.currentUser != null) {
                navController.navigate(R.id.action_login_fragment_to_homeScreen)
            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.homeFragment,R.id.profileFragment -> {
                        bottomNavigationView.visibility = View.VISIBLE
                    }

                    else -> {
                        bottomNavigationView.visibility = View.GONE
                    }
                }
            }


            binding.homeBottomBar.setOnItemSelectedListener { item ->
                Log.d("sadfasdfasdfadsfasdf","hereeere")
                when (item.itemId) {
                    R.id.homeFragment -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
                        true
                    }
                    R.id.exit ->{
                        MainActivity.logoutUser()
                        findNavController(R.id.nav_host_fragment).navigate(R.id.loginFragment)
                        navController.clearBackStack(R.id.profileFragment)

                        navController.clearBackStack(R.id.loginFragment)
                    }

                    R.id.profile_bottom_bar ->{
                        findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
                        true
                    }

                    else -> false
                }
            }






        }





    }

    var myNavController:NavController? =null
     fun signIn(onSuccess: () -> Unit) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    myNavController?.navigate(R.id.action_login_fragment_to_homeScreen)
                    myNavController?.clearBackStack(R.id.loginFragment)
                    myNavController?.clearBackStack(R.id.registerFragment)

                    showSuccessAlert(this)
                    Toast.makeText(this, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    companion object {
        private const val RC_SIGN_IN = 9001

        fun showSuccessAlert(context: Context) {
            val view = LayoutInflater.from(context).inflate(R.layout.success_login_dialog, null)

            val builder = AlertDialog.Builder(context)
            builder.setView(view)
            val alertDialog = builder.create()

// Find the button inside the inflated dialog layout
            val button = view.findViewById<FrameLayout>(R.id.alert_success_button)
            button.setOnClickListener {
                alertDialog.dismiss()
            }

            if (alertDialog.window != null) {
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            alertDialog.show()
        }

         lateinit var auth: FirebaseAuth

        fun register(email: String, password: String, onSuccess: () -> Unit) {

            Log.e("asdasdasdasdasd", "start")

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.e("asdasdasdasdasd", "successs")


                        // Sign-up successful
                        val user = auth.currentUser
                        onSuccess()
                    } else {
                        Log.e("asdasdasdasdasd", "Regisrter failed: ${task.exception?.message}")
                    }
                }
        }


        fun signInWithEmail(email: String, password: String, onSuccess: () -> Unit) {
            Log.e("jdsfhkjsdhfkjsdf", "jkfsdahkjlsdfh")

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign-in successful
                        val user = auth.currentUser
                        onSuccess()
                    } else {
                        Log.e("Auth", "Sign-up failed: ${task.exception?.message}")


                    }
                }
        }


        fun logoutUser() {
            FirebaseAuth.getInstance().signOut()
        }

        private fun showSuccessDialog() {
        }
    }

}