package com.moyee.android.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.moyee.android.ui.theme.MoyeeTheme

@Composable
fun MoyeeIndicator(
    count: Int,
    selectedIndex: Int,
    selectedColor: Color = MaterialTheme.colors.primary,
    unselectedColor: Color = MaterialTheme.colors.onBackground,
    indicatorShape: Shape = CircleShape,
    selectedIndicatorSize: Dp = SelectedIndicatorSize,
    unselectedIndicatorSize: Dp = UnselectedIndicatorSize,
    indicatorSpace: Dp = IndicatorSpace,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(indicatorSpace),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(count) { index ->
            val (indicatorColor, indicatorSize) =
                if (index == selectedIndex) Pair(selectedColor, selectedIndicatorSize)
                else Pair(unselectedColor, unselectedIndicatorSize)

            Box(
                Modifier
                    .size(indicatorSize)
                    .clip(indicatorShape)
                    .background(indicatorColor)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000)
fun PreviewMoyeeIndicator() {
    MoyeeTheme {
        MoyeeIndicator(count = 3, selectedIndex = 0)
    }
}

private val IndicatorSpace = 13.dp
private val SelectedIndicatorSize = 6.dp
private val UnselectedIndicatorSize = 4.dp