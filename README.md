
[![CircleCI](https://circleci.com/gh/JacquesSmuts/OverengineeredAndroid.svg?style=svg)](https://circleci.com/gh/JacquesSmuts/OverengineeredAndroid) [![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

# OverengineeredAndroid
An overengineered android app. Meant to showcase the nice things you can use for larger apps. 

Uses http://deckofcardsapi.com/ as a simple api to connect to.

## Libraries and patterns

- 100% Kotlin
- [Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html) instead of Groovy for Gradle
- [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
- [FlowBinding](https://github.com/ReactiveCircus/FlowBinding)
- [Android Architecture MVVM](https://developer.android.com/topic/libraries/architecture/viewmodel) (using [Coroutine Support](https://developer.android.com/topic/libraries/architecture/coroutines))
- [Retrofit](https://square.github.io/retrofit/) + [Moshi](https://square.github.io/okhttp/) + [OkHttp](https://square.github.io/okhttp/)
- [Glide](https://github.com/bumptech/glide) for Images
- [SqlDelight](https://github.com/cashapp/sqldelight) for Persistence
- [Koin](https://github.com/InsertKoinIO/koin)
- [Timber](https://github.com/JakeWharton/timber)
- [Mockk](https://mockk.io/)
- [JUnit5](https://junit.org/junit5/docs/current/user-guide/)

## Ways in which this Repo wil overengineer:

- Abstract too early
- Lasagna more than is strictly necessary
- Use big/complex systems to implement one thing, just in case it needs to be implemented many times.
