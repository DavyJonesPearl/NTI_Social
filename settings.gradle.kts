pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NTISocial"

// App module
include(":app")

// Core modules
include(":core-security")
include(":core-network")

// Data layer
include(":data-local")
include(":data-remote")

// Feature modules (Stage 13: Active)
include(":feature-auth")
include(":feature-party")
include(":feature-camera")
include(":feature-gallery")

// Feature modules (Inactive - Stage 14+)
// include(":feature-voting")
// include(":feature-voice")
// include(":feature-recap")
