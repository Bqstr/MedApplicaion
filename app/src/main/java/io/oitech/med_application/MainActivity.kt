package io.oitech.med_application

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.oitech.med_application.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

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
        if(auth.currentUser!=null){
            val nav= findNavController(R.id.nav_host_fragment)
            nav.navigate(R.id.action_login_fragment_to_homeScreen)
        }


    }



    companion object {
        private lateinit var auth: FirebaseAuth

         fun register(email: String, password: String,onSuccess :() ->Unit) {

             auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign-up successful
                        val user = auth.currentUser
                        onSuccess()
                    } else {
                        Log.e("Auth", "Regisrter failed: ${task.exception?.message}")
                    }
                }
        }

         fun signInWithEmail(email: String, password: String,onSuccess :() ->Unit) {
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




            // Redirect to login screen or update UI
        }

    }
}