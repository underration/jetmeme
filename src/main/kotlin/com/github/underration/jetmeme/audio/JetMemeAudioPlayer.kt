package com.github.underration.jetmeme.audio

import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.concurrency.AppExecutorUtil
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineEvent

class JetMemeAudioPlayer {

    fun play(path: String) {
        if (path.isBlank()) return

        AppExecutorUtil.getAppExecutorService().execute {
            runCatching {
                val file = File(path)
                if (!file.isFile) return@execute

                AudioSystem.getAudioInputStream(file).use { stream ->
                    val clip = AudioSystem.getClip()
                    clip.open(stream)
                    clip.addLineListener { event ->
                        if (event.type == LineEvent.Type.STOP) {
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

    companion object {
        private val LOG = Logger.getInstance(JetMemeAudioPlayer::class.java)
    }
}
