package com.github.underration.jetmeme.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class JetMemeSettingsConfigurable : Configurable {

    private var panel: JPanel? = null
    private val editorErrorsEnabledCheckBox = JBCheckBox("Play a sound when editor errors appear")
    private val runFailuresEnabledCheckBox = JBCheckBox("Play a sound when Run Configurations fail")
    private val terminalFailuresEnabledCheckBox = JBCheckBox("Play a sound when Terminal commands fail")
    private val soundFileField = TextFieldWithBrowseButton()
    private val cooldownField = JBTextField()

    override fun getDisplayName(): String = "JetMeme"

    override fun createComponent(): JComponent {
        soundFileField.addBrowseFolderListener(
            "Choose Sound File or Directory",
            "Choose a local WAV sound file, or a directory containing WAV files. JetMeme does not include third-party audio.",
            null,
            FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()
        )

        panel = FormBuilder.createFormBuilder()
            .addComponent(editorErrorsEnabledCheckBox)
            .addComponent(runFailuresEnabledCheckBox)
            .addComponent(terminalFailuresEnabledCheckBox)
            .addLabeledComponent(JBLabel("Sound file or directory:"), soundFileField, 1, false)
            .addLabeledComponent(JBLabel("Cooldown (ms):"), cooldownField, 1, false)
            .addComponentFillVertically(JBPanel<JBPanel<*>>(), 0)
            .panel

        reset()
        return panel!!
    }

    override fun isModified(): Boolean {
        val settings = JetMemeSettingsState.getInstance().state
        return editorErrorsEnabledCheckBox.isSelected != settings.editorErrorsEnabled ||
            runFailuresEnabledCheckBox.isSelected != settings.runFailuresEnabled ||
            terminalFailuresEnabledCheckBox.isSelected != settings.terminalFailuresEnabled ||
            soundFileField.text != settings.soundFilePath ||
            cooldownField.text.toLongOrNull() != settings.cooldownMillis
    }

    override fun apply() {
        val settings = JetMemeSettingsState.getInstance().state
        settings.editorErrorsEnabled = editorErrorsEnabledCheckBox.isSelected
        settings.runFailuresEnabled = runFailuresEnabledCheckBox.isSelected
        settings.terminalFailuresEnabled = terminalFailuresEnabledCheckBox.isSelected
        settings.soundFilePath = soundFileField.text.trim()
        settings.cooldownMillis = cooldownField.text.toLongOrNull()?.coerceAtLeast(0) ?: 5_000
        cooldownField.text = settings.cooldownMillis.toString()
    }

    override fun reset() {
        val settings = JetMemeSettingsState.getInstance().state
        editorErrorsEnabledCheckBox.isSelected = settings.editorErrorsEnabled
        runFailuresEnabledCheckBox.isSelected = settings.runFailuresEnabled
        terminalFailuresEnabledCheckBox.isSelected = settings.terminalFailuresEnabled
        soundFileField.text = settings.soundFilePath
        cooldownField.text = settings.cooldownMillis.toString()
    }

    override fun disposeUIResources() {
        panel = null
    }
}
