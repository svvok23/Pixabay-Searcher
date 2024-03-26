package com.vstudio.pixabay.feature.image.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State.Error
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.vstudio.pixabay.core.common.shareIntent
import com.vstudio.pixabay.core.common.startUrlIntent
import com.vstudio.pixabay.core.common.ui.component.CircleCropBorderTransformation
import com.vstudio.pixabay.core.common.ui.tagsToString
import com.vstudio.pixabay.core.common.ui.theme.PixabayTheme
import com.vstudio.pixabay.core.common.ui.toPx
import com.vstudio.pixabay.core.common.R as common_R

@Composable
fun ImageScreen(
    viewModel: ImageViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val (image, cashedUrl) = viewModel.imageFlow.collectAsState().value
    if (image == null) return

    Box(modifier = Modifier.fillMaxSize()) {

        var scale by remember { mutableFloatStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 92.dp)
                .aspectRatio(
                    ratio = image.aspectRatio,
                    matchHeightConstraintsFirst = false
                )
        ) {
            val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
                scale = (scale * zoomChange).coerceIn(1f, 5f)

                val extraWidth = (scale - 1) * constraints.maxWidth
                val extraHeight = (scale - 1) * constraints.maxHeight

                val maxX = extraWidth / 2
                val maxY = extraHeight / 2

                offset = Offset(
                    x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                    y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY),
                )
            }

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(image.getLargestImageUrl())
                    .crossfade(true)
                    .crossfade(200)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(image.aspectRatio)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y
                    }
                    .transformable(state)
            ) {
                Crossfade(targetState = painter.state, label = "") { state ->
                    when (state) {
                        is Loading, is Error -> {
                            if (cashedUrl != null) {
                                // Show cached image with lower resolution
                                AsyncImage(
                                    model = ImageRequest.Builder(context = LocalContext.current)
                                        .data(cashedUrl)
                                        .crossfade(true)
                                        .crossfade(200)
                                        .build(),
                                    error = painterResource(common_R.drawable.ic_broken_image),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }

                        else -> SubcomposeAsyncImageContent()
                    }
                }
            }
        }

        val context = LocalContext.current

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(common_R.string.back),
                modifier = Modifier.padding(end = 4.dp)
            )
        }

        val shareTitle = stringResource(common_R.string.share_pixabay)
        IconButton(
            onClick = { context.shareIntent(image.pageUrl, shareTitle) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = stringResource(common_R.string.share),
                modifier = Modifier.padding(end = 4.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .background(color = Color.Black.copy(alpha = 0.3f))
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            if (image.tags.isNotEmpty()) {
                Text(
                    text = tagsToString(image.tags),
                    textAlign = TextAlign.Start,
                    color = Color.White,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.Start)
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    tint = Color.White,
                    contentDescription = stringResource(id = common_R.string.likes)
                )
                Text(
                    text = image.likes.toString(),
                    color = Color.White,
                    modifier = Modifier.padding(4.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    imageVector = Icons.Filled.Download,
                    tint = Color.White,
                    contentDescription = stringResource(id = common_R.string.downloads)
                )
                Text(
                    text = image.downloads.toString(),
                    color = Color.White,
                    modifier = Modifier.padding(4.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Comment,
                    tint = Color.White,
                    contentDescription = stringResource(id = common_R.string.comments)
                )
                Text(
                    text = image.comments.toString(),
                    color = Color.White,
                    modifier = Modifier.padding(4.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.Start)
                    .height(48.dp)
                    .background(
                        color = Color.White,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .clickable { context.startUrlIntent(image.user.profileUrl) }
            ) {
                if (image.user.avatarUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(image.user.avatarUrl)
                            .transformations(CircleCropBorderTransformation(4.dp.toPx()))
                            .crossfade(true)
                            .crossfade(100)
                            .build(),
                        contentDescription = image.user.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(48.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = image.user.name,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp)
                )
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageScreenPreview() {
    PixabayTheme {
        ImageScreen {}
    }
}