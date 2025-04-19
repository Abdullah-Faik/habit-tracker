package com.fola.habit_tracker.ui.components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val PlusSmall: ImageVector
    get() {
        if (_undefined != null) {
            return _undefined!!
        }
        _undefined = ImageVector.Builder(
            name = "PlusSmall",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeAlpha = 4.0f,
                strokeLineWidth = 2.75f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 2f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 6f)
                verticalLineTo(18f)
                moveTo(18f, 12f)
                lineTo(6f, 12f)
            }
        }.build()
        return _undefined!!
    }

private var _undefined: ImageVector? = null
