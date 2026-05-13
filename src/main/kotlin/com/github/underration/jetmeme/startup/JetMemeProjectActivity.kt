package com.github.underration.jetmeme.startup

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.github.underration.jetmeme.errors.JetMemeErrorWatcher

class JetMemeProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        LOG.info("JetMeme loaded for project: ${project.name}")
        JetMemeErrorWatcher(project).start(project)
    }

    companion object {
        private val LOG = Logger.getInstance(JetMemeProjectActivity::class.java)
    }
}
