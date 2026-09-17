package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.data.model.SessionType
import java.util.Random

@Composable
fun HourglassCanvas(
    progress: Float, // 0.0 (just started, upper full) to 1.0 (finished, bottom full)
    isRunning: Boolean,
    sessionType: SessionType,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SandStreamAnimation")
    val sandPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SandPhase"
    )

    val pulsePhase by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowPulse"
    )

    val primarySandColor = when (sessionType) {
        SessionType.FOCUS -> Color(0xFFFF9E0B)
        SessionType.SHORT_BREAK -> Color(0xFF10B981)
        SessionType.LONG_BREAK -> Color(0xFF06B6D4)
    }

    val secondarySandColor = when (sessionType) {
        SessionType.FOCUS -> Color(0xFFFFAE33)
        SessionType.SHORT_BREAK -> Color(0xFF34D399)
        SessionType.LONG_BREAK -> Color(0xFF38BDF8)
    }

    val darkSandColor = when (sessionType) {
        SessionType.FOCUS -> Color(0xFFD97706)
        SessionType.SHORT_BREAK -> Color(0xFF059669)
        SessionType.LONG_BREAK -> Color(0xFF0284C7)
    }

    val particles = remember {
        val r = Random(42)
        List(24) {
            Triple(r.nextFloat(), r.nextFloat(), r.nextFloat() * 2f + 1f)
        }
    }

    Box(
        modifier = modifier.padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val canvasW = size.width
            val canvasH = size.height
            if (canvasW <= 10f || canvasH <= 10f) return@Canvas

            // Scale bounding box
            val marginX = canvasW * 0.16f
            val marginY = canvasH * 0.06f
            val left = marginX
            val right = canvasW - marginX
            val top = marginY
            val bottom = canvasH - marginY
            val width = right - left
            val height = bottom - top
            val centerX = (left + right) / 2f
            val centerY = (top + bottom) / 2f

            // Radial background glow behind hourglass
            val glowRadius = width * 0.9f * pulsePhase
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primarySandColor.copy(alpha = if (isRunning) 0.18f else 0.08f),
                        primarySandColor.copy(alpha = if (isRunning) 0.06f else 0.02f),
                        Color.Transparent
                    ),
                    center = Offset(centerX, centerY + height * 0.2f),
                    radius = glowRadius
                ),
                radius = glowRadius,
                center = Offset(centerX, centerY + height * 0.2f)
            )

            // Outer glass cylinder housing
            val capHeight = height * 0.06f
            val neckWidth = width * 0.12f
            val neckY = centerY
            val neckHalfW = neckWidth / 2f

            val glassTopY = top + capHeight
            val glassBottomY = bottom - capHeight
            val glassBulbTop = glassTopY + height * 0.02f
            val glassBulbBottom = glassBottomY - height * 0.02f

            // Sand parameters
            val clampedProgress = progress.coerceIn(0f, 1f)
            val topSandDrain = clampedProgress // 0 -> 1: drains completely
            val bottomSandFill = clampedProgress // 0 -> 1: fills bottom bulb

            // 1. Draw Upper Bulb Sand (Drains from full down to empty)
            if (topSandDrain < 0.999f) {
                val remainingRatio = (1f - topSandDrain).coerceIn(0f, 1f)
                val upperBulbHeight = neckY - glassBulbTop
                // Sand surface Y moves from glassBulbTop down to neckY
                val sandSurfaceY = glassBulbTop + upperBulbHeight * (1f - remainingRatio)
                val t = ((sandSurfaceY - glassBulbTop) / upperBulbHeight).coerceIn(0f, 1f)
                val surfaceHalfW = (width * 0.42f) * (1f - t) + neckHalfW * t

                val upperSandPath = Path().apply {
                    moveTo(centerX - surfaceHalfW, sandSurfaceY)
                    // Curve down left wall to neck
                    cubicTo(
                        centerX - surfaceHalfW, sandSurfaceY + (neckY - sandSurfaceY) * 0.4f,
                        centerX - neckHalfW * 1.5f, neckY - height * 0.04f,
                        centerX - neckHalfW * 0.7f, neckY - 2f
                    )
                    // Across neck orifice
                    lineTo(centerX + neckHalfW * 0.7f, neckY - 2f)
                    // Curve up right wall
                    cubicTo(
                        centerX + neckHalfW * 1.5f, neckY - height * 0.04f,
                        centerX + surfaceHalfW, sandSurfaceY + (neckY - sandSurfaceY) * 0.4f,
                        centerX + surfaceHalfW, sandSurfaceY
                    )
                    // Curved top sand meniscus (slight funnel dip in the middle while running)
                    val dip = if (isRunning && remainingRatio > 0.05f) 8f else 2f
                    quadraticTo(centerX, sandSurfaceY + dip, centerX - surfaceHalfW, sandSurfaceY)
                    close()
                }

                drawPath(
                    path = upperSandPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            secondarySandColor,
                            primarySandColor,
                            darkSandColor
                        ),
                        startY = sandSurfaceY,
                        endY = neckY
                    )
                )

                // Top sand surface highlight / edge line
                drawPath(
                    path = Path().apply {
                        moveTo(centerX - surfaceHalfW, sandSurfaceY)
                        val dip = if (isRunning && remainingRatio > 0.05f) 8f else 2f
                        quadraticTo(centerX, sandSurfaceY + dip, centerX + surfaceHalfW, sandSurfaceY)
                    },
                    color = Color.White.copy(alpha = 0.5f),
                    style = Stroke(width = 2f, cap = StrokeCap.Round)
                )
            }

            // 2. Draw Bottom Bulb Sand (Fills from empty up to 100% COMPLETELY FULL at progress = 1.0)
            if (bottomSandFill > 0.001f) {
                val lowerBulbHeight = glassBottomY - 4f - neckY
                // When bottomSandFill = 1.0, sandTopY is exactly neckY
                val sandTopY = (glassBottomY - 4f) - lowerBulbHeight * bottomSandFill
                val tBottom = ((sandTopY - neckY) / lowerBulbHeight).coerceIn(0f, 1f)
                // Width at sand level: near bottom it's width * 0.42f, at neck it narrows to neckHalfW
                val halfWAtTop = neckHalfW + (width * 0.42f - neckHalfW) * tBottom

                // Center heap/cone height while filling, flattens smoothly as it approaches full
                val heapHeight = if (bottomSandFill < 0.95f) {
                    (1f - bottomSandFill) * (if (isRunning) 14f else 8f)
                } else {
                    (1f - bottomSandFill) * 20f // seamlessly flattens to 0 at 1.0
                }
                val peakY = (sandTopY - heapHeight).coerceAtLeast(neckY)

                val lowerSandPath = Path().apply {
                    // Left side at sand top level
                    moveTo(centerX - halfWAtTop, sandTopY)
                    // Curve down left bulb wall to bottom
                    cubicTo(
                        centerX - (width * 0.42f).coerceAtLeast(halfWAtTop), sandTopY + (glassBottomY - 4f - sandTopY) * 0.4f,
                        centerX - width * 0.42f, glassBottomY - height * 0.12f,
                        centerX - width * 0.42f, glassBottomY - 4f
                    )
                    // Bottom base
                    lineTo(centerX + width * 0.42f, glassBottomY - 4f)
                    // Curve up right bulb wall to sand top level
                    cubicTo(
                        centerX + width * 0.42f, glassBottomY - height * 0.12f,
                        centerX + (width * 0.42f).coerceAtLeast(halfWAtTop), sandTopY + (glassBottomY - 4f - sandTopY) * 0.4f,
                        centerX + halfWAtTop, sandTopY
                    )
                    // Top sand surface of the bottom bulb (conical heap or flat when full)
                    if (bottomSandFill >= 0.99f) {
                        lineTo(centerX - halfWAtTop, sandTopY)
                    } else {
                        // Peak in center where falling sand lands
                        quadraticTo(centerX + halfWAtTop * 0.4f, peakY + 2f, centerX, peakY)
                        quadraticTo(centerX - halfWAtTop * 0.4f, peakY + 2f, centerX - halfWAtTop, sandTopY)
                    }
                    close()
                }

                drawPath(
                    path = lowerSandPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            secondarySandColor,
                            primarySandColor,
                            darkSandColor
                        ),
                        startY = peakY,
                        endY = glassBottomY
                    )
                )

                // Sand mound landing glow / particle impact
                if (isRunning && bottomSandFill < 0.999f) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.9f),
                                secondarySandColor.copy(alpha = 0.6f),
                                Color.Transparent
                            ),
                            center = Offset(centerX, peakY),
                            radius = 16f * pulsePhase
                        ),
                        radius = 16f * pulsePhase,
                        center = Offset(centerX, peakY)
                    )
                }

                // If fully filled (progress = 1.0), draw a bright crowning highlight across the top surface
                if (bottomSandFill >= 0.99f) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.6f),
                        start = Offset(centerX - neckHalfW, neckY),
                        end = Offset(centerX + neckHalfW, neckY),
                        strokeWidth = 2.5f,
                        cap = StrokeCap.Round
                    )
                }
            }

            // 3. Falling Sand Stream / Trickle when Running
            if (isRunning && clampedProgress < 0.999f) {
                val lowerBulbHeight = glassBottomY - 4f - neckY
                val sandTopY = (glassBottomY - 4f) - lowerBulbHeight * bottomSandFill
                val heapHeight = (1f - bottomSandFill) * 14f
                val streamEndY = (sandTopY - heapHeight).coerceAtLeast(neckY + 4f)

                // Continuous central glow line
                drawLine(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            secondarySandColor,
                            primarySandColor
                        ),
                        startY = neckY - 5f,
                        endY = streamEndY
                    ),
                    start = Offset(centerX, neckY - 5f),
                    end = Offset(centerX, streamEndY),
                    strokeWidth = 3.5f,
                    cap = StrokeCap.Round
                )

                // Animated falling sand dots / sparkles
                val streamLength = streamEndY - neckY
                particles.forEachIndexed { index, (randX, randY, sizeP) ->
                    val progressY = (randY + sandPhase * 1.5f + index * 0.07f) % 1f
                    val particleY = neckY + streamLength * progressY
                    val scatterX = (randX - 0.5f) * 6f
                    drawCircle(
                        color = if (index % 2 == 0) Color.White.copy(alpha = 0.85f) else secondarySandColor,
                        radius = sizeP,
                        center = Offset(centerX + scatterX, particleY)
                    )
                }

                // Impact splashes at the landing spot
                val impactY = streamEndY
                drawCircle(
                    color = Color.White.copy(alpha = 0.9f),
                    radius = 4.5f * pulsePhase,
                    center = Offset(centerX, impactY)
                )
            }

            // 4. Glass Bulbs Structure (Upper & Lower Contour)
            val glassPath = Path().apply {
                // Top rim
                moveTo(centerX - width * 0.42f, glassTopY + 4f)
                // Upper left bulb curve into neck
                cubicTo(
                    centerX - width * 0.42f, glassTopY + height * 0.22f,
                    centerX - neckHalfW * 1.6f, neckY - height * 0.06f,
                    centerX - neckHalfW, neckY
                )
                // Lower left bulb curve down from neck
                cubicTo(
                    centerX - neckHalfW * 1.6f, neckY + height * 0.06f,
                    centerX - width * 0.42f, glassBottomY - height * 0.22f,
                    centerX - width * 0.42f, glassBottomY - 4f
                )
                // Bottom curve
                lineTo(centerX + width * 0.42f, glassBottomY - 4f)
                // Lower right bulb curve up to neck
                cubicTo(
                    centerX + width * 0.42f, glassBottomY - height * 0.22f,
                    centerX + neckHalfW * 1.6f, neckY + height * 0.06f,
                    centerX + neckHalfW, neckY
                )
                // Upper right bulb curve up from neck
                cubicTo(
                    centerX + neckHalfW * 1.6f, neckY - height * 0.06f,
                    centerX + width * 0.42f, glassTopY + height * 0.22f,
                    centerX + width * 0.42f, glassTopY + 4f
                )
                close()
            }

            // Glass background tint & transparency
            drawPath(
                path = glassPath,
                color = Color(0x18FFFFFF),
                style = Fill
            )

            // Glass outer stroke contour
            drawPath(
                path = glassPath,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0x80FFFFFF),
                        Color(0x20FFFFFF),
                        Color(0x50FFFFFF),
                        Color(0x15FFFFFF),
                        Color(0x60FFFFFF)
                    ),
                    start = Offset(left, top),
                    end = Offset(right, bottom)
                ),
                style = Stroke(width = 2.5f)
            )

            // Outer protective glass cylinder column lines (Vertical casing)
            drawLine(
                color = Color(0x33FFFFFF),
                start = Offset(centerX - width * 0.46f, top + capHeight),
                end = Offset(centerX - width * 0.46f, bottom - capHeight),
                strokeWidth = 2f
            )
            drawLine(
                color = Color(0x33FFFFFF),
                start = Offset(centerX + width * 0.46f, top + capHeight),
                end = Offset(centerX + width * 0.46f, bottom - capHeight),
                strokeWidth = 2f
            )

            // 5. Specular Highlights / Light Reflections on the Glass
            val highlightPathLeft = Path().apply {
                moveTo(centerX - width * 0.38f, glassTopY + height * 0.06f)
                cubicTo(
                    centerX - width * 0.37f, glassTopY + height * 0.20f,
                    centerX - neckHalfW * 1.9f, neckY - height * 0.08f,
                    centerX - neckHalfW * 1.2f, neckY - height * 0.02f
                )
            }
            drawPath(
                path = highlightPathLeft,
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color(0x70FFFFFF), Color.Transparent),
                    startY = glassTopY,
                    endY = neckY
                ),
                style = Stroke(width = 3.5f, cap = StrokeCap.Round)
            )

            val highlightPathLowerLeft = Path().apply {
                moveTo(centerX - neckHalfW * 1.2f, neckY + height * 0.02f)
                cubicTo(
                    centerX - neckHalfW * 1.9f, neckY + height * 0.08f,
                    centerX - width * 0.37f, glassBottomY - height * 0.20f,
                    centerX - width * 0.38f, glassBottomY - height * 0.06f
                )
            }
            drawPath(
                path = highlightPathLowerLeft,
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color(0x60FFFFFF), Color.Transparent),
                    startY = neckY,
                    endY = glassBottomY
                ),
                style = Stroke(width = 3f, cap = StrokeCap.Round)
            )

            // 6. Metallic Neck Ring / Orifice
            drawOval(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF2E3240),
                        Color(0xFFD4AF37),
                        Color(0xFF8B6508),
                        Color(0xFF2E3240)
                    ),
                    startX = centerX - neckHalfW * 1.2f,
                    endX = centerX + neckHalfW * 1.2f
                ),
                topLeft = Offset(centerX - neckHalfW * 1.1f, neckY - height * 0.012f),
                size = Size(neckHalfW * 2.2f, height * 0.024f)
            )

            // Inner orifice hole
            drawOval(
                color = Color(0xFF0F1015),
                topLeft = Offset(centerX - neckHalfW * 0.6f, neckY - height * 0.006f),
                size = Size(neckHalfW * 1.2f, height * 0.012f)
            )

            // 7. Top & Bottom Caps (Metallic obsidian rim with golden beveled edges)
            drawMetallicCap(
                centerX = centerX,
                y = top,
                width = width * 0.98f,
                capHeight = capHeight,
                isTop = true
            )

            drawMetallicCap(
                centerX = centerX,
                y = bottom - capHeight,
                width = width * 0.98f,
                capHeight = capHeight,
                isTop = false
            )
        }
    }
}

private fun DrawScope.drawMetallicCap(
    centerX: Float,
    y: Float,
    width: Float,
    capHeight: Float,
    isTop: Boolean
) {
    val halfW = width / 2f
    val ovalHeight = capHeight * 0.75f
    val bodyHeight = capHeight * 0.5f

    val topOvalY = if (isTop) y else y + bodyHeight
    val bottomOvalY = if (isTop) y + bodyHeight else y

    // Base body fill
    drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF1B1D28),
                Color(0xFF383C50),
                Color(0xFF5A607C),
                Color(0xFF383C50),
                Color(0xFF1B1D28)
            ),
            startX = centerX - halfW,
            endX = centerX + halfW
        ),
        topLeft = Offset(centerX - halfW, if (isTop) y + ovalHeight * 0.5f else y),
        size = Size(width, bodyHeight)
    )

    // Outer rim bevels
    drawOval(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF14161F),
                Color(0xFF4B516D),
                Color(0xFF8E99BE),
                Color(0xFF4B516D),
                Color(0xFF14161F)
            ),
            startX = centerX - halfW,
            endX = centerX + halfW
        ),
        topLeft = Offset(centerX - halfW, topOvalY),
        size = Size(width, ovalHeight)
    )

    // Golden trim ring on the cap
    val innerHalfW = halfW * 0.88f
    drawOval(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF5A4510),
                Color(0xFFD4AF37),
                Color(0xFFFFF2A8),
                Color(0xFFD4AF37),
                Color(0xFF5A4510)
            ),
            startX = centerX - innerHalfW,
            endX = centerX + innerHalfW
        ),
        topLeft = Offset(centerX - innerHalfW, topOvalY + ovalHeight * 0.12f),
        size = Size(innerHalfW * 2f, ovalHeight * 0.76f),
        style = Stroke(width = 2.5f)
    )

    // Dark inset disk inside gold ring
    drawOval(
        color = Color(0xFF0F1118),
        topLeft = Offset(centerX - innerHalfW * 0.94f, topOvalY + ovalHeight * 0.18f),
        size = Size(innerHalfW * 1.88f, ovalHeight * 0.64f)
    )
}
