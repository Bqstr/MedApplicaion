package io.oitech.med_application

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.oitech.med_application.ComposableUtils.Space
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem
import java.time.LocalDateTime


@Composable
fun DoctorDetailsScreen(doctor: HomeDoctorUiItem) {
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp,end =24.dp, top = 24.dp)
            )
            {
                Image(
                    painter = painterResource(R.drawable.arrow),
                    contentDescription = null,
                    modifier = Modifier.align(
                        Alignment.CenterStart
                    )
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
            DoctorItemForDetails(doctor)

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

            val selectedDate = remember { mutableStateOf<DateOfTheWeek?>(null) }
            LazyRow(Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 12.dp)) {
                items(doctor.listOfTimes.size) { dateNum ->
                    SelectedDateSlot(
                        doctor.listOfTimes[dateNum],
                        selectedDate.value == doctor.listOfTimes[dateNum]
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

            val listOfTimeSlots = doctor.listOfTimes.find { it == selectedDate.value }

            val selectedTImeSliteIndex = remember {
                mutableStateOf(-1)
            }

            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
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
                )
            )
    ) {
        Text(
            modifier =Modifier.align(Alignment.Center),
            text = item.time, fontSize = 12.sp, color = if (isSelected) {
                Color.White
            }else{
                if(item.available){
                    Color.Black
                }else{
                    colorResource(id = R.color.time_slot_border_color)
                }
            }
        )
    }
}

@Composable
fun SelectedDateSlot(dateOfTheWeek: DateOfTheWeek, selected: Boolean) {
    Box(
        Modifier
            .height(64.dp)
            .width(46.dp)
            .background(
                if (selected) {
                    colorResource(id = R.color.blue)
                } else {
                    Color.White
                }, RoundedCornerShape(16.dp)
            )
    ) {
        Column {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = dateOfTheWeek.dateName, color = if (selected) {
                    Color.White
                } else {
                    Color.Black
                }, fontSize = 10.sp, modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = dateOfTheWeek.dateName, color = if (selected) {
                    Color.White
                } else {
                    Color.Black
                }, fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        }
    }
}



@Composable
fun DoctorItemForDetails(doctor:HomeDoctorUiItem){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
    )
    {
        Image(
            painter = painterResource(id = R.drawable.doctor_mock_image),
            contentDescription = null,
            modifier = Modifier
                .size(115.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
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

data class DateOfTheWeek(
    val dateTime:LocalDateTime,
    val dateNumber: Int,
    val dateName: String,
    val listOfDates: List<TimeSlot>
)

data class TimeSlot(val time: String, val available: Boolean)

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