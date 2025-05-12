package io.oitech.med_application.fragments

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.oitech.med_application.R
import io.oitech.med_application.fragments.doctor_details.DoctorItemForDetails
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItem
import io.oitech.med_application.fragments.homeFragment.HomeDoctorUiItemWithout
import io.oitech.med_application.utils.ComposableUtils.Space
import io.oitech.med_application.utils.Fonts
import io.oitech.med_application.utils.Resource
import androidx.compose.foundation.lazy.items
@Composable
fun SearchScreen(
    navigateBack: () -> Unit,
    navigateToDrugDetail: (Int) -> Unit,
    navigateToDoctor: (Int) -> Unit,
    viewModel: SearchViewModel
) {


    val searchText =remember{ mutableStateOf("")}
    Column {
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
                fontFamily = Fonts.semiBaldFontInter,
                text = "Search",
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

        Space(10.dp)
        OutlinedTextField(
            placeholder = {
                Text(
                    fontSize = 14.sp,
                    text = "Type here",
                    color = colorResource(id = R.color.text_gray)
                )
            },
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = io.oitech.med_application.Color.ColorScheduleWeakBlue,
                focusedContainerColor = io.oitech.med_application.Color.ColorScheduleWeakBlue
            ), value = searchText.value, onValueChange = {
                searchText.value =it
                viewModel.searchDoctors(searchText.value)
            })

        val doctors =viewModel.doctors.collectAsState()

        LaunchedEffect(doctors.value) {
            Log.d("asdfasdfasdfasdfasfd",doctors.value.toString())
        }



        LazyColumn {
            item {
                //doctors
                LazyRow(Modifier.fillMaxWidth()){

                }
            }
            item {
                //Drugs
                LazyRow(Modifier.fillMaxWidth()){

                }
            }
            item {
                //hospitals
                LazyRow (Modifier.fillMaxWidth()){
                    if(doctors.value is Resource.Success) {
                        val doctorList = (doctors.value as Resource.Success<List<HomeDoctorUiItem>>).data ?: emptyList()
                        items(doctorList) {
                            DoctorItemForDetails(doctor =it, withHorizontalPadding =true)
                        }
                    }
                }
            }
        }



    }
}