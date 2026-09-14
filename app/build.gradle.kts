plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("pmd")
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    toolVersion = "1.23.8"
    ignoreFailures = true
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
}

android {
    namespace = "com.cst338.cst438_p1"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cst338.cst438_p1"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation(libs.androidx.ui.test.junit4)
    implementation(libs.androidx.ui)
    val room_version = "2.8.4"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:${room_version}")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    kapt("androidx.room:room-compiler:2.8.4")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.0")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    pmd("net.sourceforge.pmd:pmd-kotlin:7.27.0")
    detektPlugins(
        "io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8"
    )
}

pmd {
    toolVersion = "7.27.0"
    isIgnoreFailures = false
}

tasks.register<Pmd>("pmdCheck") {
    description = "Runs PMD against Kotlin sources."
    group = "verification"

    source = fileTree("src/main/kotlin") {
        include("**/*.kt")
    }

    ruleSetFiles = files(
        project.file("config/pmd/ruleset.xml")
    )
    ruleSets = emptyList()

    reports {
        xml.required.set(true)
        html.required.set(true)
        isConsoleOutput = true
    }
}

tasks.named("check") {
    dependsOn("pmdCheck")
}