plugins {
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    id("org.jetbrains.kotlin.kapt") version "2.0.20" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.devtools.ksp") version "2.0.20-1.0.24" apply false
    // 👇 OBRIGATÓRIO com Kotlin 2.0 + Compose
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20" apply false
}
