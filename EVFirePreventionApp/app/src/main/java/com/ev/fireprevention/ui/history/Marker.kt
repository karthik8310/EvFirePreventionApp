package com.ev.fireprevention.ui.history

import android.graphics.Typeface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.ev.fireprevention.ui.theme.BrandSurface
import com.ev.fireprevention.ui.theme.BrandTextPrimary
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.overlayingComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import com.patrykandpatrick.vico.core.marker.Marker

@Composable
internal fun rememberMarker(labelFormatter: MarkerLabelFormatter? = null): Marker {
    val labelBackgroundColor = BrandSurface
    val labelBackground = remember(labelBackgroundColor) {
        ShapeComponent(Shapes.pillShape, labelBackgroundColor.toArgb()).setShadow(
            radius = 4f,
            dy = 2f,
            applyElevationOverlay = true,
        )
    }
    val label = textComponent(
        background = labelBackground,
        lineCount = 2, // Increased line count for timestamp + value
        padding = dimensionsOf(8.dp, 4.dp),
        typeface = Typeface.MONOSPACE,
        color = BrandTextPrimary,
    )
    val indicatorInner = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.surface)
    val indicatorCenter = shapeComponent(Shapes.pillShape, Color.White)
    val indicatorOuter = shapeComponent(Shapes.pillShape, Color.White)
    val indicator = overlayingComponent(
        outer = indicatorOuter,
        inner = overlayingComponent(
            outer = indicatorCenter,
            inner = indicatorInner,
            innerPaddingAll = 5.dp,
        ),
        innerPaddingAll = 10.dp,
    )
    val guideline = lineComponent(
        MaterialTheme.colorScheme.onSurface.copy(alpha = .2f),
        2.dp,
        DashedShape(Shapes.pillShape, 8f, 4f),
    )
    return remember(label, indicator, guideline, labelFormatter) {
        object : MarkerComponent(label, indicator, guideline) {
            init {
                indicatorSizeDp = 24f
                onApplyEntryColor = { entryColor ->
                    indicatorOuter.color = entryColor.copyColor(alpha = 32)
                    with(indicatorCenter) {
                        color = entryColor
                        setShadow(radius = 12f, color = entryColor)
                    }
                }
                if (labelFormatter != null) {
                    this.labelFormatter = labelFormatter
                }
            }
        }
    }
}
