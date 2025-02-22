package com.example.aradhotelapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.aradhotelapplication.ui.theme.AradHotelApplicationTheme
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AradHotelApplicationTheme {
                LocationPage()
            }
        }
    }
}

var hotelPainterList = listOf(
    R.drawable.poster1, R.drawable.poster2, R.drawable.poster3,
    R.drawable.poster4, R.drawable.poster5
)

var facilitiesPainterList = listOf(
    R.drawable.wifi, R.drawable.bed, R.drawable.swimming, R.drawable.food, R.drawable.tv
)

var facilitiesTitleList = listOf("Network", "5 Beds", "Pool", "Dinner", "TY")


@Composable
fun LocationPage() {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.size(35.dp))
        TopIcons()
        Spacer(modifier = Modifier.size(40.dp))
        ViewPagerSlider()
        Spacer(modifier = Modifier.size(30.dp))
        HotelNameAndRate()
        Spacer(modifier = Modifier.size(15.dp))
        LocationData()
        Spacer(modifier = Modifier.size(25.dp))
        ExpandableText(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
                    "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut " +
                    "enim ad minim veniam, quis nostrud exercitation" +
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
                    "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut " +
                    "enim ad minim veniam, quis nostrud exercitation"
        )
        Spacer(modifier = Modifier.size(25.dp))
        Facilities()
        Spacer(modifier = Modifier.size(45.dp))
        PriceAndReserve()
        Spacer(modifier = Modifier.size(30.dp))
    }
}

@Composable
fun TopIcons() {
    Row(
        Modifier
            .padding(horizontal = 25.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.left_arrow),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.bookmark),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
    }
}

@ExperimentalPagerApi
@Composable
fun PosterImageSlider(page: Int, pagerState: PagerState) {
    val circleSize = 8.dp
    val spaceBetween = 2.dp
    Image(
        painter = painterResource(id = hotelPainterList[page]),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    )
    Row(
        Modifier.padding(top = 340.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.CenterStart) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spaceBetween),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val indicatorModifier = Modifier
                    .size(circleSize)
                    .background(Color(0xFFFAFAFA), CircleShape)
                repeat(pagerState.pageCount) { Box(indicatorModifier) }
            }
            Box(Modifier
                .offset {
                    val scrollPosition =
                        (pagerState.currentPage + pagerState.currentPageOffset)
                            .coerceIn(0f, (pagerState.pageCount - 1).toFloat())
                    IntOffset(
                        x = ((circleSize + spaceBetween) * scrollPosition)
                            .roundToPx(),
                        y = 0
                    )
                }
                .size(circleSize)
                .border(1.dp, Color(0xFFFAFAFA), CircleShape)
                .background(Color(0xFFA9A9A9), CircleShape)
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ViewPagerSlider() {
    val pagerState = rememberPagerState(pageCount = hotelPainterList.size, initialPage = 0)
    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(2000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % (pagerState.pageCount),
                animationSpec = tween(600)
            )
        }
    }
    Column {
        HorizontalPager(state = pagerState) { page ->
            Card(modifier = Modifier
                .graphicsLayer {
                    val pageOffset =
                        calculateCurrentOffsetForPage(page).absoluteValue
                    lerp(
                        start = 0.85f, stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }
                    alpha = lerp(
                        start = 0.5f, stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
            ) {
                PosterImageSlider(page = page, pagerState = pagerState)
            }
        }
    }
}

@Composable
fun HotelNameAndRate() {
    Row(
        Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Royal Villa", fontSize = 35.sp,
            fontWeight = FontWeight.Bold, color = Color.White
        )
        Column(
            Modifier
                .width(58.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x88121212))
                .padding(bottom = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "", tint = Color.Unspecified,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    text = "4.9", fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFB600)
                )
            }
            Text(
                text = "418 reviews", fontSize = 8.sp,
                fontWeight = FontWeight.Bold, color = Color(0xFFFFB600)
            )
        }
    }
}

@Composable
fun LocationData() {
    Row(
        Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.location),
            contentDescription = "", tint = Color.Unspecified,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            text = "Golbagh st, Bandar-e Anzali", fontSize = 16.sp,
            fontWeight = FontWeight.Medium, color = Color(0xFFA9A9A9)
        )
    }
}

private const val MINIMIZED_MAX_LINES = 3

@Composable
fun ExpandableText(text: String) {
    var isExpanded by remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    var isClickable by remember { mutableStateOf(false) }
    var finalText by remember { mutableStateOf(text) }
    val textLayoutResult = textLayoutResultState.value
    LaunchedEffect(textLayoutResult) {
        if (textLayoutResult == null) return@LaunchedEffect
        when {isExpanded -> { finalText = "$text show less" }
            !isExpanded && textLayoutResult.hasVisualOverflow -> {
                val lastCharIndex = textLayoutResult
                    .getLineEnd(MINIMIZED_MAX_LINES - 1)
                val showMoreString = " show more"
                val adjustedText = text
                    .substring(startIndex = 0, endIndex = lastCharIndex)
                    .dropLast(showMoreString.length)
                finalText = "$adjustedText$showMoreString"
                isClickable = true
    } } }
    Text(buildAnnotatedString {
            withStyle(style = SpanStyle(
                    fontWeight = FontWeight.Medium, color = Color(0xFFA9A9A9))
            ) { append(finalText.substring(
                startIndex = 0, endIndex = finalText.length - 9))
            }
            withStyle(style = SpanStyle(
                    fontWeight = FontWeight.SemiBold, color = Color(0xFF3064A8))
            ) { append(finalText.substring(startIndex = finalText.length - 10)) }
        },
        maxLines = if (isExpanded) Int.MAX_VALUE else MINIMIZED_MAX_LINES,
        onTextLayout = { textLayoutResultState.value = it },
        modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth()
            .clickable(enabled = isClickable) { isExpanded = !isExpanded }
            .animateContentSize(),
        fontSize = 12.sp, lineHeight = 15.sp
) }

@Composable
fun Facilities() {
    Text(
        text = "Facilities",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFE9E9E9),
        modifier = Modifier.padding(horizontal = 20.dp)
    )
    Spacer(modifier = Modifier.size(15.dp))
    Row(
        Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in facilitiesPainterList.indices) {
            Column(
                Modifier
                    .weight(1f)
                    .height(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF363636)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = facilitiesPainterList[i]),
                    contentDescription = "",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = facilitiesTitleList[i],
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEBEBEB)
                )
            }
        }
    }
}

@Composable
fun PriceAndReserve() {
    Row(
        Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .padding(vertical = 12.dp)
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Price", fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF747474)
            )
            Text(
                text = "$430/night", fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE9E9E9)
            )
        }
        Button(
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .height(62.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF3064AB),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Book Now",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}





