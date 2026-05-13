package com.github.underration.jetmeme.errors

import com.github.underration.jetmeme.audio.JetMemeAudioPlayer
import com.github.underration.jetmeme.settings.JetMemeSettingsState
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerEx
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import java.util.concurrent.atomic.AtomicLong

class JetMemeErrorWatcher(
    private val project: Project,
    private val audioPlayer: JetMemeAudioPlayer = JetMemeAudioPlayer(),
) {

    private val lastPlayedAt = AtomicLong(0)

    fun start(parentDisposable: Disposable) {
        project.messageBus.connect(parentDisposable).subscribe(
            DaemonCodeAnalyzer.DAEMON_EVENT_TOPIC,
            object : DaemonCodeAnalyzer.DaemonListener {
                override fun daemonFinished(fileEditors: Collection<FileEditor>) {
                    ApplicationManager.getApplication().invokeLater {
                        if (!project.isDisposed && fileEditors.any(::editorHasErrors)) {
                            playIfAllowed()
                        }
                    }
                }
            }
        )
    }

    private fun editorHasErrors(fileEditor: FileEditor): Boolean {
        val document = (fileEditor as? TextEditor)?.editor?.document ?: return false
        return documentHasErrors(document)
    }

    private fun documentHasErrors(document: Document): Boolean {
        var hasErrors = false
        DaemonCodeAnalyzerEx.processHighlights(
            document,
            project,
            HighlightSeverity.ERROR,
            0,
            document.textLength
        ) {
            hasErrors = true
            false
        }
        return hasErrors
    }

    private fun playIfAllowed() {
        val settings = JetMemeSettingsState.getInstance().state
        if (!settings.editorErrorsEnabled || settings.soundFilePath.isBlank()) return

        val now = System.currentTimeMillis()
        val previous = lastPlayedAt.get()
        if (now - previous < settings.cooldownMillis) return
        if (lastPlayedAt.compareAndSet(previous, now)) {
            audioPlayer.play(settings.soundFilePath)
        }
    }
}
