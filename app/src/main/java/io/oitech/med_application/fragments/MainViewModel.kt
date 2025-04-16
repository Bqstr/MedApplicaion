package io.oitech.med_application.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.oitech.med_application.fragments.homeFragment.DateOfTheWeek
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItemWithout
import io.oitech.med_application.fragments.homeFragment.TimeSlot
import io.oitech.med_application.fragments.schedule.ScheduleStatus
import io.oitech.med_application.fragments.schedule.ScheduleUIItem
import io.oitech.med_application.utils.FirstLaunchManager
import io.oitech.med_application.utils.Resource
import io.oitech.med_application.utils.UIdManager
import io.oitech.med_application.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val uidManager: UIdManager,
    private val firstLaunchManager: FirstLaunchManager
) : ViewModel() {




    val addressFromGeocode =MutableLiveData<Resource<String>>(Resource.Unspecified())

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
                Log.d("fdsfdasdfsfafff",doctorsList.toString())
                doctors.value = Resource.Loading()
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
                                        Log.d("asfasdfasdfasfdd","${time}     ${it.time}")
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
                                listOfTimes = listOfDates
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


    fun scheduleTime(doctor_id: Int,doctorName: String,doctorImage: String,speciality: String,time: String){
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
                            }
                        }
                        .addOnFailureListener { e ->
                        }


                    val user = auth.currentUser
                } else {
                    Log.e("Auth", "Sign-up failed: ${task.exception?.message}")


                }
            }
    }


    val isEmailAuthorization = MutableLiveData<Boolean>(true)
    fun loginByEmailAndPassword(email: String, password: String, onSuccess: () -> Unit) {
        signInWithEmail(email, password, onSuccess = onSuccess, Firebase.auth)
    }


    fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        uidManager.setUId("")
    }

}

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
) {

}
