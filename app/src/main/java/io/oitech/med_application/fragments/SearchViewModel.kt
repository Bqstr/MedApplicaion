package io.oitech.med_application.fragments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.oitech.med_application.fragments.homeFragment.DateOfTheWeek
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItemWithout
import io.oitech.med_application.fragments.homeFragment.TimeSlot
import io.oitech.med_application.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject() constructor() : ViewModel() {
    val searchText = MutableStateFlow<String>("")


    var searchJob: Job? = null


    val doctors = MutableStateFlow<Resource<List<HomeDoctorUiItem>>>(Resource.Unspecified())

    fun searchDoctors(searchText: String,) {

        searchJob?.cancel()
        if(searchText.isBlank()){
            doctors.value =Resource.Success(emptyList())
            return
        }
        searchJob = viewModelScope.launch {

            try {








                doctors.value =Resource.Loading()
                val db = FirebaseFirestore.getInstance()



                val job2 = async {
                    val snapshot = db.collection("specialist")
                        .orderBy("nameLowercase")
                        .startAt(searchText)
                        .get()
                        .await()
                    snapshot.documents.mapNotNull { it.toObject(HomeDoctorUiItemWithout::class.java) }
                }




                val timeSlot =
                    db.collection("TimeSlot")
                val snapshot = withContext(Dispatchers.IO) {
                    doctors.value = Resource.Loading()
                    Tasks.await(

                        db.collection("doctor")
                            .orderBy("nameLowercase")
                            .startAt(searchText)
                            //.endAt(searchText + "\uf8ff") // for prefix search
                            .get()
                            .addOnSuccessListener { documents ->
                                val doctorsList =
                                    documents.mapNotNull { it.toObject(HomeDoctorUiItemWithout::class.java) }

                                Log.d("sdfdsfrfrfrfrfrfrff",doctorsList.size.toString())
                                timeSlot
                                    .get()
                                    .addOnSuccessListener { timeSlotSnapshot: QuerySnapshot ->
                                        val timeSlotList = timeSlotSnapshot.documents.mapNotNull {
                                            it.toObject(TimeSlotFireBase::class.java)
                                        }

                                        doctors.value = Resource.Success(doctorsList.map
                                        { doctor ->

                                            val myMap =
                                                timeSlotList.filter { it.doctor_id == doctor.id }
                                                    .groupBy {
                                                        it.time.substring(
                                                            0,
                                                            10
                                                        )
                                                    }//TODO:check this


                                            val listOfDates = mutableListOf<DateOfTheWeek>()


                                            myMap.forEach() { key, value ->
                                                val fullTime = (value.firstOrNull()?.time ?: "")

                                                if (fullTime.isNotBlank()) {
                                                    val formatter =
                                                        DateTimeFormatter.ofPattern(
                                                            "yyyy-MM-dd HH:mm:ss",
                                                            Locale.ENGLISH
                                                        )
                                                    val dateTime =
                                                        LocalDateTime.parse(fullTime, formatter)

                                                    val dayName =
                                                        dateTime.dayOfWeek.name.lowercase()
                                                            .replaceFirstChar { it.uppercase() } // "Tuesday"
                                                    val dayOfMonth = dateTime.dayOfMonth // 18
                                                    val outputFormatter =
                                                        DateTimeFormatter.ofPattern(
                                                            "HH:mm",
                                                            Locale.ENGLISH
                                                        )


                                                    listOfDates.add(DateOfTheWeek(
                                                        dateTime = fullTime,
                                                        dateName = dayName,
                                                        dateNumber = dayOfMonth,
                                                        listOfDates = value.map {
                                                            val time =
                                                                dateTime.format(outputFormatter)
                                                            Log.d(
                                                                "asfasdfasdfasfdd",
                                                                "${time}     ${it.time}"
                                                            )
                                                            TimeSlot(
                                                                time = time,
                                                                dateTime = it.time,
                                                                available = it.available


                                                            )
                                                        }

                                                    )
                                                    )
                                                }


                                            }


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
                                        }
                                        )


                                    }.addOnFailureListener {
                                        doctors.value = Resource.Failure(it.message.toString())

                                    }


                                //onResult(users)

                            }.addOnFailureListener {

                                doctors.value = Resource.Failure(it.message.toString())

                            }
                    )
                }
            } catch (e: Exception) {
                doctors.value = Resource.Failure(e.message.toString())
            }
        }
    }



    fun searchchhhhDoc(searchText:String){

        searchJob?.cancel()
        if(searchText.isBlank()){
            doctors.value =Resource.Success(emptyList())
            return
        }
        searchJob =viewModelScope.launch(Dispatchers.IO) {
            doctors.value =Resource.Loading()
            val db = FirebaseFirestore.getInstance()


            val doctorsByName = async {
                db.collection("doctor")
                    .orderBy("nameLowercase")
                    .startAt(searchText)
                    .endAt(searchText + "\uf8ff")
                    .get()
                    .await()
                    .mapNotNull { it.toObject(HomeDoctorUiItemWithout::class.java) }
            }
            val doctorsBySpeciality =  async {
                db.collection("doctor")
                    .orderBy("speciality")
                    .startAt(searchText)
                    .endAt(searchText + "\uf8ff")
                    .get()
                    .await()
                    .mapNotNull { it.toObject(HomeDoctorUiItemWithout::class.java) }
            }

            val timeHere =async {
                db.collection("TimeSlot")
                    .get()
                    .await()
                    .mapNotNull { it.toObject(TimeSlotFireBase::class.java) }
            }

            val (doctorsByNameRes,doctorsBySpecRes,time) = awaitAll(doctorsByName,doctorsBySpeciality,timeHere)

            val realDocByName =doctorsByNameRes as List<HomeDoctorUiItemWithout>
            val realDocBySpce =doctorsBySpecRes as List<HomeDoctorUiItemWithout>
            val realTime =time as List<TimeSlotFireBase>?


            val allDocs =realDocByName+realDocBySpce


            Log.d("sfaddfasdfasdfasdf","${realDocByName.size}   ${realDocBySpce.size}  ${searchText}")
            doctors.value =Resource.Success(allDocs.map { doctor ->

                val myMap =
                    realTime?.filter { it.doctor_id == doctor.id }
                        ?.groupBy {
                            it.time.substring(
                                0,
                                10
                            )
                        }//TODO:check this


                val listOfDates = mutableListOf<DateOfTheWeek>()


                myMap?.forEach() { key, value ->
                    val fullTime = (value.firstOrNull()?.time ?: "")

                    if (fullTime.isNotBlank()) {
                        val formatter =
                            DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss",
                                Locale.ENGLISH
                            )
                        val dateTime =
                            LocalDateTime.parse(fullTime, formatter)

                        val dayName =
                            dateTime.dayOfWeek.name.lowercase()
                                .replaceFirstChar { it.uppercase() } // "Tuesday"
                        val dayOfMonth = dateTime.dayOfMonth // 18
                        val outputFormatter =
                            DateTimeFormatter.ofPattern(
                                "HH:mm",
                                Locale.ENGLISH
                            )


                        listOfDates.add(DateOfTheWeek(
                            dateTime = fullTime,
                            dateName = dayName,
                            dateNumber = dayOfMonth,
                            listOfDates = value.map {
                                val time =
                                    dateTime.format(outputFormatter)
                                Log.d(
                                    "asfasdfasdfasfdd",
                                    "${time}     ${it.time}"
                                )
                                TimeSlot(
                                    time = time,
                                    dateTime = it.time,
                                    available = it.available


                                )
                            }

                        )
                        )
                    }


                }


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
            })

        }
    }

}
