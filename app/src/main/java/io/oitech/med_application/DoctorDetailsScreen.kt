package io.oitech.med_application

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import io.oitech.med_application.Color.ColorScheduleWeakBlue
import io.oitech.med_application.fragments.homeFragment.DateOfTheWeek
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem
import io.oitech.med_application.fragments.homeFragment.TimeSlot
import io.oitech.med_application.utils.ComposableUtils.FirebaseImageLoader
import io.oitech.med_application.utils.ComposableUtils.Space


@Composable
fun DoctorDetailsScreen(
    doctor: HomeDoctorUiItem,
    navigateToAppointment: (HomeDoctorUiItem) -> Unit,
    navigateBack: () -> Boolean,
    navigateToChat:(Int) ->Unit
) {
    val appointmentInfo = remember {
        mutableStateOf<HomeDoctorUiItem?>(null)
    }
    val selectedDate = remember { mutableStateOf<DateOfTheWeek?>(null) }
    val listOfTimeSlots = doctor.listOfTimes.find { it == selectedDate.value }
    val selectedTImeSliteIndex = remember {
        mutableStateOf(-1)
    }

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
            )
            {
                Image(
                    painter = painterResource(R.drawable.arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .align(
                            Alignment.CenterStart
                        )
                        .clickable {
                            navigateBack.invoke()
                        }
                )
                Text(
                    text = "Doctor Detail",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black
                )
                Image(
                    painter = painterResource(R.drawable.more_button),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )


//

            }
            Space(34.dp)
            DoctorItemForDetails(doctor, withHorizontalPadding = true)

            Spacer(modifier = Modifier.height(30.dp))


            Text(
                text = "About",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 22.dp)
            )
            Text(
                lineHeight = 20.sp,
                text = doctor.description,
                fontSize = 12.sp,
                color = colorResource(id = R.color.doctor_discription_color),
                modifier = Modifier.padding(start = 22.dp)
            )



            Space(30.dp)

            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier =Modifier.fillMaxWidth()) {
                items(doctor.listOfTimes.size,) { dateNum ->
                    SelectedDateSlot(
                        doctor.listOfTimes[dateNum],
                        selectedDate.value == doctor.listOfTimes[dateNum],
                        onSelect = {
                            selectedTImeSliteIndex.value = -1
                            selectedDate.value = doctor.listOfTimes[dateNum]
                        }
                    )
                }
            }
            Space(30.dp)

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(horizontal = 20.dp)
                    .background(
                        colorResource(id = R.color.line_color)
                    )
            )
            Space(30.dp)



            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(horizontal = 14.dp)
            ) {
                itemsIndexed(listOfTimeSlots?.listOfDates ?: emptyList()) { index, item ->
                    TimeSlotGridItem(
                        item = item,
                        isSelected = selectedTImeSliteIndex.value == index,
                        onSelect = {
                            selectedTImeSliteIndex.value = index
                        })
                }
            }

        }
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Box(
                Modifier
                    .size(50.dp)
                    .clickable {
                        navigateToChat(doctor.id)
                    }
                    .background(
                        colorResource(id = R.color.strange_color),
                        RoundedCornerShape(16.dp)
                    )
                    .align(Alignment.CenterVertically)
            ) {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.chat_profile_item),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(18.dp))

            Column(
                Modifier
                    .background(colorResource(id = R.color.blue), RoundedCornerShape(32.dp))
                    .weight(1f)
                    .clickable {
                        val selectedTimeToSend =
                            selectedDate.value?.listOfDates?.get(selectedTImeSliteIndex.value)
                        if (selectedTimeToSend != null) {
                            val date =
                                selectedDate.value?.copy(listOfDates = listOf(selectedTimeToSend))
                            if (date != null) {
                                navigateToAppointment.invoke(
                                    HomeDoctorUiItem(
                                        id = doctor.id,
                                        description = doctor.description,
                                        name = doctor.name,
                                        distance = doctor.distance,
                                        rating = doctor.rating,
                                        speciality = doctor.speciality,
                                        image = doctor.image,
                                        listOfTimes = listOf(date),
                                        hospitalId = doctor.hospitalId,
                                        price = doctor.price

                                    )
                                )
                            }
                        }

                    }
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Book Apointment",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Space(16.dp)

            }


        }
    }
}

@Composable
fun TimeSlotGridItem(item: TimeSlot, isSelected: Boolean, onSelect: () -> Unit) {
    Box(
        Modifier
            .padding(horizontal = 6.dp)

            .background(
                if (isSelected) {
                    colorResource(id = R.color.blue)
                } else {
                    Color.White
                }, RoundedCornerShape(16.dp)
            )
            .border(
                BorderStroke(
                    1.dp,
                    colorResource(id = R.color.time_slot_border_color)
                ),
                RoundedCornerShape(16.dp)
            )
            .clickable {
                if (item.available) {
                    onSelect.invoke()
                }
            }
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 10.dp),
            text = item.time, fontSize = 12.sp, color = if (isSelected) {
                Color.White
            } else {
                if (item.available) {
                    Color.Black
                } else {
                    colorResource(id = R.color.time_slot_border_color)
                }
            }
        )
    }
}

@Composable
fun SelectedDateSlot(dateOfTheWeek: DateOfTheWeek, selected: Boolean, onSelect: () -> Unit) {
    Box(
        Modifier
            .padding(horizontal = 6.dp)
            .height(64.dp)
            .width(46.dp)
            .background(
                if (selected) {
                    colorResource(id = R.color.blue)
                } else {
                    Color.White
                }, RoundedCornerShape(16.dp)
            )
            .border(
                BorderStroke(
                    1.dp, if (selected) {
                        Color.Transparent
                    } else {
                        ColorScheduleWeakBlue
                    }
                ), RoundedCornerShape(16.dp)
            )
            .clickable {
                onSelect.invoke()
            }
    ) {
        Column(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = dateOfTheWeek.dateName.substring(0..3), color = if (selected) {
                    Color.White
                } else {
                    Color.Black
                }, fontSize = 10.sp, modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = dateOfTheWeek.dateNumber.toString(), color = if (selected) {
                    Color.White
                } else {
                    Color.Black
                }, fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DoctorItemForDetails(doctor: HomeDoctorUiItem, withHorizontalPadding: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (withHorizontalPadding) {
                    28.dp
                } else {
                    0.dp
                }
            )
    )
    {


        Log.d("slkdjflksjdfkljdsfkl",doctor.toString())
        if(doctor.image.isNotBlank()
            ) {
            FirebaseImageLoader(
                imagePath = doctor.image, modifier = Modifier
                    .size(115.dp)
                    .clip(RoundedCornerShape(8.dp),), contentScale = ContentScale.Crop
            )

        }

//        Image(
//            painter = painterResource(id = R.drawable.doctor_mock_image),
//            contentDescription = null,
//            modifier = Modifier
//                .size(115.dp)
//                .clip(RoundedCornerShape(8.dp)),
//            contentScale = ContentScale.Crop
//        )
        Column(Modifier.padding(start = 18.dp)) {
            Text(text = doctor.name, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = doctor.speciality,
                color = colorResource(id = R.color.text_gray),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.background(
                    colorResource(id = R.color.strange_color),
                    RoundedCornerShape(2.dp)
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 4.dp, end = 5.dp)
                )
                Text(
                    text = doctor.rating,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp, end = 3.dp, bottom = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = null,
                    Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = doctor.distance.toString(),
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.text_gray)
                )
            }


        }
    }
}


//@Composable
//@Preview
//fun preview() {
//    DoctorDetailsScreen(
//        HomeDoctorUiItem(
//            "Dr. Smith",
//            "https://example.com/image1.jpg",
//            "Cardiologist",
//            6000.0,
//            "5.0"
//        ),
//
//        )
//}