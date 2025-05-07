package io.oitech.med_application.fragments.chatList

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.oitech.med_application.Color.ColorOfMessageInChat
import io.oitech.med_application.Color.ColorOfSpecialityInSchedule
import io.oitech.med_application.Color.ColorScheduleWeakBlue
import io.oitech.med_application.R
import io.oitech.med_application.fragments.MainViewModel
import io.oitech.med_application.fragments.chat.ChatRoomModel
import io.oitech.med_application.utils.ComposableUtils
import io.oitech.med_application.utils.ComposableUtils.Space
import io.oitech.med_application.utils.Fonts
import io.oitech.med_application.utils.Utils
import io.oitech.med_application.utils.Utils.noRippleClickable

@Composable
fun MessagesScreen(
    navigateBack: () -> Unit,
    viewModel: MainViewModel,
    navigateToChatRoom: (Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        val messageRoomsList = viewModel.messageRooms.collectAsState().value ?: emptyList()
        Space(24.dp)
        Row() {
            Text(

                fontFamily = Fonts.semiBaldFontInter,
               text = "Messages", fontSize = 24.sp, color = Color.Black)
            Space()
            Icon(
                modifier = Modifier.align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.notification),
                contentDescription = null
            )
        }
        Space(40.dp)
        val selectedScheduleStatus = remember {
            mutableStateOf<MessagesRoomType>(MessagesRoomType.ALL)
        }
        val isStatusAll = selectedScheduleStatus.value == MessagesRoomType.ALL

        val isStatusGroup = selectedScheduleStatus.value == MessagesRoomType.GROUP

        val isStatusPrivate = selectedScheduleStatus.value == MessagesRoomType.PRIVATE


        Row(
            Modifier

                .fillMaxWidth()
                .background(ColorScheduleWeakBlue, RoundedCornerShape(8.dp))
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .background(
                        if (isStatusAll) {
                            colorResource(id = R.color.blue)
                        } else {
                            ColorScheduleWeakBlue
                        }, RoundedCornerShape(8.dp)
                    )
                    .padding(7.dp)
                    .noRippleClickable() {
                        selectedScheduleStatus.value = MessagesRoomType.ALL
                    }
            ) {
                Text(
                    fontFamily = Fonts.semiBaldFontInter,
                    lineHeight = 32.sp,
                    text = "All", color = if (isStatusAll) {
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
                        if (isStatusGroup) {
                            colorResource(id = R.color.blue)
                        } else {
                            ColorScheduleWeakBlue
                        }, RoundedCornerShape(8.dp)
                    )
                    .padding(7.dp)

                    .noRippleClickable() {
                        selectedScheduleStatus.value = MessagesRoomType.GROUP
                    }
            ) {
                Text(
                    fontFamily = Fonts.semiBaldFontInter,
                    lineHeight = 32.sp,

                    text = "Group", color = if (isStatusGroup) {
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
                        if (isStatusPrivate) {
                            colorResource(id = R.color.blue)
                        } else {
                            ColorScheduleWeakBlue
                        }, RoundedCornerShape(8.dp)
                    )
                    .padding(7.dp)
                    .noRippleClickable() {
                        selectedScheduleStatus.value = MessagesRoomType.PRIVATE
                    }
            ) {
                Text(
                    fontFamily = Fonts.semiBaldFontInter,
                    lineHeight = 32.sp,
                    text = "Private", color = if (isStatusPrivate) {
                        Color.White
                    } else {
                        Color.Black
                    }, fontSize = 14.sp, modifier = Modifier.align(Alignment.Center)
                )
            }


        }

        Space(20.dp)

        LazyColumn(Modifier.fillMaxWidth()) {
            items(messageRoomsList) {
                MessageRoomComposeItem(it, goToChat =navigateToChatRoom)
            }
        }
    }
}

@Composable
fun MessageRoomComposeItem(
    item: ChatRoomModel,
    goToChat: (Int)//doctorId
    -> Unit
) {
    val lastMessageTime =Utils.formatMessageTimeForLastMessage(item.lastMessageTime)
    Box(Modifier.noRippleClickable {
        goToChat(item.doctorId)
    }) {
        Row(Modifier.padding(vertical = 12.dp)) {
            ComposableUtils.FirebaseImageLoader(
                imagePath = item.doctorImage, modifier = Modifier
                    .size(50.dp)
                    .clip(
                        CircleShape
                    )
                    .align(Alignment.CenterVertically), contentScale = ContentScale.Fit
            )

            Space(15.dp)

            Column(Modifier.align(Alignment.CenterVertically)) {
                Row() {
                    Text(
                        fontFamily = Fonts.semiBaldFontInter,

                        text = item.doctorName,
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Space()
                    Text(
                        fontFamily = Fonts.regularFontInter,

                        color = ColorOfMessageInChat,
                        text =   lastMessageTime,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        fontSize = 12.sp
                    )
                }
                Space(dp = 3.dp)
                Text(
                    fontFamily = Fonts.semiBaldFontInter,

                    text = item.lastMessage,
                    color = ColorOfSpecialityInSchedule,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Space(10.dp)


        }
    }
}

enum class MessagesRoomType(val type: String) {
    ALL("ALL"),
    GROUP("GROUP"),
    PRIVATE("PRIVATE"),

}