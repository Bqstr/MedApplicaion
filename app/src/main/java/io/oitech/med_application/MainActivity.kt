package io.oitech.med_application

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import io.oitech.med_application.databinding.ActivityMainBinding
import io.oitech.med_application.fragments.MainViewModel
import io.oitech.med_application.fragments.homeFragment.DateOfTheWeek
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItemWithout
import io.oitech.med_application.fragments.homeFragment.TimeSlot
import kotlin.jvm.Throws

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val database = FirebaseDatabase.getInstance()
    val doctorRef = database.getReference("doctors")
    private lateinit var googleSignInClient: GoogleSignInClient
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = Firebase.auth






        val doctor = HomeDoctorUiItem(
            id = 1,
            name = "Dr. John Doe",
            image = "https://example.com/image.jpg",
            speciality = "Cardiologist",
            distance = 5.0,
            rating = "4.8",
            description = "Experienced heart specialist",
            listOfTimes = listOf(
                DateOfTheWeek("Monday", 11,"date name", listOf(
                TimeSlot("10:00-11:00", available = true)
            ))
            )
        )




        binding.callAmbulance.setOnClickListener{
            dialEmergencyNumber("103")
        }



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


            if(mainViewModel.firstLaunch.value){
                navController.navigate(R.id.onboardingFragment)
            }else {
                if (auth.currentUser != null) {
                    navController.navigate(R.id.action_login_fragment_to_homeScreen)
                }
            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                if(destination.id==R.id.homeFragment){
                    binding.callAmbulance.visibility =View.VISIBLE
                }else{
                    binding.callAmbulance.visibility =View.GONE

                }
                when (destination.id) {
                    R.id.homeFragment,R.id.profileFragment,R.id.scheduleFragment -> {
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
                    R.id.profile_bottom_bar ->{
                        findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
                        true
                    }
                    R.id.calendar_bottom_bar ->{
                        findNavController(R.id.nav_host_fragment).navigate(R.id.scheduleFragment)
                        true
                    }

                    else -> false
                }
            }






        }



         val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri =data?.data
                //setImage

            }else{
                Toast.makeText(this,"No Image ",Toast.LENGTH_SHORT).show()
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
                firebaseAuthWithGoogle(account.idToken!!, auth = Firebase.auth)
            } catch (e: ApiException) {
                Toast.makeText(this, "Sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String,auth: FirebaseAuth) {
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



        private fun showSuccessDialog() {
        }
    }


    private fun dialEmergencyNumber(number: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(dialIntent)
    }

}