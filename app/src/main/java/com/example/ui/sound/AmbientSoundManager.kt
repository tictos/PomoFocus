package com.example.ui.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.sin

enum class AmbientSound(val label: String, val iconName: String) {
    NONE("Désactivé", "volume_off"),
    RAIN("Pluie douce", "water_drop"),
    WHITE_NOISE("Bruit blanc", "air"),
    CLOCK_TICK("Tic-tac zen", "schedule"),
    BINAURAL_FOCUS("Ondes Alpha (Focus)", "graphic_eq"),
    CAMPFIRE("Feu de camp", "local_fire_department")
}

class AmbientSoundManager(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var soundJob: Job? = null
    private var audioTrack: AudioTrack? = null
    private val random = Random()

    private val vibrator: Vibrator? = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    } catch (_: Exception) {
        null
    }

    fun playAmbientSound(sound: AmbientSound, volume: Float = 0.5f) {
        stopAmbientSound()
        if (sound == AmbientSound.NONE) return

        soundJob = scope.launch {
            try {
                val sampleRate = 22050 // Lower sample rate for lightweight CPU usage
                val minBuf = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val bufferSize = if (minBuf > 0) minBuf.coerceAtLeast(2048) else 2048

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize * 2)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack = track
                track.setVolume(volume.coerceIn(0f, 1f))
                track.play()

                val buffer = ShortArray(bufferSize)
                var phase = 0.0
                var b0 = 0.0
                var b1 = 0.0
                var b2 = 0.0
                var tickCount = 0

                while (isActive && track.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    when (sound) {
                        AmbientSound.RAIN -> {
                            for (i in buffer.indices) {
                                val white = (random.nextDouble() * 2.0 - 1.0)
                                b0 = 0.99765 * b0 + white * 0.0990460
                                b1 = 0.96300 * b1 + white * 0.2965164
                                b2 = 0.57000 * b2 + white * 1.0526913
                                val pink = b0 + b1 + b2 + white * 0.1848
                                val drop = if (random.nextInt(1000) < 3) (random.nextDouble() * 2.0 - 1.0) * 0.4 else 0.0
                                val sample = (pink * 0.15 + drop).coerceIn(-1.0, 1.0)
                                buffer[i] = (sample * Short.MAX_VALUE * 0.3).toInt().toShort()
                            }
                        }
                        AmbientSound.WHITE_NOISE -> {
                            for (i in buffer.indices) {
                                val white = (random.nextDouble() * 2.0 - 1.0)
                                b0 = 0.85 * b0 + 0.15 * white
                                buffer[i] = (b0 * Short.MAX_VALUE * 0.25).toInt().toShort()
                            }
                        }
                        AmbientSound.CLOCK_TICK -> {
                            for (i in buffer.indices) {
                                tickCount++
                                val tickSample = tickCount % sampleRate
                                if (tickSample < 400) {
                                    val t = tickSample.toDouble() / sampleRate
                                    val env = 1.0 - (tickSample / 400.0)
                                    val tickSine = sin(2.0 * Math.PI * 1800.0 * t) * env
                                    buffer[i] = (tickSine * Short.MAX_VALUE * 0.4).toInt().toShort()
                                } else {
                                    buffer[i] = 0
                                }
                            }
                        }
                        AmbientSound.BINAURAL_FOCUS -> {
                            val freq1 = 216.0
                            val freq2 = 226.0
                            for (i in buffer.indices) {
                                phase += (2.0 * Math.PI * freq1) / sampleRate
                                val s1 = sin(phase)
                                val s2 = sin(phase * (freq2 / freq1))
                                val combined = (s1 + s2) * 0.5
                                buffer[i] = (combined * Short.MAX_VALUE * 0.3).toInt().toShort()
                            }
                        }
                        AmbientSound.CAMPFIRE -> {
                            for (i in buffer.indices) {
                                val white = (random.nextDouble() * 2.0 - 1.0)
                                b0 = 0.95 * b0 + 0.05 * white
                                val pop = if (random.nextInt(3500) < 2) (random.nextDouble() * 2.0 - 1.0) * 0.7 else 0.0
                                val sample = (b0 * 0.2 + pop).coerceIn(-1.0, 1.0)
                                buffer[i] = (sample * Short.MAX_VALUE * 0.35).toInt().toShort()
                            }
                        }
                        AmbientSound.NONE -> break
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        track.write(buffer, 0, buffer.size, AudioTrack.WRITE_NON_BLOCKING)
                    } else {
                        track.write(buffer, 0, buffer.size)
                    }
                    kotlinx.coroutines.yield()
                }
            } catch (_: Exception) {
                // Ignore audio failure gracefully on emulators without sound devices
            }
        }
    }

    fun stopAmbientSound() {
        soundJob?.cancel()
        soundJob = null
        try {
            audioTrack?.apply {
                if (playState == AudioTrack.PLAYSTATE_PLAYING) {
                    stop()
                }
                release()
            }
        } catch (_: Exception) {}
        audioTrack = null
    }

    fun playCompletionChime() {
        scope.launch {
            val sampleRate = 44100
            val durationSec = 1.5
            val totalSamples = (sampleRate * durationSec).toInt()
            val buffer = ShortArray(totalSamples)

            // Harmonic chord chime: C5 (523Hz), E5 (659Hz), G5 (784Hz), C6 (1046Hz)
            val freqs = doubleArrayOf(523.25, 659.25, 783.99, 1046.50)
            for (i in 0 until totalSamples) {
                val t = i.toDouble() / sampleRate
                val decay = (1.0 - (t / durationSec)).coerceAtLeast(0.0)
                var sample = 0.0
                for (f in freqs) {
                    sample += sin(2.0 * Math.PI * f * t)
                }
                sample = (sample / freqs.size) * decay * decay
                buffer[i] = (sample * Short.MAX_VALUE * 0.5).toInt().toShort()
            }

            try {
                val chimeTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                chimeTrack.write(buffer, 0, buffer.size)
                chimeTrack.play()
            } catch (_: Exception) {}
        }
    }

    fun triggerVibration() {
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 200, 100, 200, 100, 400),
                        intArrayOf(0, 180, 0, 220, 0, 255),
                        -1
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(500)
            }
        }
    }
}
