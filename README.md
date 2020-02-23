
[![CircleCI](https://circleci.com/gh/JacquesSmuts/OverengineeredAndroid.svg?style=svg)](https://circleci.com/gh/JacquesSmuts/OverengineeredAndroid) [![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

# OverengineeredAndroid
An overengineered android app. Meant to showcase the nice things you can use for larger apps. 

Uses http://deckofcardsapi.com/ as a simple api to connect to.

## Libraries and patterns

- 100% Kotlin
- Kotlin instead of Groovy for Gradle
- Kotlin Coroutines
- Android Architecture MVVM (using Coroutine Support)
- Retrofit + Moshi + OkHttp
- SqlDelight for Persistence
- Koin
- Timber

## Ways in which this Repo wil overengineer:

- Abstract too early
- Lasagna more than is strictly necessary
- Use big/complex systems to implement one thing, just in case it needs to be implemented many times.
