package io.oitech.med_application.fragments.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.collectAsState
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
import io.oitech.med_application.Color.ColorOfDetailsOfSchedule
import io.oitech.med_application.Color.ColorOfSpecialityInSchedule
import io.oitech.med_application.Color.ColorScheduleWeakBlue
import io.oitech.med_application.R
import io.oitech.med_application.fragments.MainViewModel
import io.oitech.med_application.utils.ComposableUtils.FirebaseImageLoader
import io.oitech.med_application.utils.ComposableUtils.Space
import io.oitech.med_application.utils.Resource
import io.oitech.med_application.utils.Utils.noRippleClickable

@Composable
fun ScheduleScreen(viewModel: MainViewModel) {
//    val scheduledItems = remember {
//        mutableStateOf(listOf<ScheduleUIItem>(
//            ScheduleUIItem(doctorName = "saasdsa", status = ScheduleStatus.COMPLETED, isConfirmed = true, doctorImage = "asd", doctorSpeciality = "speciality", time ="2025-03-18 14:30:45" )
//        ))
//    }

    val scheduledItems = viewModel.scheduleList.collectAsState().value



    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Space(24.dp)
        Row() {
            Text("Schedule", fontSize = 24.sp, color = Color.Black)
            Space()
            Icon(
                modifier = Modifier.align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.notification),
                contentDescription = null
            )
        }
        Space(40.dp)
        val selectedScheduleStatus = remember {
            mutableStateOf<ScheduleStatus>(ScheduleStatus.UPCOMING)
        }
        val isStatusUpcoming = selectedScheduleStatus.value == ScheduleStatus.UPCOMING

        val isStatusCompleted = selectedScheduleStatus.value == ScheduleStatus.COMPLETED

        val isStatusCanceled = selectedScheduleStatus.value == ScheduleStatus.CANCELED


        Row(
            Modifier

                .fillMaxWidth()
                .background(ColorScheduleWeakBlue, RoundedCornerShape(8.dp))
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .background(
                        if (isStatusUpcoming) {
                            colorResource(id = R.color.blue)
                        } else {
                            ColorScheduleWeakBlue
                        }, RoundedCornerShape(8.dp)
                    )
                    .padding(7.dp)
                    .noRippleClickable() {
                        selectedScheduleStatus.value = ScheduleStatus.UPCOMING
                    }
            ) {
                Text(
                    lineHeight = 32.sp,
                    text = "Upcoming", color = if (isStatusUpcoming) {
                        Color.White
                    } else {
                        Color.Black
                    }, fontSize = 14.sp, modifier = Modifier.align(Alignment.Center)
                )
            }



            Box(
                Modifier
                    .weight(1f)
                    .background(
                        if (isStatusCompleted) {
                            colorResource(id = R.color.blue)
                        } else {
                            ColorScheduleWeakBlue
                        }, RoundedCornerShape(8.dp)
                    )
                    .padding(7.dp)

                    .noRippleClickable() {
                        selectedScheduleStatus.value = ScheduleStatus.COMPLETED
                    }
            ) {
                Text(
                    lineHeight = 32.sp,

                    text = "Completed", color = if (isStatusCompleted) {
                        Color.White
                    } else {
                        Color.Black
                    }, fontSize = 14.sp, modifier = Modifier.align(Alignment.Center)
                )
            }




            Box(
                Modifier
                    .weight(1f)
                    .background(
                        if (isStatusCanceled) {
                            colorResource(id = R.color.blue)
                        } else {
                            ColorScheduleWeakBlue
                        }, RoundedCornerShape(8.dp)
                    )
                    .padding(7.dp)
                    .noRippleClickable() {
                        selectedScheduleStatus.value = ScheduleStatus.CANCELED
                    }
            ) {
                Text(
                    lineHeight = 32.sp,
                    text = "Canceled", color = if (isStatusCanceled) {
                        Color.White
                    } else {
                        Color.Black
                    }, fontSize = 14.sp, modifier = Modifier.align(Alignment.Center)
                )
            }


        }

        Space(20.dp)

        LazyColumn(Modifier.fillMaxWidth()) {
            if (scheduledItems is Resource.Success) {
                items(scheduledItems.data ?: emptyList()) {
                    if (it.status == selectedScheduleStatus.value) {
                        ScheduleListComposeItem(it, onReschedule = {}, onCancel = {})
                    }
                }
            }

        }


    }
}

@Composable
fun ScheduleListComposeItem(
    scheduleItem: ScheduleUIItem,
    onCancel: () -> Unit,
    onReschedule: () -> Unit
) {
    Column(
        Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                BorderStroke(
                    1.dp,
                    ColorScheduleWeakBlue
                ), RoundedCornerShape(8.dp)
            )
    ) {
        Space(14.dp)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 14.dp, end = 22.dp)
        ) {
            Column {
                Text(text = scheduleItem.doctorName, fontSize = 18.sp, color = Color.Black)
                Space(4.dp)
                Text(
                    text = scheduleItem.doctorSpeciality,
                    fontSize = 12.sp,
                    color = ColorOfSpecialityInSchedule,
                )

            }
            Space()
            FirebaseImageLoader(
                imagePath = scheduleItem.doctorImage, modifier = Modifier
                    .size(46.dp)
                    .clip(
                        CircleShape
                    ), contentScale =ContentScale.Crop
            )

        }
        Space(25.dp)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        {
            Icon(
                modifier = Modifier.size(13.dp),
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = null,
                tint = ColorOfDetailsOfSchedule
            )
            Space(6.dp)
            Text(
                text = scheduleItem.time.substring(0, 10).replace("-", "/"),
                color = ColorOfDetailsOfSchedule,
                fontSize = 12.sp
            )

            Space(10.dp)

            Icon(
                painter = painterResource(id = R.drawable.clock),
                contentDescription = null,
                tint = ColorOfDetailsOfSchedule
            )
            Space(6.dp)
            Text(
                text = scheduleItem.time.substring(11, 16),
                color = ColorOfDetailsOfSchedule,
                fontSize = 12.sp
            )

            Space(10.dp)

            Box(
                Modifier
                    .size(6.dp)
                    .align(Alignment.CenterVertically)
                    .background(
                        if (scheduleItem.isConfirmed) {
                            Color.Green
                        } else {
                            Color.Red
                        }, CircleShape
                    )
            )
            Space(6.dp)
            Text(
                text =
                if (scheduleItem.isConfirmed) {
                    "Confirmed"
                } else {
                    "Not Confrmed"
                }, color = ColorOfDetailsOfSchedule, fontSize = 12.sp
            )


        }
        Space(14.dp)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(ColorScheduleWeakBlue, RoundedCornerShape(8.dp))
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    lineHeight = 32.sp,
                    text = "Cancel",
                    fontSize = 14.sp,
                    color = ColorOfDetailsOfSchedule,
                    modifier = Modifier.align(Alignment.Center)
                )

            }
            Space(16.dp)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(colorResource(id = R.color.blue), RoundedCornerShape(8.dp))
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    lineHeight = 32.sp,
                    text = "Reschedule",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Space(16.dp)
    }
}

data class ScheduleUIItem(
    val doctorName: String,
    val doctorSpeciality: String,
    val doctorImage: String,
    val time: String,
    val isConfirmed: Boolean,
    val status: ScheduleStatus
)

enum class ScheduleStatus(
    val status: String
) {
    UPCOMING("Upcoming"),
    COMPLETED("Completed"),
    CANCELED("Canceled")
}




