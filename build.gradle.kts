plugins {
    id("com.android.application") version "8.13.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    // Only keep KSP if you actually use it in a module
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}
