package com.github.underration.jetmeme.audio

import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.concurrency.AppExecutorUtil
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent
import kotlin.random.Random

class JetMemeAudioPlayer {

    fun play(path: String) {
        if (path.isBlank()) return

        AppExecutorUtil.getAppExecutorService().execute {
            runCatching {
                val file = selectSoundFile(path) ?: return@execute

                AudioSystem.getAudioInputStream(file).use { stream ->
                    val clip = AudioSystem.getClip()
                    clip.open(stream)
                    activeClips.add(clip)
                    clip.addLineListener { event ->
                        if (event.type == LineEvent.Type.STOP) {
                            activeClips.remove(event.line)
                            event.line.close()
                        }
                    }
                    clip.start()
                }
            }.onFailure {
                LOG.warn("Failed to play JetMeme sound: $path", it)
            }
        }
    }

    private fun selectSoundFile(path: String): File? {
        val file = File(path)
        if (file.isFile) return file.takeIf { it.extension.equals("wav", ignoreCase = true) }
        if (!file.isDirectory) return null

        val wavFiles = file.listFiles { candidate ->
            candidate.isFile && candidate.extension.equals("wav", ignoreCase = true)
        }?.toList().orEmpty()

        return wavFiles.takeIf { it.isNotEmpty() }?.random(Random.Default)
    }

    companion object {
        private val LOG = Logger.getInstance(JetMemeAudioPlayer::class.java)
        private val activeClips = ConcurrentHashMap.newKeySet<Clip>()

        fun stopAll() {
            activeClips.toList().forEach { clip ->
                runCatching {
                    if (clip.isRunning) {
                        clip.stop()
                    } else {
                        activeClips.remove(clip)
                        clip.close()
                    }
                }.onFailure {
                    LOG.warn("Failed to stop JetMeme sound", it)
                }
            }
        }
    }
}
