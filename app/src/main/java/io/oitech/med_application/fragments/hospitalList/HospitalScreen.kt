package io.oitech.med_application.fragments.hospitalList

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import io.oitech.med_application.Color.ColorScheduleWeakBlue
import io.oitech.med_application.R
import io.oitech.med_application.fragments.MainViewModel
import io.oitech.med_application.utils.ComposableUtils
import io.oitech.med_application.utils.ComposableUtils.Space
import io.oitech.med_application.utils.Fonts
import io.oitech.med_application.utils.Resource
import io.oitech.med_application.utils.Utils.noRippleClickable

@Composable
fun HospitalScreen(navigateBack: () -> Unit, viewModel: MainViewModel) {
    LaunchedEffect(Unit) {
        viewModel.getHospitals()
    }

    val hospitals = viewModel.hospitals.collectAsState().value

    Column() {
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
                    .noRippleClickable {
                        navigateBack.invoke()
                    }
            )
            Text(
                fontFamily = Fonts.semiBaldFontInter,
                text = "Top Hospital",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black
            )
            Image(
                painter = painterResource(R.drawable.more_button),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        Space(25.dp)
        when (hospitals) {
            is Resource.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(hospitals.data ?: emptyList()) {
                        HospitalListItemCompose(it)
                    }
                }
            }

            is Resource.Loading -> {
                Column(Modifier.fillMaxWidth()) {
                    Space()
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    Space()
                }
            }

            is Resource.Failure -> {

            }

            else -> {

            }
        }

    }
}

@Composable
fun HospitalListItemCompose(hospital: HospitalModel) {
    Row(
        Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, ColorScheduleWeakBlue), RoundedCornerShape(12.dp))
    ) {
        if (hospital.image.isNotBlank()
        ) {
            ComposableUtils.FirebaseImageLoader(
                imagePath = hospital.image, modifier = Modifier
                    .size(115.dp)
                    .clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop
            )

        } else {
            Box(
                Modifier
                    .size(115.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {

            }
        }
        Column(Modifier.padding(start = 18.dp)) {
            Space(14.dp)
            Text(
                fontFamily = Fonts.semiBaldFontInter,

                text = hospital.name, fontSize = 18.sp, color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                fontFamily = Fonts.mediumFontInter,

                text = hospital.organization,
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
                    fontFamily = Fonts.mediumFontInter,

                    text = hospital.rating.toString(),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp, end = 3.dp, bottom = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = null,
                    Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    fontFamily = Fonts.mediumFontInter,
                    text = hospital.distance.toString(),
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.text_gray)
                )
            }
            Space(dp = 13.dp)
        }
    }
}

data class HospitalModel(
    val nameLowercase :String,
    val distance: String = "",
    val image: String = "",
    val name: String = "",
    val organization: String = "",
    val rating: Double = 0.0
)

