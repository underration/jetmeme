package com.github.underration.jetmeme

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JetMemePluginTest : BasePlatformTestCase() {

    fun testProjectLoads() {
        assertNotNull(project)
    }
}
