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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import coil.compose.rememberImagePainter
import kotlin.math.abs
import kotlin.math.roundToInt

@Immutable
data class Movie(
  val title: String,
  val posterUrl: String,
  val bgUrl: String,
  val color: Color,
  val chips: List<String>,
  val actors: List<MovieActor> = emptyList(),
  val introduction: String = ""
)

data class MovieActor(
  val name: String,
  val image: String
)

val movies = listOf(
  Movie(
    title = "Good Boys",
    posterUrl = "https://m.media-amazon.com/images/M/MV5BMTc1NjIzODAxMF5BMl5BanBnXkFtZTgwMTgzNzk1NzM@._V1_.jpg",
    bgUrl = "https://m.media-amazon.com/images/M/MV5BMTc1NjIzODAxMF5BMl5BanBnXkFtZTgwMTgzNzk1NzM@._V1_.jpg",
    color = Color.Red,
    chips = listOf("Comedy", "1980s", "Drama"),
    actors = listOf(
      MovieActor(
        "Jaoquin Phoenix",
        "https://image.tmdb.org/t/p/w138_and_h175_face/nXMzvVF6xR3OXOedozfOcoA20xh.jpg"
      ),
      MovieActor(
        "Robert De Niro",
        "https://image.tmdb.org/t/p/w138_and_h175_face/cT8htcckIuyI1Lqwt1CvD02ynTh.jpg"
      ),
      MovieActor(
        "Zazie Beetz",
        "https://image.tmdb.org/t/p/w138_and_h175_face/sgxzT54GnvgeMnOZgpQQx9csAdd.jpg"
      )
    ),
    introduction = "During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure."
  ),
  Movie(
    title = "Joker",
    posterUrl = "https://i.etsystatic.com/15963200/r/il/25182b/2045311689/il_794xN.2045311689_7m2o.jpg",
    bgUrl = "https://images-na.ssl-images-amazon.com/images/I/61gtGlalRvL._AC_SY741_.jpg",
    color = Color.Blue,
    chips = listOf("Action", "Drama", "History"),
    actors = listOf(
      MovieActor(
        "Jaoquin Phoenix",
        "https://image.tmdb.org/t/p/w138_and_h175_face/nXMzvVF6xR3OXOedozfOcoA20xh.jpg"
      ),
      MovieActor(
        "Robert De Niro",
        "https://image.tmdb.org/t/p/w138_and_h175_face/cT8htcckIuyI1Lqwt1CvD02ynTh.jpg"
      ),
      MovieActor(
        "Zazie Beetz",
        "https://image.tmdb.org/t/p/w138_and_h175_face/sgxzT54GnvgeMnOZgpQQx9csAdd.jpg"
      )
    ),
    introduction = "During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure."
  ),
  Movie(
    title = "The Hustle",
    posterUrl = "https://m.media-amazon.com/images/M/MV5BMTc3MDcyNzE5N15BMl5BanBnXkFtZTgwNzE2MDE0NzM@._V1_.jpg",
    bgUrl = "https://m.media-amazon.com/images/M/MV5BMTc3MDcyNzE5N15BMl5BanBnXkFtZTgwNzE2MDE0NzM@._V1_.jpg",
    color = Color.Yellow,
    chips = listOf("Action", "Drama", "History"),
    actors = listOf(
      MovieActor(
        "Jaoquin Phoenix",
        "https://image.tmdb.org/t/p/w138_and_h175_face/nXMzvVF6xR3OXOedozfOcoA20xh.jpg"
      ),
      MovieActor(
        "Robert De Niro",
        "https://image.tmdb.org/t/p/w138_and_h175_face/cT8htcckIuyI1Lqwt1CvD02ynTh.jpg"
      ),
      MovieActor(
        "Zazie Beetz",
        "https://image.tmdb.org/t/p/w138_and_h175_face/sgxzT54GnvgeMnOZgpQQx9csAdd.jpg"
      )
    ),
    introduction = "During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure."
  )
)
val posterAspectRatio = 0.674f

@Composable
fun Screen() {
  val configuration = LocalConfiguration.current
  val density = LocalDensity.current
  val screenWidth = configuration.screenWidthDp.dp
  val screenWidthPx = density.run { screenWidth.toPx() }
  val screenHeight = configuration.screenHeightDp.dp
  val screenHeightPx = density.run { screenHeight.toPx() }
  var offset by remember { mutableStateOf(0f) }
  val ctrl = rememberScrollableState {
    offset += it
    it
  }
  val posterWidthDp = screenWidth * 0.6f
  val posterSpacingPx = with(density) { posterWidthDp.toPx() + 30.dp.toPx() }// screenWidthPx * 0.7f
  val indexFraction = -1 * offset / posterSpacingPx

  Box(
    Modifier
      .background(color = Color.Black)
      .fillMaxSize(fraction = 1f)
      .scrollable(
        orientation = Orientation.Horizontal,
        state = ctrl
      )
  ) {

    movies.forEachIndexed { index, movie ->
//      val opacity = if (indexFraction.roundToInt() == index) 1f else 0f
      val isInRange = (index >= indexFraction - 1 && indexFraction + 1 >= index)
      val opacity = if (isInRange) 1f else 0f
      val shape = when {
        !isInRange -> RectangleShape
        // same direction as MoviePoster swipe
//        else -> FractionalRectangleShape(index - indexFraction, index + 1 - indexFraction)

        // opposite direction of MoviePoster swipe
        index <= indexFraction -> {
          val fraction = indexFraction - index
          FractionalRectangleShape(fraction, 1f)
        }
        else -> {
          val fraction = indexFraction - index + 1
          FractionalRectangleShape(0f, fraction)
        }

      }
      Image(
        painter = rememberImagePainter(movie.bgUrl),
        contentScale = ContentScale.FillWidth,
        contentDescription = null,
        modifier = Modifier
          .graphicsLayer(
            alpha = opacity,
            shape = shape,
            clip = true
          )
          .fillMaxWidth()
          .aspectRatio(posterAspectRatio)
      )
    }

    // whiteGradBackground
    Box(
      modifier = Modifier
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.White),
            startY = screenHeightPx * 0.4f,
            endY = screenHeightPx * 0.8f
          )
        )
        .fillMaxSize()
    )

    movies.forEachIndexed { index, movie ->
      val center = posterSpacingPx * index
      val distanceFromCenter = abs(offset + center) / posterSpacingPx
      MoviePoster(
        movie = movie,
        modifier = Modifier
          .offset(
            getX = { offset + center },
            getY = { lerp(start = 0f, stop = 50f, distanceFromCenter) })
          .width(posterWidthDp)
          .align(Alignment.BottomCenter)
          .padding(bottom = screenHeight * 0.1f)
      )
    }

    Box(modifier = Modifier
      .width(screenWidth * 0.6f - 40.dp)
      .align(Alignment.BottomCenter)
      .padding(bottom = 30.dp)
    ) {
      BuyTicketButton(
        onClick = {},
      )
    }
  }
}

fun FractionalRectangleShape(startFraction: Float, endFraction: Float) = object : Shape {
  override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density) =
    Outline.Rectangle(
      Rect(
        top = 0f,
        left = startFraction * size.width,
        bottom = size.height,
        right = endFraction * size.width
      )
    )
}

// where the hell is androidx.compose.ui.util ???
fun lerp(start: Float, stop: Float, fraction: Float): Float {
  return (1 - fraction) * start + fraction * stop
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.offset(
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
fun MoviePoster(modifier: Modifier = Modifier, movie: Movie) {

  Column(
    modifier = modifier
      .clip(RoundedCornerShape(40.dp))
      .background(color = Color.White)
      .padding(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Image(
      painter = rememberImagePainter(movie.posterUrl),
      contentDescription = null,
      contentScale = ContentScale.FillWidth,
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(posterAspectRatio)
        .padding(bottom = 10.dp)
        .clip(RoundedCornerShape(20.dp))
    )

    Text(
      text = movie.title,
      fontSize = 24.sp,
      modifier = Modifier.padding(10.dp)
    )

    Row(
      modifier = Modifier
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.Center
    ) {
      for (chip in movie.chips) {
        Chip(
          modifier = Modifier
            .padding(horizontal = 2.dp),
          label = chip
        )
      }
    }
  }
}

@Composable
fun BuyTicketButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
  Button(
    onClick = onClick,
    shape = RoundedCornerShape(10),
    elevation = null,
    colors = ButtonDefaults.buttonColors(
      backgroundColor = Color.DarkGray,
      contentColor = Color.White
    ),
    contentPadding = PaddingValues(vertical = 15.dp),
    modifier = modifier
      .fillMaxWidth()
  ) {
    Text(
      text = "Buy Ticket".uppercase(),
      fontSize = 12.sp
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








