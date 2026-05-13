package com.github.underration.jetmeme.toolwindow

import com.github.underration.jetmeme.audio.JetMemeAudioPlayer
import com.github.underration.jetmeme.settings.JetMemeSettingsState
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JPanel

class JetMemeToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = JetMemeToolWindowPanel()
        val content = ContentFactory.getInstance().createContent(panel.component, null, false)
        toolWindow.contentManager.addContent(content)
    }
}

private class JetMemeToolWindowPanel {

    private val settings = JetMemeSettingsState.getInstance().state
    private val audioPlayer = JetMemeAudioPlayer()
    private val editorErrorsCheckBox = JBCheckBox("Editor errors")
    private val runFailuresCheckBox = JBCheckBox("Run Configuration failures")
    private val terminalFailuresCheckBox = JBCheckBox("Terminal command failures")
    private val soundPathLabel = JBLabel()

    val component: JPanel = JPanel(BorderLayout())

    init {
        editorErrorsCheckBox.isSelected = settings.editorErrorsEnabled
        runFailuresCheckBox.isSelected = settings.runFailuresEnabled
        terminalFailuresCheckBox.isSelected = settings.terminalFailuresEnabled
        refreshSoundPath()

        editorErrorsCheckBox.addActionListener {
            settings.editorErrorsEnabled = editorErrorsCheckBox.isSelected
        }
        runFailuresCheckBox.addActionListener {
            settings.runFailuresEnabled = runFailuresCheckBox.isSelected
        }
        terminalFailuresCheckBox.addActionListener {
            settings.terminalFailuresEnabled = terminalFailuresCheckBox.isSelected
        }

        val stopButton = JButton("Stop Sound").apply {
            addActionListener {
                JetMemeAudioPlayer.stopAll()
            }
        }
        val testButton = JButton("Test Sound").apply {
            addActionListener {
                audioPlayer.play(settings.soundFilePath)
            }
        }
        val refreshButton = JButton("Refresh").apply {
            addActionListener {
                syncFromSettings()
            }
        }

        component.add(
            FormBuilder.createFormBuilder()
                .addComponent(JBLabel("Triggers"))
                .addComponent(editorErrorsCheckBox)
                .addComponent(runFailuresCheckBox)
                .addComponent(terminalFailuresCheckBox)
                .addSeparator()
                .addComponent(JBLabel("Sound file or directory"))
                .addComponent(soundPathLabel)
                .addSeparator()
                .addComponent(testButton)
                .addComponent(stopButton)
                .addComponent(refreshButton)
                .addComponentFillVertically(JPanel(), 0)
                .panel,
            BorderLayout.CENTER
        )
    }

    private fun syncFromSettings() {
        editorErrorsCheckBox.isSelected = settings.editorErrorsEnabled
        runFailuresCheckBox.isSelected = settings.runFailuresEnabled
        terminalFailuresCheckBox.isSelected = settings.terminalFailuresEnabled
        refreshSoundPath()
    }

    private fun refreshSoundPath() {
        soundPathLabel.text = settings.soundFilePath.ifBlank { "(not set)" }
    }
}
