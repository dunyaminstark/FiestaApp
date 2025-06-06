// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    //alias(libs.plugins.kotlin.compose) apply false

    // Use the alias from libs.versions.toml for the Compose compiler plugin

    // Use the alias from libs.versions.toml for Hilt plugin
    alias(libs.plugins.hilt) apply false
    // Use the alias from libs.versions.toml for KSP plugin
    alias(libs.plugins.ksp) apply false

    alias(libs.plugins.compose.compiler) apply false

}