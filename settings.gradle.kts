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
include(":feature:onboarding")
include(":feature:chats")
include(":feature:calls")
include(":feature:stories")
include(":feature:settings")
include(":feature:profile")
include(":feature:chat_box")
include(":feature:user_detail")
include(":feature:call_info")
include(":feature:chat_docs")
include(":feature:chat_detail")
include(":core:authentication")
include(":core:database")
