package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SessionRecord
import com.example.data.model.SessionType
import com.example.ui.theme.AmberGoldDark
import com.example.ui.theme.AmberGoldLight
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.CyanBreak
import com.example.ui.theme.EmeraldBreak
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.RedDanger
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.DailyFocusStat
import com.example.ui.viewmodel.PomodoroViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class StatsPeriod(val label: String) {
    DAY("Jour"),
    WEEK("Semaine"),
    MONTH("Mois")
}

@Composable
fun StatsScreen(
    viewModel: PomodoroViewModel,
    modifier: Modifier = Modifier
) {
    val stats by viewModel.weeklyStats.collectAsState()
    val recentSessions by viewModel.recentSessions.collectAsState()
    val insights by viewModel.insights.collectAsState()
    val isAnalyzingInsights by viewModel.isAnalyzingInsights.collectAsState()
    var selectedPeriod by remember { mutableStateOf(StatsPeriod.WEEK) }
    var selectedDayIndex by remember { mutableStateOf(4) } // Default peak is Vendredi (index 4)
    var showClearConfirmDialog by remember { mutableStateOf(false) }

    // Calculated / Dynamic Display Stats
    val totalFocusMinutes = stats.totalFocusMinutes
    val totalHours = totalFocusMinutes / 60
    val remainingMins = totalFocusMinutes % 60
    val totalCompletedBlocks = stats.totalSessionsCount
    val streakDays = stats.currentStreakDays

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Top App Bar: ChronoGlass Brand Logo & Actions
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Brand Logo + Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF191C28),
                            border = BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.5f)),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.HourglassBottom,
                                    contentDescription = "ChronoGlass Logo",
                                    tint = AmberGoldPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Text(
                            text = "ChronoGlass",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.3.sp
                        )
                    }

                    // Right Actions (Share + Profile)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF191C28),
                            border = BorderStroke(1.dp, Color(0xFF262B3E)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            IconButton(onClick = { /* Share summary */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = "Partager",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF191C28),
                            border = BorderStroke(1.dp, Color(0xFF262B3E)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            IconButton(onClick = { /* Profile / settings */ }) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = "Profil",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 2. Screen Header Title & Period Selector (Jour, Semaine, Mois)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Statistiques",
                            color = TextPrimary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Synthèse d'activité & clarté mentale",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    // Segmented Period Selector
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF141622),
                        border = BorderStroke(1.dp, Color(0xFF222638)),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            StatsPeriod.entries.forEach { period ->
                                val isSelected = selectedPeriod == period
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) AmberGoldPrimary else Color.Transparent,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { selectedPeriod = period }
                                ) {
                                    Text(
                                        text = period.label,
                                        color = if (isSelected) Color(0xFF0F1016) else TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Card 1: TEMPS TOTAL DE FOCUS (Hero Stats Card)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF161824),
                    border = BorderStroke(1.dp, Color(0xFF24283B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Header row with amber dot & trend badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(AmberGoldPrimary)
                                )
                                Text(
                                    text = "TEMPS TOTAL DE FOCUS",
                                    color = TextSecondary,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.1.sp
                                )
                            }

                            // Trend badge: ↗ +14%
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF222636),
                                border = BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.2f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingUp,
                                        contentDescription = null,
                                        tint = AmberGoldLight,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "+14%",
                                        color = AmberGoldLight,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Large Digital Display: 32h 45m
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "$totalHours",
                                color = TextPrimary,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-1).sp
                            )
                            Text(
                                text = "h",
                                color = TextPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 6.dp, end = 12.dp, start = 2.dp)
                            )
                            Text(
                                text = String.format(Locale.US, "%02d", remainingMins),
                                color = TextPrimary,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-1).sp
                            )
                            Text(
                                text = "m",
                                color = TextPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Bottom Row: 54 blocs complétés • Série de 12j
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = AmberGoldLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "$totalCompletedBlocks blocs complétés",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Text(
                                text = "•",
                                color = TextMuted,
                                fontSize = 12.sp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = AmberGoldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Série de ${streakDays}j",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // 4. Card 2: Activité Hebdomadaire (7-Day Bar Chart)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF161824),
                    border = BorderStroke(1.dp, Color(0xFF24283B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = "Activité Hebdomadaire",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Moyenne : 5h 20m / jour",
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            Text(
                                text = "Pic : Ven (6h 15m)",
                                color = AmberGoldLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        // Bar Chart with Target Guide Line and 7 Capsule Bars
                        val days = listOf("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim")
                        val barRatios = listOf(0.42f, 0.62f, 0.48f, 0.68f, 0.94f, 0.28f, 0.38f)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            // Target guide line: "Cible 5h"
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                            ) {
                                val targetY = size.height * 0.35f
                                drawLine(
                                    color = Color(0xFF2B3147),
                                    start = Offset(0f, targetY),
                                    end = Offset(size.width - 70f, targetY),
                                    strokeWidth = 1.5f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                                )
                            }

                            // "Cible 5h" Label
                            Text(
                                text = "Cible 5h",
                                color = TextMuted,
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 38.dp, end = 4.dp)
                            )

                            // 7 Capsule Bars
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(155.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                days.forEachIndexed { index, dayName ->
                                    val isPeak = index == selectedDayIndex
                                    val ratio = barRatios[index]

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { selectedDayIndex = index }
                                    ) {
                                        // Peak Highlight Dot above the peak bar
                                        if (isPeak) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(AmberGoldLight)
                                                    .shadow(4.dp, CircleShape, spotColor = AmberGoldLight)
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                        } else {
                                            Spacer(modifier = Modifier.height(10.dp))
                                        }

                                        // Capsule Bar
                                        Box(
                                            modifier = Modifier
                                                .width(18.dp)
                                                .height(115.dp),
                                            contentAlignment = Alignment.BottomCenter
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(18.dp)
                                                    .fillMaxHeight(ratio)
                                                    .clip(RoundedCornerShape(9.dp))
                                                    .background(
                                                        brush = if (isPeak) {
                                                            Brush.verticalGradient(
                                                                listOf(
                                                                    Color(0xFFFDE68A),
                                                                    AmberGoldPrimary,
                                                                    AmberGoldDark
                                                                )
                                                            )
                                                        } else {
                                                            Brush.verticalGradient(
                                                                listOf(
                                                                    Color(0xFF242838),
                                                                    Color(0xFF1E2130)
                                                                )
                                                            )
                                                        }
                                                    )
                                                    .then(
                                                        if (isPeak) {
                                                            Modifier.border(
                                                                1.dp,
                                                                Color(0xFFFDE68A).copy(alpha = 0.5f),
                                                                RoundedCornerShape(9.dp)
                                                            )
                                                        } else Modifier
                                                    )
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Day label below bar
                                        Text(
                                            text = dayName,
                                            color = if (isPeak) AmberGoldLight else TextSecondary,
                                            fontSize = 11.5.sp,
                                            fontWeight = if (isPeak) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. Card 3: Répartition par Projet
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF161824),
                    border = BorderStroke(1.dp, Color(0xFF24283B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Répartition par Projet",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "32h 45m",
                                color = AmberGoldLight,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Segmented Progress Bar (Design 45%, Code 30%, Recherche 15%, Écriture 10%)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(0.45f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFE89327))
                            )
                            Box(
                                modifier = Modifier
                                    .weight(0.30f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFF5B57A))
                            )
                            Box(
                                modifier = Modifier
                                    .weight(0.15f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFFDE68A))
                            )
                            Box(
                                modifier = Modifier
                                    .weight(0.10f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF33384C))
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // 2x2 Grid Legend
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                ProjectLegendItem(
                                    name = "Design",
                                    percent = "45%",
                                    dotColor = Color(0xFFE89327),
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                ProjectLegendItem(
                                    name = "Code",
                                    percent = "30%",
                                    dotColor = Color(0xFFF5B57A),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                ProjectLegendItem(
                                    name = "Recherche",
                                    percent = "15%",
                                    dotColor = Color(0xFFFDE68A),
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                ProjectLegendItem(
                                    name = "Écriture",
                                    percent = "10%",
                                    dotColor = Color(0xFF8E95AA),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // 6. Card 4: PIC D'ÉNERGIE COGNITIVE
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF161824),
                    border = BorderStroke(1.dp, Color(0xFF24283B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Sun / Energy Icon Container
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF222432),
                            border = BorderStroke(1.dp, Color(0xFF34384E)),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.WbSunny,
                                    contentDescription = null,
                                    tint = Color(0xFFFDE68A),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        // Text content
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "PIC D'ÉNERGIE COGNITIVE",
                                color = Color(0xFFF5B57A),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "09:00 — 11:30",
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Moment idéal pour vos sessions critiques...",
                                color = TextSecondary,
                                fontSize = 11.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Right Badge: +35% focus
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF262329),
                            border = BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.25f))
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "+35%",
                                    color = AmberGoldLight,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "focus",
                                    color = AmberGoldLight,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // 6.5. Section: Asynchronous AI Productivity Insights Engine
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF161824),
                    border = BorderStroke(1.dp, Color(0xFF262A3E)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Header with Async Action Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = AmberGoldPrimary.copy(alpha = 0.15f),
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = AmberGoldPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "Analyse Cognitive IA (Async)",
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Traitement coroutine en tâche de fond",
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            IconButton(
                                onClick = { viewModel.generateProductivityInsightsAsync() },
                                enabled = !isAnalyzingInsights,
                                modifier = Modifier.size(34.dp)
                            ) {
                                if (isAnalyzingInsights) {
                                    CircularProgressIndicator(
                                        strokeWidth = 2.dp,
                                        color = AmberGoldPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Régénérer l'analyse",
                                        tint = AmberGoldLight,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        // Insights List
                        insights.forEach { insight ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF1E2130),
                                border = BorderStroke(1.dp, Color(0xFF2B3045)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = AmberGoldPrimary.copy(alpha = 0.12f)
                                        ) {
                                            Text(
                                                text = insight.category.uppercase(),
                                                color = AmberGoldLight,
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        Text(
                                            text = insight.impact,
                                            color = AmberGoldPrimary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Text(
                                        text = insight.title,
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = insight.description,
                                        color = TextSecondary,
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 7. Recent Sessions History Header & List (Optional collapsible)
            if (recentSessions.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Historique Récent",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            onClick = { showClearConfirmDialog = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DeleteOutline,
                                contentDescription = "Effacer l'historique",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                items(recentSessions.take(5), key = { it.id }) { session ->
                    SessionHistoryRow(
                        session = session,
                        onDelete = { viewModel.deleteSession(session.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Clear confirmation dialog
    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            containerColor = SurfaceCardElevated,
            title = {
                Text(
                    text = "Effacer l'historique ?",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Cette action supprimera toutes les sessions passées.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedDanger, contentColor = Color.White)
                ) {
                    Text("Effacer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text("Annuler", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun ProjectLegendItem(
    name: String,
    percent: String,
    dotColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Text(
                text = name,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = percent,
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SessionHistoryRow(
    session: SessionRecord,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.FRENCH) }
    val formattedDate = dateFormat.format(Date(session.completedAt))

    val sessionColor = when (session.sessionType) {
        SessionType.FOCUS -> AmberGoldPrimary
        SessionType.SHORT_BREAK -> EmeraldBreak
        SessionType.LONG_BREAK -> CyanBreak
    }

    val typeLabel = when (session.sessionType) {
        SessionType.FOCUS -> "Concentration"
        SessionType.SHORT_BREAK -> "Pause courte"
        SessionType.LONG_BREAK -> "Pause longue"
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF161824),
        border = BorderStroke(1.dp, Color(0xFF24283B)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(sessionColor)
                )

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = typeLabel,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${session.durationSeconds / 60} min",
                            color = sessionColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = if (!session.taskTitle.isNullOrBlank()) "🎯 ${session.taskTitle} • $formattedDate" else formattedDate,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = "Supprimer",
                    tint = TextMuted,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}
