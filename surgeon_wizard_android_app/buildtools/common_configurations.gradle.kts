val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

if (!plugins.hasPlugin("org.jetbrains.kotlin.android")) {
    plugins.apply("org.jetbrains.kotlin.android")
}

plugins.apply("org.jetbrains.kotlin.kapt")

plugins.apply(libs.findPlugin("hilt").get().get().pluginId)

dependencies {
    "implementation"(libs.findLibrary("hilt.android").get())
    "kapt"(libs.findLibrary("hilt.compiler").get())
}