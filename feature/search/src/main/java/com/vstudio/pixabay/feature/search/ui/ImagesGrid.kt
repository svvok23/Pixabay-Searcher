package com.vstudio.pixabay.feature.search.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vstudio.pixabay.core.common.R
import com.vstudio.pixabay.core.common.ui.theme.placeholderColors
import com.vstudio.pixabay.core.common.ui.toPx
import com.vstudio.pixabay.core.domain.model.Image
import com.vstudio.pixabay.core.domain.model.ImageSize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImagesGrid(
    images: LazyPagingItems<Image>,
    onImageClick: (imageId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(6.dp),
        verticalItemSpacing = 6.dp,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier,
    ) {
        items(images.itemCount, key = { index -> images[index]?.dbId ?: index }) { index ->
            images[index]?.let { image ->
                ImageCard(
                    image = image,
                    onClick = onImageClick,
                    modifier = modifier
                        .fillMaxWidth()
                        .aspectRatio(image.aspectRatio)
                        .animateItemPlacement(tween(durationMillis = 250))
                )
            } ?: EmptyCard(
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .animateItemPlacement(tween(durationMillis = 250))
            )
        }
    }
}

@Composable
private fun ImageCard(
    image: Image,
    onClick: (imageId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
    ) {
        Box(modifier = modifier) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(image.multiSizeImage.getNearestUrl(
                        ImageSize(
                            // TODO real size
                            width = 200.dp.toPx().toInt(),
                            height = 200.dp.toPx().toInt()
                    )))
                    .crossfade(true)
                    .crossfade(200)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .background(placeholderColors[image.dbId.toInt() % placeholderColors.size])
                    .fillMaxWidth()
                    .clickable { onClick(image.id) }
            )

            Text(
                text = image.user.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(horizontal = 8.dp, vertical = 0.dp)
            )
        }
    }
}