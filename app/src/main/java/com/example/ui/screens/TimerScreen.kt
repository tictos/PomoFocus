package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SessionType
import com.example.ui.components.HourglassCanvas
import com.example.ui.sound.AmbientSound
import com.example.ui.theme.AmberGoldDark
import com.example.ui.theme.AmberGoldLight
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.CyanBreak
import com.example.ui.theme.EmeraldBreak
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.PomodoroViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    viewModel: PomodoroViewModel,
    modifier: Modifier = Modifier,
    onNavigateToTasks: () -> Unit = {}
) {
    val currentType by viewModel.currentType.collectAsState()
    val totalSeconds by viewModel.totalSeconds.collectAsState()
    val remainingSeconds by viewModel.remainingSeconds.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val currentInterval by viewModel.currentInterval.collectAsState()
    val activeTaskId by viewModel.activeTaskId.collectAsState()
    val currentAmbientSound by viewModel.currentAmbientSound.collectAsState()
    val ambientVolume by viewModel.ambientVolume.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val tasks by viewModel.tasks.collectAsState()

    val activeTask = tasks.find { it.id == activeTaskId }

    var isFullscreen by remember { mutableStateOf(false) }
    var showSoundSheet by remember { mutableStateOf(false) }
    var showTaskPickerSheet by remember { mutableStateOf(false) }

    val progress = if (totalSeconds > 0) {
        (totalSeconds - remainingSeconds).toFloat() / totalSeconds.toFloat()
    } else 0f

    val accentColor = when (currentType) {
        SessionType.FOCUS -> AmberGoldPrimary
        SessionType.SHORT_BREAK -> EmeraldBreak
        SessionType.LONG_BREAK -> CyanBreak
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Brand & Action Icons
            if (!isFullscreen) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo + Brand
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceCardElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, AmberGoldPrimary.copy(alpha = 0.3f)),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.HourglassBottom,
                                    contentDescription = "ChronoGlass Logo",
                                    tint = AmberGoldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "ChronoGlass",
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "IMMERSIVE FOCUS",
                                color = AmberGoldLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.5.sp
                            )
                        }
                    }

                    // Action Icons: Sound, Fullscreen
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Ambient Sound Button
                        IconButton(
                            onClick = { showSoundSheet = true },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (currentAmbientSound != AmbientSound.NONE) AmberGoldPrimary.copy(alpha = 0.2f) else SurfaceCard)
                                .border(1.dp, if (currentAmbientSound != AmbientSound.NONE) AmberGoldPrimary.copy(alpha = 0.5f) else Color.Transparent, CircleShape)
                                .testTag("ambient_sound_button")
                        ) {
                            Icon(
                                imageVector = if (currentAmbientSound == AmbientSound.NONE) Icons.Default.VolumeMute else Icons.Default.GraphicEq,
                                contentDescription = "Sons d'ambiance",
                                tint = if (currentAmbientSound != AmbientSound.NONE) AmberGoldLight else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Fullscreen Toggle
                        IconButton(
                            onClick = { isFullscreen = !isFullscreen },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SurfaceCard)
                                .testTag("fullscreen_button")
                        ) {
                            Icon(
                                imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                                contentDescription = "Plein écran",
                                tint = TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Preset Chips Row (25 min, 50 min, 15 min Break, 5 min Short Break)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PresetChip(
                        label = "25 min",
                        subtitle = "Focus",
                        isSelected = currentType == SessionType.FOCUS && totalSeconds == 25 * 60,
                        accentColor = AmberGoldPrimary,
                        onClick = { viewModel.selectPreset(25, SessionType.FOCUS) }
                    )
                    PresetChip(
                        label = "50 min",
                        subtitle = "Deep",
                        isSelected = currentType == SessionType.FOCUS && totalSeconds == 50 * 60,
                        accentColor = AmberGoldPrimary,
                        onClick = { viewModel.selectPreset(50, SessionType.FOCUS) }
                    )
                    PresetChip(
                        label = "15 min",
                        subtitle = "Longue pause",
                        isSelected = currentType == SessionType.LONG_BREAK,
                        accentColor = CyanBreak,
                        onClick = { viewModel.selectPreset(15, SessionType.LONG_BREAK) }
                    )
                    PresetChip(
                        label = "5 min",
                        subtitle = "Pause courte",
                        isSelected = currentType == SessionType.SHORT_BREAK,
                        accentColor = EmeraldBreak,
                        onClick = { viewModel.selectPreset(5, SessionType.SHORT_BREAK) }
                    )
                }
            } else {
                // In fullscreen mode, small exit button at top right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { isFullscreen = false },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SurfaceCard)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FullscreenExit,
                            contentDescription = "Quitter plein écran",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dynamic contrast parameters based on sand drain progress (0 = full top bulb, 1 = empty top bulb)
            val drainFraction = progress.coerceIn(0f, 1f)

            // When upper sand is full (beginning of session), text is bold black (#0A0C12) for maximum contrast on luminous container.
            // As sand level drains down, text dynamically transitions to bright glowing accent (Orange for Focus, Cyan for Long Break, Emerald for Short Break)
            val startTextColor = Color(0xFF0F1016)
            val targetTextColor = when (currentType) {
                SessionType.FOCUS -> AmberGoldLight
                SessionType.SHORT_BREAK -> Color(0xFF6EE7B7)
                SessionType.LONG_BREAK -> Color(0xFF7DD3FC)
            }
            val dynamicBadgeTextColor = lerp(startTextColor, targetTextColor, drainFraction)

            val startBgColor = when (currentType) {
                SessionType.FOCUS -> AmberGoldPrimary
                SessionType.SHORT_BREAK -> EmeraldBreak
                SessionType.LONG_BREAK -> CyanBreak
            }
            val targetBgColor = SurfaceCardElevated
            val dynamicBadgeBgColor = lerp(startBgColor, targetBgColor, drainFraction)

            val dynamicBadgeBorderColor = lerp(
                Color.White.copy(alpha = 0.5f),
                accentColor.copy(alpha = 0.6f),
                drainFraction
            )

            val statusIcon = when (currentType) {
                SessionType.FOCUS -> Icons.Default.LocalFireDepartment
                SessionType.SHORT_BREAK -> Icons.Default.Air
                SessionType.LONG_BREAK -> Icons.Default.WaterDrop
            }

            // Focus Mode Dynamic Pill Badge
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = dynamicBadgeBgColor,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, dynamicBadgeBorderColor),
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .shadow(
                        elevation = if (drainFraction < 0.3f) 8.dp else 4.dp,
                        shape = RoundedCornerShape(22.dp),
                        spotColor = accentColor,
                        ambientColor = accentColor
                    )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = statusIcon,
                        contentDescription = null,
                        tint = dynamicBadgeTextColor,
                        modifier = Modifier.size(15.dp)
                    )

                    Text(
                        text = when (currentType) {
                            SessionType.FOCUS -> "FOCUS SESSION • DEEP WORK"
                            SessionType.SHORT_BREAK -> "PAUSE COURTE • REPOS"
                            SessionType.LONG_BREAK -> "PAUSE LONGUE • RÉGÉNÉRATION"
                        },
                        color = dynamicBadgeTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            // Visual Hourglass (Sablier)
            HourglassCanvas(
                progress = progress,
                isRunning = isRunning,
                sessionType = currentType,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isFullscreen) 360.dp else 290.dp)
            )

            // Countdown Digital Display
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            val timeString = String.format(Locale.US, "%02d:%02d", minutes, seconds)

            Text(
                text = timeString,
                color = TextPrimary,
                fontSize = if (isFullscreen) 64.sp else 54.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            // Interval / Flow status
            Row(
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Intervalle",
                    tint = AmberGoldLight,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "Intervalle $currentInterval sur ${settings.targetIntervals} • In The Flow",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Active Task Tag
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (activeTask != null) AmberGoldPrimary.copy(alpha = 0.4f) else Color.Transparent),
                modifier = Modifier
                    .clickable { showTaskPickerSheet = true }
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TaskAlt,
                        contentDescription = "Tâche active",
                        tint = if (activeTask != null) AmberGoldPrimary else TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = activeTask?.let { "🎯 ${it.title} (${it.completedPomodoros}/${it.estimatedPomodoros})" }
                            ?: "Sélectionner une tâche à focaliser",
                        color = if (activeTask != null) TextPrimary else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = if (activeTask != null) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Bottom Timer Action Buttons (Reset, Start/Pause, Skip)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset Button
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { viewModel.resetTimer() },
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(SurfaceCard)
                            .border(1.dp, BorderSubtleShape(), CircleShape)
                            .testTag("reset_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Réinitialiser",
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "RÉINITIALISER",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }

                // Main Start / Pause CTA Button
                Box(
                    modifier = Modifier
                        .width(170.dp)
                        .height(60.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(30.dp),
                            spotColor = accentColor,
                            ambientColor = accentColor
                        )
                        .clip(RoundedCornerShape(30.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    accentColor,
                                    when (currentType) {
                                        SessionType.FOCUS -> AmberGoldLight
                                        SessionType.SHORT_BREAK -> EmeraldBreak
                                        SessionType.LONG_BREAK -> CyanBreak
                                    }
                                )
                            )
                        )
                        .clickable { viewModel.toggleStartPause() }
                        .testTag("toggle_timer_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isRunning) "Pause" else "Démarrer",
                            tint = ObsidianBg,
                            modifier = Modifier.size(26.dp)
                        )
                        Text(
                            text = if (isRunning) "PAUSE" else "DÉMARRER",
                            color = ObsidianBg,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Skip Button
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { viewModel.skipSession() },
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(SurfaceCard)
                            .border(1.dp, BorderSubtleShape(), CircleShape)
                            .testTag("skip_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Passer",
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "PASSER",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }

    // Ambient Sound Bottom Sheet
    if (showSoundSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSoundSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = SurfaceCardElevated
        ) {
            SoundPickerSheetContent(
                currentSound = currentAmbientSound,
                volume = ambientVolume,
                onSelectSound = { viewModel.setAmbientSound(it) },
                onVolumeChange = { viewModel.setAmbientVolume(it) },
                onClose = { showSoundSheet = false }
            )
        }
    }

    // Task Picker Bottom Sheet
    if (showTaskPickerSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTaskPickerSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = SurfaceCardElevated
        ) {
            TaskPickerSheetContent(
                tasks = tasks,
                activeTaskId = activeTaskId,
                onSelectTask = {
                    viewModel.setActiveTask(it)
                    showTaskPickerSheet = false
                },
                onGoToTasks = {
                    showTaskPickerSheet = false
                    onNavigateToTasks()
                }
            )
        }
    }
}

@Composable
private fun BorderSubtleShape(): Color = Color(0xFF2A2E3D)

@Composable
private fun PresetChip(
    label: String,
    subtitle: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = if (isSelected) accentColor else SurfaceCard,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isSelected) accentColor else Color(0xFF2E3345)
        ),
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag("preset_$label")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                color = if (isSelected) ObsidianBg else TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = if (isSelected) ObsidianBg.copy(alpha = 0.8f) else TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun SoundPickerSheetContent(
    currentSound: AmbientSound,
    volume: Float,
    onSelectSound: (AmbientSound) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ambiance sonore de concentration",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sound options list
        AmbientSound.entries.forEach { sound ->
            val isSelected = currentSound == sound
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) AmberGoldPrimary.copy(alpha = 0.15f) else SurfaceCard,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) AmberGoldPrimary else Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onSelectSound(sound) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val icon: ImageVector = when (sound) {
                            AmbientSound.NONE -> Icons.Default.VolumeMute
                            AmbientSound.RAIN -> Icons.Default.WaterDrop
                            AmbientSound.WHITE_NOISE -> Icons.Default.Air
                            AmbientSound.CLOCK_TICK -> Icons.Default.Schedule
                            AmbientSound.BINAURAL_FOCUS -> Icons.Default.GraphicEq
                            AmbientSound.CAMPFIRE -> Icons.Default.LocalFireDepartment
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = sound.label,
                            tint = if (isSelected) AmberGoldPrimary else TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = sound.label,
                            color = if (isSelected) AmberGoldLight else TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Sélectionné",
                            tint = AmberGoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Volume control
        if (currentSound != AmbientSound.NONE) {
            Text(
                text = "Volume sonore (${(volume * 100).toInt()}%)",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Default.VolumeMute, contentDescription = null, tint = TextMuted)
                Slider(
                    value = volume,
                    onValueChange = onVolumeChange,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = AmberGoldPrimary,
                        activeTrackColor = AmberGoldPrimary,
                        inactiveTrackColor = Color(0xFF33384A)
                    )
                )
                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = AmberGoldPrimary)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun TaskPickerSheetContent(
    tasks: List<com.example.data.model.TaskItem>,
    activeTaskId: Long?,
    onSelectTask: (Long?) -> Unit,
    onGoToTasks: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Associer une tâche à ce cycle",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Gérer les tâches",
                color = AmberGoldLight,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onGoToTasks)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // None option
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (activeTaskId == null) AmberGoldPrimary.copy(alpha = 0.15f) else SurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, if (activeTaskId == null) AmberGoldPrimary else Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onSelectTask(null) }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Aucune tâche spécifique (Focus libre)",
                    color = if (activeTaskId == null) AmberGoldLight else TextSecondary,
                    fontSize = 13.sp
                )
                if (activeTaskId == null) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = AmberGoldPrimary, modifier = Modifier.size(16.dp))
                }
            }
        }

        val activeTasks = tasks.filter { !it.isCompleted }
        if (activeTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aucune tâche active pour le moment.\nCréez vos tâches dans l'onglet Tâches !",
                    color = TextMuted,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            activeTasks.forEach { task ->
                val isSelected = activeTaskId == task.id
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) AmberGoldPrimary.copy(alpha = 0.15f) else SurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) AmberGoldPrimary else Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onSelectTask(task.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.title,
                                color = if (isSelected) AmberGoldLight else TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "🍅 ${task.completedPomodoros}/${task.estimatedPomodoros} pomodoros • ${task.category}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = AmberGoldPrimary, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
