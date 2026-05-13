package com.github.underration.jetmeme.startup

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class JetMemeProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        LOG.info("JetMeme loaded for project: ${project.name}")
    }

    companion object {
        private val LOG = Logger.getInstance(JetMemeProjectActivity::class.java)
    }
}
