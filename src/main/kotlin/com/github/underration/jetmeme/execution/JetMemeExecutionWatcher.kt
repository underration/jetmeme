package com.github.underration.jetmeme.execution

import com.github.underration.jetmeme.audio.JetMemeAudioPlayer
import com.github.underration.jetmeme.settings.JetMemeSettingsState
import com.intellij.execution.ExecutionListener
import com.intellij.execution.ExecutionManager
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import java.util.concurrent.atomic.AtomicLong

class JetMemeExecutionWatcher(
    private val project: Project,
    private val audioPlayer: JetMemeAudioPlayer = JetMemeAudioPlayer(),
) {

    private val lastPlayedAt = AtomicLong(0)

    fun start(parentDisposable: Disposable) {
        project.messageBus.connect(parentDisposable).subscribe(
            ExecutionManager.EXECUTION_TOPIC,
            object : ExecutionListener {
                override fun processTerminated(
                    executorId: String,
                    env: ExecutionEnvironment,
                    handler: ProcessHandler,
                    exitCode: Int,
                ) {
                    if (exitCode != 0) {
                        playIfAllowed()
                    }
                }
            }
        )
    }

    private fun playIfAllowed() {
        val settings = JetMemeSettingsState.getInstance().state
        if (!settings.runFailuresEnabled || settings.soundFilePath.isBlank()) return

        val now = System.currentTimeMillis()
        val previous = lastPlayedAt.get()
        if (now - previous < settings.cooldownMillis) return
        if (lastPlayedAt.compareAndSet(previous, now)) {
            audioPlayer.play(settings.soundFilePath)
        }
    }
}
