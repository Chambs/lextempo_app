plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    // 👇 habilita o Compose Compiler plugin
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.lextempo.calculadorapenal"
    compileSdk = 35

    val baseApplicationId = "com.lextempo.calculadorapenal"
    // Gera um sufixo determinístico para o appId de debug baseado no caminho do projeto.
    // Isso evita colisões com builds antigos (ex.: "celobackendareal.dev") quando
    // Android Studio tenta instalar múltiplos APKs com o mesmo applicationId.
    val debugApplicationIdSuffix = run {
        val projectHash = Integer.toHexString(rootProject.projectDir.absolutePath.hashCode())
        ".dev$projectHash"
    }

    defaultConfig {
        applicationId = baseApplicationId
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            // Usa um applicationId exclusivo para desenvolvimento e evita conflito no dispositivo
            applicationIdSuffix = debugApplicationIdSuffix
            versionNameSuffix = "-dev"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures { compose = true }

    // ⚠️ Com o plugin acima NÃO precisa declarar kotlinCompilerExtensionVersion
    // composeOptions { kotlinCompilerExtensionVersion = "..." }
    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
}

dependencies {
    // Compose BOM para manter versões alinhadas
    val composeBom = platform("androidx.compose:compose-bom:2024.10.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Compose UI
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // 👇 Necessário para KeyboardOptions (módulo ui-text)
    implementation("androidx.compose.ui:ui-text")

    // Tema XML Material3 (Manifest usa Theme.Material3.*)
    implementation("com.google.android.material:material:1.12.0")
    // (opcional) AppCompat
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Outros úteis
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")

    // Testes
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}

kapt {
    correctErrorTypes = true
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.generateKotlin", "true")
}
