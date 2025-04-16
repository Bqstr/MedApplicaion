package io.oitech.med_application

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.oitech.med_application.Color.Color_Platinum200
import io.oitech.med_application.Color.Color_Platinum50
import io.oitech.med_application.Color.Color_Platinum500
import io.oitech.med_application.fragments.MainViewModel
import io.oitech.med_application.utils.ComposableUtils.Space
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    doctor: HomeDoctorUiItem,
    selectedTimeAndDate: String,
    navigateBacktoHome: () -> Unit,
    navigateToChatWithDoctor: () -> Unit,
    navigateBack:() ->Unit,
    viewModel:MainViewModel
) {

    val reason = remember {
        mutableStateOf("")
    }
    val showReasonModalBottomSheet = remember {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState()

    val consultationPrice = remember {
        mutableStateOf(1000)
    }
    val adminFee = remember {
        mutableStateOf(1000)
    }
    val additionalDiscount = remember {
        mutableStateOf(0)
    }
    val totalPrice = consultationPrice.value + adminFee.value - additionalDiscount.value

    val showSuccessDialog = remember {
        mutableStateOf(false)
    }

    if (showSuccessDialog.value) {
        SuccessAppointmentDialog(
            onDismiss = {
                showSuccessDialog.value = false
                navigateBacktoHome.invoke()
            }, onChat = {
                navigateToChatWithDoctor()
            }
        )
    }

    if(showReasonModalBottomSheet.value){
        ModalBottomSheet(containerColor = Color.White,onDismissRequest = { showReasonModalBottomSheet.value = false }) {
            val innerReason = remember {
                mutableStateOf(reason.value)
            }
            Column(
                Modifier.padding(horizontal = 16.dp),
                ) {
                Space(40.dp)
                Text(text = "Reason", fontSize = 18.sp,color =Color.Black)
                Space(8.dp)
                BasicTextField(
                    cursorBrush = SolidColor(Color.Black),
                    modifier = Modifier
                        .height(104.dp),
                    value = innerReason.value,
                    onValueChange = {
                        innerReason.value = it
                    },
                    decorationBox = { innerTextField ->
                        Row(
                            Modifier
                                .background(Color_Platinum50, RoundedCornerShape(12.dp))
                                .height(104.dp)
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (innerReason.value.isEmpty()) {
                                            Color_Platinum200
                                        } else {
                                            Color_Platinum500
                                        }
                                    ), RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp)
                                .fillMaxWidth()

                        ) {

                            Box() {
                                if (innerReason.value.isEmpty()) {
                                    Text(
                                        "Write down your reason here",
                                        color = Color_Platinum500,
                                        fontSize = 14.sp,
                                    )
                                }
                                innerTextField()
                            }

                        }
                    },
                )
                Space(8.dp)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            colorResource(id = R.color.blue),
                            RoundedCornerShape(32.dp)
                        )
                        .clickable {
                            showReasonModalBottomSheet.value = false
                            reason.value = innerReason.value
                        }
                ) {
                    Text(
                        text = "Save",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(vertical = 14.dp)
                    )
                }
                Space(8.dp)


            }
        }
    }
    Box(
        Modifier.padding(horizontal = 20.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        )
        {
            val context = LocalContext.current

            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 34.dp)
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
            DoctorItemForDetails(doctor = doctor, withHorizontalPadding = false)

            Space(dp = 30.dp)

            Row {
                Text(
                    text = "Date",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Space()
                Text(

                    text = "Change",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.text_gray),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            navigateBack()
                        }
                )
            }
            Space(dp = 10.dp)

            Row {
                Box(
                    Modifier
                        .size(36.dp)
                        .background(colorResource(id = R.color.strange_color), CircleShape)
                ) {
                    Icon(
                        tint = colorResource(id = R.color.blue),
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )

                }
                Space(14.dp)
                Text(
                    text = selectedTimeAndDate,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }


            Space(12.dp)
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.strange_color))
            )
            Space(12.dp)





            Row {
                Text(
                    text = "Reason",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Space()
                Text(
                    text = "Change",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.text_gray),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            showReasonModalBottomSheet.value = true
                        }
                )
            }
            Space(dp = 10.dp)

            Row {
                Box(
                    Modifier
                        .size(36.dp)
                        .background(colorResource(id = R.color.strange_color), CircleShape)
                ) {
                    Icon(
                        tint = colorResource(id = R.color.blue),
                        painter = painterResource(id = R.drawable.reason_icon),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )

                }
                Space(14.dp)
                Text(
                    text = reason.value,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Space(12.dp)
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.strange_color))
            )
            Space(12.dp)

            PaymentDetailsPart(consultationPrice.value, adminFee.value, additionalDiscount.value)//TODO:chage when backend will added

            Space(15.dp)
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.strange_color))
            )
            Space(15.dp)


            Text(text = "Payment Method", fontSize = 16.sp, color = Color.Black)

            Space(16.dp)

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(
                        BorderStroke(1.dp, colorResource(id = R.color.strange_color)),
                        RoundedCornerShape(12.dp)
                    )
            )
            {

                Image(
                    painter = painterResource(id = R.drawable.visa),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 22.dp, top = 15.dp, bottom = 15.dp)
                )

                Space()


                Text(
                    text = "Change",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.text_gray),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Space(14.dp)


            }


        }

        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .align(Alignment.BottomCenter)
        ) {
            Column() {
                Text(
                    text = "Total",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.different_gray)
                )
                Text(text = stringResource(id = R.string.price_tenge,totalPrice), fontSize = 18.sp, color = Color.Black)
            }
            Space()
            Box(
                modifier = Modifier
                    .background(
                        colorResource(id = R.color.blue),
                        RoundedCornerShape(32.dp)
                    )
                    .clickable {
                        viewModel.scheduleTime(doctorName = doctor.name, doctor_id = doctor.id, speciality = doctor.speciality, doctorImage = doctor.image, time = selectedTimeAndDate)
                        showSuccessDialog.value = true
                        navigateBacktoHome.invoke()
                    }
            ) {
                Text(
                    text = "Booking",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 14.dp, horizontal = 68.dp)
                )
            }
        }

    }

}

@Composable
fun PaymentDetailsPart(consultationPrice: Int, adminFee: Int, additionalDiscount: Int) {
    Column(Modifier.fillMaxWidth()) {
        val total = consultationPrice + adminFee - additionalDiscount
        Text(text = "Payment Details", fontSize = 16.sp, color = Color.Black)
        Space(14.dp)

        Row(
            Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Consultation",
                fontSize = 14.sp,
                color = colorResource(id = R.color.different_gray)
            )
            Space()
            Text(text =  stringResource(id = R.string.price_tenge,consultationPrice), color = Color.Black, fontSize = 14.sp)
        }
        Space(12.dp)
        Row(
            Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Admin Fee",
                fontSize = 14.sp,
                color = colorResource(id = R.color.different_gray)
            )
            Space()
            Text(text = stringResource(id = R.string.price_tenge,adminFee), color = Color.Black, fontSize = 14.sp)
        }
        Space(12.dp)
        Row(
            Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Additional Discount",
                fontSize = 14.sp,
                color = colorResource(id = R.color.different_gray)
            )
            Space()
            Text(
                text = if (additionalDiscount <= 0) {
                    "-"
                } else {
                    stringResource(id = R.string.price_tenge,additionalDiscount)
                }, color = Color.Black, fontSize = 14.sp
            )
        }

        Space(12.dp)
        Row(
            Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Total",
                fontSize = 14.sp,
                color = colorResource(id = R.color.different_gray)
            )
            Space()
            Text(text =  stringResource(id = R.string.price_tenge,total), color = Color.Black, fontSize = 14.sp)
        }

    }

}

@Composable
fun SuccessAppointmentDialog(onDismiss: () -> Unit, onChat: () -> Unit) {

    Dialog(onDismissRequest = onDismiss) {


        Column(
            Modifier
                .background(Color.White, RoundedCornerShape(24.dp))
                .width(326.dp)
                .height(424.dp)
        )
        {
            Space(56.dp)
            Box(
                Modifier
                    .size(102.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(colorResource(id = R.color.custom_alert_success_color), CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.done_24px),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                    tint = colorResource(
                        id = R.color.blue
                    )
                )
            }
            Space(40.dp)
            Text(
                text = "Payment Suceess", fontSize = 20.sp,
                color = Color.Black, modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Space(8.dp)

            Text(
                text = "Your payment has been successful, you can have a consultation session with your trusted doctor",
                color = colorResource(
                    id = R.color.different_gray
                ),
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 28.dp),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Space(24.dp)

            Box(
                Modifier
                    .background(colorResource(id = R.color.blue), RoundedCornerShape(32.dp))
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        onChat.invoke()
                        onDismiss.invoke()
                    }
            ) {
                Text(
                    text = "Chat Doctor",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 16.dp, horizontal = 44.dp),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }


        }
    }
}



