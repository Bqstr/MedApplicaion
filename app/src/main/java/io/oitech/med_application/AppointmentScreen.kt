package io.oitech.med_application

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.oitech.med_application.ComposableUtils.Space
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem
import java.time.LocalDateTime


@Composable
fun AppointmentScreen(doctor: HomeDoctorUiItem,selectedTimeAndDate:LocalDateTime) {
    val scrollState = rememberScrollState()
    Box(Modifier.padding(horizontal = 20.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        )
        {
            val context = LocalContext.current

            val reason = remember { mutableStateOf("Reason") }
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
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
            DoctorItemForDetails(doctor = doctor)

            Space(dp = 18.dp)

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
                    modifier = Modifier.align(Alignment.CenterVertically)
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
                    text = Utils.getItemForSelectedTime(selectedTimeAndDate),
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }


            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.strange_color))
                    .padding(vertical = 12.dp)
            )



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
                    modifier = Modifier.align(Alignment.CenterVertically)
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

            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.strange_color))
                    .padding(top = 12.dp, bottom = 14.dp)
            )

            PaymentDetailsPart(1.0, 1.0, 1.0)//TODO:chage when backend will added

            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.strange_color))
                    .padding(top = 15.dp, bottom = 15.dp)
            )


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

                Icon(
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


            }


        }

        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)){
            Column() {
                Text(text = "Total", fontSize = 14.sp,)
            }
        }

    }

}

@Composable
fun PaymentDetailsPart(consultationPrice:Double,adminFee:Double,additionalDiscount:Double) {
    Column(Modifier.fillMaxWidth()) {
        val total =consultationPrice+adminFee-additionalDiscount
        Text(text = "Payment Details", fontSize = 16.sp, color = Color.Black)
        Space(14.dp)

        Row(
            Modifier.fillMaxWidth()
        ){
            Text(text = "Consultation", fontSize = 14.sp,color = colorResource(id = R.color.different_gray))
            Space()
            Text(text = "${consultationPrice}",color = Color.Black, fontSize = 14.sp)
        }
        Space(12.dp)
        Row(
            Modifier.fillMaxWidth()
        ){
            Text(text = "Admin Fee", fontSize = 14.sp,color = colorResource(id = R.color.different_gray))
            Space()
            Text(text = "${adminFee}",color = Color.Black, fontSize = 14.sp)
        }
        Space(12.dp)
        Row(
            Modifier.fillMaxWidth()
        ){
            Text(text = "Admin Fee", fontSize = 14.sp,color = colorResource(id = R.color.different_gray))
            Space()
            Text(text = if(adminFee<=0){
                "-"
            }else{
                adminFee.toString()
            },color = Color.Black, fontSize = 14.sp)
        }

        Space(12.dp)
        Row(
            Modifier.fillMaxWidth()
        ){
            Text(text = "Total", fontSize = 14.sp,color = colorResource(id = R.color.different_gray))
            Space()
            Text(text = total.toString(),color = Color.Black, fontSize = 14.sp)
        }







    }

}


