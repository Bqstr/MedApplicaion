package io.oitech.med_application.fragments.chat

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.oitech.med_application.Color.ColorOfMessageInChat
import io.oitech.med_application.Color.ColorScheduleWeakBlue
import io.oitech.med_application.R
import io.oitech.med_application.fragments.MainViewModel
import io.oitech.med_application.utils.ComposableUtils
import io.oitech.med_application.utils.ComposableUtils.Space
import io.oitech.med_application.utils.Fonts

@Composable
fun ChatScreen(onBackPress: () -> Unit, chatRoomModel: ChatRoomModel, viewModel: MainViewModel) {

    val chatText = remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        viewModel.getMessagesOfOneRoom(chatRoomModel.doctorId)
    }
    val messages =
        viewModel.messages.collectAsState().value.get(chatRoomModel.doctorId) ?: emptyList()

    Box(Modifier.fillMaxSize()) {


        Column() {
            Space(16.dp)
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Space(30.dp)
                Icon(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = null,
                    modifier = Modifier.align(
                        Alignment.CenterVertically
                    )
                )
                Space(26.dp)

                Text(
                    fontFamily = Fonts.semiBaldFontInter,
                    text = chatRoomModel.doctorName,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Space()

                Icon(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.video_call),
                    contentDescription = null
                )

                Space(16.dp)

                Icon(
                    tint = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(16.dp),
                    painter = painterResource(id = R.drawable.telephone),
                    contentDescription = null
                )

                Space(16.dp)

                Icon(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.more_button),
                    contentDescription = null
                )

                Space(16.dp)

            }


            Column(
                Modifier
                    .height(77.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(
                        BorderStroke(1.dp, colorResource(id = R.color.strange_color)),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(20.dp)
            ) {
                Space()
                Text(
                    fontFamily = Fonts.semiBaldFontInter,

                    text = "Consultation Start",
                    color = colorResource(id = R.color.blue),
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Space(6.dp)


                Text(
                fontFamily = Fonts.semiBaldFontInter,

                    text = "You can consult your problem to the doctor",
                    color = colorResource(id = R.color.text_gray),
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Space()
            }

            Log.d("jrjujrufjrufjurfjr", messages.toString())


            LazyColumn(
                reverseLayout = true, // Items appear from bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                items(messages.size) { messageNum ->
                    val message = messages[messageNum]
                    if (message.isMyMessage
                    ) {
                        MyMessage(message = message.message, time = message.time)
                    } else {
                        Log.d("asdfasdfsadfasdf", "aaaaaaaaa ths")
                        DoctorMessage(
                            doctorName = chatRoomModel.doctorName,
                            doctorImage = chatRoomModel.doctorName,
                            message = message.message,
                            time = message.time,
                            showTopBar = if (messages.getOrNull(messageNum - 1)?.isMyMessage == true) {
                                true
                            } else if (messageNum == 0) {
                                true

                            } else {
                                false
                            }
                        )
                    }
                }

            }


        }



        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Row(modifier = Modifier.padding(horizontal = 20.dp)) {
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
                        unfocusedContainerColor = ColorScheduleWeakBlue,
                        focusedContainerColor = ColorScheduleWeakBlue
                    ), value = chatText.value, onValueChange = {
                        chatText.value = it
                    })

                Space(16.dp)
                Row(
                    Modifier
                        .background(
                            colorResource(id = R.color.blue),
                            roundedCornerShape()
                        )
                        .clip(
                            RoundedCornerShape(30.dp)

                        )
                        .clickable {
                            chatText.value =""
                            viewModel.writeMessage(
                                doctor_id = chatRoomModel.doctorId,
                                message = chatText.value,
                                isMyMessage = true
                            )
                        }
                ) {

                    Space()
                    Text(
                        text = "Send",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(vertical = 16.dp)
                    )
                    Space()

                }
            }
            Space(20.dp)
        }
    }
}

@Composable
private fun roundedCornerShape() = RoundedCornerShape(30.dp)

@Composable
fun DoctorMessage(
    doctorName: String,
    doctorImage: String,
    message: String,
    time: String,
    showTopBar: Boolean
) {
    val timePassed = "asdfsfsdf"
    Column() {
        if (showTopBar) {
            Row() {
                ComposableUtils.FirebaseImageLoader(
                    imagePath = doctorImage, modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                        .clip(
                            CircleShape
                        ), contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(13.dp)
                ) {
                    Text(
                        fontFamily = Fonts.semiBaldFontInter,
                        text = doctorName, fontSize = 14.sp, color = Color.Black)
                    Space(4.dp)
                    Text(
                    fontFamily = Fonts.mediumFontInter,

                        text = timePassed,
                        fontSize = 10.sp,
                        color = colorResource(id = R.color.text_gray)
                    )
                }
            }
        }
        Space(8.dp)
        Box(
            modifier = Modifier.background(
                colorResource(id = R.color.strange_color),
                RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 8.dp)
            )
        ) {
            Text(
                fontFamily = Fonts.regularFontInter,

                text = message,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 9.dp, horizontal = 15.dp),
                color = ColorOfMessageInChat,
                fontSize = 14.sp
            )
        }


    }
}


@Composable
fun MyMessage(message: String, time: String) {
    Row() {
        Space()
        Column() {
            Space(10.dp)
            Box(
                modifier = Modifier.background(
                    colorResource(id = R.color.strange_color),
                    RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp, bottomStart = 8.dp)
                )
            ) {
                Text(
                    text = message,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 9.dp, horizontal = 15.dp),
                    color = ColorOfMessageInChat,
                    fontSize = 14.sp
                )
            }
        }
    }
}


data class ChatRoomModel(
    val doctorId: Int = 0,
    val lastMessage: String = "",
    val lastMessageTime: String = "2025-03-18 14:30:45",
    val userUid: String = "",
    val doctorName: String = "",
    val doctorNumber: String = "",
    val doctorImage: String = ""
)


data class ChatMessageModel(
    val isMyMessage: Boolean = false,
    val doctorId: Int = 0,
    val message: String = "",
    val time: String = "",
    val userUid: String = ""
)