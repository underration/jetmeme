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
    private val enabledCheckBox = JBCheckBox("Play a sound when errors appear")
    private val soundFileField = TextFieldWithBrowseButton()
    private val cooldownField = JBTextField()

    override fun getDisplayName(): String = "JetMeme"

    override fun createComponent(): JComponent {
        soundFileField.addBrowseFolderListener(
            "Choose Sound File",
            "Choose a local WAV sound file. JetMeme does not include third-party audio.",
            null,
            FileChooserDescriptorFactory.createSingleFileDescriptor()
        )

        panel = FormBuilder.createFormBuilder()
            .addComponent(enabledCheckBox)
            .addLabeledComponent(JBLabel("Sound file:"), soundFileField, 1, false)
            .addLabeledComponent(JBLabel("Cooldown (ms):"), cooldownField, 1, false)
            .addComponentFillVertically(JBPanel<JBPanel<*>>(), 0)
            .panel

        reset()
        return panel!!
    }

    override fun isModified(): Boolean {
        val settings = JetMemeSettingsState.getInstance().state
        return enabledCheckBox.isSelected != settings.enabled ||
            soundFileField.text != settings.soundFilePath ||
            cooldownField.text.toLongOrNull() != settings.cooldownMillis
    }

    override fun apply() {
        val settings = JetMemeSettingsState.getInstance().state
        settings.enabled = enabledCheckBox.isSelected
        settings.soundFilePath = soundFileField.text.trim()
        settings.cooldownMillis = cooldownField.text.toLongOrNull()?.coerceAtLeast(0) ?: 5_000
        cooldownField.text = settings.cooldownMillis.toString()
    }

    override fun reset() {
        val settings = JetMemeSettingsState.getInstance().state
        enabledCheckBox.isSelected = settings.enabled
        soundFileField.text = settings.soundFilePath
        cooldownField.text = settings.cooldownMillis.toString()
    }

    override fun disposeUIResources() {
        panel = null
    }
}
