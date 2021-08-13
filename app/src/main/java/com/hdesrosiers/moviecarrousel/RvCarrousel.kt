package com.hdesrosiers.moviecarrousel

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import kotlin.math.roundToInt

@Preview
@Composable
fun Screen() {

  var offset by remember { mutableStateOf(0f) }
//  val offset = remember { mutableStateOf(0f) }
  val ctrl = rememberScrollableState {
    offset += it
//    offset.value += it
    it
  }

  Row(
    Modifier
      .background(color = Color.Black)
      .fillMaxSize(fraction = 1f)
      .scrollable(
        orientation = Orientation.Horizontal,
        state = ctrl
      )
  ) {
//    Row(Modifier.offset(x = offset.roundToInt().dp, y = 0.dp)) {
//      MoviePoster()
//      MoviePoster()
//      MoviePoster()
//    }

    Row(Modifier
      .offsetCustom(getX = { offset }, getY = { 0f })
    ) {
      MoviePoster()
      MoviePoster()
      MoviePoster()
    }
  }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.offsetCustom(
  getX: () -> Float,
  getY: () -> Float,
  rtlAware: Boolean = true
) = object : LayoutModifier {
  override fun MeasureScope.measure(
    measurable: Measurable,
    constraints: Constraints
  ): MeasureResult {
    val placeable = measurable.measure(constraints)
    return layout(placeable.width, placeable.height) {
      if (rtlAware) {
        placeable.placeRelative(getX().roundToInt(), getY().roundToInt())
      } else {
        placeable.place(getX().roundToInt(), getY().roundToInt())
      }
    }
  }
}

@Composable
fun MoviePoster(modifier: Modifier = Modifier) {

  val screenSize = LocalConfiguration.current.screenWidthDp.dp * 0.5f

  Column(
    modifier = modifier
      .clip(RoundedCornerShape(40.dp))
      .background(color = Color.White)
      .width(screenSize)
//      .fillMaxWidth(fraction = 0.5f)
      .padding(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Image(
      painter = rememberImagePainter("https://i.etsystatic.com/15963200/r/il/25182b/2045311689/il_570xN.2045311689_7m2o.jpg"),
      contentDescription = null,
      modifier = Modifier
        .width(180.dp)
        .aspectRatio(0.674f)
        .clip(RoundedCornerShape(20.dp))
    )

    Text(
      text = "Joker",
      fontSize = 24.sp,
    )

    Row {
      Chip(label = "Action")
      Chip(label = "Drama")
      Chip(label = "History")
    }

    Spacer(modifier = Modifier.height(20.dp))

//    StarRating(9.0f)

    BuyTicketButton(onClick = {})
  }
}

@Composable
fun BuyTicketButton(onClick: () -> Unit) {
  Button(
    onClick = onClick,
    shape = RoundedCornerShape(50),
    elevation = null,
    colors = ButtonDefaults.buttonColors(
      backgroundColor = Color.DarkGray,
      contentColor = Color.White
    ),
    modifier = Modifier
      .fillMaxWidth()
  ) {
    Text(
      text = "Buy Ticket".uppercase()
    )
  }
}

@Composable
fun StarRating(rating: Float) {
  TODO("Not yet implemented")
}

@Composable
fun Chip(label: String, modifier: Modifier = Modifier) {
  Text(
    text = label,
    fontSize = 9.sp,
    color = Color.Gray,
    modifier = modifier
      .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(50))
      .padding(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 4.dp)
  )
}
















