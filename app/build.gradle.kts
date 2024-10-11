

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.chaquo.python")
}

android {
    namespace = "com.pzbdownloaders.redpdfpro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pzbdownloaders.redpdfpro"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ndk {
            // On Apple silicon, you can omit x86_64.
            abiFilters += "arm64-v8a"
        }
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }
        chaquopy {
            defaultConfig {
                version = "3.8"
                buildPython("/opt/homebrew/bin/python3")
                pip {
                    install("requests")
                    install("datetime")
                    install("pypdf")
                    install("pypdf[image]")
                    install("PYPDF2")
                    install("pillow")
                    install("python-docx")
                    install("ppt2pdf")
                    install("python-pptx")
                }
            }
            productFlavors {

            }
            sourceSets { }
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //NAVIGATION
    implementation("androidx.navigation:navigation-compose:2.7.7")

    //MATERIAL 3
    implementation("androidx.compose.material3:material3:1.2.1")

    //ML PDF SCANNER
    implementation("com.google.android.gms:play-services-mlkit-document-scanner:16.0.0-beta1")

    //COIL
    implementation("io.coil-kt:coil-compose:2.6.0")

    // EXTENDED ICONS
    implementation("androidx.compose.material:material-icons-extended:1.6.4")

    //TESSERACT
    implementation("cz.adaptech.tesseract4android:tesseract4android-openmp:4.7.0")

    //DOCUMENT TREE
    implementation("androidx.documentfile:documentfile:1.0.1")


    //PDF READER
    implementation("io.github.grizzi91:bouquet:1.1.2")

    implementation ("io.github.afreakyelf:Pdf-Viewer:2.1.1")


}