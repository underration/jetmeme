package com.github.underration.jetmeme.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@Service(Service.Level.APP)
@State(name = "JetMemeSettings", storages = [Storage("jetmeme.xml")])
class JetMemeSettingsState : PersistentStateComponent<JetMemeSettingsState.State> {

    data class State(
        var enabled: Boolean = true,
        var soundFilePath: String = "",
        var cooldownMillis: Long = 5_000,
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        fun getInstance(): JetMemeSettingsState =
            ApplicationManager.getApplication().getService(JetMemeSettingsState::class.java)
    }
}
