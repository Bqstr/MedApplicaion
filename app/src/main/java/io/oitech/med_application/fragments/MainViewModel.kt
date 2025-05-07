package io.oitech.med_application.fragments

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.oitech.med_application.fragments.chat.ChatMessageModel
import io.oitech.med_application.fragments.chat.ChatRoomModel
import io.oitech.med_application.fragments.homeFragment.DateOfTheWeek
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItemWithout
import io.oitech.med_application.fragments.homeFragment.TimeSlot
import io.oitech.med_application.fragments.hospitalList.HospitalModel
import io.oitech.med_application.fragments.schedule.ScheduleStatus
import io.oitech.med_application.fragments.schedule.ScheduleUIItem
import io.oitech.med_application.utils.FirstLaunchManager
import io.oitech.med_application.utils.Resource
import io.oitech.med_application.utils.UIdManager
import io.oitech.med_application.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val uidManager: UIdManager,
    private val firstLaunchManager: FirstLaunchManager
) : ViewModel() {


    val hospitals =MutableStateFlow<Resource<List<HospitalModel>>>(Resource.Unspecified())
    val messages = MutableStateFlow<Map<Int, List<ChatMessageModel>>>(emptyMap())

    private val _messageRooms = MutableStateFlow<List<ChatRoomModel>>(emptyList())

    val messageRooms: StateFlow<List<ChatRoomModel>> = _messageRooms


    val messageLiveDataRooms: LiveData<List<ChatRoomModel>> = messageRooms.asLiveData()


    val addressFromGeocode = MutableLiveData<Resource<String>>(Resource.Unspecified())

    val doctors = MutableLiveData<Resource<List<HomeDoctorUiItem>>>(Resource.Unspecified())

    val firstLaunch = MutableStateFlow<Boolean>(false)


    val scheduleList = MutableStateFlow<Resource<List<ScheduleUIItem>>>(Resource.Unspecified())

    init {
        firstLaunch.value = isFirstLaunch()
    }

    fun isFirstLaunch(): Boolean {
        val bol = firstLaunchManager.getBooleanValue()
        firstLaunch.value = bol
        return bol
    }

    fun setNotFirstLaunch() {
        firstLaunchManager.setBooleanValue(false)
        firstLaunch.value = false
    }


    fun getAllDoctors() {
        val db = FirebaseFirestore.getInstance()
        val doctorsRef = db.collection("doctor") // Ensure the collection name is correct
        val timeSlot = db.collection("TimeSlot") // Ensure the collection name is correct
        val dateOfTheWeek = db.collection("DateOfTheWeek") // Ensure the collection name is correct


        doctorsRef
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->


                val doctorsList = querySnapshot.documents.mapNotNull {
                    it.toObject(HomeDoctorUiItemWithout::class.java)
                }
                doctors.value = Resource.Loading()
                timeSlot.get()
                    .addOnSuccessListener { timeSlotSnapshot: QuerySnapshot ->

                        val timeSlotList = timeSlotSnapshot.documents.mapNotNull {
                            it.toObject(TimeSlotFireBase::class.java)
                        }



                        val myMap =
                            timeSlotList.groupBy { it.time.substring(0, 10) }//TODO:check this



                        val listOfDates = mutableListOf<DateOfTheWeek>()

                        myMap.forEach() { key, value ->
                            val fullTime = (value.firstOrNull()?.time ?: "")

                            if (fullTime.isNotBlank()) {
                                val formatter =
                                    DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd HH:mm:ss",
                                        Locale.ENGLISH
                                    )
                                val dateTime = LocalDateTime.parse(fullTime, formatter)

                                val dayName = dateTime.dayOfWeek.name.lowercase()
                                    .replaceFirstChar { it.uppercase() } // "Tuesday"
                                val dayOfMonth = dateTime.dayOfMonth // 18
                                val outputFormatter =
                                    DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)


                                listOfDates.add(DateOfTheWeek(
                                    dateTime = fullTime,
                                    dateName = dayName,
                                    dateNumber = dayOfMonth,
                                    listOfDates = value.map {
                                        val time = dateTime.format(outputFormatter)
                                        Log.d("asfasdfasdfasfdd", "${time}     ${it.time}")
                                        TimeSlot(
                                            time = time,
                                            dateTime = it.time,
                                            available = it.aviable


                                        )
                                    }

                                )
                                )
                            }


                        }


                        val listOfMockDates = listOf(
                            DateOfTheWeek(
                                localDateTime = LocalDateTime.now(),
                                dateNumber = 15,
                                dateName = "Monday",
                                listOfDates = listOf(
                                    TimeSlot(
                                        time = "09:00",
                                        available = true,
                                        dateTime = Utils.getStringForSelectedTime(
                                            LocalDateTime.now().withHour(9).withMinute(0)
                                        )
                                    ),
                                    TimeSlot(
                                        time = "11:00",
                                        available = false,
                                        dateTime = Utils.getStringForSelectedTime(
                                            LocalDateTime.now().withHour(10).withMinute(0)
                                        )
                                    )
                                )
                            ),
                            DateOfTheWeek(
                                localDateTime = LocalDateTime.now().plusDays(1),
                                dateNumber = 16,
                                dateName = "Tuesday",
                                listOfDates = listOf(
                                    TimeSlot(
                                        time = "13:00",
                                        available = true,
                                        dateTime = Utils.getStringForSelectedTime(
                                            LocalDateTime.now().plusDays(1).withHour(13)
                                                .withMinute(0)
                                        )
                                    ),
                                    TimeSlot(
                                        time = "16:00",
                                        available = true,
                                        dateTime = Utils.getStringForSelectedTime(
                                            LocalDateTime.now().plusDays(1).withHour(16)
                                                .withMinute(0)
                                        )
                                    )
                                )
                            )
                        )

                        doctors.value = Resource.Success(doctorsList.map {
                            HomeDoctorUiItem(
                                image = it.image,
                                id = it.id,
                                description = it.description,
                                distance = it.distance,
                                name = it.name,
                                speciality = it.speciality,
                                rating = it.rating.toString(),
                                listOfTimes = listOfDates,
                                hospitalId =it.hospitalId,
                                price = it.price
                            )
                        })


                    }






                Log.d("sadfasdfasdfasdf", "${doctorsList}")

            }
            .addOnFailureListener { e ->
                doctors.value = Resource.Failure(e.message.toString())

                Log.d("sadfasdfasdfasdf", "${e.message}")
            }


    }

    fun getDoctorById(doctorId: Int, onSuccess: (HomeDoctorUiItem) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val doctorsRef = db.collection("doctor") // Ensure the collection name is correct
        val timeSlot = db.collection("TimeSlot") // Ensure the collection name is correct


        doctorsRef
            .whereEqualTo("id", doctorId)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->


                val doctorsList = querySnapshot.documents.mapNotNull {
                    it.toObject(HomeDoctorUiItemWithout::class.java)
                }

                if (doctorsList.firstOrNull() != null) {
                    timeSlot.get()
                        .addOnSuccessListener { timeSlotSnapshot: QuerySnapshot ->

                            val timeSlotList = timeSlotSnapshot.documents.mapNotNull {
                                it.toObject(TimeSlotFireBase::class.java)
                            }


                            Log.d("sadlkjfldasdfjaskldfj", timeSlotList.toString())

                            val myMap =
                                timeSlotList.groupBy { it.time.substring(0, 10) }//TODO:check this

                            Log.d("sdafasdfasdfdddddasdfasdf", myMap.toString())


                            val listOfDates = mutableListOf<DateOfTheWeek>()

                            myMap.forEach() { key, value ->
                                val fullTime = (value.firstOrNull()?.time ?: "")

                                Log.d("sdafasdfasdfasdfasdf", fullTime)
                                if (fullTime.isNotBlank()) {
                                    val formatter =
                                        DateTimeFormatter.ofPattern(
                                            "yyyy-MM-dd HH:mm:ss",
                                            Locale.ENGLISH
                                        )
                                    val dateTime = LocalDateTime.parse(fullTime, formatter)

                                    val dayName = dateTime.dayOfWeek.name.lowercase()
                                        .replaceFirstChar { it.uppercase() } // "Tuesday"
                                    val dayOfMonth = dateTime.dayOfMonth // 18
                                    val outputFormatter =
                                        DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)


                                    listOfDates.add(DateOfTheWeek(
                                        dateTime = fullTime,
                                        dateName = dayName,
                                        dateNumber = dayOfMonth,
                                        listOfDates = value.map {
                                            val time = dateTime.format(outputFormatter)
                                            Log.d("asfasdfasdfasfdd", "${time}     ${it.time}")
                                            TimeSlot(
                                                time = time,
                                                dateTime = it.time,
                                                available = it.aviable


                                            )
                                        }

                                    )
                                    )
                                }


                            }


                            val doctor = doctorsList.firstOrNull()

                            if (doctor != null) {

                                onSuccess(
                                    HomeDoctorUiItem(
                                        image = doctor.image,
                                        id = doctor.id,
                                        description = doctor.description,
                                        distance = doctor.distance,
                                        name = doctor.name,
                                        speciality = doctor.speciality,
                                        rating = doctor.rating.toString(),
                                        listOfTimes = listOfDates,
                                        hospitalId = doctor.hospitalId,
                                        price = doctor.price
                                    )
                                )


                            }


                        }
                } else {

                }






                Log.d("sadfasdfasdfasdf", "${doctorsList}")

            }
            .addOnFailureListener { e ->

                Log.d("sadfasdfasdfasdf", "${e.message}")
            }

    }

    fun getUid(): String {
        return uidManager.getUId()
    }

    fun getScheduleList() {
        val db = FirebaseFirestore.getInstance()
        val uid = getUid()
        Log.d("asdfasdfasdfasdfasdf", "uid     ${uid}")

        val scheduleRef = db.collection("Schedule") // Ensure the collection name is correct
        scheduleRef
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val schedules = querySnapshot.documents.mapNotNull {
                    it.toObject(ScheduleFireBase::class.java)
                }
                Log.d("asdfasdfasdfasdfasdf", schedules.toString())
                scheduleList.value = Resource.Success(schedules.map {
                    ScheduleUIItem(
                        doctorName = it.doctorName,
                        doctorImage = it.doctorImage,
                        doctorSpeciality = it.speciality,
                        time = it.time,
                        isConfirmed = it.isConfirmed,
                        status = when (it.status) {
                            ScheduleStatus.COMPLETED.status -> {
                                ScheduleStatus.COMPLETED
                            }

                            ScheduleStatus.UPCOMING.status -> {
                                ScheduleStatus.UPCOMING
                            }

                            ScheduleStatus.CANCELED.status -> {
                                ScheduleStatus.CANCELED
                            }

                            else -> {
                                ScheduleStatus.CANCELED

                            }


                        }
                    )

                })
            }
            .addOnFailureListener() {
                scheduleList.value = Resource.Success(emptyList())
            }


    }

    fun getHospitals() {
        val db = FirebaseFirestore.getInstance()
        val scheduleRef = db.collection("Hospital") // Ensure the collection name is correct
        scheduleRef
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val hospitalList = querySnapshot.documents.mapNotNull {
                    it.toObject(HospitalModel::class.java)
                }
                hospitals.value = Resource.Success(hospitalList)

            }.addOnFailureListener{
                hospitals.value = Resource.Failure(it.message.toString())

            }
    }

    fun scheduleTime(
        doctor_id: Int,
        doctorName: String,
        doctorImage: String,
        speciality: String,
        time: String
    ) {
        val db = FirebaseFirestore.getInstance()

        val newScheduleItem = hashMapOf(
            "doctorImage" to doctorImage,
            "doctorName" to doctorName,
            "doctor_id" to doctor_id,
            "isConfirmed" to true,
            "speciality" to speciality,
            "status" to ScheduleStatus.UPCOMING.status,
            "time" to time,
            "uid" to getUid(),
        )

        db.collection("Schedule").add(newScheduleItem)
            .addOnSuccessListener {
                //  onResult(true, "User registered and data saved")
            }
            .addOnFailureListener { e ->
                //onResult(false, "Failed to save user data: ${e.message}")
            }


    }

    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        name: String,
        auth: FirebaseAuth
    ) {

        val db = Firebase.firestore
        Log.e("asdasdasdasdasd", "start")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("asdasdasdasdasd", "successs")

                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    // Create a user object
                    val localUser = hashMapOf(
                        "uid" to userId,
                        "name" to name,
                        "email" to email,
                    )

                    uidManager.setUId(userId)

                    // Store user data in Firestore
                    db.collection("users").document(userId).set(localUser)
                        .addOnSuccessListener {
                            onSuccess()
                            //  onResult(true, "User registered and data saved")
                        }
                        .addOnFailureListener { e ->
                            //onResult(false, "Failed to save user data: ${e.message}")
                        }
                    // Sign-up successful
                } else {
                    Log.e("asdasdasdasdasd", "Regisrter failed: ${task.exception?.message}")
                }
            }
    }

    private  var verificationId =MutableLiveData<String?>(null)

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Auto verification or instant verification
            Log.e("PhoneAuth", "Verification success")

            //signInWithPhoneAuthCredential(credential,Firebase.auth)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Handle error
            Log.e("PhoneAuth", "Verification failed", e)
        }

        override fun onCodeSent(verificationIds: String, token: PhoneAuthProvider.ForceResendingToken) {
            // Save verification ID to use later
            verificationId.value = verificationIds
            Log.d("codeeeeeeeee", "Code sent: ${verificationId.value}")
        }
    }

    fun sendVerificationCode(phoneNumber: String,activity: Activity) {
        Log.d("PhoneAuth",phoneNumber)
        val auth =Firebase.auth
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity )                 // Activity (for callback binding)
            .setCallbacks(callbacks)           // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    fun sendVerificationForSignInWithPhone(phoneNumber: String,password: String,activity: Activity,onSuccess: () -> Unit){
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber) // Phone number in E.164 format
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity) // Your current activity
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto verification (instant or auto-retrieval)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = task.result?.user
                                Log.d("Auth", "Signed in with phone: ${user?.uid}")
                                //signInWithPhone(email = phoneNumber,password =password, onSuccess = onSuccess, auth = Firebase.auth)
                            }
                        }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("Auth", "Phone verification failed", e)
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    // Save verificationId and prompt user to enter code manually
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode(code: String,onSuccess: () -> Unit,phoneNumber: String,name:String,password: String) {
        Log.d("codeeeeeeeee",verificationId.value?:"")
        if(verificationId.value!=null) {
            val credential = PhoneAuthProvider.getCredential(verificationId.value!!, code)
            registerWithPhone(name =name , password = password, phone = phoneNumber, auth = Firebase.auth, onSuccess =onSuccess,credential =credential)
            verificationId.value =null
            //signInWithPhoneAuthCredential(credential, Firebase.auth,onSuccess )
        }
    }


    fun registerWithPhone(name:String,phone:String,password:String,auth:FirebaseAuth,onSuccess: () -> Unit,credential :PhoneAuthCredential){
        val db = Firebase.firestore
        Log.d("codeeeeeeeee","register with phone")

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("codeeeeeeeee","sign in with creditionals")

                    // Success, navigate to the next screen
                    val user = task.result?.user


                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    // Fetch user details from Firestore

                    val newUser = hashMapOf(
                        "email" to phone,
                        "name" to name,
                        "uid" to userId,
                    )


                    db.collection("users").add(
                        newUser
                    )
                        .addOnSuccessListener { document ->
                            Log.d("codeeeeeeeee","adding user")

                                Log.d("codeeeeeeeee","document exist")

                                //val userData = document.data ?: emptyMap()

                                uidManager.setUId(userId)
                                onSuccess()
                        }
                        .addOnFailureListener { e ->

                        }




                } else {
                    // Error
                    Log.e("PhoneAuth", "Sign in failed", task.exception)
                }
            }
    }

//    fun signInWithPhone(
//        email: String,
//        password: String,
//        onSuccess: () -> Unit,
//        auth: FirebaseAuth
//    ){
//        auth.si(email, password)//need to verify before login , just like in register
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    // Sign-in successful
//
//                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
//
//                    // Fetch user details from Firestore
//                    db.collection("users").document(userId).get()
//                        .addOnSuccessListener { document ->
//                            if (document.exists()) {
//                                val userData = document.data ?: emptyMap()
//
//                                uidManager.setUId(userId)
//                                onSuccess()
//                            } else {
//                                //TODO: show error
//                            }
//                        }
//                        .addOnFailureListener { e ->
//                            //TODO: show error
//
//                        }
//
//
//                    val user = auth.currentUser
//                } else {
//                    Log.e("Auth", "Sign-up failed: ${task.exception?.message}")
//
//
//                }
//            }
//    }
    fun signInWithEmail(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        auth: FirebaseAuth
    ) {
        Log.e("jdsfhkjsdhfkjsdf", "jkfsdahkjlsdfh")
        val db = Firebase.firestore

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign-in successful

                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    // Fetch user details from Firestore
                    db.collection("users").document(userId).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val userData = document.data ?: emptyMap()

                                uidManager.setUId(userId)
                                onSuccess()
                            } else {
                                //TODO: show error
                            }
                        }
                        .addOnFailureListener { e ->
                            //TODO: show error

                        }


                    val user = auth.currentUser
                } else {
                    Log.e("Auth", "Sign-up failed: ${task.exception?.message}")


                }
            }
    }
    val isEmailAuthorizationForRegister = MutableLiveData<Boolean>(true)



    val isEmailAuthorizationForLogin = MutableLiveData<Boolean>(true)
    fun loginByEmailAndPassword(email: String, password: String, onSuccess: () -> Unit) {
        signInWithEmail(email, password, onSuccess = onSuccess, Firebase.auth)
    }


    fun logoutUser() {
        Firebase.auth.signOut()
        uidManager.setUId("")
    }

    val profileName = MutableLiveData<String>("")

    fun getMyProfile() {
        val db = FirebaseFirestore.getInstance()
        val doctorsRef = db.collection("users") // Ensure the collection name is correct


        doctorsRef
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->


                val user = querySnapshot.documents.mapNotNull {
                    it.toObject(UserFirebase::class.java)
                }.firstOrNull()
//
                profileName.value = user?.name


                //Log.d("sadfasdfasdfasdf", "${doctorsList}")

            }
            .addOnFailureListener { e ->

                Log.d("sadfasdfasdfasdf", "${e.message}")
            }
    }


    fun getMessagesOfOneRoom(doctorId: Int,onSuccess: (List<ChatMessageModel>) -> Unit ={}) {
        val db = FirebaseFirestore.getInstance()
        val doctorsRef = db.collection("ChatMessage") // Ensure the collection name is correct

        doctorsRef
            .whereEqualTo("userUid", getUid())
            .whereEqualTo("doctorId", doctorId)
           // .orderBy("time", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->

                val messagesList = querySnapshot.documents.map { doc ->
                    ChatMessageModel(
                        isMyMessage = doc.getBoolean("isMyMessage") ?: false,
                        doctorId = doc.getLong("doctorId")?.toInt() ?: 0,
                        message = doc.getString("message") ?: "",
                        time = doc.getString("time") ?: "",
                        userUid = doc.getString("userUid") ?: ""
                    )
                }.sortedByDescending { it.time }
                Log.d("dsadasfadfasfdasdf", "list ${messagesList}")

                onSuccess.invoke(messagesList)


                val map = messages.value.toMutableMap()
                map.put(doctorId, messagesList)

                messages.value = map
            }.addOnFailureListener {
                Log.d("dsadasfadfasfdasdf","fail  ${it.message.toString()}")
            }
    }


    //
    fun getMessageRooms() {
        val db = FirebaseFirestore.getInstance()
        val doctorsRef = db.collection("ChatRoom") // Ensure the collection name is correct

        doctorsRef
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->

                val listOfRooms = querySnapshot.documents.mapNotNull {
                    it.toObject(ChatRoomModel::class.java)
                }

                _messageRooms.value = listOfRooms


            }
            .addOnFailureListener { e ->

                Log.d("sadfasdfasdfasdf", "${e.message}")
            }

    }


    fun writeMessage(doctor_id: Int, message: String, isMyMessage: Boolean) {
        val db = FirebaseFirestore.getInstance()

        val newMessage = hashMapOf(
            "doctorId" to doctor_id,
            "isMyMessage" to isMyMessage,
            "message" to message,
            "time" to LocalDateTime.now().toString(),
            "userUid" to getUid(),
        )

        db.collection("ChatMessage").add(newMessage)
            .addOnSuccessListener {

                getMessagesOfOneRoom(doctor_id, onSuccess = {messagesList ->
                    if (messagesList.size==1) {
                        getDoctorById(doctor_id, onSuccess = {
                            createMessageRoom(
                                doctor_id,
                                doctorName = it.name,
                                lastMessage = messagesList.last().message,
                                lastMessageTime = messagesList.last().time
                            )
                        })
                    }
                })


            }
            .addOnFailureListener { e ->
                //onResult(false, "Failed to save user data: ${e.message}")
            }


    }

    fun createMessageRoom(
        doctorId: Int,
        lastMessage: String,
        lastMessageTime: String,
        doctorName: String
    ) {
        val db = FirebaseFirestore.getInstance()

        val newRoom = hashMapOf(
            "doctorId" to doctorId,
            "lastMessage" to lastMessage,
            "lastMessageTime" to lastMessageTime,
            "userUid" to getUid(),
            "doctorName" to doctorName
        )

        db.collection("ChatRoom").add(newRoom)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->
                //onResult(false, "Failed to save user data: ${e.message}")
            }

    }

    val currentChatRoom = MutableLiveData<ChatRoomModel?>(null)
    fun getMessageRoomByDoctorId(doctorId: Int) {
        val db = FirebaseFirestore.getInstance()
        val doctorsRef = db.collection("ChatRoom") // Ensure the collection name is correct

        doctorsRef
            .whereEqualTo("userUid", getUid())
            .whereEqualTo("doctorId", doctorId)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->

                val chatRoomList = querySnapshot.documents.mapNotNull {
                    it.toObject(ChatRoomModel::class.java)
                }
                Log.d("sdfsdfsadfsdfasdfsdf","succ ${chatRoomList} ")


                if (chatRoomList.firstOrNull() != null) {
                    currentChatRoom.value = chatRoomList.firstOrNull()!!
                }else{
                    Log.d("sdfsdfsadfsdfasdfsdf","fail buttt")

                    getDoctorById(doctorId, onSuccess = {
                        Log.d("sdfsdfsadfsdfasdfsdf","fail succcce")

                        currentChatRoom.value = ChatRoomModel(
                            doctorId = doctorId,
                            doctorName = it.name,
                            userUid = getUid(),
                            doctorNumber = it.number
                        )
                    })
                }

            }
            .addOnFailureListener {
                Log.d("sdfsdfsadfsdfasdfsdf","fail buttt")

                getDoctorById(doctorId, onSuccess = {
                    Log.d("sdfsdfsadfsdfasdfsdf","fail succcce")

                    currentChatRoom.value = ChatRoomModel(
                        doctorId = doctorId,
                        doctorName = it.name,
                        userUid = getUid(),
                        doctorNumber = it.number
                    )
                })
            }
    }




}

data class UserFirebase(
    val email: String = "",
    val name: String = "",
    val uid: String = ""
)

data class ScheduleFireBase(
    val doctorName: String = "mr Robot",
    val speciality: String = "some speciality",
    val status: String = "Upcoming",
    val doctorImage: String = "",
    val doctor_id: Int = 0,
    val isConfirmed: Boolean = false,//TODO:for some reasons , this field not coming from firebase and just assigning false(as default here),don't work as String as well
    val time: String = "2030-10-30 14:30:45",
    val uid: String = ""
)

data class TimeSlotFireBase(
    val aviable: Boolean = false,
    val doctor_id: Int = 0,
    val id: Int = 0,
    val time: String = "2025-10-30 14:30:45"
)
