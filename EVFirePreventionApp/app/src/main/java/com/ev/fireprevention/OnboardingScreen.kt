package com.ev.fireprevention

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ev.fireprevention.R
import kotlinx.coroutines.launch

data class OnboardingScreen(
    val title: String,
    val subtitle: String,
    val image: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val screens = listOf(
        OnboardingScreen("Real-time Battery Monitoring", "Track SOC, SOH, temperature, and cell health instantly.", R.drawable.ic_battery),
        OnboardingScreen("AI-Powered Fire Risk Detection", "Get AI predictions for Low, Medium, or High fire risk status.", R.drawable.ic_ai),
        OnboardingScreen("Live MQTT Data Integration", "View live EV battery data from MQTT sensors or simulators.", R.drawable.ic_mqtt),
        OnboardingScreen("Alerts & Prevention", "Receive alerts for overheating, imbalance, and low charge.", R.drawable.ic_alert)
    )

    val pagerState = rememberPagerState(pageCount = { screens.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPage(screen = screens[page])
        }

        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) { ->
            repeat(screens.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)

                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pagerState.currentPage < screens.size - 1) {
                TextButton(onClick = { navController.navigate("auth") }) {
                    Text("Skip")
                }
            } else {
                // Empty spacer to keep the "Get Started" button centered
                Spacer(modifier = Modifier.width(64.dp)) // Adjust width as needed
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < screens.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        navController.navigate("auth")
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = if (pagerState.currentPage < screens.size - 1) "Next" else "Get Started")
            }

            // This spacer helps balance the layout when the Skip button is not present
            if (pagerState.currentPage == screens.size - 1) {
                Spacer(modifier = Modifier.width(64.dp))
            }
        }
    }
}

@Composable
fun OnboardingPage(screen: OnboardingScreen) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = screen.image),
            contentDescription = screen.title,
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = screen.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = screen.subtitle,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
