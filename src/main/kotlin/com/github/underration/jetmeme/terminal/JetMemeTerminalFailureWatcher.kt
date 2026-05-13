package com.github.underration.jetmeme.terminal

import com.github.underration.jetmeme.audio.JetMemeAudioPlayer
import com.github.underration.jetmeme.settings.JetMemeSettingsState
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentManagerEvent
import com.intellij.ui.content.ContentManagerListener
import com.jediterm.terminal.TerminalCustomCommandListener
import org.jetbrains.plugins.terminal.TerminalToolWindowInitializer
import org.jetbrains.plugins.terminal.TerminalView
import java.util.Collections
import java.util.IdentityHashMap
import java.util.concurrent.atomic.AtomicLong

class JetMemeTerminalFailureWatcher : TerminalToolWindowInitializer {

    private val audioPlayer = JetMemeAudioPlayer()
    private val lastPlayedAt = AtomicLong(0)
    private val installedWidgets = Collections.newSetFromMap(IdentityHashMap<Any, Boolean>())

    override fun initialize(toolWindow: ToolWindow) {
        toolWindow.contentManager.contents.forEach(::installForContent)
        toolWindow.contentManager.addContentManagerListener(
            object : ContentManagerListener {
                override fun contentAdded(event: ContentManagerEvent) {
                    installForContent(event.content)
                }
            }
        )
    }

    private fun installForContent(content: Content) {
        val widget = TerminalView.getWidgetByContent(content) ?: return
        if (!installedWidgets.add(widget)) return

        val terminal = widget.terminal
        val listener = object : TerminalCustomCommandListener {
            override fun process(args: List<String>) {
                handleShellIntegrationEvent(args)
            }
        }

        terminal.addCustomCommandListener(listener)
        Disposer.register(widget, Disposable {
            terminal.removeCustomCommandListener(listener)
        })
        LOG.debug("Installed JetMeme terminal watcher")
    }

    private fun handleShellIntegrationEvent(args: List<String>) {
        if (args.firstOrNull() != "command_finished") return

        val exitCode = args.getOrNull(1)?.toIntOrNull() ?: return
        if (exitCode != 0) {
            playIfAllowed()
        }
    }

    private fun playIfAllowed() {
        val settings = JetMemeSettingsState.getInstance().state
        if (!settings.terminalFailuresEnabled || settings.soundFilePath.isBlank()) return

        val now = System.currentTimeMillis()
        val previous = lastPlayedAt.get()
        if (now - previous < settings.cooldownMillis) return
        if (lastPlayedAt.compareAndSet(previous, now)) {
            audioPlayer.play(settings.soundFilePath)
        }
    }

    companion object {
        private val LOG = Logger.getInstance(JetMemeTerminalFailureWatcher::class.java)
    }
}
