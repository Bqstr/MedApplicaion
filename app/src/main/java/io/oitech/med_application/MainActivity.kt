package io.oitech.med_application

import android.app.Activity
import android.app.Dialog
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import io.oitech.med_application.databinding.ActivityMainBinding
import io.oitech.med_application.fragments.MainViewModel
import android.content.Context
import android.graphics.*
import android.view.KeyEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.MotionEvent
import android.view.View.OnKeyListener
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random

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












        binding.callAmbulance.setOnClickListener {
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
            myNavController = navController
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.home_bottomBar)

            bottomNavigationView.setupWithNavController(navController)


            if (mainViewModel.firstLaunch.value) {
                navController.navigate(R.id.onboardingFragment)
            } else {
                if (auth.currentUser != null) {
                    navController.navigate(
                        R.id.action_login_fragment_to_homeScreen, null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.homeFragment, true) // Clears all previous fragments
                            .build()
                    )
                }
            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.homeFragment) {
                    binding.callAmbulance.visibility = View.VISIBLE
                } else {
                    binding.callAmbulance.visibility = View.GONE

                }
                when (destination.id) {
                    R.id.homeFragment, R.id.profileFragment, R.id.scheduleFragment, R.id.messagesFragment -> {
                        bottomNavigationView.visibility = View.VISIBLE
                    }

                    else -> {
                        bottomNavigationView.visibility = View.GONE
                    }
                }
            }



            binding.homeBottomBar.setOnItemSelectedListener { item ->
                Log.d("sadfasdfasdfadsfasdf", "hereeere")
                when (item.itemId) {
                    R.id.homeFragment -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
                        true
                    }

                    R.id.profile_bottom_bar -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
                        true
                    }

                    R.id.calendar_bottom_bar -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.scheduleFragment)
                        true
                    }

                    R.id.chatListFragments -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.messagesFragment)
                        true
                    }

                    else -> false
                }
            }


        }

    }

    var myNavController: NavController? = null
    fun signIn(onSuccess: () -> Unit) {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)!!
//                firebaseAuthWithGoogle(account.idToken!!, auth = Firebase.auth)
//            } catch (e: ApiException) {
//                Toast.makeText(this, "Sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

//    private fun firebaseAuthWithGoogle(idToken: String, auth: FirebaseAuth) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = auth.currentUser
//                    Log.d("sadfsdasdafdsfsadfasdf",user?.uid?:"")
//                    if (user != null && user.email?.isNotBlank() == true) {
//                        myNavController?.navigate(
//                            R.id.action_login_fragment_to_homeScreen, null, NavOptions.Builder()
//                                .setPopUpTo(
//                                    R.id.homeFragment,
//                                    true
//                                ) // Clears all previous fragments
//                                .build()
//                        )
//
//                        mainViewModel.createUser(
//                            auth = auth,
//                            name = if (user.displayName?.isBlank() == true) {
//                                user.email ?: ""
//                            } else {
//                                user.displayName ?: ""
//                            },
//                            email = user.email!! , onSuccess = {
//
//                            }
//                        )
//
//                        showSuccessAlert(this)
//
//                        Toast.makeText(this, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                } else {
//                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }


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




class SnakeGameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private val snake = mutableListOf<Point>()
    private var direction = Direction.RIGHT
    private var apple = Point()
    private var cellSize = 50
    private var numCellsX = 0
    private var numCellsY = 0
    private var gameRunning = true

    private val paintSnake = Paint().apply { color = android.graphics.Color.GREEN }
    private val paintApple = Paint().apply { color = android.graphics.Color.RED }
    private val paintBackground = Paint().apply { color =android.graphics.Color.BLACK }

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        numCellsX = width / cellSize
        numCellsY = height / cellSize
        startNewGame()

        fixedRateTimer("gameTimer", false, 0L, 200L) {
            if (gameRunning) {
                updateGame()
                drawGame()
            }
        }
    }

    private fun startNewGame() {
        snake.clear()
        snake.addAll(listOf(Point(5, 5),Point(5,6)))
        direction = Direction.RIGHT
        spawnApple()
    }

    private fun spawnApple() {
        do {
            apple = Point(Random.nextInt(numCellsX), Random.nextInt(numCellsY))
        } while (snake.contains(apple))
    }

    private fun updateGame() {
        val head = snake.first()
        val newHead = Point(head.x, head.y)

        when (direction) {
            Direction.UP -> newHead.y--
            Direction.DOWN -> newHead.y++
            Direction.LEFT -> newHead.x--
            Direction.RIGHT -> newHead.x++
        }

        //eat itself
        if(snake.contains(newHead)){
            gameRunning = false
            return
        }

        // Проверка столкновений
        if (newHead.x < 0
        ) {
            newHead.x =numCellsX-1

        }
        else if(newHead.x >= numCellsX){
            newHead.x =0

        }
        if(
            newHead.y < 0

        ){
            newHead.y =numCellsY-1

        }
        else if( newHead.y >= numCellsY){
            newHead.y =0
        }

        snake.add(0, newHead)

        if (newHead == apple) {
            spawnApple()
        } else {
            snake.removeAt(snake.lastIndex)
        }
    }

    private fun drawGame() {
        val canvas = holder.lockCanvas()
        canvas?.let {
            it.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)

            // Змейка
            for (p in snake) {
                it.drawRect(
                    (p.x * cellSize).toFloat(),
                    (p.y * cellSize).toFloat(),
                    ((p.x + 1) * cellSize).toFloat(),
                    ((p.y + 1) * cellSize).toFloat(),
                    paintSnake
                )
            }

            // Яблоко
            it.drawRect(
                (apple.x * cellSize).toFloat(),
                (apple.y * cellSize).toFloat(),
                ((apple.x + 1) * cellSize).toFloat(),
                ((apple.y + 1) * cellSize).toFloat(),
                paintApple
            )

            holder.unlockCanvasAndPost(it)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("asfasdfasdfasdasdf","is moution down ${event.action}    ${MotionEvent.ACTION_DOWN}")

        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y
            val head = snake.first()

            val centerX = head.x * cellSize + cellSize / 2
            val centerY = head.y * cellSize + cellSize / 2

            val dx = x - centerX
            val dy = y - centerY

            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0 && direction != Direction.LEFT) direction = Direction.RIGHT
                else if (dx < 0 && direction != Direction.RIGHT) direction = Direction.LEFT
            } else {
                if (dy > 0 && direction != Direction.UP) direction = Direction.DOWN
                else if (dy < 0 && direction != Direction.DOWN) direction = Direction.UP
            }
        }
        return true
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {}
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("dtgggggg",keyCode.toString())
        if(keyCode == KeyEvent.KEYCODE_W){
            if(direction == Direction.DOWN || direction == Direction.UP){
                return true
            }
            else{
                direction =Direction.UP

            }
        }
        else if(keyCode == KeyEvent.KEYCODE_S){
            if(direction == Direction.DOWN || direction == Direction.UP){
                return true
            }
            else{
                direction =Direction.DOWN

            }
        }
        else if(keyCode == KeyEvent.KEYCODE_A){
            if(direction == Direction.LEFT || direction == Direction.RIGHT){
                return true
            }
            else{
                direction =Direction.LEFT

            }
        }
        else if(keyCode == KeyEvent.KEYCODE_D) {
            if(direction == Direction.LEFT || direction == Direction.RIGHT){
                return true
            }
            else{
                direction =Direction.RIGHT

            }
        }
        return super.onKeyDown(keyCode, event)

    }
    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }


}
