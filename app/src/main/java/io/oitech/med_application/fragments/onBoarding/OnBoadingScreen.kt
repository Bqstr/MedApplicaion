package io.oitech.med_application.fragments.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.oitech.med_application.Color.ColorOnBoardingIndicator
import io.oitech.med_application.Color.SecondaryColor
import io.oitech.med_application.R
import io.oitech.med_application.utils.Fonts
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(navigateToMainScreen: () -> Unit ) {
    Box(Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(initialPage = 1) {
            2
        }

        val scope = rememberCoroutineScope()
        HorizontalPager(
            pagerState,
            Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Box(
                Modifier.fillMaxSize()
            ) {
                when (pagerState.currentPage) {
                    1 -> {
                        Image(
                            painter = painterResource(id = R.drawable.onboarding_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .width(250.dp)
                                .height(100.dp)
                                .align(Alignment.Center)
                        )

                        Column(
                            Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)  // Ensure it aligns at the bottom
                                .padding(horizontal = 56.dp, vertical = 24.dp)
                        ) {
                            Text(
                                fontFamily = Fonts.semiBaldFontInter,
                                text = "Find Medical Facilities Easily",
                                fontSize = 24.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                lineHeight = 32.sp
                            )
                            Text(
                                fontFamily = Fonts.lightFontInter,

                                text = "Discover nearby medical facilities with comprehensive details to help you choose the right care.",
                                fontSize = 14.sp,
                                color = SecondaryColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            OnBoardingPagerButtons(currentState = pagerState.currentPage, count = pagerState.pageCount)

                            Spacer(modifier = Modifier.height(24.dp))

                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navigateToMainScreen()
                                    }
                                    .background(
                                        colorResource(id = R.color.blue),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    fontFamily = Fonts.semiBaldFontInter,
                                    text = "Get Started", color = Color.White, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                    }

                    0 -> {
                        Box(Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = R.drawable.onboarding_logo),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(250.dp)
                                    .height(100.dp)
                                    .align(Alignment.TopCenter)
                                    .padding(top = 30.dp)
                            )

                            Image(
                                painter = painterResource(id = R.drawable.second_onboard_image),
                                contentDescription = null,
                                modifier = Modifier.align(Alignment.Center)
                            )

                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)  // Aligns the content at the bottom
                                    .padding(start = 56.dp, bottom = 16.dp, end = 56.dp)
                            ) {
                                Text(
                                    fontFamily = Fonts.semiBaldFontInter,

                                    text = "Your Healthcare Journey Starts Here",
                                    fontSize = 24.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    lineHeight = 32.sp
                                )
                                Text(
                                    fontFamily = Fonts.lightFontInter,

                                    text = "Easily find medical facilities, book appointments, manage your schedule, and track your queue status—all in one place",
                                    fontSize = 14.sp,
                                    color = SecondaryColor,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(24.dp))

                                OnBoardingPagerButtons(currentState = pagerState.currentPage, count = pagerState.pageCount)

                                Spacer(modifier = Modifier.height(24.dp))

                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            scope.launch{
                                                pagerState.scrollToPage(1) // Moves directly to page index 1
                                            }
                                        }
                                        .background(
                                            colorResource(id = R.color.blue),
                                            RoundedCornerShape(14.dp)
                                        )
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        fontFamily = Fonts.semiBaldFontInter,
                                        text = "Next", color = Color.White, fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.height(50.dp))
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun OnBoardingPagerButtons(currentState: Int, count: Int) {
    Box(Modifier.fillMaxWidth()) {
        Row(
            Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(4.dp) // Adds spacing between indicators
        ) {
            repeat(count) {
                Box(
                    Modifier
                        .size(if (it == currentState) 24.dp else 8.dp, 8.dp)
                        .background(
                            if (it == currentState) colorResource(id = R.color.blue)
                            else ColorOnBoardingIndicator,
                            if (it == currentState) RoundedCornerShape(4.dp) else CircleShape
                        )
                )
            }
        }
    }
}


@Composable
@Preview
fun prevvv(){
    OnBoardingScreen({})
}

