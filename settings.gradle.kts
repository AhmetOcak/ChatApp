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
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
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

rootProject.name = "ChatApp"
include(":app")
include(":core")
include(":core:common")
include(":core:data")
include(":core:datastore")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:network")
include(":core:ui")
include(":feature")
include(":feature:login")
include(":feature:signup")
include(":feature:chats")
include(":feature:settings")
include(":feature:profile")
include(":feature:chat_box")
include(":core:authentication")
include(":core:database")
include(":feature:camera")
